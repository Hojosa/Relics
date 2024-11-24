package hojosa.relics.common.block.entity;

import hojosa.relics.common.init.RelicsBlockEntities;
import hojosa.relics.common.init.RelicsTags;
import hojosa.relics.lib.block.entity.RelicsBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;


public class SwordPedestalBlockEntityNew extends RelicsBlockEntity
{
	public static final String INFUSED_TAG = "Infused";
	private boolean isInfused = false;
	
	public SwordPedestalBlockEntityNew(BlockPos blockPos, BlockState blockState) {
		super(RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), 1, blockPos, blockState);
	}
	
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.is(ItemTags.SWORDS) || stack.is(RelicsTags.Items.SWORD_PEDESTAL_SWORDS);
    }

    public boolean accpetsItem(ItemStack stack) {
        return stack.is(RelicsTags.Items.SWORD_PEDESTAL_INFUSEABLE);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
	
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if(tag.contains(INFUSED_TAG)) {
			isInfused = tag.getBoolean(INFUSED_TAG);
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putBoolean(INFUSED_TAG, isInfused);
	}
    
    public void repairSword() {
    	if(!this.isEmpty()){
    		ItemStack sword = this.itemHandler.extractItem(0, 1, false);
    		System.out.println("reparing value: " + sword.getDamageValue());
    		sword.setDamageValue(sword.getDamageValue() - 1);
    		this.itemHandler.insertItem(0, sword, false);
    	}
    }
    
    public boolean isSwordDamaged() {
    	return this.itemHandler.getStackInSlot(0).getDamageValue() > 0;
    }
    
    public void infusePedestal() {
    	this.isInfused = true;
    	level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
    
    public boolean isInfused() {
    	return this.isInfused;
    }
    
}
