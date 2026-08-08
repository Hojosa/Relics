package hojosa.relics_of_old.common.block.entity;

import hojosa.relics_of_old.common.init.RelicsBlockEntities;
import hojosa.relics_of_old.lib.block.entity.RelicsRetexturedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ClayJarBlockEntity extends RelicsRetexturedBlockEntity {

	public ClayJarBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(RelicsBlockEntities.CLAY_JAR_ENTITY.get(), 1, blockPos, blockState);
	}
	
	public ClayJarBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
		super(type, 1, blockPos, blockState);
	}
}