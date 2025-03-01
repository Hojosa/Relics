package hojosa.relics.common.block;

import hojosa.relics.common.block.entity.SkybeamBlockEntity;
import hojosa.relics.lib.block.RelicsNormalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class SkybeamBlock extends RelicsNormalBlock implements EntityBlock{
	
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public SkybeamBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new SkybeamBlockEntity(pPos, pState);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		// TODO Auto-generated method stub
		super.createBlockStateDefinition(pBuilder.add(LIT));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.defaultBlockState().setValue(LIT, Boolean.valueOf(pContext.getLevel().hasNeighborSignal(pContext.getClickedPos())));
	}
	
	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
		if (!pLevel.isClientSide) {
	         boolean flag = pState.getValue(LIT);
	         if (flag != pLevel.hasNeighborSignal(pPos)) {
	               pLevel.setBlock(pPos, pState.cycle(LIT), 2);
	            }
	         }
	      }
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		if (pLevel.isClientSide) {
            // We don't have anything to do on the client side
            return null;
        } else {
            // Server side we delegate ticking to our block entity
            return (lvl, pos, st, blockEntity) -> {
                if (blockEntity instanceof SkybeamBlockEntity be) {
//                	pLevel.markAndNotifyBlock(pos, pLevel.getChunkAt(pos), pState, pState, UPDATE_CLIENTS, 1);
                    be.tick();
                }
            };
        }
    }
}
