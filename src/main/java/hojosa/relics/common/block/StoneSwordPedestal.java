package hojosa.relics.common.block;

import hojosa.relics.common.block.entity.SwordPedestalStoneBlockEntity;
import hojosa.relics.lib.block.SwordPedestalBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StoneSwordPedestal extends SwordPedestalBaseBlock {

	private final VoxelShape PEDESTAL_SHAPE = Block.box(1.3, 0, 1.6, 14.7, 16, 13.7).optimize();

	public StoneSwordPedestal(Properties builder) {
		super(builder, 0.0);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return PEDESTAL_SHAPE;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SwordPedestalStoneBlockEntity(pos, state);
	}
}
