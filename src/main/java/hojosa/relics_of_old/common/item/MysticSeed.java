package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class MysticSeed extends ItemNameBlockItem {
	
	public MysticSeed(Block shrubBlock) {
		super(shrubBlock, new Item.Properties());
	}
	
	@Override
	public boolean isFoil(ItemStack pStack) {
		return true;
	}

	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		InteractionResult result = super.useOn(pContext);
        if (result.consumesAction()) {
            Level level = pContext.getLevel();
            BlockPos pos = pContext.getClickedPos().above();

            if (!level.isClientSide) {
                RelicsUtil.buildShrubStar(level, pos, level.isThundering());
            } else {
                RandomSource random = level.getRandom();
                for (int i = 0; i < 12; i++) {
                    level.addParticle(ParticleTypes.CRIT,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        random.nextGaussian(), 1, random.nextGaussian());
                }
            }
        }
        return result;
	}	
}