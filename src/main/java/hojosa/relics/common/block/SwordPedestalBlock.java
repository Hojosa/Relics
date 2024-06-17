package hojosa.relics.common.block;

import java.util.Map;
import java.util.Random;

import hojosa.relics.Relics;
import hojosa.relics.common.block.entity.SwordPedestalBlockEntity;
import hojosa.relics.common.init.RelicsSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
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
	public static final BooleanProperty REPAIR = BooleanProperty.create("repair");
	private Map<Direction, VoxelShape> SWORD_SHAPE;
	private Map<Direction, VoxelShape> SIDES_SHAPE;
	
	public SwordPedestalBlock(Map<Direction, VoxelShape> shapeMap, Map<Direction, VoxelShape> swordShapeMap) {
		this(getInitProperties());
		this.SIDES_SHAPE = shapeMap;
		this.SWORD_SHAPE = swordShapeMap;
	}
	
	public SwordPedestalBlock(Properties properties) {
		super(properties);
	}
	
	private static Properties getInitProperties() {
		Properties properties = Properties.copy(Blocks.STONECUTTER);
		properties.sound(SoundType.STONE);
		properties.noOcclusion();
		return properties;
	}
	
	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
//		SIDES_SHAPE = Util.make(new EnumMap<>(Direction.class), map -> {
//    		map.put(Direction.NORTH, Block.box(1.0D, 0.0D, 4.5D, 15.0D, 4.1D, 11.5D));
//    		map.put(Direction.EAST, Block.box(4.5D, 0.0D, 1.0D, 11.5D, 4.1D, 15.0D));
//    		map.put(Direction.SOUTH, Block.box(1.0D, 0.0D, 4.5D, 15.0D, 4.1D, 11.5D));
//    		map.put(Direction.WEST, Block.box(4.5D, 0.0D, 1.0D, 11.5D, 4.1D, 15.0D));
//    	});
//		return Block.box(2,3.9,7.5,14,24.5,8.5);
		return blockState.getValue(SWORD) ? Shapes.or(SIDES_SHAPE.get(blockState.getValue(FACING)),SWORD_SHAPE.get(blockState.getValue(FACING))) : SIDES_SHAPE.get(blockState.getValue(FACING));
	}
	
    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
	   super.createBlockStateDefinition(blockStateBuilder.add(FACING).add(SWORD).add(REPAIR));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		
		return this.defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection().getOpposite())
				.setValue(SWORD, Boolean.FALSE)
				.setValue(REPAIR, false); 
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SwordPedestalBlockEntity(pos, state);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if(!level.isClientSide()) {
			SwordPedestalBlockEntity blockEntity = (SwordPedestalBlockEntity) level.getBlockEntity(pos);
			ItemStack itemInHand = player.getItemInHand(hand);
			//check if the inventory is empty and the player item is a sword. yes -> place sword in inventory
			if(!blockEntity.isStackInSlot() && blockEntity.canPlaceSword(itemInHand)) {
				player.setItemInHand(hand, blockEntity.placeSword(itemInHand));
				level.playSound(null, pos, RelicsSounds.SWORD_PLACE_SOUND.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
				state = state.setValue(SWORD, true);

				if(blockEntity.isSwordDamaged()) {
					System.out.println("need repairs");
					state = state.setValue(REPAIR, true);
					level.scheduleTick(pos, this, 600);
				}
				System.out.println("getvalues " + state.getValue(SWORD) + " " + state.getValue(REPAIR));
			}
			//item in the slot, we try to give the item to the player
			else if (blockEntity.isStackInSlot())
			{
				//if hand is empty, get item
				if(itemInHand.isEmpty()) {
					player.setItemInHand(hand, blockEntity.returnSword(level.getGameTime()));
					state = state.setValue(SWORD, false).setValue(REPAIR, false);
				}
				//when there is a free slot, we move the item to the free slot and get the item
				else if (!(player.getInventory().getFreeSlot() == -1)) {
					player.setItemInHand(hand, blockEntity.returnSword(level.getGameTime()));
					player.getInventory().add(itemInHand);
					state = state.setValue(SWORD, false).setValue(REPAIR, false);
				}
				else return InteractionResult.FAIL;
			}
			else if (itemInHand.getItem() == Items.NETHER_STAR.asItem() && !blockEntity.repairUpgrade) {
				player.getItemInHand(hand).setCount(itemInHand.getCount() - 1);
				level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
				blockEntity.upgradePedestal();
				blockEntity.setChanged();
			}
			level.setBlock(pos, state, UPDATE_ALL);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public boolean isSignalSource(BlockState state) {
		return state.getValue(SWORD);
	}
	
	@Override
	public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
	   if (pBlockState.getValue(SWORD)) {
		   SwordPedestalBlockEntity blockEntity = (SwordPedestalBlockEntity) pBlockAccess.getBlockEntity(pPos);
		   SwordItem sword = (SwordItem)blockEntity.getSword().getItem();
		   return sword.getTier().getLevel() + 5;
	   }
	   else	return 0;
	}
	
    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        if (blockState.getBlock() != newBlockState.getBlock()) {
            try {
            	SwordPedestalBlockEntity blockEntity = (SwordPedestalBlockEntity) level.getBlockEntity(blockPos);
                blockEntity.dropItems();
            } catch (Exception ex) {
                Relics.LOGGER.error(String.format("Invalid blockEntity type at %s, expected SwordPedestalBlockEntity", blockPos));
            }
        }
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }
    
    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
    	SwordPedestalBlockEntity blockEntity = (SwordPedestalBlockEntity) pLevel.getBlockEntity(pPos);
    	if(blockEntity.isSwordDamaged()) {
    		blockEntity.repairSword();
    		if(blockEntity.isSwordDamaged()) {
    			pLevel.scheduleTick(pPos, this, 600);
    		}
    		else pLevel.setBlock(pPos, pState.setValue(REPAIR, false), UPDATE_ALL);
    	}
    }
}
