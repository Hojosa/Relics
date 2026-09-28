package hojosa.relics_of_old.common.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class StarglassBlock extends AbstractGlassBlock {

	public StarglassBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.GLASS).strength(1.5f).lightLevel(state -> 3).noOcclusion().requiresCorrectToolForDrops());
	}

	@Override
	public boolean skipRendering(BlockState state, BlockState adjacentState, Direction direction) {
		if (adjacentState.is(this)) {
			return true;
		}
		return super.skipRendering(state, adjacentState, direction);
	}
}