package hojosa.relics_of_old.common.block.entity;

import hojosa.relics_of_old.common.init.RelicsBlockEntities;
import hojosa.relics_of_old.lib.block.entity.RelicsBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PhoenixAltarBlockEntity extends RelicsBlockEntity {
	public PhoenixAltarBlockEntity(BlockPos pPos, BlockState pState) {
		super(RelicsBlockEntities.PHOENIX_ALTAR_BLOCK_ENTITY.get(), 1, pPos, pState);
	}
}