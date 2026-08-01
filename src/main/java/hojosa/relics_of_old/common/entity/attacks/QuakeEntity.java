package hojosa.relics_of_old.common.entity.attacks;

import java.util.List;
import java.util.UUID;

import hojosa.relics_of_old.client.particle.RelicsParticles;
import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class QuakeEntity extends Entity {

	public static final int MAX_LIFESPAN = 60;
	public static final int PULSE_INTERVAL = 7;
	public static final double FIRST_PULSE_RADIUS = 1.0;
	public static final double RADIUS_PER_TICK = 0.2;
	public static final double VERTICAL_LAUNCH = 0.5;
	public static final double HORIZONTAL_LAUNCH = 0.3;
	public static final int DAMAGE_PER_HIT = 7;

	private static final EntityDataAccessor<Integer> DATA_LIFETIME = SynchedEntityData.defineId(QuakeEntity.class, EntityDataSerializers.INT);

	private UUID throwerUUID;

	public QuakeEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.noPhysics = true;
	}

	public QuakeEntity(Level pLevel, Vec3 pPos, LivingEntity thrower) {
		super(RelicsEntities.QUAKE.get(), pLevel);
		this.setPos(pPos);
		this.noPhysics = true;
		if (thrower != null)
			this.throwerUUID = thrower.getUUID();
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
			} else {
				level().playSound(null, blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 2.0f, 0.3f);
			}
		}

		if (lifetime % PULSE_INTERVAL == 0) {
			double radius = FIRST_PULSE_RADIUS + RADIUS_PER_TICK * lifetime;
			doPulse(radius);
		}

		setLifetime(lifetime + 1);
		if (lifetime >= MAX_LIFESPAN) {
			discard();
		}
	}

	private void doPulse(double radius) {
		if (level().isClientSide) {
			for (int i = 0; i < 12; i++) {
				double th = (double) i * Math.PI * 2.0 / 12.0;
				double vx = Math.cos(th);
				double vz = Math.sin(th);
				level().addParticle(ParticleTypes.EXPLOSION, getX() + vx * radius, getY(), getZ() + vz * radius, vx * 0.05, 0.2, vz * 0.05);
			}
		} else {
			Entity thrower = getThrower();
			List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(radius));

			for (LivingEntity target : entities) {
				if (!target.onGround())
					continue;
				if (target.distanceTo(this) > radius)
					continue;
				if (target.equals(thrower))
					continue;

				target.hurt(damageSources().indirectMagic(this, thrower), DAMAGE_PER_HIT);
				target.push(random.nextGaussian() * HORIZONTAL_LAUNCH, VERTICAL_LAUNCH, random.nextGaussian() * HORIZONTAL_LAUNCH);
				target.hurtMarked = true;
			}
		}
	}

	private Entity getThrower() {
		if (throwerUUID != null && level() instanceof ServerLevel serverLevel) {
			return serverLevel.getEntity(throwerUUID);
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
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putInt("Lifetime", getLifetime());
		if (throwerUUID != null)
			tag.putUUID("Thrower", throwerUUID);
	}

}
