package hojosa.relics.lib.block.entity;

import java.util.Objects;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import slimeknights.mantle.block.entity.MantleBlockEntity;

public class RelicsBlockEntity extends MantleBlockEntity implements Container {
	public static final String ITEMS_TAG = "Inventory";
	protected final ItemStackHandler itemHandler;
	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

	public RelicsBlockEntity(BlockEntityType<?> type, int containerSize, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		itemHandler = new ItemStackHandler(containerSize) {
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
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
		super.load(tag);
		if (tag.contains(ITEMS_TAG)) {
			itemHandler.deserializeNBT(tag.getCompound(ITEMS_TAG));
		}
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
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public @NotNull CompoundTag getUpdateTag() {
		return this.serializeNBT();
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
			if (!itemHandler.getStackInSlot(i).isEmpty())
				return false;
		}
		return true;
	}
	
	public int emptySlots() {
		int count = 0;
		for (int i = 0; i < getContainerSize(); i++) {
			if (itemHandler.getStackInSlot(i).isEmpty())
				count++;
		}
		return count;
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
		}
	}
	
  public boolean isStackInSlot(int slot) {
	    return !this.getItem(slot).isEmpty();
	  }

	@Override
	public boolean stillValid(Player pPlayer) {
		return Objects.requireNonNull(level).getBlockEntity(getBlockPos()) == this && pPlayer.distanceToSqr(Vec3.atCenterOf(getBlockPos())) <= 64;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
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
