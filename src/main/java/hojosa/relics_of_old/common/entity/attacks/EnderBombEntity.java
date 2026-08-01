package hojosa.relics_of_old.common.entity.attacks;

import java.util.List;

import hojosa.relics_of_old.client.particle.RelicsParticles;
import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EnderBombEntity extends Entity {

	public static final int EXPAND_TIME = 110;
	public static final int COLLAPSE_TIME = 10;
	public static final double MAX_RADIUS = 10.0;

	private static final EntityDataAccessor<Integer> DATA_LIFETIME = SynchedEntityData.defineId(EnderBombEntity.class, EntityDataSerializers.INT);

	public EnderBombEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.noPhysics = true;
	}

	public EnderBombEntity(Level pLevel, Vec3 pPos) {
		super(RelicsEntities.ENDER_BOMB.get(), pLevel);
		this.setPos(pPos);
		this.noPhysics = true;
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
				level().playSound(null, blockPosition(), SoundEvents.ENDERMAN_STARE, SoundSource.HOSTILE, 3.0f, 2.0f);
			}
		}

		if (lifetime >= EXPAND_TIME) {
			int collapseProgress = lifetime - EXPAND_TIME;
			double radius = MAX_RADIUS * (double) (COLLAPSE_TIME - collapseProgress) / COLLAPSE_TIME;

			if (!level().isClientSide) {
				List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(MAX_RADIUS));

				for (LivingEntity target : entities) {
					double dist = target.distanceTo(this);
					if (dist > MAX_RADIUS || dist < radius)
						continue;

					double randX = target.getX() + random.nextGaussian() * 20.0;
					double randZ = target.getZ() + random.nextGaussian() * 20.0;
					double randY = target.getY() + 18.0;

					teleportEntityTo(target, randX, randY, randZ);
				}
			}

			if (level().isClientSide) {
				int count = 8 + collapseProgress * 3;
				for (int i = 0; i < count; i++) {
					double th = random.nextDouble() * Math.PI * 2.0;
					double ox = Math.cos(th) * radius;
					double oz = Math.sin(th) * radius;
					level().addParticle(ParticleTypes.PORTAL, getX() + ox, getY() + random.nextDouble() * 1.5, getZ() + oz, -ox * 0.15, 0.1, -oz * 0.15);
				}

				if (collapseProgress >= COLLAPSE_TIME - 1) {
					for (int i = 0; i < 80; i++) {
						double th = random.nextDouble() * Math.PI * 2.0;
						double phi = random.nextDouble() * Math.PI;
						double speed = 0.5 + random.nextDouble() * 0.5;
						level().addParticle(ParticleTypes.REVERSE_PORTAL, getX(), getY() + 0.5, getZ(), Math.cos(th) * Math.sin(phi) * speed, Math.cos(phi) * speed + 0.5, Math.sin(th) * Math.sin(phi) * speed);
					}
				}
			}
		}

		setLifetime(lifetime + 1);
		if (lifetime > EXPAND_TIME + COLLAPSE_TIME) {
			discard();
		}
	}

	private void teleportEntityTo(LivingEntity entity, double x, double y, double z) {
		double oldX = entity.getX();
		double oldY = entity.getY();
		double oldZ = entity.getZ();

		// find solid ground at the target position
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos((int) x, (int) y, (int) z);
		while (mutablePos.getY() > level().getMinBuildHeight()) {
			mutablePos.move(0, -1, 0);
			BlockState state = level().getBlockState(mutablePos);
			if (state.blocksMotion()) {
				// found ground — place entity on top
				double finalY = mutablePos.getY() + 1.0;
				entity.teleportTo(x, finalY, z);

				if (level().noCollision(entity) && !level().containsAnyLiquid(entity.getBoundingBox())) {
					// valid position — spawn trail particles
					spawnTeleportTrail(oldX, oldY, oldZ, entity);
					level().playSound(null, BlockPos.containing(oldX, oldY, oldZ), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0f, 1.0f);
					level().playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0f, 1.0f);
					return;
				}
			}
		}
		// no valid position found — put them back
		entity.teleportTo(oldX, oldY, oldZ);
	}

	private void spawnTeleportTrail(double oldX, double oldY, double oldZ, LivingEntity entity) {
		for (int i = 0; i < 128; i++) {
			double progress = (double) i / 127.0;
			double px = oldX + (entity.getX() - oldX) * progress + (random.nextDouble() - 0.5) * entity.getBbWidth() * 2.0;
			double py = oldY + (entity.getY() - oldY) * progress + random.nextDouble() * entity.getBbHeight();
			double pz = oldZ + (entity.getZ() - oldZ) * progress + (random.nextDouble() - 0.5) * entity.getBbWidth() * 2.0;
			level().addParticle(ParticleTypes.PORTAL, px, py, pz, (random.nextFloat() - 0.5f) * 0.2, (random.nextFloat() - 0.5f) * 0.2, (random.nextFloat() - 0.5f) * 0.2);
		}
	}

	public int getLifetime() {
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
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putInt("Lifetime", getLifetime());
	}
}