package mysticwater.blocks;

import java.util.List;
import java.util.Random;

import mysticwater.base.BaseSlab;
import mysticwater.core.handler.EnumHandler;
import mysticwater.core.handler.EnumHandler.Category;
import mysticwater.init.ModBlocks;
import mysticwater.lib.Strings;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class StainedGlassSlab extends BaseSlab
{
	private static final IIcon[] MetaColor1 = new IIcon[8];
	private static final IIcon[] MetaColor2 = new IIcon[8];
	
	public static final String[] Color1 = new String[] {"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray"};
	public static final String[] Color2 = new String[] {"silver", "cyan", "purple", "blue", "brown", "green", "red", "black"};
	public final EnumHandler.Category category;


	public StainedGlassSlab(Material material, boolean isDoubleSlab, mysticwater.core.handler.EnumHandler.Category color)
	{
		super(Material.glass, color);//, isDoubleSlab, color);
		this.setHardness(0.3F);
		this.setStepSound(soundTypeGlass);
		this.setUnlocalizedName(Strings.StainedGlassSlabName);
		//this.setBlockTextureName("glass");
		category = color;
		
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List par3list)
	{
		if (category == Category.COLOR1)
		for (int meta = 0; meta < Color1.length; ++meta)
		{
			par3list.add(new ItemStack(item, 1, meta));

		}
		else
		{
			for (int meta = 0; meta < Color2.length; ++meta)
			{
				par3list.add(new ItemStack(item, 1, meta));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (category == Category.COLOR1)
		{
		return MetaColor1[meta % Color1.length];}
		
		else
			return MetaColor2[meta % Color2.length];
	}
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister Icons)
	{
		if (category == Category.COLOR1)
		{
			for (int meta = 0; meta < Color1.length; ++meta)
			{
				MetaColor1[meta] = Icons.registerIcon(/*this.getTextureName() +*/ "_" + Color1[meta]);
			}
		}
		else
		{	
			for (int meta = 0; meta < Color2.length; ++meta)
			{
				MetaColor2[meta] = Icons.registerIcon(/*this.getTextureName() +*/ "_" + Color2[meta]); 
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		return 1;
	}

	protected boolean canSilkHarvest()
	{
		return true;
	}
	
	public int damageDropped (int meta)
	{
		return meta;
	}
	
	public int quantityDropped(Random random)
	{
		return 0;
	}
	
	protected ItemStack createStackedBlock(int meta)
	{
		if (category == Category.COLOR1)
		{
			return new ItemStack(Item.getItemFromBlock(ModBlocks.stainedGlassSlab), getSlabDrops(0), meta & 7);
		}
		else
			{
			return new ItemStack(Item.getItemFromBlock(ModBlocks.stainedGlassSlab2), getSlabDrops(0), meta & 7);
			}
	}
	
}
