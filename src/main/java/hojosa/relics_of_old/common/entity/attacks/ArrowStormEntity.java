package hojosa.relics_of_old.common.entity.attacks;

import java.util.UUID;

import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsParticles;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ArrowStormEntity extends Entity {

	public static final int MAX_LIFESPAN = 50;
	public static final double SPREAD_RADIUS = 4.0;
	public static final double ARROW_DAMAGE = 5.0;
	private UUID targetUUID;
	private LivingEntity cachedTarget;

	private static final EntityDataAccessor<Integer> DATA_LIFETIME = SynchedEntityData.defineId(ArrowStormEntity.class, EntityDataSerializers.INT);

	private UUID throwerUUID;
	private LivingEntity cachedThrower;

	public ArrowStormEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.noPhysics = true;
	}

	public ArrowStormEntity(Level pLevel, Vec3 pPos, LivingEntity thrower, LivingEntity target) {
		super(RelicsEntities.ARROW_STORM.get(), pLevel);
		this.setPos(pPos);
		this.noPhysics = true;
		this.throwerUUID = thrower.getUUID();
		this.cachedThrower = thrower;
		if (target != null) {
			this.targetUUID = target.getUUID();
			this.cachedTarget = target;
		}

	}

	@Override
	public void tick() {
		super.tick();
		int lifetime = getLifetime();

		if (lifetime == 0) {
			if (level().isClientSide) {
				for (int i = 0; i < 15; i++) {
					level().addParticle(new RelicsParticleOptions(RelicsParticles.RUNE_PARTICLE, 30, 0.15f), getX() + random.nextGaussian() * 0.3, getY(), getZ() + random.nextGaussian() * 0.3, 0.0, random.nextDouble(),
							0.0);
				}
			}
		}

		if (!level().isClientSide && getThrower() != null) {
			LivingEntity target = getTarget();
			if (target != null && target.isAlive()) {
				this.setPos(target.getX(), target.getY(), target.getZ());
			}

			spawnArrow();
		}

		if (level().isClientSide) {
			// swirling wind particles around the storm
			double theta = lifetime * 0.5;
			for (int i = 0; i < 2; i++) {
				double ox = Math.cos(theta) * SPREAD_RADIUS * 0.5;
				double oz = Math.sin(theta) * SPREAD_RADIUS * 0.5;
				level().addParticle(RelicsParticles.SPARKLE_PARTICLES.get(), getX() + ox, getY() + 2.0 + random.nextDouble(), getZ() + oz, 0.0, -0.3, 0.0);
				theta += Math.PI;
			}
		}

		setLifetime(lifetime + 1);
		if (lifetime >= MAX_LIFESPAN) {
			discard();
		}
	}

	private void spawnArrow() {
		LivingEntity thrower = getThrower();
		if (thrower == null)
			return;

		Arrow arrow = new Arrow(level(), thrower);
		arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
		arrow.setBaseDamage(ARROW_DAMAGE);

		// spawn above the thrower, aim toward impact point
		arrow.setPos(thrower.getX(), thrower.getY() + 3.0, thrower.getZ());
		double dist = distanceTo(arrow);
		double vx = getX() - arrow.getX();
		double vy = getY() + dist * 0.05 * 2.5 - arrow.getY();
		double vz = getZ() - arrow.getZ();
		arrow.shoot(vx, vy, vz, 2.5f, 12.0f);

		level().addFreshEntity(arrow);
		level().playSound(null, thrower.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.1f, 1.0f / (random.nextFloat() * 0.4f + 1.2f) + 0.5f);
	}

	private LivingEntity getThrower() {
		if (cachedThrower != null && !cachedThrower.isRemoved())
			return cachedThrower;
		if (throwerUUID != null && level() instanceof ServerLevel serverLevel) {
			Entity e = serverLevel.getEntity(throwerUUID);
			if (e instanceof LivingEntity living) {
				cachedThrower = living;
				return living;
			}
		}
		return null;
	}

	private LivingEntity getTarget() {
		if (cachedTarget != null && !cachedTarget.isRemoved())
			return cachedTarget;
		if (targetUUID != null && level() instanceof ServerLevel serverLevel) {
			Entity e = serverLevel.getEntity(targetUUID);
			if (e instanceof LivingEntity living) {
				cachedTarget = living;
				return living;
			}
		}
		return null;
	}

	private int getLifetime() {
		return entityData.get(DATA_LIFETIME);
	}

	private void setLifetime(int value) {
		entityData.set(DATA_LIFETIME, value);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(DATA_LIFETIME, 0);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		setLifetime(tag.getInt("Lifetime"));
		if (tag.hasUUID("Thrower"))
			throwerUUID = tag.getUUID("Thrower");
		if (tag.hasUUID("Target"))
			targetUUID = tag.getUUID("Target");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putInt("Lifetime", getLifetime());
		if (throwerUUID != null)
			tag.putUUID("Thrower", throwerUUID);
		if (targetUUID != null)
			tag.putUUID("Target", targetUUID);
	}
}