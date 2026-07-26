package hojosa.relics_of_old.common.block;

import hojosa.relics_of_old.common.init.RelicsConfig;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.block.RelicsNormalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.ForgeSoundType;

public class Caltrops extends RelicsNormalBlock {
	private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 3, 13).optimize();
	private static final ForgeSoundType CALTROPS_SOUNDS = new ForgeSoundType(1.0f, 1.0f, RelicsSounds.CALTROPS_LAND, RelicsSounds.CALTROPS_TAP, RelicsSounds.CALTROPS_LAND, RelicsSounds.CALTROPS_TAP, RelicsSounds.CALTROPS_LAND);

	public Caltrops(Block material) {
		super(Properties.copy(material)
				.pushReaction(PushReaction.DESTROY)
				.noCollission()
				.noOcclusion()
				.strength(0.25f)
				.sound(CALTROPS_SOUNDS));
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}
	
	
	@Override
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		return canSupportRigidBlock(pLevel, pPos.below());
	}
	
	@Override
	public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
		if (pDirection == Direction.DOWN && !canSurvive(pState, pLevel, pPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return pState;
	}
	
	@Override
	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
		if (pEntity instanceof LivingEntity living && pEntity.onGround() && !pLevel.isClientSide) {
            if (living.hurt(pLevel.damageSources().cactus(), 2.0f)) {
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 2));
                living.addEffect(new MobEffectInstance(MobEffects.JUMP, 5 * 20, -2));
                if (pLevel.random.nextInt(10) < RelicsConfig.COMMON.caltropsDropChance.get()) {
                	pLevel.destroyBlock(pPos, false);
                }   
            }
        }
	}
}