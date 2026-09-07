package hojosa.relics_of_old.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.player.PlayerGlideData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(LivingEntity.class)
public abstract class LivingEntityFallFlyingMixin extends Entity {
	/**
	 * After vanilla updateFallFlying clears flag 7 (no elytra in CHEST), re-enable it if player is wearing an azure/phoenix mantle with active glide
	 * charge.
	 */
	private LivingEntityFallFlyingMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Inject(method = "updateFallFlying", at = @At("TAIL"))
	private void relics_restoreFallFlying(CallbackInfo ci) {
		if (!((Object) this instanceof Player player))
			return;

		// tick down sky lens launch charge each tick
		PlayerGlideData glide = PlayerGlideData.get(player);
		if (glide != null) {
			glide.tickSkylensCharge();
		}

		// already flying (real elytra) — don't interfere
		if (this.isFallFlying())
			return;

		if (glide == null || !glide.isGliding())
			return;

		if (!RelicsItems.AZURE_MANTLE.get().isEquipped(player) && !RelicsItems.PHOENIX_MANTLE.get().isEquipped(player)) {
			glide.stopGliding();
			return;
		}

		// stop on ground, collision, water, or riding
		if (player.onGround() || this.horizontalCollision || player.isInWater() || player.isPassenger()) {
			glide.stopGliding();
			return;
		}

		// re-enable fall flying flag — accessible via Entity inheritance
		this.setSharedFlag(7, true);
	}

	@Shadow
	public abstract boolean isFallFlying();
}