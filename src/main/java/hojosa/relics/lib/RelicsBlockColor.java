package hojosa.relics.lib;

import hojosa.relics.lib.block.SwordPedestalBaseBlock;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class RelicsBlockColor implements BlockColor {
	
	@Override
	public int getColor(BlockState pState, BlockAndTintGetter pLevel, BlockPos pPos, int pTintIndex) {
		if (pLevel != null && pState.getBlock() instanceof SwordPedestalBaseBlock swordPedestal) {
			return swordPedestal.getColor(pTintIndex, pLevel.getBlockEntity(pPos));
		}
		else return -1;
	}
}
