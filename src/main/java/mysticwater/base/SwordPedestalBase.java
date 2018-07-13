package mysticwater.base;

import java.util.List;

import javax.annotation.Nullable;

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
import relics.Relics;
import relics.common.tileentity.TileEntitySwordPedestal;

public abstract class SwordPedestalBase extends BlockContainer
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");//, EnumFacing.Plane.HORIZONTAL);

	public SwordPedestalBase(Material materialIn, String name)
	{
		super(materialIn);
		this.setUnlocalizedName(name);
		this.setHardness(0.8F);
		this.setHarvestLevel("pickaxe", 0);
		this.setCreativeTab(Relics.getCreativTab());
		//this.setLightOpacity(0);
		//this.setLightLevel(1);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public abstract AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos);
	

	public abstract void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn);
	
	@Override
	public abstract TileEntity createNewTileEntity(World worldIn, int meta);
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate)
	{
		TileEntitySwordPedestal te = ((TileEntitySwordPedestal) world.getTileEntity(pos));
		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
	    ItemStack stack = itemHandler.getStackInSlot(0);
	    if(stack != null) {
	    	EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
	    	world.spawnEntity(item);
	    }
		//InventoryHelper.dropInventoryItems(world, pos, te.inventory.);
		//InventoryHelper.dropInventoryItems(worldIn, pos, inventory);
		super.breakBlock(world, pos, blockstate);
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		TileEntitySwordPedestal te = ((TileEntitySwordPedestal) worldIn.getTileEntity(pos));
		
		if(te.inventory.getStackInSlot(0) != null)//sword in pedestal
		{
			if(playerIn.inventory.getFirstEmptyStack() != -1)//checks if we have a free slot in the player inventory, returns -1 if inventory is full
			{
				ItemStack tmp = heldItem;
				playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, te.sword);
				if(heldItem != null);
				{
					playerIn.inventory.addItemStackToInventory(tmp);
				} 
				//te.sword = null;
				te.getUpdatePacket();
				worldIn.notifyBlockUpdate(pos, state, state, 3);
				//System.out.println("SENDING "+ te.getUpdatePacket());
				te.markDirty();
				
				return true;
			}
			if(!worldIn.isRemote)
			{
				playerIn.sendMessage((new TextComponentString("Make some room in your Inventory!")));
			}
			return true;
		}
		if((heldItem != null) && (heldItem.getItem() instanceof ItemSword))
		{
			//te.sword = heldItem;
			playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null);
			if(side == EnumFacing.EAST || side == EnumFacing.WEST)
				((TileEntitySwordPedestal)worldIn.getTileEntity(pos)).baseRotation = 90;
			te.getUpdatePacket();
			worldIn.notifyBlockUpdate(pos, state, state, 3);
			te.markDirty();
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
}
