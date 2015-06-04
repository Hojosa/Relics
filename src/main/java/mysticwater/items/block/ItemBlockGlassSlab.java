package mysticwater.items.block;

import mysticwater.base.BaseSlab;
import mysticwater.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;

public class ItemBlockGlassSlab extends ItemSlab
{

	public ItemBlockGlassSlab(Block block)
	{
		super(block, (BaseSlab)ModBlocks.glassSlab, (BaseSlab)ModBlocks.doubleGlassSlab);
	}

	public String getUnlocalizedName(ItemStack itemStack)
	{
		return super.getUnlocalizedName();
	}
}
