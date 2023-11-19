package hojosa.relics.common.item;

import hojosa.relics.Relics;
import hojosa.relics.common.init.RelicsItems;
import net.minecraft.item.Item;

public class ItemRelics extends Item
{
	public ItemRelics(String name)
	{
		setUnlocalizedName(Relics.MOD_ID + "." + name);
		setRegistryName(name);
		RelicsItems.register(this);
	}
}
