package hojosa.relics_of_old.common.entity;

import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class PingEntity extends Entity {

	private static final EntityDataAccessor<Integer> DATA_ENERGY = SynchedEntityData.defineId(PingEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(PingEntity.class, EntityDataSerializers.INT);

    private int age = 0;

    public PingEntity(EntityType<?> type, Level level) {
            super(type, level);
            this.noCulling = true;
    }

    public PingEntity(Level level, double x, double y, double z, int energy, int color) {
            this(RelicsEntities.PING.get(), level);
            this.setPos(x, y, z);
            this.entityData.set(DATA_ENERGY, energy);
            this.entityData.set(DATA_COLOR, color);
    }

    @Override
    protected void defineSynchedData() {
            this.entityData.define(DATA_ENERGY, 500);
            this.entityData.define(DATA_COLOR, 0);
    }

    @Override
    public void tick() {
            super.tick();
            // Play chime on first tick
            if (age == 0 && !level().isClientSide) {
                    level().playSound(null, getX(), getY(), getZ(), RelicsSounds.RING_0.get(), SoundSource.PLAYERS, 20.0f, 1.0f);
                    level().playSound(null, getX(), getY(), getZ(), RelicsSounds.RING_0.get(), SoundSource.PLAYERS, 20.0f, 1.5f);
            }
            age++;
            int energy = entityData.get(DATA_ENERGY) - 1;
            entityData.set(DATA_ENERGY, energy);
            if (energy <= 0) {
                    discard();
            }
    }

    public int getEnergy() {
            return entityData.get(DATA_ENERGY);
    }

    public int getColor() {
            return entityData.get(DATA_COLOR);
    }

    public int getAge() {
            return age;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
            entityData.set(DATA_COLOR, tag.getInt("color"));
            entityData.set(DATA_ENERGY, tag.getInt("energy"));
            age = tag.getInt("age");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
            tag.putInt("color", entityData.get(DATA_COLOR));
            tag.putInt("energy", entityData.get(DATA_ENERGY));
            tag.putInt("age", age);
    }
}