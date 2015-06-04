package mysticwater.base;

import mysticwater.MysticWater;
import mysticwater.lib.References;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseBlock extends Block
{
	public BaseBlock()
	{
		super(Material.rock);
		this.setCreativeTab(MysticWater.getCreativTab());
	}

	public BaseBlock(Material material)
	{
		super(material);
	}

	public String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);

	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("%s%s", References.RESOURCESPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
/*
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		//this.blockIcon = iconRegister.registerIcon(References.RESOURCESPREFIX + getUnwrappedUnlocalizedName(super.getUnlocalizedName()));

	}*/

}
