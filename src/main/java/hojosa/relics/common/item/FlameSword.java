package com.hojosa.relics.common.item;

import mysticwater.lib.Strings;


public class FlameSword extends SwordRelic
{
	public FlameSword(ToolMaterial material, String name)
	{
		super(material, name);
		//this.setCreativeTab(Relics.getCreativTab());
		this.setUnlocalizedName(Strings.FlameSwordName);
		this.setRegistryName(name);
		
	}
	
}
