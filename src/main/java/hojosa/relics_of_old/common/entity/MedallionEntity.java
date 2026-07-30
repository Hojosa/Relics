package hojosa.relics_of_old.common.entity;

import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.item.EmptyMedallion.MedallionType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class MedallionEntity extends ThrowableItemProjectile {
	
	private MedallionType type;
	
	public MedallionEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level level) {
		super(pEntityType, level);
	}

	public MedallionEntity(Level pLevel, LivingEntity pShooter, ItemStack item, MedallionType type) {
		super(RelicsEntities.MEDALLION.get(), pShooter, pLevel);
		this.type = type;
		this.setItem(item);
	}

	@Override
	protected Item getDefaultItem() {
		return RelicsItems.FIRE_MEDALLION.get();
	}
	
	@Override
	public void tick() {
		//todo movement of charge shot
	}
	
	@Override
	protected void onHit(HitResult pResult) {
		//todo cast magic effect
	}

}