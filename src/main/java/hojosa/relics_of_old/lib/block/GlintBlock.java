package hojosa.relics_of_old.lib.block;

import hojosa.relics_of_old.lib.block.entity.GlintBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GlintBlock extends RelicsNormalBlock implements EntityBlock{
	
	private boolean rainbowGlint;

	public GlintBlock(Properties properties, boolean raindbow) {
		super(properties);
		this.rainbowGlint = raindbow;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new GlintBlockEntity(pPos, pState);
	}
}