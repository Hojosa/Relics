package com.hojosa.relics.common.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabRelics extends CreativeTabs
{
	public static CreativeTabs instance = new CreativeTabRelics("tabRelics");
	public CreativeTabRelics(String modid)
	{
		super(modid);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(Items.BOOK);
	}

}
