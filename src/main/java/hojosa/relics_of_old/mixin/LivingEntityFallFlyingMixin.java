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
	 * Skip vanilla updateFallFlying for mantle-flying players to prevent data watcher churn. Vanilla clears flag 7 (no elytra in CHEST) then our old
	 * TAIL set it back — dirtying the SynchedEntityData byte every tick and sending unnecessary entity metadata packets. HEAD cancel keeps flag 7
	 * stable so the byte is never marked dirty during flight.
	 */
	private LivingEntityFallFlyingMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
    private void relics_mantleFallFlying(CallbackInfo ci) {
            if (!((Object) this instanceof Player player))
                    return;

            // tick down sky lens launch charge each tick (all players)
            PlayerGlideData glide = PlayerGlideData.get(player);
            if (glide != null) {
                    glide.tickSkylensCharge();
            }

            // only intercept for actively gliding mantle players
            if (glide == null || !glide.isGliding())
                    return;

            if (!RelicsItems.AZURE_MANTLE.get().isEquipped(player) && !RelicsItems.PHOENIX_MANTLE.get().isEquipped(player)) {
                    glide.stopGliding();
                    return;
            }

            // stop on ground, collision, water, or riding
            if (player.onGround() || this.horizontalCollision || player.isInWater() || player.isPassenger()) {
                    glide.stopGliding();
                    this.setSharedFlag(7, false);
                    ci.cancel();
                    return;
            }

            // ensure flag 7 is set — on first tick this changes false→true (dirty, correct),
            // on subsequent ticks true→true is a no-op (ObjectUtils.notEqual = false, not dirty)
            this.setSharedFlag(7, true);
            ci.cancel();
	}

	@Shadow
	public abstract boolean isFallFlying();
}