package hojosa.relics_of_old.common.entity;

import hojosa.relics_of_old.client.particle.RelicsParticles;
import hojosa.relics_of_old.common.entity.attacks.ArrowStormEntity;
import hojosa.relics_of_old.common.entity.attacks.EnderBombEntity;
import hojosa.relics_of_old.common.entity.attacks.FireblastEntity;
import hojosa.relics_of_old.common.entity.attacks.QuakeEntity;
import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
import hojosa.relics_of_old.lib.RelicsUtil.ElementType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class MedallionEntity extends ThrowableItemProjectile {

	private static final EntityDataAccessor<String> DATA_MEDALLION_TYPE = SynchedEntityData.defineId(MedallionEntity.class, EntityDataSerializers.STRING);
	private ElementType medallionType;

	public MedallionEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level level) {
		super(pEntityType, level);
	}

	public MedallionEntity(Level pLevel, LivingEntity pShooter, ItemStack item, ElementType type) {
		super(RelicsEntities.MEDALLION.get(), pShooter, pLevel);
		this.medallionType = type;
		this.setItem(item);
		this.entityData.set(DATA_MEDALLION_TYPE, type.name());
	}

	@Override
	protected Item getDefaultItem() {
		return RelicsItems.FIRE_MEDALLION.get();
	}

	@Override
	public void tick() {
		super.tick();
		this.level().addParticle(new RelicsParticleOptions(RelicsParticles.RUNE_PARTICLE, 30, 0.15f), this.getX() + this.random.nextGaussian() * 0.1, this.getY() + this.random.nextGaussian() * 0.1,
				this.getZ() + this.random.nextGaussian() * 0.1, this.random.nextGaussian() * 0.03, this.random.nextGaussian() * 0.03, this.random.nextGaussian() * 0.03);
	}

	@Override
	protected void onHit(HitResult pResult) {
		if (!this.level().isClientSide && this.getMedallionType() != null) {
			switch (this.getMedallionType()) {
			case FIRE -> this.level().addFreshEntity(new FireblastEntity(this.level(), this.position()));
			case WIND -> {
				LivingEntity target = null;
				if (pResult instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity living) {
					target = living;
				}
				this.level().addFreshEntity(new ArrowStormEntity(this.level(), this.position(), (LivingEntity) this.getOwner(), target));
			}
			case EARTH -> this.level().addFreshEntity(new QuakeEntity(this.level(), this.position(), (LivingEntity) this.getOwner()));
			case ENDER -> this.level().addFreshEntity(new EnderBombEntity(this.level(), this.position()));
			default -> {
			}
			}
			this.level().playSound(null, blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 2.0f, 0.7f);
			this.discard();
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_MEDALLION_TYPE, "");
	}

	public ElementType getMedallionType() {
		String name = entityData.get(DATA_MEDALLION_TYPE);
		if (name.isEmpty())
			return null;
		return ElementType.valueOf(name);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (medallionType != null) {
			tag.putString("MedallionType", medallionType.name());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("MedallionType")) {
			this.medallionType = ElementType.valueOf(tag.getString("MedallionType"));
			this.entityData.set(DATA_MEDALLION_TYPE, medallionType.name());
		}
	}
}