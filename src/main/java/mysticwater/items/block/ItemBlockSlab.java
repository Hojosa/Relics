package mysticwater.items.block;

import mysticwater.base.BaseSlab;
import mysticwater.blocks.DoubleSlab;
import mysticwater.blocks.SingleSlab;
import mysticwater.core.handler.EnumHandler.ColorSet;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;

public class ItemBlockSlab extends ItemSlab
{
	//private final SingleSlab singleSlab;
	
	public ItemBlockSlab(Block block, SingleSlab singleSlab, DoubleSlab doubleSlab)
	{
		super(block, singleSlab, doubleSlab);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		//this.singleSlab = singleSlab;
		doubleSlab.setSingleBlock(singleSlab);
		
		
	}
	
	public String getUnlocalizedName(ItemStack itemStack)
    { 
		return ((BaseSlab)block).getUnlocalizedName();
		//return super.getUnlocalizedName() + "." + ColorSet.getStateList(blockSlab.blockTyp, false).get(itemStack.getItemDamage() & 7);
	}
	
}

