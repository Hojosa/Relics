package hojosa.relics.common.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import hojosa.relics.common.init.RelicsBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;


public class SwordPedestalBlockEntity extends BlockEntity
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
	
	private ItemStackHandler itemHandler = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
		
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack itemStack) {
			return itemStack.getItem() instanceof SwordItem;
		}
	};
	
	private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
	
	public SwordPedestalBlockEntity(BlockPos blockPos, BlockState blockState) 
	{
		super(RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), blockPos, blockState);
		
	}
	
	@Override
	public void setRemoved() {
		super.setRemoved();
		handler.invalidate();
	}
	
	
//	public IItemHandler getInventory()
//	{
//		return this.itemHandler;
//	}
	//safty check if the item is a sword, then place it. returns empty after placing, so that player#setItemInHand can be used. 
	//returns the input item back, if not valid
	
	public ItemStack placeSword(ItemStack sword) {
		if (this.itemHandler.isItemValid(0, sword)) {
			this.itemHandler.setStackInSlot(0, sword);
			this.setChanged();
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
			return new ItemStack(Items.AIR);
		}
		else return sword;
	}
	
	public boolean canPlaceSword(ItemStack sword) {
		return itemHandler.isItemValid(0, sword);
	}
	
	public ItemStack getSword() {
		ItemStack item = this.itemHandler.getStackInSlot(0);
		this.itemHandler.setStackInSlot(0, new ItemStack(Items.AIR));
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
		return item;
	}
	
	public ItemStack getSwordForRender() {
		ItemStack item = this.itemHandler.getStackInSlot(0);
		return item;
	}
	
	@Override
	public void load(CompoundTag tag) {
//		System.out.println("laod tag: " + tag);
		if(tag.contains("Inventory")) {
			itemHandler.deserializeNBT(tag.getCompound("Inventory"));
		}
		super.load(tag);
	}
	
	@Override
	public void saveAdditional(CompoundTag tag) {
		tag.put("Inventory", itemHandler.serializeNBT());
		
		super.saveAdditional(tag);
	}
	
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
//    	System.out.println("onDataPacket");
        this.load(pkt.getTag());
    }
    
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
//    	System.out.println("getUpdatePacket");
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
//    	System.out.println("getUpdateTag");
        return this.saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
//    	System.out.println("handleUpdateTag");
        this.load(tag);
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        handler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        handler.invalidate();
    }
    
	
//	@Override
//	public NBTTagCompound writeToNBT(NBTTagCompound compound)
//	{
//		
//		super.writeToNBT(compound);
//		
//		compound.setInteger("rotation", this.rotation);
//		compound.setInteger("rotationSpeed", this.rotationSpeed);
//		compound.setInteger("baseRotation", this.baseRotation);
//		compound.setIntArray("colorRGB", this.colorRGBA);
//		compound.setBoolean("isFloating", this.isFloating);
//		compound.setBoolean("isRotating", this.isRotating);
//		compound.setBoolean("lightBeam", this.lightBeamEnabled);
//		compound.setBoolean("enchantmentGlimmer", this.enchantmentGlimmer);
//		compound.setBoolean("clockwiseRotation", this.clockwiseRotation);
//		
////		NBTTagCompound stack = new NBTTagCompound();
////		if(sword != null)
////			{
////			this.sword.writeToNBT(stack);
////			}
////		compound.setTag("sword", stack);
//		compound.setTag("inventory", inventory.serializeNBT());
//		
//		return compound;
//	}
	
//	@Override
//	public void readFromNBT(NBTTagCompound compound)
//	{
//		super.readFromNBT(compound);
//		
//		this.rotation = compound.getInteger("rotation");
//		this.rotationSpeed = compound.getInteger("rotationSpeed");
//		this.baseRotation = compound.getInteger("baseRotation");
//		this.colorRGBA = compound.getIntArray("colorRGB");
//		this.isFloating = compound.getBoolean("isFloating");
//		this.isRotating = compound.getBoolean("isRotating");
//		this.lightBeamEnabled = compound.getBoolean("lightBeam");
//		this.enchantmentGlimmer = compound.getBoolean("enchantmentGlimmer");
//		this.clockwiseRotation = compound.getBoolean("clockwiseRotation");
//		
//		//this.sword = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("sword"));
//		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
//	}
//	
//	 @Override
//	 public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) 
//	 {
//		 super.onDataPacket(net, packet);
//		 readFromNBT(packet.getNbtCompound());
//	 }
//
//	
//	@Nullable
//    public SPacketUpdateTileEntity getUpdatePacket()
//    {
////		
//		//System.out.println("PACKET");
//		
//        return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), this.getUpdateTag());
//    }
//
//    public NBTTagCompound getUpdateTag()
//    {
//        return writeToNBT(new NBTTagCompound());
//    }
    
//    @Override
//    public boolean hasCapability(Capability<?> capability, @Nullable Direction facing)
//    {
//    	System.out.println("HI");
//    	// TODO Auto-generated method stub
//    	return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
//    }
    
    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
    {
    	return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? handler.cast() : super.getCapability(capability, facing);
    }
	
	public boolean isStackInSlot()
	{
		//System.out.println(this.inventory.getStackInSlot(0).isEmpty());
		//System.out.println((this.sword != null) + " YOUUUU");
//		System.out.println("inStackinSlot");
//		System.out.println(this.itemHandler.getStackInSlot(0));
//		System.out.println(this.itemHandler.getStackInSlot(0).isEmpty());
//		System.out.println("result: "+ !this.itemHandler.getStackInSlot(0).isEmpty());
		return !this.itemHandler.getStackInSlot(0).isEmpty();
	}
	
//	@SideOnly(Side.CLIENT)
//	public AxisAlignedBB getRenderBoundingBox()
//	{
//		System.out.println("HELLLLOOO");
//		if(this.lightBeamEnabled)
//			return INFINITE_EXTENT_AABB;
//		
//		else return super.getRenderBoundingBox();
//	}
//	
//	@SideOnly(Side.CLIENT)
//	public void renderItemStack()
//	{
//		RenderItem item;
//		//item.getItemModelWithOverrides(stack, worldIn, entitylivingbaseIn)
////		this.inventory.getStackInSlot(0).getItem().getTileEntityItemStackRenderer().renderByItem(itemStackIn);
//		this.inventory.getStackInSlot(0).getItem().getTileEntityItemStackRenderer().renderByItem(this.inventory.getStackInSlot(0));
//	}

}
