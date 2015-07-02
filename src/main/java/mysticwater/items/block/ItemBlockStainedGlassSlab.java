package mysticwater.items.block;

import mysticwater.blocks.DoubleMetaSlab;
import mysticwater.blocks.SingleMetaSlab;
import mysticwater.blocks.StainedGlassSlab;
import mysticwater.core.handler.EnumHandler;
import mysticwater.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ItemBlockStainedGlassSlab extends ItemSlab
{
	
	public ItemBlockStainedGlassSlab(Block block)
	{
		super(block, (SingleMetaSlab)ModBlocks.stainedGlassSlab, (DoubleMetaSlab)ModBlocks.doubleStainedGlassSlab);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
	
	public String getUnlocalizedName(ItemStack itemStack)
    { 
			int i = MathHelper.clamp_int(itemStack.getItemDamage(), 0, 15);
			return super.getUnlocalizedName() + "." + EnumHandler.Color1[i];
		}
	
    }

