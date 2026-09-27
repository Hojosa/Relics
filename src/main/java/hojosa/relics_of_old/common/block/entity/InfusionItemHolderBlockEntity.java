package hojosa.relics_of_old.common.block.entity;

import hojosa.relics_of_old.common.init.RelicsBlockEntities;
import hojosa.relics_of_old.lib.block.entity.RelicsBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class InfusionItemHolderBlockEntity extends RelicsBlockEntity {
	private BlockPos locusPos;
	public float itemRotation;

	public InfusionItemHolderBlockEntity(BlockPos pos, BlockState state) {
		super(RelicsBlockEntities.INFUSION_ITEM_HOLDER_BLOCK_ENTITY.get(), 1, pos, state);
	}

	public BlockPos getLocusPos() {
		return locusPos;
	}

	public void setLocusPos(BlockPos pos) {
		this.locusPos = pos;
		setChanged();
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("LocusX")) {
			locusPos = new BlockPos(tag.getInt("LocusX"), tag.getInt("LocusY"), tag.getInt("LocusZ"));
		}
		if (tag.contains("ItemRotation")) {
			itemRotation = tag.getFloat("ItemRotation");
		}
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (locusPos != null) {
			tag.putInt("LocusX", locusPos.getX());
			tag.putInt("LocusY", locusPos.getY());
			tag.putInt("LocusZ", locusPos.getZ());
			tag.putFloat("ItemRotation", itemRotation);
		}
	}
}