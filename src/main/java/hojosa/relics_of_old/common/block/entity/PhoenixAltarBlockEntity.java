package hojosa.relics_of_old.common.block.entity;

import hojosa.relics_of_old.common.init.RelicsBlockEntities;
import hojosa.relics_of_old.lib.block.entity.RelicsBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class PhoenixAltarBlockEntity extends RelicsBlockEntity {
	private Direction itemFacing = Direction.SOUTH;

	public PhoenixAltarBlockEntity(BlockPos pPos, BlockState pState) {
		super(RelicsBlockEntities.PHOENIX_ALTAR_BLOCK_ENTITY.get(), 1, pPos, pState);
	}

	public Direction getItemFacing() {
		return itemFacing;
	}

	public void setItemFacing(Direction facing) {
		this.itemFacing = facing;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("ItemFacing"))
			this.itemFacing = Direction.from2DDataValue(tag.getInt("ItemFacing"));
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("ItemFacing", itemFacing.get2DDataValue());
	}
}