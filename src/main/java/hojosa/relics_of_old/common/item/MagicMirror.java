package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.init.RelicsParticles;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.common.player.PlayerSkyTracker;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class MagicMirror extends RelicsItem {

	public MagicMirror(Rarity rarity) {
		super(1, rarity);
	}

	public MagicMirror(Rarity rarity, int durability) {
		super(rarity, durability);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.BLOCK;
	}

	@Override
	public int getUseDuration(ItemStack pStack) {
		return 70;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return this.getMaxDamage() == 0;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);
		CompoundTag nbt = stack.getOrCreateTag();

		if (pLevel.getGameTime() < nbt.getLong("lastwarp") + 60) {
			pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.5f, 0.5f);
			return InteractionResultHolder.fail(stack);
		}
		PlayerSkyTracker tracker = PlayerSkyTracker.get(pPlayer);
		if (!isOutdoors(pPlayer, pLevel) && tracker != null && tracker.hasPosition() && tracker.isInSameDimension(pPlayer)) {
			pPlayer.startUsingItem(pUsedHand);
			return InteractionResultHolder.consume(stack);
		}
		pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.5f, 0.5f);
		return InteractionResultHolder.fail(stack);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
		if (pLivingEntity instanceof ServerPlayer player && !pLevel.isClientSide) {
			PlayerSkyTracker tracker = PlayerSkyTracker.get(player);
			if (tracker != null && tracker.hasPosition()) {
				double tx = tracker.getLastSkyX();
				double ty = tracker.getLastSkyY();
				double tz = tracker.getLastSkyZ();

				player.fallDistance = 0;
				((ServerLevel) pLevel).sendParticles(ParticleTypes.EXPLOSION_EMITTER, player.getX(), player.getY() - 1, player.getZ(), 1, 0, 0, 0, 0);
				player.teleportTo(tx, ty, tz);
				// push up if stuck
				while (player.isInWall()) {
					player.teleportTo(player.getX(), player.getY() + 2, player.getZ());
				}

				pLevel.playSound(null, player.blockPosition(), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.3f, 1.0f);
				((ServerLevel) pLevel).sendParticles(ParticleTypes.EXPLOSION_EMITTER, player.getX(), player.getY() - 1, player.getZ(), 1, 0, 0, 0, 0);
				pStack.hurtAndBreak(1, pLivingEntity, e -> e.broadcastBreakEvent(pLivingEntity.getUsedItemHand()));
				pStack.getOrCreateTag().putLong("lastwarp", pLevel.getGameTime());
			}
		}
		return pStack;
	}

	@Override
	public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
		int dur = getUseDuration(pStack) - pRemainingUseDuration;
		float progress = dur * 1.0f / getUseDuration(pStack);

		if (pLevel.isClientSide && pLivingEntity instanceof Player player) {
			// portal overlay effect
			((LocalPlayer) player).spinningEffectIntensity = Math.max(progress * 0.95f, ((LocalPlayer) player).spinningEffectIntensity);

			// spiral particles
			double theta = Math.PI * 6 * progress;
			double r = 2 * (1 - progress);
			for (int i = 0; i < 3; i++) {
				pLevel.addParticle(new RelicsParticleOptions(RelicsParticles.RUNE_PARTICLE, 45, 0.15f), player.getX() + Math.cos(theta) * r, player.getY(), player.getZ() + Math.sin(theta) * r, 0, 0.1, 0);
				theta += Math.PI * 2 / 3;
			}
		}

		int soundOften = 8;
		if (dur % soundOften == 0) {
			pLevel.playSound(null, pLivingEntity.blockPosition(), RelicsSounds.SINE.get(), SoundSource.PLAYERS, 0.3f, 0.2f * (2 + dur / soundOften));
		}
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
		if (pEntity instanceof Player player && !pLevel.isClientSide) {
			PlayerSkyTracker tracker = PlayerSkyTracker.get(player);
			if (tracker != null) {
				tracker.updateIfUnderSky(player);
			}
			// Update outdoors flag for mirror rendering
			CompoundTag nbt = pStack.getOrCreateTag();
			if (isOutdoors(player, pLevel)) {
				nbt.putBoolean("outdoors", true);
			} else {
				long lastWarp = nbt.getLong("lastwarp");
				nbt.putBoolean("outdoors", pLevel.getGameTime() < lastWarp + 60);
			}
		}
	}

	private boolean isOutdoors(Player pPlayer, Level pLevel) {
		return pLevel.canSeeSky(pPlayer.blockPosition().above(2));
	}
}