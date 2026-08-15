package hojosa.relics_of_old.common.block;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import hojosa.relics_of_old.common.entity.BombEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BombFlower extends BushBlock {

	public static final EnumProperty<FlowerState> STATE = EnumProperty.create("state", FlowerState.class);
	private static final VoxelShape NORMAL_SHAPE = Block.box(0, 0, 0, 16, 14, 16).optimize();
	private static final VoxelShape CUT_SHAPE = Block.box(0, 0, 0, 16, 3, 16).optimize();

	public enum FlowerState implements StringRepresentable {
		NORMAL("normal"), CUT("cut");

		private final String name;

		FlowerState(String name) {
			this.name = name;
		}

		@Override
		public @NotNull String getSerializedName() {
			return name;
		}
	}

	public BombFlower() {
		super(BlockBehaviour.Properties.copy(Blocks.GRASS).randomTicks().noCollission().offsetType(OffsetType.NONE));
		registerDefaultState(stateDefinition.any().setValue(STATE, FlowerState.NORMAL));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(STATE));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(STATE) == FlowerState.CUT ? CUT_SHAPE : NORMAL_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
		// CUT stump is tougher, NORMAL is instant-break
		if (state.getValue(STATE) == FlowerState.CUT) {
			return player.getDigSpeed(state, pos) / 1.0f / 100.0f;
		}
		return 1.0f;
	}

	// valid support: stone, netherrack, or deepslate
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		Block block = state.getBlock();
		return block == Blocks.STONE || block == Blocks.NETHERRACK || block == Blocks.DEEPSLATE || block == Blocks.BASALT || block == Blocks.BLACKSTONE;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos below = pos.below();
		BlockState belowState = level.getBlockState(below);
		if (!mayPlaceOn(belowState, level, below))
			return false;

		// must have air above
		if (!level.getBlockState(pos.above()).isAir())
			return false;

		// must have lava within 1 block horizontally at same Y
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				BlockPos check = pos.offset(x, 0, z);
				if (level.getBlockState(check).getFluidState().is(Fluids.LAVA) || level.getBlockState(check.below()).getFluidState().is(Fluids.LAVA)) {
					return true;
				}
			}
		}
		return false;
	}

	// breaking the flower spawns a bomb, leaves the stump
	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		super.playerDestroy(level, player, pos, state, blockEntity, tool);
		if (state.getValue(STATE) == FlowerState.NORMAL && !(tool.getItem() instanceof net.minecraft.world.item.ShearsItem)) {
			level.setBlock(pos, defaultBlockState().setValue(STATE, FlowerState.CUT), UPDATE_ALL);
			spawnBomb(level, pos, BombEntity.LONG_FUSE_TIME);
		}
	}

	// explosion triggers a short-fuse bomb (chain reaction)
	@Override
	public void onBlockExploded(BlockState state, Level level, BlockPos pos, net.minecraft.world.level.Explosion explosion) {
		if (state.getValue(STATE) == FlowerState.NORMAL) {
			level.setBlock(pos, defaultBlockState().setValue(STATE, FlowerState.CUT), UPDATE_ALL);
			spawnBomb(level, pos, BombEntity.SHORT_FUSE_TIME);
		} else {
			super.onBlockExploded(state, level, pos, explosion);
		}
	}

	// if support is lost while NORMAL, pop off as a bomb
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		if (!canSurvive(state, level, pos)) {
			if (state.getValue(STATE) == FlowerState.NORMAL) {
				spawnBomb(level, pos, BombEntity.LONG_FUSE_TIME);
			}
			level.removeBlock(pos, false);
		}
	}

	// regrowth: 1 in 5 chance per random tick
	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!canSurvive(state, level, pos)) {
			level.destroyBlock(pos, false);
			return;
		}
		if (state.getValue(STATE) == FlowerState.CUT && random.nextInt(5) == 0) {
			level.setBlock(pos, defaultBlockState().setValue(STATE, FlowerState.NORMAL), UPDATE_ALL);
		}
	}

	private void spawnBomb(Level level, BlockPos pos, int fuseTime) {
		if (!level.isClientSide) {
			BombEntity bomb = new BombEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, fuseTime - level.random.nextInt(5));
			bomb.setDeltaMovement(0, 0.1, 0);
			level.addFreshEntity(bomb);
		}
	}
}