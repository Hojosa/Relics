package mysticwater.base;

import java.util.List;
import java.util.Random;

import hojosa.relics.lib.References;
import mysticwater.core.handler.EnumHandler.Category;
import mysticwater.core.handler.EnumHandler.ColorSet;
import mysticwater.lib.BlockPropertyHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BaseMetaSlab extends BlockSlab
{	
	private IProperty actualVariantProperty;
	static IProperty property;
	public Category blockTyp;
	public boolean isDouble;
	private static final int HALF_META_BIT = 8;
	

	public static Category createProperty(Category cat)
	{
		System.out.println("WHY" + cat);
		System.out.println("WHY" + PropertyEnum.<ColorSet>create("variant", ColorSet.class, ColorSet.getStateList(cat, false)));
		property = PropertyEnum.<ColorSet>create("variant", ColorSet.class, ColorSet.getStateList(cat, false));
		return cat;
	}
	
	public BaseMetaSlab(Material material, Category typ, String name, boolean fullBock)
	{
		super(material);
		//if (!this.isDouble()) this.setCreativeTab(MysticWater.getCreativTab());
		this.setLightOpacity(0);
		this.useNeighborBrightness = !this.isDouble();
		blockTyp = typ;
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.setDefaultState(this.blockState.getBaseState().withProperty(HALF, EnumBlockHalf.BOTTOM));
	}
	
	@Override 
	public final IBlockState getStateFromMeta (int meta)
	{
		ColorSet.getStateList(blockTyp, false);

		Object[] blockProperties = BlockPropertyHelper.setBlockPropertyPerState(this, ColorSet.getStateList(blockTyp, false).get(meta & 7).toString(), blockTyp);

		this.setHardness((Float)blockProperties[0]);
		this.setResistance((Float)blockProperties[1]);
		this.setSoundType((SoundType)blockProperties[2]);
		actualVariantProperty.getAllowedValues();
			//getDefaultState().withProperty(actualVariantProperty, actualVariantProperty.getAllowedValues());
		//return this.getDefaultState().withProperty(actualVariantProperty, ColorSet.getStateList(blockTyp, false).get(meta & 7)).withProperty(HALF, (meta & HALF_META_BIT) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
	//	return getDefaultState().withProperty(actualVariantProperty, actualVariantProperty.getAllowedValues().toArray()[meta]).withProperty(HALF, meta == 8 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
return null;
	}
	
	@Override
	public final int getMetaFromState(IBlockState state)
	{
		Object[] blockProperties = BlockPropertyHelper.setBlockPropertyPerState(this, state.getValue(actualVariantProperty).toString(), blockTyp);
		
		this.setHardness((Float)blockProperties[0]);
		this.setResistance((Float)blockProperties[1]);
		this.setSoundType((SoundType)blockProperties[2]);
		
		//ColorSet color = (ColorSet) state.getValue(actualVariantProperty);
		ColorSet.getStateList(blockTyp, false).indexOf(actualVariantProperty);
			
		return (state.getValue(HALF) == EnumBlockHalf.TOP ? HALF_META_BIT : 0) + ColorSet.getStateList(blockTyp, false).indexOf(state.getValue(actualVariantProperty));
	}
	
	@Override
	public IProperty getVariantProperty()
	{
		return actualVariantProperty;
	}
	
	public Object getVariant(ItemStack stack)
	{
		return ColorSet.getStateList(blockTyp, false).get(stack.getMetadata() & 7);
	}
	
	@Override
	public final BlockStateContainer createBlockState()
	{
		actualVariantProperty = property;
	
		return new BlockStateContainer(this, new IProperty[] {HALF, actualVariantProperty});
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
		System.out.println(this.getUnlocalizedName() + ColorSet.getStateList(blockTyp, false).get(meta) + " NAME");
		return this.getUnlocalizedName() + ColorSet.getStateList(blockTyp, false).get(meta);
	}
	
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	public boolean isFullCube(IBlockState state)
    { 
		return false;
    }
	
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		
		IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        
		if(worldIn.getBlockState(pos.offset(side.getOpposite())) != iblockstate)
		{
			return true;
		}
		
		if(block == this || iblockstate.getMaterial().equals(Material.GLASS))
		{
			return false;
		}
		
		return block == this ? false :  super.shouldSideBeRendered(blockState, worldIn, pos, side);
	}	
	
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
        // face is on the block being rendered, not this block.
        EnumBlockHalf side = world.getBlockState(pos).getValue(HALF);
        return (side == EnumBlockHalf.TOP && face == EnumFacing.DOWN);
 	}
	
	public int quantityDropped(Random random) 
	{
		if(blockTyp == Category.GLASS || blockTyp == Category.GLASSCOLOR_0 || blockTyp == Category.GLASSCOLOR_1)
		{
			return 0;
		}
		else
			return this.isDouble() ? 2 : 1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
		if(blockTyp == Category.GLASSCOLOR_0 || blockTyp == Category.GLASSCOLOR_1)
		{
			return BlockRenderLayer.TRANSLUCENT;
		}
		else if(blockTyp == Category.GLASS)
		{
			return BlockRenderLayer.CUTOUT;
		}
		else
		{
			return BlockRenderLayer.SOLID;
		}
    }	
	
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if(blockTyp == Category.GLASSCOLOR_0 || blockTyp == Category.GLASSCOLOR_1 || blockTyp == Category.GLASS)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean isDouble()
	{
		return isDouble;
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getMetaFromState(state.withProperty(HALF, EnumBlockHalf.BOTTOM));
	}

	@Override
	public Item getItemDropped(IBlockState blockState, Random random, int fortune)
	{
		return this.isDouble() ? this.getSingleSlab(this) : Item.getItemFromBlock(this);
	}
	
	protected ItemStack createStackedBlock(IBlockState state)
	{		
		return new ItemStack(this.isDouble() ? this.getSingleSlab(this) : Item.getItemFromBlock(this), this.isDouble() ? 2 : 1,  ColorSet.getStateList(blockTyp, false).indexOf(state.getValue(actualVariantProperty)));	
	}
	
	private Item getSingleSlab(Block block)
	{
		
		StringBuilder changeName = new StringBuilder(this.getUnlocalizedName());
		changeName.delete(12, 18);
		changeName.replace(12, 13, changeName.substring(12, 13).toLowerCase());
		
		return Item.getByNameOrId(changeName.toString());	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(int meta = 0; meta < this.actualVariantProperty.getAllowedValues().size(); ++meta)
		{
			list.add(new ItemStack(item, 1, meta));
		}
	}
	
}
