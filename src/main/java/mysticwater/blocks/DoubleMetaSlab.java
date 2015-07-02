package mysticwater.blocks;

import java.util.List;

import mysticwater.base.BaseMetaSlab;
import mysticwater.base.BaseSlab;
import mysticwater.core.handler.EnumHandler;
import mysticwater.core.handler.EnumHandler.Category;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class DoubleMetaSlab extends BaseMetaSlab
{

	public DoubleMetaSlab(Material material, Category typ)
	{
		super(material, typ);
		// TODO Automatisch generierter Konstruktorstub
	}
	
	@Override
	public boolean isDouble()
	{
		return true;
	}
	
	/*@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List par3list)
	{
		if (blockTyp == Category.COLOR1)
		for (int meta = 0; meta < EnumHandler.Color1.length; ++meta)
		{
			par3list.add(new ItemStack(item, 1, meta));

		}
		else if(blockTyp == Category.COLOR2)
		{
			for (int meta = 0; meta < EnumHandler.Color2.length; ++meta)
			{
				par3list.add(new ItemStack(item, 1, meta));
			}
		}
	}
*/
	

}
