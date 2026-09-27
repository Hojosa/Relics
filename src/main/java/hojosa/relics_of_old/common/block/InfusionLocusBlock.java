package hojosa.relics_of_old.common.block;

import hojosa.relics_of_old.common.block.entity.InfusionLocusBlockEntity;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.lib.block.RelicsNormalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class InfusionLocusBlock extends RelicsNormalBlock implements EntityBlock {

	public InfusionLocusBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5f).sound(SoundType.STONE).requiresCorrectToolForDrops());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new InfusionLocusBlockEntity(pos, state);
	}

	// Right-click to place/swap the target item on the locus
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (hand != InteractionHand.MAIN_HAND)
			return InteractionResult.PASS;

		if (level.getBlockEntity(pos) instanceof InfusionLocusBlockEntity locus) {
			ItemStack held = player.getItemInHand(hand);
			//skip infused star dust so the spell for the activation is spawned. maybe this should be a tag instead.....
			if (held.is(RelicsItems.INFUSED_STAR_DUST.get()))
                return InteractionResult.PASS;
			if (!level.isClientSide) {
				ItemStack current = locus.getTargetItem();
				
				if (!held.isEmpty() || !current.isEmpty()) {
					locus.setTargetItem(held.split(1));
					locus.itemRotation = player.getYRot();
					if (!current.isEmpty()) {
						player.addItem(current);
					}
					level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0f, 1.0f);
				}
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}

	// Drop target item and remove all helpers when broken
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (!state.is(newState.getBlock())) {
			if (level.getBlockEntity(pos) instanceof InfusionLocusBlockEntity locus) {
				ItemStack target = locus.getTargetItem();
				if (!target.isEmpty()) {
					Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, target);
				}
				locus.removeHelpers(true);
			}
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return (lvl, pos, st, be) -> {
			if (be instanceof InfusionLocusBlockEntity locus) {
				locus.tick();
			}
		};
	}
}