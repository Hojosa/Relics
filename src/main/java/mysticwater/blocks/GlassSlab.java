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

public class GlassSlab extends BaseSlab
{

	public GlassSlab(Material material, boolean isDoubleSlab, Category category)
	{
		super(material, category);
		this.setUnlocalizedName(Strings.GlassSlabName);
		this.setStepSound(soundTypeGlass);
		this.setHardness(0.3F);
		
	}
	
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		return 1;
	}
	
	/*protected ItemStack createStackedBlock(int meta)
	{
		
		return new ItemStack(Item.getItemFromBlock(ModBlocks.glassSlab), getSlabDrops(0), meta & 7);
	}*/
	
	//@Override
	public Item getItemDropped(int meta, Random random, int number)
	{
		return Item.getItemFromBlock(this);
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}
	
	/*@Override
	public int damageDropped (int meta)
	{
		return 0;
	}*/
	
	@Override
	protected boolean canSilkHarvest()
	{
		return true;
	}
	

	
	
}
