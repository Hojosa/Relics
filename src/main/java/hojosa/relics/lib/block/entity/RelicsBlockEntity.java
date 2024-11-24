package hojosa.relics.lib.block.entity;

import java.util.Objects;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import hojosa.relics.common.init.RelicsSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;


public class RelicsBlockEntity extends BlockEntity implements Container{
	private static final String ITEMS_TAG = "Inventory";
	protected final ItemStackHandler itemHandler;
	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
	
	public RelicsBlockEntity(BlockEntityType<?> type, int containerSize, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);	
    	itemHandler = new ItemStackHandler(containerSize) {
    		@Override
    		protected void onContentsChanged(int slot) {
    			super.onContentsChanged(slot);
    			setChanged();
                Objects.requireNonNull(level).sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    		}
    	};
      }
	
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }
	
	@Override
	public void load(CompoundTag tag) {
		if(tag.contains(ITEMS_TAG)) {
			itemHandler.deserializeNBT(tag.getCompound(ITEMS_TAG));
		}
		super.load(tag);
	}
	
	@Override
	public void saveAdditional(CompoundTag tag) {		
		super.saveAdditional(tag);
		tag.put(ITEMS_TAG, itemHandler.serializeNBT());
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
	public void clearContent() {
        for (int i = 0; i < getContainerSize(); i++) {
        	itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
	}

	@Override
	public int getContainerSize() {
		return itemHandler.getSlots();
	}

	@Override
	public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) return false;
        }
        return true;
	}

	@Override
	public ItemStack getItem(int pSlot) {
        return pSlot < getContainerSize() ? itemHandler.getStackInSlot(pSlot) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItem(int pSlot, int pAmount) {
        return pSlot < getContainerSize() ? itemHandler.getStackInSlot(pSlot).split(pAmount) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int pSlot) {
        if (pSlot < getContainerSize()) {
            ItemStack stack = itemHandler.getStackInSlot(pSlot);
            itemHandler.setStackInSlot(pSlot, ItemStack.EMPTY);
            return stack;
        }
        return ItemStack.EMPTY;
    }

	@Override
	public void setItem(int pSlot, ItemStack pStack) {
        if (pSlot < getContainerSize()) {
        	itemHandler.setStackInSlot(pSlot, pStack);
        	if(!pStack.is(Items.AIR))
        		level.playSound(null, worldPosition, RelicsSounds.SWORD_PLACE_SOUND.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        	this.setChanged();
        }
    }

	@Override
	public boolean stillValid(Player pPlayer) {
		  BlockPos pos = getBlockPos();
	      return Objects.requireNonNull(level).getBlockEntity(pos) == this && pPlayer.distanceToSqr(Vec3.atCenterOf(pos)) <= 64;
	}
	
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }
    
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
}
