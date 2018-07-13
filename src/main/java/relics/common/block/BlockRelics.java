package relics.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import relics.Relics;
import relics.common.init.ModBlocks;
import relics.common.util.CreativeTabRelics;

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
