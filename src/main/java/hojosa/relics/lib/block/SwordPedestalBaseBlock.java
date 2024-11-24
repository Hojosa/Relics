package hojosa.relics.lib.block;

import hojosa.relics.common.block.entity.SwordPedestalBlockEntityNew;
import hojosa.relics.lib.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class SwordPedestalBaseBlock extends RelicsFacingEntityBlock {
	public static final BooleanProperty SWORD = BooleanProperty.create("sword");
	public static final BooleanProperty REPAIR = BooleanProperty.create("repair");
	private final VoxelShape SHAPE;
	private final VoxelShape SHAPE_WITH_SWORD;
	
	public SwordPedestalBaseBlock(Block block, VoxelShape blockShape, VoxelShape swordShape) {
		super(getInitProperties(block));
		SHAPE = blockShape;
		SHAPE_WITH_SWORD = swordShape;
	}
	

	
	private static Properties getInitProperties(Block block) {
		Properties properties = Properties.copy(block);
		properties.sound(SoundType.STONE);
		properties.noOcclusion();
		return properties;
	}
	
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    	return state.getValue(SWORD).booleanValue() ? state.getValue(FACING).getAxis() == Direction.Axis.X ? ShapeUtil.rotate(SHAPE_WITH_SWORD, Rotation.CLOCKWISE_90) : SHAPE_WITH_SWORD  : state.getValue(FACING).getAxis() == Direction.Axis.X ? ShapeUtil.rotate(SHAPE, Rotation.CLOCKWISE_90) : SHAPE;
    }
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
	   super.createBlockStateDefinition(blockStateBuilder.add(SWORD).add(REPAIR));
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
		return new SwordPedestalBlockEntityNew(pos, state);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		int slot = 0;
		SwordPedestalBlockEntityNew blockEntity = (SwordPedestalBlockEntityNew) level.getBlockEntity(pos);
        ItemStack itemInHand = player.getItemInHand(hand);
         	ItemStack slotStack = blockEntity.getItem(slot);
            if ((!itemInHand.isEmpty() && blockEntity.canPlaceItem(slot, itemInHand)) || !slotStack.isEmpty() && (itemInHand.isEmpty() || blockEntity.canPlaceItem(slot, itemInHand))) {
            	blockEntity.setItem(slot, itemInHand);
                player.setItemInHand(hand, slotStack);

                state = state.setValue(SWORD, !blockEntity.isEmpty()).setValue(REPAIR, blockEntity.isSwordDamaged() && blockEntity.isInfused());
                level.setBlock(pos, state, UPDATE_ALL);
                return InteractionResult.SUCCESS; 
            }
            //pedestal upgrade
            else if (blockEntity.accpetsItem(itemInHand)) {
            	blockEntity.infusePedestal();
				player.getItemInHand(hand).setCount(itemInHand.getCount() - 1);
				level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
				return InteractionResult.SUCCESS; 
            }
    			return InteractionResult.FAIL;
	}
	
	@Override
	public boolean isSignalSource(BlockState state) {
		return state.getValue(SWORD);
	}
	
	@Override
	public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
	   if (pBlockState.getValue(SWORD)) {
		   SwordPedestalBlockEntityNew blockEntity = (SwordPedestalBlockEntityNew) pBlockAccess.getBlockEntity(pPos);
		   SwordItem sword = (SwordItem)blockEntity.getItem(0).getItem();
		   return sword.getTier().getLevel() + 5;
	   }
	   else	return 0;
	}
    
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
    	System.out.println("are we ticking?");
    	SwordPedestalBlockEntityNew blockEntity = (SwordPedestalBlockEntityNew) pLevel.getBlockEntity(pPos);
    		blockEntity.repairSword();
    }
    
    @Override
    public boolean isRandomlyTicking(BlockState pState) {
    	return pState.getValue(REPAIR);
    }
    
    public float getRenderOffSet() {
    	return (float) (SHAPE.max(Axis.Y) - 0.375);
    }
}
