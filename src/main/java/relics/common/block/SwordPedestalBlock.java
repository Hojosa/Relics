package relics.common.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import relics.common.tileentity.TileEntitySwordPedestal;

public class SwordPedestalBlock extends BlockRelics//Container
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public SwordPedestalBlock(Material materialIn, String name)
	{
		super(materialIn, name);
//		this.setUnlocalizedName(Relics.MOD_ID +"." + name);
//		setRegistryName(name); 
		this.setHardness(0.8F);
		this.setHarvestLevel("pickaxe", 0);
//		this.setCreativeTab(CreativeTabRelics.instance);
		//this.setLightOpacity(0);
		//this.setLightLevel(1);
		// TODO Auto-generated constructor stub
//		ModBlocks.register(this)
		
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos)
	{
		final TileEntity entity = access.getTileEntity(pos);
		
		if(entity instanceof TileEntitySwordPedestal)
		{
			switch((EnumFacing)state.getValue(FACING))
			{
				default:
					break;
				case NORTH:
					return new AxisAlignedBB(0.1275F, 0.01F, 0.3145F, 0.875F, ((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH).getStackInSlot(0).isEmpty()? 0.375F : 1.45F , 0.685F);	
				case EAST:
					return new AxisAlignedBB(0.3145F, 0.01F, 0.1275F, 0.685F, ((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.EAST).getStackInSlot(0).isEmpty()? 0.375F : 1.45F , 0.875F);
				case SOUTH:
					return new AxisAlignedBB(0.1275F, 0.01F, 0.3145F, 0.875F, ((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH).getStackInSlot(0).isEmpty()? 0.375F : 1.45F  , 0.685F);
				case WEST:
					return new AxisAlignedBB(0.3145F, 0.01F, 0.1275F, 0.685F, ((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.WEST).getStackInSlot(0).isEmpty()? 0.375F : 1.45F , 0.875F);
			}
		}
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
	{
		TileEntity entity = worldIn.getTileEntity(pos);
		
		if(entity instanceof TileEntitySwordPedestal)// && !((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)state.getValue(FACING)).getStackInSlot(0).isEmpty())
		{
			switch((EnumFacing)state.getValue(FACING))
			{
				default:
					break;
				case NORTH:
					addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1275F, 0.01F, 0.3145F, 0.875F, 0.375F, 0.685F));
					if(!((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)state.getValue(FACING)).getStackInSlot(0).isEmpty()){
						addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1275F, 0.37F, 0.48F, 0.875F, 1.45F , 0.525F));
					}
					break;
				case EAST:
					addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.3145F, 0.01F, 0.1275F, 0.685F, 0.375F, 0.875F));
					if(!((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.EAST).getStackInSlot(0).isEmpty()) {
						addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.48F, 0.37F, 0.1275F, 0.525F, 1.45F , 0.875F));
					}
					break;
				case SOUTH:
					addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1275F, 0.01F, 0.3145F, 0.875F, 0.375F, 0.685F));
					if(!((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)state.getValue(FACING)).getStackInSlot(0).isEmpty()){
						addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1275F, 0.37F, 0.48F, 0.875F, 1.45F , 0.525F));
					}
					break;
				case WEST:
					addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.3145F, 0.01F, 0.1275F, 0.685F, 0.375F, 0.875F));
					if(!((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)state.getValue(FACING)).getStackInSlot(0).isEmpty()) {
						addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.48F, 0.37F, 0.1275F, 0.525F, 1.45F , 0.875F));
					}
					break;
			}
		}
	}
	
	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state)
	{
		return new TileEntitySwordPedestal();
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		
		return true;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate)
	{
		TileEntitySwordPedestal te = (TileEntitySwordPedestal) world.getTileEntity(pos);
		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, blockstate.getValue(FACING));
		ItemStack stack = itemHandler.getStackInSlot(0);
		if(stack != null)
		{
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
		}
		super.breakBlock(world, pos, blockstate);
	}
	
	@Override 
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		if(!worldIn.isRemote)
		{
			TileEntitySwordPedestal te = (TileEntitySwordPedestal) worldIn.getTileEntity(pos);
			IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
			
			if(!itemHandler.getStackInSlot(0).isEmpty()) //get sword from pedestal
			{
				if(player.inventory.getFirstEmptyStack() != -1) //check if inventory is full
				{
					if(player.getHeldItemMainhand().isEmpty())//check if main hand is free, if not move ItemStack to another slot and give sword
					{
						player.setHeldItem(hand, itemHandler.extractItem(0, 1, false));
					}
					else
					{
						player.replaceItemInInventory(player.inventory.getFirstEmptyStack(), player.getHeldItemMainhand());
						player.setHeldItem(hand, itemHandler.extractItem(0, 1, false));
					}
					worldIn.notifyBlockUpdate(pos, state, state, 3);
					
				}
				else //full inventroy
				{
					if(!worldIn.isRemote)
					{
							player.sendMessage((new TextComponentString("Make some room in your Inventory!")));
					}
				}
			}
			else if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemSword) //place sword in pedestal
			{
				player.setHeldItem(hand, itemHandler.insertItem(0, player.getHeldItemMainhand(), false));
				te.renderItemStack();
				worldIn.notifyBlockUpdate(pos, state, state, 3);
			}
		}
		return true;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
	    return false;
	}
	@Override
	public boolean isFullBlock(IBlockState state)
    {
        return true;
    }
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing) state.getValue(FACING)).getIndex();
	}
	
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);
		return this.getDefaultState().withProperty(FACING, enumfacing); 
	}
}
