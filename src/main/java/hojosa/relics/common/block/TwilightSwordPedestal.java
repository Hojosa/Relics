package hojosa.relics.common.block;

import hojosa.relics.common.init.RelicsSounds;
import hojosa.relics.lib.ShapeUtil;
import hojosa.relics.lib.block.SwordPedestalBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TwilightSwordPedestal extends SwordPedestalBaseBlock {
	private static final VoxelShape PEDESTAL_SHAPE = Block.box(0.15D, 0.0D, 2.4D, 15.9D, 4D, 13.5D).optimize();
	private static final VoxelShape SWORD_SHAPE = Shapes.or(Block.box(2, 3.9, 7.5, 14, 23.2, 8.5), PEDESTAL_SHAPE).optimize();

	public TwilightSwordPedestal(Properties builder) {
		super(builder, 0.13);
		this.drawSound = RelicsSounds.TP_SWORD_DRAW.get();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(SWORD).booleanValue() ? state.getValue(FACING).getAxis() == Direction.Axis.X ? ShapeUtil.rotate(SWORD_SHAPE, Rotation.CLOCKWISE_90) : SWORD_SHAPE
				: state.getValue(FACING).getAxis() == Direction.Axis.X ? ShapeUtil.rotate(PEDESTAL_SHAPE, Rotation.CLOCKWISE_90) : PEDESTAL_SHAPE;
	}
}
