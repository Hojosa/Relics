package mysticwater.items.block;

import mysticwater.blocks.DoubleMetaSlab;
import mysticwater.blocks.SingleMetaSlab;
import mysticwater.blocks.StainedGlassSlab;
import mysticwater.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ItemBlockStainedGlassSlab2 extends ItemSlab
{
	public ItemBlockStainedGlassSlab2(Block block)
	{
		super(block, (SingleMetaSlab)ModBlocks.stainedGlassSlab2, (DoubleMetaSlab)ModBlocks.doubleStainedGlassSlab2);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
	
	
	public String getUnlocalizedName(ItemStack itemStack)
    { 
			int i = MathHelper.clamp_int(itemStack.getItemDamage(), 0, 7);
			return super.getUnlocalizedName() + "." + StainedGlassSlab.Color2[i];
		}
	
    }

