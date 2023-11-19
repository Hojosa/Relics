package com.hojosa.relics.common.tileentity;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;


public class TileEntitySwordPedestal extends TileEntity
{
	
	public ItemStack sword = null;
	
	public int rotation = 0;
	public int sinShift;
	public int rotationSpeed = 1;
	public int[] colorRGBA = new int[]{255,255,255,48};
	public boolean isFloating = false;
	public boolean isRotating = false;
	public boolean lightBeamEnabled = false;
	public boolean enchantmentGlimmer = true;
	public String pedestalName = "Basic";
	public int baseRotation = 0;
	public boolean clockwiseRotation = true;
	
	private ItemStackHandler inventory;
	
	public TileEntitySwordPedestal() 
	{
		this.inventory = new ItemStackHandler(1);
		
	}
	
	
	public IItemHandler getInventory()
	{
		return this.inventory;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		
		super.writeToNBT(compound);
		
		compound.setInteger("rotation", this.rotation);
		compound.setInteger("rotationSpeed", this.rotationSpeed);
		compound.setInteger("baseRotation", this.baseRotation);
		compound.setIntArray("colorRGB", this.colorRGBA);
		compound.setBoolean("isFloating", this.isFloating);
		compound.setBoolean("isRotating", this.isRotating);
		compound.setBoolean("lightBeam", this.lightBeamEnabled);
		compound.setBoolean("enchantmentGlimmer", this.enchantmentGlimmer);
		compound.setBoolean("clockwiseRotation", this.clockwiseRotation);
		
//		NBTTagCompound stack = new NBTTagCompound();
//		if(sword != null)
//			{
//			this.sword.writeToNBT(stack);
//			}
//		compound.setTag("sword", stack);
		compound.setTag("inventory", inventory.serializeNBT());
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		this.rotation = compound.getInteger("rotation");
		this.rotationSpeed = compound.getInteger("rotationSpeed");
		this.baseRotation = compound.getInteger("baseRotation");
		this.colorRGBA = compound.getIntArray("colorRGB");
		this.isFloating = compound.getBoolean("isFloating");
		this.isRotating = compound.getBoolean("isRotating");
		this.lightBeamEnabled = compound.getBoolean("lightBeam");
		this.enchantmentGlimmer = compound.getBoolean("enchantmentGlimmer");
		this.clockwiseRotation = compound.getBoolean("clockwiseRotation");
		
		//this.sword = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("sword"));
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
	}
	
	 @Override
	 public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) 
	 {
		 super.onDataPacket(net, packet);
		 readFromNBT(packet.getNbtCompound());
	 }

	
	@Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
//		
		//System.out.println("PACKET");
		
        return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
    	System.out.println("HI");
    	// TODO Auto-generated method stub
    	return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }
    
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
    	// TODO Auto-generated method stub
    	return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
    }
	
	public boolean isStackInSlot()
	{
		//System.out.println(this.inventory.getStackInSlot(0).isEmpty());
		//System.out.println((this.sword != null) + " YOUUUU");
		if(inventory.getStackInSlot(0).isEmpty())
		{
			return false;
		}
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		System.out.println("HELLLLOOO");
		if(this.lightBeamEnabled)
			return INFINITE_EXTENT_AABB;
		
		else return super.getRenderBoundingBox();
	}
	
	@SideOnly(Side.CLIENT)
	public void renderItemStack()
	{
		RenderItem item;
		//item.getItemModelWithOverrides(stack, worldIn, entitylivingbaseIn)
//		this.inventory.getStackInSlot(0).getItem().getTileEntityItemStackRenderer().renderByItem(itemStackIn);
		this.inventory.getStackInSlot(0).getItem().getTileEntityItemStackRenderer().renderByItem(this.inventory.getStackInSlot(0));
	}

}
