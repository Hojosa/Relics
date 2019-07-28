package com.hojosa.relics.common.block;

import com.hojosa.relics.Relics;
import com.hojosa.relics.common.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;


public class BlockRelics extends Block
{
	public BlockRelics(Material material, String name)
	{
		super(material);
		setUnlocalizedName(Relics.MOD_ID + "." + name);
		setRegistryName(Relics.MOD_ID, name);
		ModBlocks.register(this);
	}
}
