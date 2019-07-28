package com.hojosa.relics.common.item;

import com.hojosa.relics.Relics;
import com.hojosa.relics.common.init.ModItems;

import net.minecraft.item.Item;

public class ItemRelics extends Item
{
	public ItemRelics(String name)
	{
		setUnlocalizedName(Relics.MOD_ID + "." + name);
		setRegistryName(name);
		ModItems.register(this);
	}
}
