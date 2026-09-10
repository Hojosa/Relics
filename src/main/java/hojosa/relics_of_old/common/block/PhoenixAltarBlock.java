package hojosa.relics_of_old.common.block;

import hojosa.relics_of_old.lib.block.RelicsBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PhoenixAltarBlock extends RelicsBlock {
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 12, 16);

	public PhoenixAltarBlock() {
		super(BlockBehaviour.Properties.of().strength(50.0f, 2000.0f).requiresCorrectToolForDrops().sound(SoundType.STONE).lightLevel(state -> 10).noOcclusion());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}