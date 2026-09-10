package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class DimensionalCatalystItem extends RelicsItem {

	public DimensionalCatalystItem() {
		super(64);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);

		if (level.isClientSide)
			return InteractionResult.sidedSuccess(true);

		if (context.getPlayer() == null || !context.getPlayer().mayBuild())
			return InteractionResult.PASS;

		// Block must be movable, no block entity, and harvestable
		if (state.hasBlockEntity() || state.getPistonPushReaction() != PushReaction.NORMAL)
			return InteractionResult.PASS;

		// Azurite extraction: catalyst on ore → replace with stone, drop 3 azurite dots
		if (state.is(RelicsBlocks.AZURITE_ORE.get())) {
			level.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
			Direction face = context.getClickedFace();
			double spawnX = pos.getX() + 0.5 + face.getStepX();
			double spawnY = pos.getY() + 0.5 + face.getStepY();
			double spawnZ = pos.getZ() + 0.5 + face.getStepZ();
			for (int j = 0; j < 3; j++) {
				ItemEntity dot = new ItemEntity(level, spawnX, spawnY, spawnZ, new ItemStack(RelicsItems.AZURITE_DOT.get()));
				level.addFreshEntity(dot);
			}
			level.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0f, 1.0f);
			context.getItemInHand().shrink(1);
			return InteractionResult.SUCCESS;
		}

		// Try to teleport block to a random adjacent position
		for (int i = 0; i < 6; i++) {
			int dx = level.random.nextInt(3) - 1;
			int dy = level.random.nextInt(3) - 1;
			int dz = level.random.nextInt(3) - 1;
			BlockPos target = pos.offset(dx, dy, dz);

			if (level.isEmptyBlock(target) && state.canSurvive(level, target)) {
				level.setBlockAndUpdate(target, state);
				level.removeBlock(pos, false);
				level.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0f, 1.0f);
				context.getItemInHand().shrink(1);
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}
}