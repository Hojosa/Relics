package mysticwater.items.block;

import mysticwater.blocks.DoubleSlab;
import mysticwater.blocks.SingleSlab;
import mysticwater.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;

public class ItemBlockSlab extends ItemSlab
{
	public ItemBlockSlab(Block block)//, final SingleSlab slab, final BaseSlabDouble doubleSlab, final Boolean stacked)
	{
		super(block, (SingleSlab)ModBlocks.glassSlab, (DoubleSlab)ModBlocks.doubleGlassSlab);
		
	}
	
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return super.getUnlocalizedName();
	}
}
