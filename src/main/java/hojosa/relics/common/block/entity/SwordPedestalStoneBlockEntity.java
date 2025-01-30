package hojosa.relics.common.block.entity;

import hojosa.relics.common.init.RelicsBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SwordPedestalStoneBlockEntity extends SwordPedestalBlockEntity{

	public SwordPedestalStoneBlockEntity(BlockPos pos, BlockState state) {
		super(RelicsBlockEntities.SWORD_PEDESTAL_STONE_BLOCK_ENTITY.get(), pos, state);
	}
}
