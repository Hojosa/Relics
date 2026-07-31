package hojosa.relics_of_old.common.entity.attacks;

import java.util.List;

import hojosa.relics_of_old.client.particle.RelicsParticles;
import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
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
import net.minecraft.world.phys.Vec3;

public class FireblastEntity extends Entity {
	
	public static final int MAX_LIFESPAN = 70;
    public static final int DETONATION_TIME = 60;
    public static final double DETONATION_RADIUS = 8.0;
    public static final int DAMAGE_PER_HIT = 15;
    public static final int FIRE_DURATION = 200; // 10 seconds
    
    
    private static final EntityDataAccessor<Integer> DATA_LIFETIME = SynchedEntityData.defineId(FireblastEntity.class, EntityDataSerializers.INT);


    public FireblastEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    public FireblastEntity(Level pLevel, Vec3 pPos) {
        super(RelicsEntities.FIREBLAST.get(), pLevel);
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
                    level().addParticle(new RelicsParticleOptions(RelicsParticles.RUNE_PARTICLE, 20, 0.15f),
                            getX() + random.nextGaussian() * 0.3, getY(), getZ() + random.nextGaussian() * 0.3,
                            0.0, random.nextDouble(), 0.0);
                }
            } else {
                level().playSound(null, blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.HOSTILE, 3.0f, 1.0f);
            }
        }

        // charge-up spiral particles
        if (lifetime < DETONATION_TIME && level().isClientSide) {
            double progress = (double) lifetime / DETONATION_TIME;
            double decel = 1.0 - progress;
            decel = decel * decel * decel;
            decel = 1.0 - decel;
            double r = DETONATION_RADIUS * decel;
            double theta = decel * Math.PI * 4.0;

            for (int i = 0; i < 3; i++) {
                double ox = Math.cos(theta) * r;
                double oz = Math.sin(theta) * r;
                level().addParticle(RelicsParticles.FLARE_PARTICLE.get(),
                        getX() + ox, getY() + 0.5f, getZ() + oz,
                        0.0, 0.05, 0.0);
                theta += 2.0943951023931953; // 2π/3 = 120°
            }
        }

        // detonation
        if (lifetime == DETONATION_TIME && !level().isClientSide) {
            level().playSound(null, blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 3.0f, 0.8f);
            level().playSound(null, blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 3.0f, 1.0f);
            level().playSound(null, blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 3.0f, 1.2f);
            burnThings(DETONATION_RADIUS);
        }
        
        // aftermath particles
        if (lifetime > DETONATION_TIME && level().isClientSide) {
            for (int i = 0; i < 5; i++) {
                level().addParticle(RelicsParticles.FLARE_PARTICLE.get(),
                        getX() + random.nextGaussian() * DETONATION_RADIUS * 0.5, getY() + 0.5f,
                        getZ() + random.nextGaussian() * DETONATION_RADIUS * 0.5,
                        0.0, 0.5, 0.0);
                level().addParticle(ParticleTypes.LAVA,
                        getX() + random.nextGaussian() * DETONATION_RADIUS * 0.5, getY(),
                        getZ() + random.nextGaussian() * DETONATION_RADIUS * 0.5,
                        0.0, 0.5, 0.0);
            }
        }

        setLifetime(lifetime + 1);
        if (lifetime >= MAX_LIFESPAN) {
            discard();
        }
    }

    private void burnThings(double radius) {
        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class,
                getBoundingBox().inflate(radius));

        for (LivingEntity target : entities) {
            if (target.distanceTo(this) > radius) continue;
            target.setRemainingFireTicks(FIRE_DURATION);
            target.hurt(damageSources().onFire(), DAMAGE_PER_HIT);
        }
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
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Lifetime", getLifetime());
    } 
}