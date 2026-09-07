package hojosa.relics_of_old.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hojosa.relics_of_old.common.init.RelicsParticles;
import hojosa.relics_of_old.common.player.PlayerGlideData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@Mixin(LivingEntity.class)
public abstract class LivingEntityTravelMixin extends Entity {
	/**
	 * LG2 energy-budget glide physics, replaces vanilla elytra travel when mantle-flying.
	 */
	private LivingEntityTravelMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Shadow
	protected abstract SoundEvent getFallDamageSound(int pDamageAmount);

	@Shadow
	public abstract boolean isFallFlying();

	@Inject(method = "travel", at = @At("HEAD"), cancellable = true)
	private void relics_mantleTravel(Vec3 pTravelVector, CallbackInfo ci) {
		if (!((Object) this instanceof Player player))
			return;
		if (!this.isFallFlying())
			return;

		PlayerGlideData glide = PlayerGlideData.get(player);
		if (glide == null || !glide.isGliding())
			return;

		// -- LG2 physics constants --
		double gravity = 0.08;
		double max_accel = 0.24;
		double speed_cap = 1.5;
		double min_airspeed = 0.5;
		double glide_ratio = glide.getGlideRatio();

		// energy budget: height from Y ceiling determines available speed
		double height = glide.getGlideCharge() - player.getY();
		double energy = gravity * height;
		if (energy < 0.0)
			energy = 0.0;
		double speed = Math.sqrt(energy);

		// target velocity along look direction
		Vec3 look = player.getLookAngle();
		double targetX = look.x * speed;
		double targetY = look.y * speed;
		double targetZ = look.z * speed;

		// real velocity (compensate for gravity that hasn't been applied yet)
		Vec3 motion = this.getDeltaMovement();
		double realX = motion.x;
		double realY = motion.y + gravity;
		double realZ = motion.z;

		// airspeed = dot product of look and real velocity
		double airspeed = look.x * realX + look.y * realY + look.z * realZ;
		if (airspeed < 0.0)
			airspeed = 0.0;

		// lift factor scales with airspeed — no lift below min_airspeed
		double lift_factor = 1.0;
		if (airspeed < min_airspeed) {
			lift_factor = airspeed / min_airspeed;
		}

		// acceleration toward target velocity
		double accelX = targetX - realX;
		double accelY = targetY - realY;
		double accelZ = targetZ - realZ;
		double needed = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);

		double accelMag = Math.min(max_accel, needed * lift_factor);
		if (needed > 0.0) {
			double invNeeded = 1.0 / needed;
			accelX *= invNeeded * accelMag;
			accelY *= invNeeded * accelMag;
			accelZ *= invNeeded * accelMag;
		}

		// apply acceleration
		double resultX = realX + accelX;
		double resultY = realY + accelY;
		double resultZ = realZ + accelZ;

		// subtract gravity
		resultY -= gravity;

		// speed cap
		double resultSpeed = Math.sqrt(resultX * resultX + resultY * resultY + resultZ * resultZ);
		if (resultSpeed > speed_cap) {
			double scale = speed_cap / resultSpeed;
			resultX *= scale;
			resultY *= scale;
			resultZ *= scale;
			resultSpeed = speed_cap;
		}

		// boost ripple when charge recently increased by 4+ (feather/sky lens)
		if (glide.getLastGlideCharge() + 4.0f < glide.getGlideCharge()) {
			double px = player.getX() + resultX * 4.0;
			double py = player.getY() + player.getEyeHeight() + resultY * 4.0;
			double pz = player.getZ() + resultZ * 4.0;
			// dx encodes scale (30.0f from LG2)
			player.level().addParticle(RelicsParticles.BOOST_RIPPLE_PARTICLE.get(), px, py, pz, 30.0, 0.0, 0.0);
		}

		// apply motion
		this.setDeltaMovement(resultX, resultY, resultZ);
		this.move(MoverType.SELF, this.getDeltaMovement());
		Vec3 afterMove = this.getDeltaMovement();
		this.setDeltaMovement(afterMove.x * 0.91, afterMove.y * 0.98, afterMove.z * 0.91);

		// wall collision damage
		if (this.horizontalCollision && !this.level().isClientSide) {
			double newHSpeed = this.getDeltaMovement().horizontalDistance();
			double lostSpeed = Math.sqrt(resultX * resultX + resultZ * resultZ) - newHSpeed;
			float wallDmg = (float) (lostSpeed * 10.0 - 3.0);
			if (wallDmg > 0.0f) {
				// getFallDamageSound via @Shadow, playSound/hurt via Entity inheritance
				this.playSound(this.getFallDamageSound((int) wallDmg), 1.0f, 1.0f);
				this.hurt(this.damageSources().flyIntoWall(), wallDmg);
			}
		}

		// consume energy budget
		double distanceTraveled = resultSpeed;
		height -= distanceTraveled / glide_ratio;
		glide.setGlideCharge((float) (height + player.getY()));

		// fall damage reset if not plummeting
		if (this.getDeltaMovement().y > -0.7) {
			this.fallDistance = 0.0f;
		}

		// update last charge for next tick's particle check
		glide.setLastGlideCharge(glide.getGlideCharge());

		// ground landing
		if (this.onGround() && !this.level().isClientSide) {
			this.setSharedFlag(7, false);
			glide.stopGliding();
		}
		ci.cancel();
	}
}