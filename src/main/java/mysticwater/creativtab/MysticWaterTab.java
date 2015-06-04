package mysticwater.creativtab;

import mysticwater.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MysticWaterTab extends CreativeTabs
{
	public MysticWaterTab(int id, String modid)
	{
		super(id, modid);
	}
	
	@Override
	public Item getTabIconItem()
	{
		return ModItems.iceCrystal;
	}

}
