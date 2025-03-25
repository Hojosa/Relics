package hojosa.relics.common.block;

import hojosa.relics.lib.ShapeUtil;
import hojosa.relics.lib.block.RetexturedSwordPedestal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RelicRetexturedSwordPedestal extends RetexturedSwordPedestal {
	private static final VoxelShape PEDESTAL_SHAPE = Block.box(2.0D, 0.0D, 5.0D, 14.0D, 6.0D, 11.0D).optimize();
	private static final VoxelShape SWORD_SHAPE = Shapes.or(Block.box(2, 6, 7.5, 14, 25.2, 8.5), PEDESTAL_SHAPE).optimize();

	public RelicRetexturedSwordPedestal(Properties builder) {
		super(builder, 0);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(SWORD).booleanValue() ? state.getValue(FACING).getAxis() == Direction.Axis.X ? ShapeUtil.rotate(SWORD_SHAPE, Rotation.CLOCKWISE_90) : SWORD_SHAPE
				: state.getValue(FACING).getAxis() == Direction.Axis.X ? ShapeUtil.rotate(PEDESTAL_SHAPE, Rotation.CLOCKWISE_90) : PEDESTAL_SHAPE;
	}
}
