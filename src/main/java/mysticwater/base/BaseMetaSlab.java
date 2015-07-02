package mysticwater.base;

import java.util.List;
import java.util.Random;

import mysticwater.MysticWater;
import mysticwater.core.handler.EnumHandler;
import mysticwater.core.handler.EnumHandler.Category;
import mysticwater.core.handler.EnumHandler.EnumColor1;
import mysticwater.core.handler.EnumHandler.EnumColor2;
import mysticwater.lib.References;
import mysticwater.lib.Strings;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumWorldBlockLayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BaseMetaSlab extends BlockSlab
{
	
	private static final PropertyEnum VARIANT_PROPERTY = PropertyEnum.create("variant", EnumHandler.EnumColor1.class);
	//private static final PropertyEnum VARIANT_PROPERTY2 = PropertyEnum.create("variant", EnumHandler.EnumColor2.class);
	private static final int HALF_META_BIT = 8;
	public final Category blockTyp;
	

	public BaseMetaSlab(Material material, Category typ)
	{
		super(material);
		if (!isDouble()) this.setCreativeTab(MysticWater.getCreativTab());
		//this.doubleSlab = isDouble;
		this.setLightOpacity(0);
		this.useNeighborBrightness = !this.isDouble();
		this.blockTyp = typ;
		this.setUnlocalizedName(getName(isDouble()));
		
		IBlockState blockState = this.blockState.getBaseState();
		//if(blockTyp == Category.COLOR1)
			//{
		blockState = blockState.withProperty(VARIANT_PROPERTY, EnumColor1.WHITE);
			//}
		//else
			//{
			//blockState = blockState.withProperty(VARIANT_PROPERTY2, EnumColor2.SILVER);
			//}
		if(!this.isDouble())
		{
			blockState = blockState.withProperty(HALF, EnumBlockHalf.BOTTOM);
		}
		setDefaultState(blockState);
		
	}
	
	@Override 
	public final IBlockState getStateFromMeta (int meta)
	{
		IBlockState blockState = this.getDefaultState();
		blockState = blockState.withProperty(VARIANT_PROPERTY, EnumColor1.WHITE);
		if (!this.isDouble()){
			EnumBlockHalf value = EnumBlockHalf.BOTTOM;
			if ((meta & HALF_META_BIT) !=0)
			{
				value = EnumBlockHalf.TOP;
			}
			
			blockState = blockState.withProperty(HALF, value);
		}
		return blockState;
	}
	
	@Override
	public final int getMetaFromState(final IBlockState state)
	{
		if(this.isDouble())
		{
			return 0;
		}
		
		if ((EnumBlockHalf) state.getValue(HALF) == EnumBlockHalf.TOP)
		{
			return HALF_META_BIT;
		}
		else
		{
			return 0;
		}
	}
	
	@Override
	public IProperty getVariantProperty()
	{
		
		return VARIANT_PROPERTY;
	}
	
	@Override
	public Object getVariant(ItemStack stack)
	{
		// TODO Automatisch generierter Methodenstub
		return null;
	}
	
	public String getVariantName()
	{
		if (blockTyp == Category.COLOR1)
			for (int meta = 0; meta < 6; ++meta)
			{
				return EnumHandler.Color1[meta];

			}
			if(blockTyp == Category.COLOR2)
			{
				for (int meta = 7; meta < 15; ++meta)
				{
					return EnumHandler.Color1[meta];
				}
			}
		/*if(category == Category.GLASS)
		{
			return "glass";
		}
		else if(category == Category.LAPIS)
		{
			return "lapis";
		}
		else if(category == Category.COLOR1)
		{
			return Category.COLOR1;
					
		}*/
		return null;
	}
	
	@Override
	protected final BlockState createBlockState()
	{
		
		if(this.isDouble())
		{
			return new BlockState(this, new IProperty[] {VARIANT_PROPERTY});
		}
		else
		{
			return new BlockState(this, new IProperty[] {VARIANT_PROPERTY, HALF});
		}
		/*if(this.isDouble())
		{
			if(blockTyp == Category.COLOR1)
			{
				return new BlockState(this, new IProperty[] {VARIANT_PROPERTY});
			}
			if(blockTyp == Category.COLOR2)
			{
				return new BlockState(this, new IProperty[] {VARIANT_PROPERTY2});
			}
		}
		else
		{
			if(blockTyp == Category.COLOR1)
			{
				return new BlockState(this, new IProperty[] {VARIANT_PROPERTY, HALF});
			}
			if(blockTyp == Category.COLOR2)
			{
				return new BlockState(this, new IProperty[] {VARIANT_PROPERTY2, HALF});
			}
		}*/

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
	
	@Override
	public String getUnlocalizedName(int meta)
	{
		return this.getUnlocalizedName();
	}
	
	public String getName(boolean slab)
	{
		if(!slab)
		{
		if(blockTyp == Category.GLASS)
		{
			return Strings.GlassSlabName;
		}
		if(blockTyp == Category.LAPIS)
		{
			return Strings.LapisBrickSlabName;
		}
		if(blockTyp == Category.COLOR1 | blockTyp == Category.COLOR2);
		{
			return Strings.StainedGlassSlabName;
		}
		}
		else
		{
			if(blockTyp == Category.GLASS)
			{
				return Strings.DoubleGlassName;
			}
			if(blockTyp == Category.LAPIS)
			{
				return Strings.DoubleLapisBrickSlabName;
			}
			if(blockTyp == Category.COLOR1 | blockTyp == Category.COLOR2);
			{
				return Strings.StainedDoubleGlassSlabName;
			}
		}
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	
	public int getSlabDrops (int number)
	{
		if(blockTyp == Category.GLASS)
		{
			return 0;
		}
		else
		{
			if (!this.isDouble()) 
			{
				number = 1;
			}
			else
			{
				number = 2;
			}
			return number;
		}
	}
	
	@SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
		if (blockTyp == Category.GLASS)
        return EnumWorldBlockLayer.CUTOUT;
		
		else
		{
			return EnumWorldBlockLayer.SOLID;
		}
    }	
	
	protected boolean canSilkHarvest()
	{
		if(blockTyp == Category.GLASS | blockTyp == Category.COLOR1 | blockTyp == Category.COLOR2)
			return true;
		else
			return false;
	}

	@Override
	public boolean isDouble()
	{
		return false;
	}
	
	@Override
	public final int damageDropped(final IBlockState state)
	{
		return 0;
	}
	
	@Override
	public final Item getItemDropped(final IBlockState blockState, final Random random, final int unused)
	{
		if(this.isDouble())
		{
			return Item.getByNameOrId(References.MODID + ":" + getName(false));
		}
		else 
		{
			return Item.getItemFromBlock(this);
		}	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List par3list)
	{
		/*for (int meta = 0; meta < 15; ++meta)
		{
			par3list.add(new ItemStack(item, 1, meta));

		}*/
		if (blockTyp == Category.COLOR1)
		for (int meta = 0; meta < 6; ++meta)
		{
			par3list.add(new ItemStack(item, 1, meta));

		}
		else
		{
			for (int meta = 7; meta < 15; ++meta)
			{
				par3list.add(new ItemStack(item, 1, meta));
			}
		}
	}
	
}
