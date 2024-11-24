package hojosa.relics.common.block.entity;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import hojosa.relics.common.init.RelicsBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

@Deprecated
public class SwordPedestalBlockEntity extends BlockEntity
{
	public static final String ITEMS_TAG = "Inventory";
	public boolean repairUpgrade = false;
	
	private ItemStackHandler itemHandler = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
		
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack itemStack) {
			return itemStack.getItem() instanceof SwordItem;
		}
	};
	
	private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
	
	public SwordPedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), blockPos, blockState);
	}
	
	@Override
	public void setRemoved() {
		super.setRemoved();
		handler.invalidate();
	}
	
	public ItemStack placeSword(ItemStack sword) {
		if (this.itemHandler.isItemValid(0, sword)) {
			this.itemHandler.setStackInSlot(0, sword);
			this.setChanged();
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
			return new ItemStack(Items.AIR);
		}
		else return sword;
	}
	
	public boolean canPlaceSword(ItemStack sword) {
		return itemHandler.isItemValid(0, sword);
	}
	
	//returns the sword to the player
	public ItemStack returnSword(long currentGameTime) {
		ItemStack item = this.itemHandler.getStackInSlot(0);

		this.itemHandler.setStackInSlot(0, new ItemStack(Items.AIR));
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
		return item;
	}
	
	//get copy of placed sword
	public ItemStack getSword() {
		ItemStack item = this.itemHandler.getStackInSlot(0);
		return item;
	}
	
	@Override
	public void load(CompoundTag tag) {
		if(tag.contains(ITEMS_TAG)) {
			itemHandler.deserializeNBT(tag.getCompound(ITEMS_TAG));
		}
		if(tag.contains("Info")) {
			repairUpgrade = tag.getCompound("Info").getBoolean("Upgrade");
		}
		super.load(tag);
	}
	
	@Override
	public void saveAdditional(CompoundTag tag) {
		tag.put(ITEMS_TAG, itemHandler.serializeNBT());
		
		CompoundTag upgradeTag = new CompoundTag();
		upgradeTag.putBoolean("Upgrade", repairUpgrade);
		tag.put("Info", upgradeTag);

		super.saveAdditional(tag);
	}
	
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }
    
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        handler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        handler.invalidate();
    }
    
    public void repairSword() {
    	if(isStackInSlot()){
    		ItemStack sword = this.itemHandler.extractItem(0, 1, false);
    		System.out.println("reparing value: " + sword.getDamageValue());
    		sword.setDamageValue(sword.getDamageValue() - 1);
    		this.itemHandler.insertItem(0, sword, false);
    	}
    }
    
    public boolean isSwordDamaged() {
    	return this.itemHandler.getStackInSlot(0).getDamageValue() > 0;
    }
    
    
    public void upgradePedestal() {
    	this.repairUpgrade = true;
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
    
    public boolean hasUpgrade(){
    	return repairUpgrade;
    }
    
    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
    	return capability == ForgeCapabilities.ITEM_HANDLER ? handler.cast() : super.getCapability(capability, facing);
    }
	
	public boolean isStackInSlot() {
		return !this.itemHandler.getStackInSlot(0).isEmpty();
	}
	
    public void dropItems() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        inventory.setItem(0, itemHandler.getStackInSlot(0));

        Containers.dropContents(
                Objects.requireNonNull(this.getLevel()),
                this.worldPosition,
                inventory
        );
    }
}
