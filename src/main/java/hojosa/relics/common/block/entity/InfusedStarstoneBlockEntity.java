package hojosa.relics.common.block.entity;

import javax.annotation.Nullable;

import hojosa.relics.common.init.RelicsBlockEntities;
import hojosa.relics.common.recipes.MagicInfusionRecipce;
import hojosa.relics.lib.block.entity.RelicsBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;

public class InfusedStarstoneBlockEntity extends RelicsBlockEntity{
    private int progress;
    private boolean active;

    @Nullable
    private MagicInfusionRecipce lastRecipe;

	public InfusedStarstoneBlockEntity(BlockPos pPos, BlockState pState) {
		super(RelicsBlockEntities.INFUSED_STARSTONE_BLOCK_ENTITY.get(), 4, pPos, pState);
	}

	public void tick() {
        if (!(this.emptySlots() == 0)) {
            this.reset();
            this.setChangedFast();
            return;
        }
        if (this.isActive()) {
        	var recipe = this.getActiveRecipe();
 
            if (recipe != null) {
            	this.progress++;

            	 if (this.progress >= 100) {
            		 for(int i = 0; i < this.getContainerSize(); i++) {
            			 this.removeItem(i, 1);
            		 }
                   this.level.addFreshEntity(new ItemEntity(this.level, this.getBlockPos().getX()+0.5, this.getBlockPos().getY()+1, this.getBlockPos().getZ()+0.5, recipe.getResultItem(null)));
                   level.markAndNotifyBlock(this.getBlockPos(), level.getChunkAt(getBlockPos()), getBlockState(), getBlockState(), 2, 1);
                   this.lastRecipe = recipe;
            	 }
            	 else {
            		 //particles todo
            	 }
            }
            else {
            	this.reset();
            }
        }
        this.setChangedFast();
	}

    public boolean isActive() {
        if (!this.active) {
            this.active = this.level != null && this.level.hasNeighborSignal(this.getBlockPos());
        }
        return this.active;
    }
    
    @Override
    public void load(CompoundTag tag) {
    	super.load(tag);
        this.progress = tag.getInt("Progress");
        this.active = tag.getBoolean("Active");
    }
    
    @Override
    public void saveAdditional(CompoundTag tag) {
    	super.saveAdditional(tag);
        tag.putInt("Progress", this.progress);
        tag.putBoolean("Active", this.active);
    }
    
    private void reset() {
        this.progress = 0;
        this.active = false;
    }
    
    public MagicInfusionRecipce getActiveRecipe() {
        if (this.level == null)
            return null;
        
        if(lastRecipe != null && lastRecipe.matches(this, level))
        	return lastRecipe;

        return this.level.getRecipeManager().getRecipeFor(MagicInfusionRecipce.Type.INSTANCE, this, level).orElse(null);
    }
}
