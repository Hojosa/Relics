package hojosa.relics.common.item;

import org.jetbrains.annotations.Nullable;

import hojosa.relics.common.item.entity.HeartItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Heart extends RelicsItem{

	public Heart() {
		super(1);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}
	
	@Override
	public @Nullable Entity createEntity(Level level, Entity location, ItemStack stack) {
		return new HeartItemEntity(level, location.getX(), location.getY(), location.getZ(), stack);
	}
}
