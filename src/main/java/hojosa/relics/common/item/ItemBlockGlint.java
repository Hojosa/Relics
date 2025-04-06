package hojosa.relics.common.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ItemBlockGlint extends BlockItem {

	public ItemBlockGlint(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
	}
	
	@Override
	public boolean isFoil(ItemStack pStack) {
		return true;
	}
}