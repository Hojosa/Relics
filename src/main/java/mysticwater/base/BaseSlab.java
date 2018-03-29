package mysticwater.base;

import java.util.List;
import java.util.Random;

import mysticwater.MysticWater;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BaseSlab extends BlockSlab implements IEnumTypes
{
	//static Category setType;
	static IProperty property;
	private IProperty actualVariantProperty;
	private Block singleSlab;
	//static IProperty property;
	
	public static Category setEnumTyp(Category typeSet)
	{
		//System.out.println("WHY" + typeSet);
		//System.out.println("WHY" + getEnumTyp(typeSet));
		//setType = typeSet;
		property = PropertyEnum.create("variant", getEnumClass(typeSet));
		//property = PropertyEnum.create("variant", getEnumTyp(typeSet));
		//System.out.println("WHYY" + setType);
		return typeSet;
	}
	
	private static Class getEnumClass(Category typeSet)
	{
		//System.out.println("WHAT" + typeSet);
		//System.out.println("WHAT" + setType);

		if(typeSet == Category.SLABCOLOR1GLASS)
		{
			return SlabColor1.class;
		}
		else if(typeSet == Category.SLABCOLOR2GLASS)
		{ 
			return SlabColor2.class;
		}
		else if (typeSet == Category.GLASS)
		{
			return Glass.class;
		}
		else return Other.class;
	}
	
	public Category blockTyp;
	boolean isDouble;
	//Enum actualEnumProperty;
	//public static VariantTypes[][] ArrayOfTypes = new VariantTypes[][]{ColorSet1.values(), ColorSet2.values(), Other.values()};
	//public static final PropertyEnum TYPE = PropertyEnum.create("variant", getEnumTyp(setType));//("type", VariantTypes.class, ColorSet1.GRAY);
	
	
	public BaseSlab(Material materialIn, Category type, String name, boolean fullBlock)
	{
		super(materialIn);
		//System.out.println("FUUU " + TYPE);
		this.isDouble = fullBlock;
		this.blockTyp = type;
		/*if (!this.isDouble())*/ this.setCreativeTab(MysticWater.getCreativTab());
		this.useNeighborBrightness = true;
		this.setPropertiesOnState(null, type);
		this.translucent = true;
		this.setUnlocalizedName(name);
		setDefaultState(blockState.getBaseState().withProperty(HALF, EnumBlockHalf.BOTTOM));
		// TODO Auto-generated constructor stub
	}
	
	public void setPropertiesOnState(IBlockState state, Category cat)
	{
		if(cat == Category.SLABCOLOR1GLASS || cat == Category.SLABCOLOR2GLASS || cat == Category.GLASS)
		{
			this.setHardness(0.3F);
			this.setResistance(1.5F);
			this.setSoundType(SoundType.GLASS);
		}
		else 
		{
			this.setHardness(1.5F);
			this.setResistance(10.0F);
			this.setSoundType(SoundType.STONE);
		}
		//System.out.println("WHO" + state.getValue(TYPE));
	}
	
	public void setSingleBlock(Block singleSlab)
	{
		this.singleSlab = singleSlab;
	}
	
//	public Enum[] getUsedEnumValues(Category cat)
//	{
//	if(cat == Category.SLABCOLOR1GLASS)
//	{
//		return SlabColor1.values();
//	}
//	else if(cat == Category.SLABCOLOR2GLASS)
//	{
//		return SlabColor2.values();
//	}
//	else if(cat == Category.GLASS)
//	{
//		return Glass.values();
//	}
//	else return Other.values();
//	}
	
	protected BlockStateContainer createBlockState()
	{
		actualVariantProperty = property;
		return new BlockStateContainer(this, HALF, actualVariantProperty);
	}
	
//	@Override
//	public void getEnumSet(int setNum)
//	{
//		actualEnumProperty = getEnumTyp(setNum);
//	}
	
	public IBlockState getStateFromMeta (int meta)
	{
		//System.out.println(	"??? 1" + 	this.isVisuallyOpaque());
		//System.out.println(	"??? 2" + 	this.translucent);

		if(this.isDouble())
		{
			//System.out.println("HO3 " + this.getRegistryName());
		}
		this.setPropertiesOnState(null, blockTyp);
		return getDefaultState().withProperty(actualVariantProperty, actualVariantProperty.getAllowedValues().toArray()[meta]).withProperty(HALF, meta == 8 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
	}
	
	public int getMetaFromState(IBlockState state)
	{
		//System.out.println(	"??? 3" + 	this.isTranslucent(state));
		this.setPropertiesOnState(state, blockTyp);
		return ((Enum) state.getValue(actualVariantProperty)).ordinal();
	}
	
	 @SideOnly(Side.CLIENT)
	 public boolean isTranslucent(IBlockState state)
	 {
		 return true;
	 }
	 
	 public boolean isVisuallyOpaque()
	    {
	        return true;
	    }

	@Override
	public String getUnlocalizedName(int meta)
	{
		//System.out.println("WORKING?");
		if(this.isDouble())
		{
			return this.getRegistryName().toString()+ "Double" + "." + getVariantProperty().getAllowedValues().toArray()[meta].toString().toLowerCase();
			//System.out.println("HO2 " + this.getRegistryName());
		}
		//System.out.println(meta);
		// TODO Auto-generated method stub
		return this.getRegistryName().toString() + "." + getVariantProperty().getAllowedValues().toArray()[meta].toString().toLowerCase();
	}

	@Override
	public boolean isDouble()
	{
		// TODO Auto-generated method stub
		return isDouble;
	}

	@Override
	public IProperty<?> getVariantProperty()
	{
		// TODO Auto-generated method stub
		return actualVariantProperty;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack)
	{
		//System.out.println("YOU" + TYPE.getAllowedValues().toArray()[stack.getMetadata() & 7]);
		// TODO Auto-generated method stub
		return (Comparable<?>) getVariantProperty().getAllowedValues().toArray()[stack.getMetadata() & 7];
	}
	
//	public boolean isOpaqueCube(IBlockState state)
//	{
//		return false;
//	}
	
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		
//		IBlockState iblockstate = blockAccess.getBlockState(pos);
//        Block block = iblockstate.getBlock();
//        System.out.println("first " + blockAccess.getBlockState(pos.offset(side.getOpposite())));
//        System.out.println("sec " + iblockstate);
//        System.out.println("third " + blockState.getMaterial());
//		if(blockAccess.getBlockState(pos.offset(side.getOpposite())) != iblockstate)
//		{
//			System.out.println("YES " + iblockstate);
//			return true;
//		}
//		
//		if(block == this || blockState.getMaterial().equals(Material.GLASS))
//		{
//			System.out.println("ITSHOULD " + blockState.getMaterial().equals(Material.GLASS));
//			return true;
//		}
//		
//		return block == this ? false :  super.shouldSideBeRendered(blockState, blockAccess, pos, side);
		//System.out.println("first " + blockAccess.getBlockState(pos));
		if(blockAccess.getBlockState(pos).getMaterial().equals(Material.GLASS))
		{
			
			IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
			Block block = iblockstate.getBlock();
		//System.out.println("sec " + iblockstate);
//			System.out.println("third " + block);
//			System.out.println();
			//System.out.println(blockState + " + " + iblockstate);
			//System.out.println(iblockstate.isBlockNormalCube());
//			System.out.println(iblockstate.isFullBlock());
//			System.out.println(iblockstate.isFullCube());
//			System.out.println(iblockstate.isNormalCube());
			

			if(blockState != iblockstate)
			{
				if(iblockstate.getMaterial().equals(Material.GLASS) && (!this.isDouble() && !iblockstate.isBlockNormalCube()))
				{
					
					return blockAccess.getBlockState(pos).getValue(HALF).equals(EnumBlockHalf.TOP) && iblockstate.getValue(HALF).equals(EnumBlockHalf.BOTTOM) ? true : false;
					//return false;
				}
				//System.out.println("YES");
				//System.out.println();
				
				return true;
			}
			if(block == this || iblockstate.getMaterial().equals(Material.GLASS))
			{
				//System.out.println("SEARCH");
				return false;
			}
			
			return block == this ? false : super.shouldSideBeRendered(iblockstate, blockAccess, pos, side);
		}
		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
		
	}	
	
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		
		if(state.getMaterial().equals(Material.GLASS))
        	{
			return Blocks.GLASS.doesSideBlockRendering(state, world, pos, face);
        	}
        else
        	{
        	return super.doesSideBlockRendering(state, world, pos, face);
        	}
        // face is on the block being rendered, not this block.
//		System.out.println("render1 " + state);
//        EnumBlockHalf side = world.getBlockState(pos).getValue(HALF);
//        System.out.println("render2 " + side);
//        System.out.println("render3 " + face);
//        System.out.println("render4 " + (side == EnumBlockHalf.TOP && face == EnumFacing.DOWN));
        //return false;//(side == EnumBlockHalf.TOP && face == EnumFacing.DOWN);
//		if(world.getBlockState(pos).getMaterial().equals(Material.GLASS))
//		{
//			return Blocks.GLASS.doesSideBlockRendering(state, world, pos, face);
//		}
//		else return super.doesSideBlockRendering(state, world, pos, face);
 	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
		if(blockTyp == Category.SLABCOLOR1GLASS || blockTyp == Category.SLABCOLOR2GLASS)
		{
			return BlockRenderLayer.TRANSLUCENT;
		}
		else if(blockTyp == Category.GLASS)
		{
			return BlockRenderLayer.CUTOUT;
		}
		else return BlockRenderLayer.SOLID;
    }	
	
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		//System.out.println("SILK " + state.getMaterial().equals(Material.GLASS));
//		if(blockTyp == Category.SLABCOLOR1GLASS || blockTyp == Category.SLABCOLOR2GLASS || blockTyp == Category.GLASS)
//		{
//			return true;
//		}
//		else return false;
		return state.getMaterial().equals(Material.GLASS);
	}
	
	@Override
	public Item getItemDropped(IBlockState blockState, Random random, int fortune)
	{
		
		//System.out.println("name " + this.getRegistryName());
		//System.out.println("block " + singleSlab);
		return blockState.getMaterial().equals(Material.GLASS) ? null : this.isDouble() ?  Item.getItemFromBlock(singleSlab) : Item.getItemFromBlock(this);
		//return Item.getItemFromBlock(this.singleSlab);
	}
	
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	protected ItemStack createStackedBlock(IBlockState state)
	{		
		return new ItemStack(this);
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		if(this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0 && state.getMaterial().equals(Material.GLASS))
	{
				ItemStack slabToDrop = this.isDouble() ? new ItemStack(singleSlab, 2) : new ItemStack(this);
				slabToDrop.setItemDamage(this.getMetaFromState(state));
				spawnAsEntity(worldIn, pos, slabToDrop);
			//spawnAsEntity(worldIn, pos, slabToDrop);
			//spawnAsEntity(worldIn, pos, new ItemStack((singleSlab).setItemDamage(2)));
	}
		// TODO Auto-generated method stub
		else super.harvestBlock(worldIn, player, pos, state, te, stack);
	}
	
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(int meta = 0; meta < getVariantProperty().getAllowedValues().size(); ++meta)
		{
			list.add(new ItemStack(item, 1, meta));
		}
	}
	

}
