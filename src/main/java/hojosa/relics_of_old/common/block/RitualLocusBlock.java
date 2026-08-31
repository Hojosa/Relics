package hojosa.relics_of_old.common.block;

import hojosa.relics_of_old.common.block.entity.RitualLocusBlockEntity;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.block.RelicsNormalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class RitualLocusBlock extends RelicsNormalBlock implements EntityBlock {
	public RitualLocusBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE)
                .strength(1.5f)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops());
    }

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RitualLocusBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return (lvl, pos, st, be) -> {
			if (be instanceof RitualLocusBlockEntity ritual) {
				ritual.tick();
			}
		};
	}
}