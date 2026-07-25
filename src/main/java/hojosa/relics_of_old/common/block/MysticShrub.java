package hojosa.relics_of_old.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

public class MysticShrub extends BushBlock {

	public static final EnumProperty<ShrubState> STATE = EnumProperty.create("state", ShrubState.class);
	private static final VoxelShape NORMAL_SHAPE = Block.box(0, 0, 0, 16, 14, 16).optimize();
    private static final VoxelShape STUMP_SHAPE = Block.box(0, 0, 0, 16, 3, 16).optimize();

	public enum ShrubState implements StringRepresentable {
		NORMAL("normal"), CHARGED("charged"), STUMP("stump");

		private final String name;

		ShrubState(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}

	public MysticShrub() {
		super(BlockBehaviour.Properties.copy(Blocks.GRASS)
				.randomTicks()
				.lightLevel(state -> switch (state.getValue(STATE)) {
					case NORMAL -> 5;
					case CHARGED -> 8;
					case STUMP -> 2;
				}));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder.add(STATE));
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return pState.getValue(STATE) == ShrubState.STUMP ? STUMP_SHAPE : NORMAL_SHAPE;
	}
	
	@Override
	public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
		if (pState.getValue(STATE) == ShrubState.STUMP) {
	          float f = 1.0f;
	          int i = ForgeHooks.isCorrectToolForDrops(pState, pPlayer) ? 30 : 100;
	          return pPlayer.getDigSpeed(pState, pPos) / f / (float) i;
	      }
	      return 1.0f;
	}
	
	@Override
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		if (!super.canSurvive(pState, pLevel, pPos)) 
			return false;
		return pLevel.canSeeSky(pPos);
	}
	
	@Override
	public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, BlockEntity pBlockEntity, ItemStack pTool) {
		super.playerDestroy(pLevel, pPlayer, pPos, pState, pBlockEntity, pTool);
		if (pState.getValue(STATE) != ShrubState.STUMP) {
			pLevel.setBlock(pPos, this.defaultBlockState().setValue(STATE, ShrubState.STUMP), UPDATE_ALL);
		}
	}
	
	@Override
	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
		if (pState.getValue(STATE) == ShrubState.CHARGED && pEntity instanceof LivingEntity) {
			if (!pLevel.isClientSide && pLevel.isRainingAt(pPos)) {
				pLevel.addFreshEntity(new LightningBolt(EntityType.LIGHTNING_BOLT, pLevel) {{
					setPos(pPos.getX(), pPos.getY(), pPos.getZ());
				}});
			}
			pLevel.setBlock(pPos, pState.setValue(STATE, ShrubState.NORMAL), UPDATE_ALL);
		}
	}
	
	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if (!canSurvive(pState, pLevel, pPos)) {
			pLevel.destroyBlock(pPos, true);
            return;
        }

        ShrubState shrubState = pState.getValue(STATE);

        // Charged reverts when no thunderstorm
        if (shrubState == ShrubState.CHARGED && !pLevel.isThundering()) {
        	pLevel.setBlock(pPos, pState.setValue(STATE, ShrubState.NORMAL), 3);
            return;
        }

        // Stump regrows (faster in rain)
        if (shrubState == ShrubState.STUMP) {
            boolean shouldGrow = pLevel.isRaining() ? pRandom.nextInt(4) == 0 : pRandom.nextInt(20) == 0;
            if (shouldGrow) {
                ShrubState newState = pLevel.isThundering() ? ShrubState.CHARGED : ShrubState.NORMAL;
                pLevel.setBlock(pPos, pState.setValue(STATE, newState), 3);
            }
        }
	}
}