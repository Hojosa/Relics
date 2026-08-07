package hojosa.relics_of_old.common.block;

import hojosa.relics_of_old.common.block.entity.ClayJarBlockEntity;
import hojosa.relics_of_old.lib.block.RelicsWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ClayJar extends RelicsWaterloggedBlock implements EntityBlock {
	private static final VoxelShape SHAPE = Shapes.or(
			Block.box(4, 15, 4, 12, 16, 12), // rim
			Block.box(5, 14, 5, 11, 15, 11), // neck
			Block.box(4, 13, 4, 12, 14, 12), // shoulder
			Block.box(3, 11, 3, 13, 13, 13), // upper body
			Block.box(2, 8, 2, 14, 11, 14), // mid body
			Block.box(1, 3, 1, 15, 8, 15), // main body
			Block.box(2, 1, 2, 14, 3, 14), // lower body
			Block.box(4, 0, 4, 12, 1, 12) // base
			.optimize());

	public ClayJar(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new ClayJarBlockEntity(pPos, pState);
	}

	@Override
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		return pLevel.getBlockState(pPos.below()).isFaceSturdy(pLevel, pPos, Direction.UP);
	}

	@Override
	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {

		if (!pLevel.isClientSide && pEntity instanceof ItemEntity item) {
			var blockEntity = (ClayJarBlockEntity) pLevel.getBlockEntity(pPos);
			if (blockEntity != null && blockEntity.isEmpty()) {
				blockEntity.setItem(0, item.getItem());
				item.remove(RemovalReason.KILLED);
				pLevel.playSound(null, pPos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 1.0f, 1.0f);
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return Block.box(1, 0, 1, 15, 16, 15);
	}
}