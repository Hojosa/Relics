package hojosa.relics.common.block;

import hojosa.relics.common.init.RelicsSounds;
import hojosa.relics.lib.ShapeUtil;
import hojosa.relics.lib.block.SwordPedestalBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FancySwordPedestal extends SwordPedestalBaseBlock {

	private final VoxelShape PEDESTAL_SHAPE;
	private final VoxelShape SWORD_SHAPE;

	public FancySwordPedestal(Properties builder, double renderOffSet, VoxelShape pedestalShape, VoxelShape swordShape, boolean fancySound) {
		super(builder, renderOffSet);
		this.PEDESTAL_SHAPE = pedestalShape;
		this.SWORD_SHAPE = swordShape;
		if(fancySound) {
			this.placeSound = RelicsSounds.FANCY_SWORD_PLACE_SOUND.get();
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(SWORD).booleanValue() ? state.getValue(FACING).getAxis() == Direction.Axis.X ? ShapeUtil.rotate(SWORD_SHAPE, Rotation.CLOCKWISE_90) : SWORD_SHAPE
				: state.getValue(FACING).getAxis() == Direction.Axis.X ? ShapeUtil.rotate(PEDESTAL_SHAPE, Rotation.CLOCKWISE_90) : PEDESTAL_SHAPE;
	}
}
