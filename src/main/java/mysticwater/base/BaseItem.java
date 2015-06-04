package mysticwater.base;

import mysticwater.lib.References;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseItem extends Item
{
	
	public String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("item.%s%s",  References.RESOURCESPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return String.format("item.%s%s",  References.RESOURCESPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	/*@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		//this.itemIcon = iconRegister.registerIcon(References.RESOURCESPREFIX + getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}*/
}
