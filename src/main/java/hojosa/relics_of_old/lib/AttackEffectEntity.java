package hojosa.relics_of_old.lib;

import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public abstract class AttackEffectEntity extends Entity {

	private static final EntityDataAccessor<Integer> DATA_LIFETIME = SynchedEntityData.defineId(AttackEffectEntity.class, EntityDataSerializers.INT);

	protected UUID throwerUUID;
	private LivingEntity cachedThrower;

	public AttackEffectEntity(EntityType<?> type, Level level) {
		super(type, level);
		this.noPhysics = true;
	}

	public int getLifetime() {
		return entityData.get(DATA_LIFETIME);
	}

	protected void setLifetime(int value) {
		entityData.set(DATA_LIFETIME, value);
	}

	protected void advanceLifetime() {
		setLifetime(getLifetime() + 1);
	}

	public abstract int getMaxLifespan();

	protected void setThrower(LivingEntity thrower) {
		if (thrower != null) {
			this.throwerUUID = thrower.getUUID();
			this.cachedThrower = thrower;
		}
	}

	protected LivingEntity getThrower() {
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

	@Override
	protected void defineSynchedData() {
		entityData.define(DATA_LIFETIME, 0);
		defineExtraData();
	}

	// override to register additional synced data
	protected void defineExtraData() {
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		setLifetime(tag.getInt("Lifetime"));
		if (tag.hasUUID("Thrower"))
			throwerUUID = tag.getUUID("Thrower");
		readExtraSaveData(tag);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putInt("Lifetime", getLifetime());
		if (throwerUUID != null)
			tag.putUUID("Thrower", throwerUUID);
		writeExtraSaveData(tag);
	}

	// override for additional NBT
	protected void readExtraSaveData(CompoundTag tag) {
	}

	protected void writeExtraSaveData(CompoundTag tag) {
	}
}