package hojosa.relics.common.block;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import hojosa.relics.common.block.entity.SwordPedestalBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class SwordPedestalBlock extends RelicsBlock implements EntityBlock//Container
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty SWORD = BooleanProperty.create("sword");
//	public static final PropertyBool SWORD = PropertyBool.create("sword");
//	private static final VoxelShape VOXEL_SHAPE_BASE = Block.box(0.1275F, 0.01F, 0.3145F, 0.875F, 0.375F , 0.685F);
	private static final VoxelShape VOXEL_SHAPE_BASE_NS = Block.box(2.0D, 0.0D, 5.0D, 14.0D, 6.0D, 11.0D);
	private static final VoxelShape VOXEL_SHAPE_BASE_EW = Block.box(5.0D, 0.0D, 2.0D, 11.0D, 6.0D, 14.0D);
	private static final Map<Direction, VoxelShape> SWORD_SHAPE = Util.make(new EnumMap<>(Direction.class), map -> {
		map.put(Direction.NORTH, box(2,6,7.5,14,24.5,8.5));
		map.put(Direction.EAST, box(7.5,6,2,8.5,24.5,14));
		map.put(Direction.SOUTH, box(2,6,7.5,14,24.5,8.5));
		map.put(Direction.WEST, box(7.5,6,2,8.5,24.5,14));
		
	});
	
	
	private static final Map<Direction, VoxelShape> SIDES_SHAPE = Util.make(new EnumMap<>(Direction.class), map -> {
		map.put(Direction.NORTH, box(2.0D, 0.0D, 5.0D, 14.0D, 6.0D, 11.0D));
		map.put(Direction.EAST, box(5.0D, 0.0D, 2.0D, 11.0D, 6.0D, 14.0D));
		map.put(Direction.SOUTH, box(2.0D, 0.0D, 5.0D, 14.0D, 6.0D, 11.0D));
		map.put(Direction.WEST, box(5.0D, 0.0D, 2.0D, 11.0D, 6.0D, 14.0D));
	});
	
	public SwordPedestalBlock() {
		this(getInitProperties());
	}
	
	public SwordPedestalBlock(Properties properties)
	{
		super(properties);
//		this.setUnlocalizedName(Relics.MOD_ID +"." + name);
//		setRegistryName(name); 
//		this.setHardness(0.8F);
//		this.setHarvestLevel("pickaxe", 0);
//		this.setCreativeTab(CreativeTabRelics.instance);
		//this.setLightOpacity(0);
		//this.setLightLevel(1);
		// TODO Auto-generated constructor stub
//		ModBlocks.register(this)
	}
	
	private static Properties getInitProperties() {
		Properties properties = Properties.copy(Blocks.STONECUTTER);
		properties.sound(SoundType.STONE);
		properties.noOcclusion();
		return properties;
	}
	
	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
//		System.out.println("get shape of state: " + blockState.getValue(FACING) + " box: " + SIDES_SHAPE.get(blockState.getValue(FACING)));
		//NS box(2,6,7.5,14,24.5,8.5)
		
//		return Shapes.or(box(5.0D, 0.0D, 2.0D, 11.0D, 6.0D, 14.0D), box(7.5,6,2,8.5,24.5,14));
		
		return blockState.getValue(SWORD) ? Shapes.or(SIDES_SHAPE.get(blockState.getValue(FACING)),SWORD_SHAPE.get(blockState.getValue(FACING))) : SIDES_SHAPE.get(blockState.getValue(FACING));
		
//		return SIDES_SHAPE.get(blockState.getValue(FACING));//(blockState.getValue(FACING) == Direction.NORTH || blockState.getValue(FACING) == Direction.SOUTH) ? VOXEL_SHAPE_BASE_NS : VOXEL_SHAPE_BASE_EW;
	}
	
    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
	   super.createBlockStateDefinition(blockStateBuilder.add(FACING).add(SWORD));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(SWORD, Boolean.FALSE); 
	}
	
//	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos)
//	{
//		final TileEntity entity = access.getTileEntity(pos);
//		
//		if(entity instanceof TileEntitySwordPedestal)
//		{
//			switch((EnumFacing)state.getValue(FACING))
//			{
//				default:
//					break;
//				case NORTH:
//					return new AxisAlignedBB(0.1275F, 0.01F, 0.3145F, 0.875F, ((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH).getStackInSlot(0).isEmpty()? 0.375F : 1.45F , 0.685F);	
//				case EAST:
//					return new AxisAlignedBB(0.3145F, 0.01F, 0.1275F, 0.685F, ((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.EAST).getStackInSlot(0).isEmpty()? 0.375F : 1.45F , 0.875F);
//				case SOUTH:
//					return new AxisAlignedBB(0.1275F, 0.01F, 0.3145F, 0.875F, ((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH).getStackInSlot(0).isEmpty()? 0.375F : 1.45F  , 0.685F);
//				case WEST:
//					return new AxisAlignedBB(0.3145F, 0.01F, 0.1275F, 0.685F, ((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.WEST).getStackInSlot(0).isEmpty()? 0.375F : 1.45F , 0.875F);
//			}
//		}
//		return FULL_BLOCK_AABB;
//	}
	
//	@Override
//	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
//	{
//		TileEntity entity = worldIn.getTileEntity(pos);
//		
//		if(entity instanceof TileEntitySwordPedestal)// && !((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)state.getValue(FACING)).getStackInSlot(0).isEmpty())
//		{
//			switch((EnumFacing)state.getValue(FACING))
//			{
//				default:
//					break;
//				case NORTH:
//					addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1275F, 0.01F, 0.3145F, 0.875F, 0.375F, 0.685F));
//					if(!((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)state.getValue(FACING)).getStackInSlot(0).isEmpty()){
//						addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1275F, 0.37F, 0.48F, 0.875F, 1.45F , 0.525F));
//					}
//					break;
//				case EAST:
//					addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.3145F, 0.01F, 0.1275F, 0.685F, 0.375F, 0.875F));
//					if(!((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.EAST).getStackInSlot(0).isEmpty()) {
//						addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.48F, 0.37F, 0.1275F, 0.525F, 1.45F , 0.875F));
//					}
//					break;
//				case SOUTH:
//					addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1275F, 0.01F, 0.3145F, 0.875F, 0.375F, 0.685F));
//					if(!((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)state.getValue(FACING)).getStackInSlot(0).isEmpty()){
//						addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1275F, 0.37F, 0.48F, 0.875F, 1.45F , 0.525F));
//					}
//					break;
//				case WEST:
//					addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.3145F, 0.01F, 0.1275F, 0.685F, 0.375F, 0.875F));
//					if(!((TileEntitySwordPedestal) entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)state.getValue(FACING)).getStackInSlot(0).isEmpty()) {
//						addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.48F, 0.37F, 0.1275F, 0.525F, 1.45F , 0.875F));
//					}
//					break;
//			}
//		}
//	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SwordPedestalBlockEntity(pos, state);
	}
	
//	@Override
//	public boolean hasTileEntity(IBlockState state) {
//		
//		return true;
//	}
	
	
//	@Override
//	public void breakBlock(World world, BlockPos pos, IBlockState blockstate)
//	{
//		TileEntitySwordPedestal te = (TileEntitySwordPedestal) world.getTileEntity(pos);
//		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, blockstate.getValue(FACING));
//		ItemStack stack = itemHandler.getStackInSlot(0);
//		if(stack != null)
//		{
//			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
//		}
//		super.breakBlock(world, pos, blockstate);
//	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if(!level.isClientSide()) {
			SwordPedestalBlockEntity blockEntity = (SwordPedestalBlockEntity) level.getBlockEntity(pos);
			
			//check if the inventory is empty and the player item is a sword. yes -> place sword in inventory
			if(!blockEntity.isStackInSlot() && blockEntity.canPlaceSword(player.getItemInHand(hand))) {
				player.setItemInHand(hand, blockEntity.placeSword(player.getItemInHand(hand)));
				level.setBlock(pos, state.setValue(SWORD, Boolean.TRUE), UPDATE_ALL);
//				state.setValue(SWORD, Boolean.TRUE);
				//set for update/safe
			}
			//item in the slot, we try to give the item to the player
			else if (blockEntity.isStackInSlot())
			{
				//if hand is empty, get item
				if(player.getItemInHand(hand).isEmpty()) {
					player.setItemInHand(hand, blockEntity.getSword());
					level.setBlock(pos, state.setValue(SWORD, Boolean.FALSE), UPDATE_ALL);
//					state.setValue(SWORD, Boolean.FALSE);
					//set for update
				}
				//when there is a free slot, we move the item to the free slot and get the item
				else if (!(player.getInventory().getFreeSlot() == -1)) {
					ItemStack currendItem = player.getItemInHand(hand);
					player.setItemInHand(hand, blockEntity.getSword());
					player.getInventory().add(currendItem);
					level.setBlock(pos, state.setValue(SWORD, Boolean.FALSE), UPDATE_ALL);
//					state.setValue(SWORD, Boolean.FALSE);
				}
				else return InteractionResult.FAIL;
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.SUCCESS;
	}
	
//	public static void retextureBlock(BlockState state, String texture) {
////		state.getValues()
////        state.getStateManager().getStates().forEach(state -> {
//        	if (state.getValue(FACING) == Direction.NORTH) {
//            // Replace the existing texture with the new texture
////        	state.
////            state.getModel().getElements().forEach(element -> {
////                element.faces.values().forEach(face -> {
////                    face.texture = new Identifier("modid", "block/" + texture);
////                });
////            });
//        }
//    }
	
//	@Override 
//	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
//    {
//		if(!worldIn.isRemote)
//		{
//			TileEntitySwordPedestal te = (TileEntitySwordPedestal) worldIn.getTileEntity(pos);
//			IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
//			
//			if(!itemHandler.getStackInSlot(0).isEmpty()) //get sword from pedestal
//			{
//				if(player.inventory.getFirstEmptyStack() != -1) //check if inventory is full
//				{
//					if(player.getHeldItemMainhand().isEmpty())//check if main hand is free, if not move ItemStack to another slot and give sword
//					{
//						player.setHeldItem(hand, itemHandler.extractItem(0, 1, false));
//					}
//					else
//					{
//						player.replaceItemInInventory(player.inventory.getFirstEmptyStack(), player.getHeldItemMainhand());
//						player.setHeldItem(hand, itemHandler.extractItem(0, 1, false));
//					}
//					System.out.println("remove sword " + state.withProperty(FACING, state.getValue(FACING)).withProperty(SWORD, false));
//					//worldIn.setBlockState(pos,state.withProperty(FACING, state.getValue(FACING)).withProperty(SWORD, false) , 3);	
//					worldIn.notifyBlockUpdate(pos, state, state.withProperty(FACING, state.getValue(FACING)).withProperty(SWORD, false), 3);
//					
//				}
//				else //full inventroy
//				{
//					if(!worldIn.isRemote)
//					{
//							player.sendMessage((new TextComponentString("Make some room in your Inventory!")));
//					}
//				}
//			}
//			else if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemSword) //place sword in pedestal
//			{
//				player.setHeldItem(hand, itemHandler.insertItem(0, player.getHeldItemMainhand(), false));
//				//te.renderItemStack();
//				System.out.println("place sword " + state.withProperty(FACING, state.getValue(FACING)).withProperty(SWORD, true));
//				//this.getActualState(state, worldIn, pos);
//				//worldIn.setBlockState(pos ,state.withProperty(FACING, state.getValue(FACING)).withProperty(SWORD, true) , 3);	
//				worldIn.notifyBlockUpdate(pos, state, state.withProperty(FACING, state.getValue(FACING)).withProperty(SWORD, true), 3);
//				System.out.println("actualstate " + this.getActualState(state, worldIn, pos));
//			}
//			System.out.println("should call after placement logic");
//		}
//		//this.getActualState(state, worldIn , pos);
//		//worldIn.setBlockState(pos, s, 3);		
//		//worldIn.notifyBlockUpdate(pos, state, this.getActualState(state, worldIn, pos), 3);
//		//System.out.println("at the end " + this.getActualState(state, worldIn, pos));
//		return true;
//    }
	
//	@Override
//	public boolean isOpaqueCube(IBlockState state)
//	{
//		return false;
//	}
//	
//	@Override
//	public boolean isFullCube(IBlockState state)
//	{
//	    return false;
//	}
//	@Override
//	public boolean isFullBlock(IBlockState state)
//    {
//        return true;
//    }
	
//	@Override
//	public final BlockState defaultBlockState()
//	{
//		return new BlockStateContainer(this, FACING, SWORD);
//	}
	
//	@Override
//	public IBlockState getStateFromMeta(int meta)
//	{
//		System.out.println("biatch " + meta);
//		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
//	}
	
//	@Override
//	public int getMetaFromState(IBlockState state)
//	{
//		System.out.println("biatch22 " + state);
//		return ((EnumFacing) state.getValue(FACING)).getIndex();
//	}
	
//	public EnumBlockRenderType getRenderType(IBlockState state)
//    {
//        return EnumBlockRenderType.MODEL;
//    }
	

	
//	@Override
//	public IBlockState getActualState(IBlockState state, IBlockAccess access, BlockPos pos)
//	{
//		final TileEntity entity = access.getTileEntity(pos);
//		System.out.println("WHAT IS THIS " + state);
//		//System.out.println("item slot " + ((TileEntitySwordPedestal)entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, state.getValue(FACING)).getStackInSlot(0).isEmpty());
//		System.out.println("The state that is set " + state.withProperty(FACING, state.getValue(FACING)).withProperty(SWORD, this.isSwordinPedestal(state, access, pos)));
//		return state;// = state.withProperty(FACING, state.getValue(FACING)).withProperty(SWORD, this.isSwordinPedestal(state, access, pos));
//	}
//	
//	private boolean isSwordinPedestal(IBlockState state, IBlockAccess access, BlockPos pos) 
//	{
//		final TileEntity entity = access.getTileEntity(pos);
//		return !((TileEntitySwordPedestal)entity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, state.getValue(FACING)).getStackInSlot(0).isEmpty();	
//	}
}
