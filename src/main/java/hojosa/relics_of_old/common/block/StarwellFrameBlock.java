package hojosa.relics_of_old.common.block;

import hojosa.relics_of_old.lib.block.RelicsNormalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class StarwellFrameBlock extends RelicsNormalBlock {
	public StarwellFrameBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.STONE).strength(50.0f, 2000f).sound(SoundType.STONE));
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		if (!level.isClientSide) {
			notifyNearbyCores(level, pos);
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		// only on actual removal, not state changes
		if (!state.is(newState.getBlock()) && !level.isClientSide) {
			notifyNearbyCores(level, pos);
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	private void notifyNearbyCores(Level level, BlockPos framePos) {
		// frame can be at y+0 (cardinal) or y+1 (ring) relative to core
		// check both possible core y-levels
		for (int dy = 0; dy >= -1; dy--) {
			for (int dx = -1; dx <= 1; dx++) {
				for (int dz = -1; dz <= 1; dz++) {
					BlockPos corePos = framePos.offset(dx, dy, dz);
					BlockState coreState = level.getBlockState(corePos);
					if (coreState.getBlock() instanceof StarwellBlock starwell) {
						starwell.recheckFrame(level, corePos, coreState);
						return; // one core found is enough
					}
				}
			}
		}
	}
}