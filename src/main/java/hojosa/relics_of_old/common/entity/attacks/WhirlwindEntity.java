package hojosa.relics_of_old.common.entity.attacks;

import java.util.List;
import java.util.UUID;

import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsParticles;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WhirlwindEntity extends Entity {

	private static final int MAX_LIFETIME = 30;
	private static final EntityDataAccessor<Integer> DATA_LIFETIME = SynchedEntityData.defineId(WhirlwindEntity.class, EntityDataSerializers.INT);

	private UUID throwerUUID;
	private Vec3 velocity;

	public WhirlwindEntity(EntityType<?> type, Level level) {
		super(type, level);
		this.noPhysics = false;
	}

	public WhirlwindEntity(Level level, LivingEntity thrower) {
		super(RelicsEntities.WHIRLWIND.get(), level);
		this.throwerUUID = thrower.getUUID();

		setPos(thrower.getX(), thrower.getEyeY(), thrower.getZ());

		// fire in look direction
		Vec3 look = thrower.getLookAngle();
		this.velocity = look.scale(0.8);
		setDeltaMovement(velocity);
	}

	@Override
    public void tick() {
            super.tick();
            int lifetime = getLifetime();

            // movement
            move(MoverType.SELF, getDeltaMovement());

            // spiral particles (client)
            if (level().isClientSide) {
                    Vec3 forward = getDeltaMovement().normalize();
                    Vec3 right = forward.cross(new Vec3(0, 1, 0)).normalize();
                    // handle edge case: looking straight up/down
                    if (right.length() < 0.001) right = forward.cross(new Vec3(1, 0, 0)).normalize();
                    Vec3 up = right.cross(forward).normalize();

                    double theta = lifetime * -0.4;
                    for (int i = 0; i < 3; i++) {
                            double lx = Math.cos(theta);
                            double ly = Math.sin(theta);
                            Vec3 out = right.scale(lx).add(up.scale(ly));

                            if (!isInWater()) {
                                    level().addParticle(new RelicsParticleOptions(RelicsParticles.SPARKLE_PARTICLES, 6, 0.1f, 1f, 1f, 1f),
                                                    getX() + out.x, getY() + out.y, getZ() + out.z,
                                                    out.x * 0.3, out.y * 0.3, out.z * 0.3);
                                    level().addParticle(ParticleTypes.POOF,
                                                    getX() + out.x * 0.5, getY() + out.y * 0.5, getZ() + out.z * 0.5,
                                                    out.x * 0.3, out.y * 0.3, out.z * 0.3);
                            } else {
                                    level().addParticle(ParticleTypes.BUBBLE,
                                                    getX() + out.x * 0.5, getY() + out.y * 0.5, getZ() + out.z * 0.5,
                                                    out.x * 0.3, out.y * 0.3, out.z * 0.3);
                            }
                            theta += 2.0943951023931953; // 120 degrees
                    }
            }
            // push nearby entities along with the whirlwind
            if (!level().isClientSide) {
            	if (lifetime == 0) {
            	      level().playSound(null, blockPosition(), RelicsSounds.WHIRLWIND.get(), SoundSource.PLAYERS, 1.0f, 0.8f + (float) Math.random() * 0.4f);
            	  }

                    List<LivingEntity> nearby = level().getEntitiesOfClass(LivingEntity.class,
                                    getBoundingBox().inflate(1.0));
                    Entity thrower = getThrower();
                    for (LivingEntity ent : nearby) {
                            if (ent.equals(thrower)) continue;
                            ent.setDeltaMovement(getDeltaMovement());
                            ent.hurtMarked = true;
                    }
            }

            // die on block collision or timeout
            if (horizontalCollision || verticalCollision) {
                    discard();
                    return;
            }

            setLifetime(lifetime + 1);
            if (lifetime >= MAX_LIFETIME) {
                    discard();
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