package hojosa.relics_of_old.common.block;

import hojosa.relics_of_old.common.block.entity.StarwellBlockEntity;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.block.RelicsNormalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class StarwellBlock extends RelicsNormalBlock implements EntityBlock {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	public static final int STARWELL_RECOVER_TIME = 6000;

	public StarwellBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).strength(50.0f, 2000.0f).sound(SoundType.STONE).requiresCorrectToolForDrops().lightLevel(state -> state.getValue(ACTIVE) ? 8 : 0).noOcclusion());
		this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		if (state.getValue(ACTIVE)) {
			return new StarwellBlockEntity(pos, state);
		}
		return null;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (!state.getValue(ACTIVE))
			return null;
		return (lvl, pos, st, be) -> {
			if (be instanceof StarwellBlockEntity starwell) {
				starwell.tick();
			}
		};
	}

	// Frame layout: 8 blocks in a ring at y+1, 4 blocks in a plus at y+0
	private static boolean checkFrameAt(Level level, BlockPos corePos, int dx, int dy, int dz) {
		return level.getBlockState(corePos.offset(dx, dy, dz)).is(RelicsBlocks.STARWELL_FRAME.get());
	}

	public static boolean checkFrame(Level level, BlockPos corePos) {
		// Ring at y+1 (8 positions around the 3x3 minus center)
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (dx == 0 && dz == 0)
					continue;
				if (!checkFrameAt(level, corePos, dx, 1, dz))
					return false;
			}
		}
		// Plus at y+0 (4 cardinal directions)
		return checkFrameAt(level, corePos, -1, 0, 0) && checkFrameAt(level, corePos, 1, 0, 0) && checkFrameAt(level, corePos, 0, 0, -1) && checkFrameAt(level, corePos, 0, 0, 1);
	}

	// Called by StarwellFrameBlock onPlace/onRemove and by neighborChanged
	public void recheckFrame(Level level, BlockPos pos, BlockState state) {
		boolean shouldBeActive = checkFrame(level, pos);
		if (state.getValue(ACTIVE) != shouldBeActive) {
			level.setBlockAndUpdate(pos, state.setValue(ACTIVE, shouldBeActive));
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		if (!level.isClientSide) {
			recheckFrame(level, pos, state);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, net.minecraft.world.phys.BlockHitResult hit) {
		// Only interact from the top on an active starwell
		if (hit.getDirection() != net.minecraft.core.Direction.UP || !state.getValue(ACTIVE)) {
			return InteractionResult.PASS;
		}

		ItemStack stack = player.getItemInHand(hand);
		// Don't interact if holding a block item
		if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
			return InteractionResult.PASS;
		}

		if (!level.isClientSide) {
			CompoundTag data = player.getPersistentData();
			long lastDrink = data.getLong(References.MOD_ID + ":starwell_drink_time");
			int charge = data.getInt(References.MOD_ID + ":starwell_charge");

			// Reset charge if enough time has passed
			long elapsed = level.getGameTime() - lastDrink;
			if (elapsed >= STARWELL_RECOVER_TIME) {
				charge = 0;
			}

			// Healing and sound
			level.playSound(null, pos, RelicsSounds.MYSTERY_SPARKLE.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
			player.heal(6.0f);
			player.getFoodData().eat(1, 1.0f);

			// Roll fudge dice: 2dF - charge
			int roll = rollDF(2, level.random) - charge;

			if (roll >= 1) {
				// Reward: drop star dust
				level.playSound(null, pos, RelicsSounds.STAR_APPEAR.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
				ItemStack reward;
				if (roll >= 2) {
					reward = new ItemStack(RelicsItems.INFUSED_STAR_DUST.get());
				} else {
					reward = new ItemStack(RelicsItems.STAR_DUST.get());
				}
				ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, reward);
				itemEntity.setPickUpDelay(20);
				level.addFreshEntity(itemEntity);
			}

			// Punishment at high charges
			if (roll <= -3 || charge >= 3) {
				player.setSecondsOnFire(10);
			}
			if (roll <= -5 && charge > 3) {
				level.explode(null, player.getX(), player.getY() + 0.25, player.getZ(), 3.0f, Level.ExplosionInteraction.NONE);
			}

			charge = Math.min(charge + 1, 6);
			data.putInt(References.MOD_ID + ":starwell_charge", charge);
			data.putLong(References.MOD_ID + ":starwell_drink_time", level.getGameTime());
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	// Fudge dice: N dice, each -1/0/+1
	private int rollDF(int dice, net.minecraft.util.RandomSource rand) {
		int total = 0;
		for (int i = 0; i < dice; i++) {
			total += rand.nextInt(3) - 1;
		}
		return total;
	}

}
