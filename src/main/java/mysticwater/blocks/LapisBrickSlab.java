package mysticwater.blocks;

import java.util.Random;

import mysticwater.base.BaseSlab;
import mysticwater.core.handler.EnumHandler.Category;
import mysticwater.init.ModBlocks;
import mysticwater.lib.Strings;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LapisBrickSlab extends BaseSlab
{

	public LapisBrickSlab(Material material, boolean isDoubleSlab, Category category)
	{
		super(material, category);
		this.setUnlocalizedName(Strings.LapisBrickSlabName);
		this.setHardness(1.5f);
		this.setResistance(10.0f);
		this.setStepSound(soundTypeStone);
	}
	
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		return 0;
	}
	
	/*protected ItemStack createStackedBlock(int meta)
	{
		return new ItemStack(Item.getItemFromBlock(ModBlocks.lapisBrickSlab), getSlabDrops(0), 7);
	}*/
	
	
	public Item getItemDropped(int meta, Random random, int number)
	{
		return Item.getItemFromBlock(this);
	}

}
