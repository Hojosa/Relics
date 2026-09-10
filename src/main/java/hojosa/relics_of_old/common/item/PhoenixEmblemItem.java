package hojosa.relics_of_old.common.item;

import java.util.List;

import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.common.mana.IMana;
import hojosa.relics_of_old.common.player.PlayerMana;
import hojosa.relics_of_old.common.player.SpiritFavorProvider;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PhoenixEmblemItem extends RelicsItem implements IMana {

	private static final String SPIRIT = "phoenix";
	private static final int HEAL_COST = 100;
	private static final int FEED_COST = 100;
	private static final int SMITE_COST = 200;

	public PhoenixEmblemItem() {
		super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	}

	@Override
	public float getManaCost() {
		return 1.0f;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BLOCK;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(player.getItemInHand(hand));
	}

	@Override
	public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingDuration) {
		if (level.isClientSide() || !(entity instanceof Player player))
			return;
		int usedTicks = getUseDuration(stack) - remainingDuration;
		if (usedTicks % 10 != 0 || usedTicks == 0)
			return;

		// Spend 1 mana per 10 ticks
		PlayerMana mana = PlayerMana.get(player);
		if (mana != null) {
			mana.expendMana(player, 1.0f);
		}

		// Investment scales with hold time (LG2: clicks/20 + 0.5)
		float investment = (float) (usedTicks / 10) / 20.0f + 0.5f;

		player.getCapability(SpiritFavorProvider.SPIRIT_FAVOR).ifPresent(favor -> {
			// Priority 1: heal if low health
			if (player.getHealth() <= 5.0f && favor.attemptIntervention(SPIRIT, player, HEAL_COST, investment)) {
				phoenixInterveneEffect(player);
				player.setHealth(player.getMaxHealth());
				return;
			}
			// Priority 2: feed if low food
			if (player.getFoodData().getFoodLevel() <= 5 && favor.attemptIntervention(SPIRIT, player, FEED_COST, investment)) {
				phoenixInterveneEffect(player);
				player.getFoodData().setFoodLevel(20);
				return;
			}
			// Priority 3: smite undead if surrounded (3+ undead, no other players)
			float radius = 8.0f;
			AABB box = player.getBoundingBox().inflate(radius);
			List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, box);
			int undead = 0;
			int players = 0;
			for (LivingEntity e : nearby) {
				if (e == player)
					continue;
				if (e.distanceTo(player) > radius)
					continue;
				if (e.isInvertedHealAndHarm())
					undead++;
				if (e instanceof Player)
					players++;
			}
			if (undead >= 3 && players == 0 && favor.attemptIntervention(SPIRIT, player, SMITE_COST, investment)) {
				// Spawn Rayfire spell effect centered on the player
				Vec3 pos = player.position();
				SpellEffectEntity spell = new SpellEffectEntity(level, SpellEffectEntity.SpellType.RAYFIRE, player, pos, radius, 20.0, true);
				level.addFreshEntity(spell);
			}
		});
	}

	// Visual/audio feedback on intervention
	private void phoenixInterveneEffect(Player player) {
		player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0));
		player.setSecondsOnFire(1);
		player.level().playSound(null, player.blockPosition(), RelicsSounds.REVIVE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
}