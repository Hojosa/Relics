package hojosa.relics_of_old.lib.block;

import javax.annotation.Nullable;

import hojosa.relics_of_old.lib.item.SelfPlacingtemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class SelfPlacingBlockItem extends BlockItem {

	public SelfPlacingBlockItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public @Nullable Entity createEntity(Level pLevel, Entity pLocation, ItemStack pStack) {
		SelfPlacingtemEntity entity = new SelfPlacingtemEntity(pLevel, pLocation.getX(), pLocation.getY(), pLocation.getZ(), pStack);
		entity.setDeltaMovement(pLocation.getDeltaMovement());
		if (pLocation instanceof ItemEntity original && original.getOwner() instanceof Player) {
			entity.setPickUpDelay(40);
		}
		return entity;
	}
}