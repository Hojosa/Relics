package hojosa.relics_of_old.common.player;

import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.network.ManaSyncPacket;
import hojosa.relics_of_old.network.RelicsNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

// Mana system ported from LG2's PlayerStarstatsExtension.
// Mana is tracked as "magic fatigue" — 0 = full mana, 20+ = exhausted.
public class PlayerMana {

	// Fatigue levels
	public static final int FINE = 0;
	public static final int FATIGUED = 1;
	public static final int EXHAUSTED = 2;

	// Regen rates (fatigue reduced per tick = rate * 0.05)
	public static final float MANA_RECHARGE_RATE = 4.0f;
	public static final float FATIGUED_RECHARGE_RATE = 0.75f;

	// Delays before regen starts (seconds, decremented 0.05/tick)
	public static final float MANA_RECHARGE_DELAY = 2.5f;
	public static final float FATIGUED_RECHARGE_DELAY = 6.0f;
	public static final float EXHAUSTED_RECHARGE_DELAY = 20.0f;

	// Ring mana constants (from LG2 MagicRing)
	public static final float RESONANCE_FACTOR = 0.5f;

	private float fatigue = 0.0f;
	private float rechargeDelay = 0.0f;
	private int lastSyncedLevel = 20;

	// Convenience accessor matching LG2's PlayerStarstatsExtension.get(player)
	public static PlayerMana get(Player player) {
		return player.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
	}

	public float getFatigue() {
		return fatigue;
	}

	// Available mana (0 = empty, 20 = full)
	public float getAvailableMana() {
		return 20.0f - fatigue;
	}

	// Integer mana level for HUD rendering (0-20)
	public int getManaLevel() {
		return (int) (20.0f - fatigue);
	}

	// Armor reduces breathing room — heavier armor = less comfortable mana pool
	public static float getBreathingRoom(Player player) {
		return 20 - player.getArmorValue();
	}

	// Three-tier fatigue state based on armor-adjusted thresholds
	public int getFatigueLevel(Player player) {
		if (fatigue >= 20.0f)
			return EXHAUSTED;
		if (fatigue > getBreathingRoom(player) - 1.0f)
			return FATIGUED;
		return FINE;
	}

	public float getRecuperationRate(Player player) {
		int level = getFatigueLevel(player);
		if (level == EXHAUSTED)
			return 0.25f;
		if (level == FATIGUED)
			return 0.5f;
		return 1.0f;
	}

	// Core mana spend — handles exhaustion damage and recharge delay
	public boolean expendMana(Player player, float amount) {
		// Taking damage when spending while exhausted
		if (getFatigueLevel(player) == EXHAUSTED && amount > 0.25f) {
			player.hurt(player.damageSources().magic(), amount);
		}

		boolean overspent = false;
		if (!player.isCreative()) {
			overspent = adjustFatigue(player, amount);
		}

		// Set recharge delay based on overspend state
		float delay;
		if (overspent) {
			delay = FATIGUED_RECHARGE_DELAY;
			if (getFatigueLevel(player) == EXHAUSTED && player.getArmorValue() > 0) {
				delay = EXHAUSTED_RECHARGE_DELAY;
			}
		} else {
			delay = MANA_RECHARGE_DELAY;
		}

		// Only extend delay, never shorten from spending
		if (delay >= rechargeDelay) {
			rechargeDelay = delay;
		}

		return overspent;
	}

	// Low-level fatigue adjustment, returns true if overspent past breathing room
	public boolean adjustFatigue(Player player, float amount) {
		fatigue += amount;
		if (fatigue < 0.0f)
			fatigue = 0.0f;

		// When recovering, cap rechargeDelay to appropriate level
		if (amount < 0.0f) {
			if (getFatigueLevel(player) == FINE && rechargeDelay > MANA_RECHARGE_DELAY) {
				rechargeDelay = MANA_RECHARGE_DELAY;
			}
			if (getFatigueLevel(player) == FATIGUED && rechargeDelay > FATIGUED_RECHARGE_DELAY) {
				rechargeDelay = FATIGUED_RECHARGE_DELAY;
			}
		}

		syncIfChanged(player);
		return fatigue > getBreathingRoom(player);
	}

	// Per-tick mana regeneration — call from server-side PlayerTickEvent
	public void tickRegen(Player player) {
		rechargeDelay -= 0.05f;
		if (rechargeDelay <= 0.0f) {
			rechargeDelay = 0.0f;
			// Don't regen while using items (eating, drawing bow, etc.)
			if (!player.isUsingItem()) {
				if (getFatigueLevel(player) == FINE) {
					adjustFatigue(player, MANA_RECHARGE_RATE * -0.05f);
				} else {
					adjustFatigue(player, FATIGUED_RECHARGE_RATE * -0.05f);
				}
			}
		}
	}

	// XP pickup heals mana (from LG2 PlayerEventHandler.ManaFromXPGain)
	public void healFromXP(Player player, int xpValue) {
		adjustFatigue(player, -xpValue * 0.25f);
	}

	// Ring mana convenience—creative bypass, resonance halves cost, plays threshold sounds
	public static void spendRingMana(Player player, float amount, boolean hasResonance) {
		if (player.isCreative())
			return;

		PlayerMana mana = get(player);
		if (mana == null)
			return;

		float multiplier = hasResonance ? RESONANCE_FACTOR : 1.0f;
		float before = mana.getAvailableMana();
		float adjustedAmount = amount * multiplier;
		mana.expendMana(player, adjustedAmount);
		float after = before - adjustedAmount;

		// Sound feedback on mana thresholds
		if (after <= 0.0f) {
			player.level().playSound(null, player.blockPosition(), RelicsSounds.RING_OUT.get(), SoundSource.PLAYERS, 0.5f, 1.0f);
		} else if ((int) ((before - 1.0f) / 2.0f) > (int) ((after - 1.0f) / 2.0f)) {
			player.level().playSound(null, player.blockPosition(), RelicsSounds.RING_TICK.get(), SoundSource.PLAYERS, 0.5f, 1.0f);
		}
	}

	// Sync integer mana level to client when it changes
	private void syncIfChanged(Player player) {
		int currentLevel = getManaLevel();
		if (currentLevel != lastSyncedLevel) {
			lastSyncedLevel = currentLevel;
			if (player instanceof ServerPlayer serverPlayer) {
				RelicsNetwork.getInstance().sendTo(new ManaSyncPacket(fatigue), serverPlayer);
			}
		}
	}

	// Force sync on login/respawn
	public void forceSync(Player player) {
		lastSyncedLevel = getManaLevel();
		if (player instanceof ServerPlayer serverPlayer) {
			RelicsNetwork.getInstance().sendTo(new ManaSyncPacket(fatigue), serverPlayer);
		}
	}

	// Client-side: set from sync packet
	public void setFatigueClient(float value) {
		this.fatigue = value;
	}

	public void copyFrom(PlayerMana source) {
		this.fatigue = source.fatigue;
		this.rechargeDelay = source.rechargeDelay;
	}

	public void saveNBTData(CompoundTag nbt) {
		nbt.putFloat("magicFatigue", fatigue);
		nbt.putFloat("rechargeDelay", rechargeDelay);
	}

	public void loadNBTData(CompoundTag nbt) {
		fatigue = nbt.getFloat("magicFatigue");
		rechargeDelay = nbt.getFloat("rechargeDelay");
	}
}