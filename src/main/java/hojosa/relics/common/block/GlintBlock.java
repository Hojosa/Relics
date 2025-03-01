package hojosa.relics.common.block;

import hojosa.relics.common.block.entity.GlintBlockEntity;
import hojosa.relics.lib.block.RelicsNormalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GlintBlock extends RelicsNormalBlock implements EntityBlock{
	
	public boolean rainbowGlint;

	public GlintBlock(Properties properties, boolean raindbow) {
		super(properties);
		this.rainbowGlint = raindbow;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new GlintBlockEntity(pPos, pState);
	}
}
