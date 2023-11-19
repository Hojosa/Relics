package com.hojosa.relics.common.item;


import com.hojosa.relics.Relics;
import com.hojosa.relics.common.init.ModItems;
import net.minecraft.item.ItemSword;


public class SwordRelic extends ItemSword
{
	//public ItemStack repair;
	

	public SwordRelic(ToolMaterial material, String name)
	{
		super(material);
		setUnlocalizedName(Relics.MOD_ID + "." + name);
		setRegistryName(name);
		ModItems.register(this);
		
	}
//	public String getUnwrappedUnlocalizedName(String unlocalizedName)
//	{
//		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
//	}
//
//	@Override
//	public String getUnlocalizedName()
//	{
//		return String.format("sword.%s%s",  References.RESOURCESPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
//	}
//	
//	@Override
//	public String getUnlocalizedName(ItemStack itemStack)
//	{
//		return String.format("sword.%s%s",  References.RESOURCESPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
//	}
	
//	@SideOnly(Side.CLIENT)
//	public void registerIcons(IIconRegister iconRegister)
//	{
//		//this.itemIcon = iconRegister.registerIcon(References.RESOURCESPREFIX + getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
//	}

}
