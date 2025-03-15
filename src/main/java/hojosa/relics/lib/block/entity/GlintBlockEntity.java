package hojosa.relics.common.block.entity;

import hojosa.relics.common.init.RelicsBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.mantle.block.entity.MantleBlockEntity;

public class GlintBlockEntity extends MantleBlockEntity{

	public GlintBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public GlintBlockEntity(BlockPos pos, BlockState state) {
		super(RelicsBlockEntities.GLINT_BLOCK.get(), pos, state);
	}
	
}
