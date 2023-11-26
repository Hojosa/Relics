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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;


public class SwordPedestalBlockEntity extends BlockEntity
{
	public boolean repairUpgrade = false;
	private long swordPlacedTime;
	
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
	
	public ItemStack placeSword(ItemStack sword, long currentWorldTime) {
		if (this.itemHandler.isItemValid(0, sword)) {
			if(repairUpgrade) {
				this.swordPlacedTime = level.getGameTime();
			}
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
		System.out.println("repair? " + repairUpgrade);
		ItemStack item = this.itemHandler.getStackInSlot(0);
		if(repairUpgrade) {
			repairSword(item, currentGameTime);
		}
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
		if(tag.contains("Inventory")) {
			itemHandler.deserializeNBT(tag.getCompound("Inventory"));
		}
		if(tag.contains("Info")) {
			repairUpgrade = tag.getCompound("Info").getBoolean("Upgrade");
		}
		super.load(tag);
	}
	
	@Override
	public void saveAdditional(CompoundTag tag) {
		tag.put("Inventory", itemHandler.serializeNBT());
		
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
    
    private ItemStack repairSword(ItemStack sword, long worldTime) {
    	if(repairUpgrade && sword.getDamageValue() > 0) {
    		long repairValue = (worldTime-this.swordPlacedTime)/20/1000;
    		sword.setDamageValue(sword.getDamageValue()-(int)repairValue);
    		return sword;
    	}
    	else return sword;
    }
    
    public void upgradePedestal() {
    	this.repairUpgrade = true;
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
 
    
    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
    	return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? handler.cast() : super.getCapability(capability, facing);
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
