package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.block.entity.RitualLocusBlockEntity;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class TuningForkItem extends RelicsItem {

	public TuningForkItem() {
		super(1, Rarity.UNCOMMON);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		level.playSound(context.getPlayer(), pos, RelicsSounds.RITUAL_FORK.get(), SoundSource.PLAYERS, 0.3f, 1.0f);

		if (level.getBlockEntity(pos) instanceof RitualLocusBlockEntity locus) {
			if (!level.isClientSide) {
				locus.tuning();
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}