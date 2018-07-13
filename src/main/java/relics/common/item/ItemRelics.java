package relics.common.item;

import net.minecraft.item.Item;
import relics.Relics;
import relics.common.init.ModItems;

public class ItemRelics extends Item
{
	public ItemRelics(String name)
	{
		setUnlocalizedName(Relics.MOD_ID + "." + name);
		setRegistryName(name);
		ModItems.register(this);
	}
}
