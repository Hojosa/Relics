package mysticwater.base;

import java.util.Random;

import mysticwater.MysticWater;
import mysticwater.core.handler.EnumHandler.Category;
import mysticwater.lib.References;
import mysticwater.lib.Strings;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumWorldBlockLayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BaseSlab extends BlockSlab
{
	
	private static final PropertyBool VARIANT_PROPERTY = PropertyBool.create("variant");
	private static final int HALF_META_BIT = 8;
	public final Category blockTyp;
	

	public BaseSlab(Material material, Category typ)
	{
		super(material);
		if (!isDouble()) this.setCreativeTab(MysticWater.getCreativTab());
		//this.doubleSlab = isDouble;
		this.setLightOpacity(0);
		this.useNeighborBrightness = !this.isDouble();
		this.blockTyp = typ;
		this.setUnlocalizedName(getName(isDouble()));
		
		IBlockState blockState = this.blockState.getBaseState();
		blockState = blockState.withProperty(VARIANT_PROPERTY, false);
		if(!this.isDouble())
		{
			blockState = blockState.withProperty(HALF, EnumBlockHalf.BOTTOM);
		}
		setDefaultState(blockState);
		
	}
	
	@Override 
	public final IBlockState getStateFromMeta (final int meta)
	{
		IBlockState blockState = this.getDefaultState();
		blockState = blockState.withProperty(VARIANT_PROPERTY, false);
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
	public Object getVariant(ItemStack itemStack)
	{
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
		
		
		return false;
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
}
