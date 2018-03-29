package mysticwater.blocks;

import java.util.List;

import javax.annotation.Nullable;

import mysticwater.MysticWater;
import mysticwater.tileentity.TileEntitySwordPedestal;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class SwordPedestalBlock extends BlockContainer
{
	//float height;
	//AxisAlignedBB BLOCK_BOUNDS = new AxisAlignedBB(0.125F, 0.01F, 0.3145F, 0.878F, 0.4375F, 0.685F);
	public static final PropertyDirection FACING = PropertyDirection.create("facing");//, EnumFacing.Plane.HORIZONTAL);

	public SwordPedestalBlock(Material materialIn, String name)
	{
		super(materialIn);
		this.setUnlocalizedName(name);
		this.setHardness(0.8F);
		this.setHarvestLevel("pickaxe", 0);
		this.setCreativeTab(MysticWater.getCreativTab());
		//this.setLightOpacity(0);
		//this.setLightLevel(1);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos)
	{
		
		//TileEntitySwordPedestal te = (TileEntitySwordPedestal) access.getTileEntity(pos);
		//System.out.println(te);
		if(state.getValue(FACING) == EnumFacing.EAST || state.getValue(FACING) == EnumFacing.WEST)
		{
			return new AxisAlignedBB(0.3145F, 0.01F, 0.1275F, 0.685F, this.getHeightForBounds((TileEntitySwordPedestal) access.getTileEntity(pos)), 0.875F);
		}
		else return new AxisAlignedBB(0.1275F, 0.01F, 0.3145F, 0.875F, this.getHeightForBounds((TileEntitySwordPedestal) access.getTileEntity(pos)), 0.685F);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
	{
		TileEntitySwordPedestal te = (TileEntitySwordPedestal)worldIn.getTileEntity(pos);
		//System.out.println(te.isSwordPresent() + " " + this.getHeightForBounds((TileEntitySwordPedestal)worldIn.getTileEntity(pos)));
		
		
		if(state.getValue(FACING) == EnumFacing.EAST || state.getValue(FACING) == EnumFacing.WEST)
		{
			addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.3145F, 0.01F, 0.1275F, 0.685F, 0.375F, 0.875F));
			if(te.isStackInSlot())
			{
				addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.48F, 0.37F, 0.1275F, 0.525F, 1.45F, 0.875F));
			}
			if(te.isStackInSlot() && te.isFloating)
			{
				addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.82F, 0.37F, 0.1275F, 0.18F, 1.95F, 0.875F));
			}
		}
		else
			{
			addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1275F, 0.01F, 0.3145F, 0.875F, 0.375F, 0.685F));
			if(te.isStackInSlot())
			{
				addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1275F, 0.37F, 0.48F, 0.875F, 1.45F, 0.525F));
			}
			if(te.isStackInSlot() && te.isFloating)
			{
				addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1275F, 0.37F, 0.82F, 0.875F, 1.95F, 0.18F));
			}
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta){
		return new TileEntitySwordPedestal();
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate)
	{
		TileEntitySwordPedestal te = (TileEntitySwordPedestal) world.getTileEntity(pos);
		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		ItemStack stack = itemHandler.getStackInSlot(0);
		if(stack != null)
		{
			world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
		}
		super.breakBlock(world, pos, blockstate);
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		TileEntitySwordPedestal te = (TileEntitySwordPedestal) worldIn.getTileEntity(pos);
		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
		
		if(itemHandler.getStackInSlot(0) != null) //get sword from pedestal
		{
			if(player.inventory.getFirstEmptyStack() != -1) //check inv, if first slot is free insert sword there, if not move first slot item and give sword
			{
				if(heldItem != null)
				{
					player.inventory.addItemStackToInventory(heldItem);
					player.setHeldItem(hand, itemHandler.extractItem(0, 1, false));
					
				}
				
				player.setHeldItem(hand, itemHandler.extractItem(0, 1, false));
				te.markDirty();
			}
			else //full inventroy
			{
				if(!worldIn.isRemote)
				{
						player.addChatMessage((new TextComponentString("Make some room in your Inventory!")));
				}
			}
		}
		else if(heldItem != null && heldItem.getItem() instanceof ItemSword)
		{
			player.setHeldItem(hand, itemHandler.insertItem(0, heldItem, false));
			te.markDirty();
		}
		
		
//		if(te.getStackInSlot(0) != null)//sword in pedestal
//		{
//			if(playerIn.inventory.getFirstEmptyStack() != -1)//checks if we have a free slot in the player inventory, returns -1 if inventory is full
//			{
//				ItemStack tmp = heldItem;
//				playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, te.sword);
//				if(heldItem != null);
//				{
//					playerIn.inventory.addItemStackToInventory(tmp);
//				} 
//				te.sword = null;
//				te.getUpdatePacket();
//				worldIn.notifyBlockUpdate(pos, state, state, 3);
//				//System.out.println("SENDING "+ te.getUpdatePacket());
//				te.markDirty();
//				
//				return true;
//			}
//			if(!worldIn.isRemote)
//			{
//				playerIn.addChatMessage((new TextComponentString("Make some room in your Inventory!")));
//			}
//			return true;
//		}
//		if((heldItem != null) && (heldItem.getItem() instanceof ItemSword))
//		{
//			te.sword = heldItem;
//			playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null);
//			if(side == EnumFacing.EAST || side == EnumFacing.WEST)
//				((TileEntitySwordPedestal)worldIn.getTileEntity(pos)).baseRotation = 90;
//			te.getUpdatePacket();
//			worldIn.notifyBlockUpdate(pos, state, state, 3);
//			te.markDirty();
//		}
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
	
	private float getHeightForBounds(TileEntitySwordPedestal entity)
	{
		float height;// = 0.375F;
		height = 0.375F;
		//System.out.println("YES");
		//System.out.println(entity);
		//System.out.println(entity.isStackInSlot() + " CRASH?");
		if(entity == null)
			return height;
		if(entity.isStackInSlot())
		{
			height = 1.45F;
		}
		if(entity.isStackInSlot() && entity.isFloating)
		{
			 height = 1.95F;
		}
		
		
		return height;
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
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		
		EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);
		return this.getDefaultState().withProperty(FACING, enumfacing);
		//return this.getDefaultState().withProperty(FACING, getFacingFromEntity(world, pos, placer));
	}
	
	public static EnumFacing getFacingFromEntity(World worldIn, BlockPos clickedBlock, EntityLivingBase entityIn)
    {
        if (MathHelper.abs((float)entityIn.posX - (float)clickedBlock.getX()) < 2.0F && MathHelper.abs((float)entityIn.posZ - (float)clickedBlock.getZ()) < 2.0F)
        {
            double d0 = entityIn.posY + (double)entityIn.getEyeHeight();

            if (d0 - (double)clickedBlock.getY() > 2.0D)
            {
                return EnumFacing.UP;
            }

            if ((double)clickedBlock.getY() - d0 > 0.0D)
            {
                return EnumFacing.DOWN;
            }
        }

        return entityIn.getHorizontalFacing().getOpposite();
    }


	
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	
	//private boolean isSwordInEntity(TileEntitySwordPedestal entity);

}
