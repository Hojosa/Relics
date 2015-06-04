package mysticwater.items.block;

import mysticwater.base.BaseSlab;
import mysticwater.blocks.DoubleSlab;
import mysticwater.blocks.SingleSlab;
import mysticwater.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;

public class ItemBlockLapisBrickSlab extends ItemSlab
{
	public ItemBlockLapisBrickSlab(Block block)
	{
		super(block, (SingleSlab)ModBlocks.lapisBrickSlab, (DoubleSlab)ModBlocks.doubleLapisBrickSlab);
	}

	public String getUnlocalizedName(ItemStack itemStack)
	{
		return super.getUnlocalizedName();
	}
}
