package hojosa.relics_of_old.common.block;

import hojosa.relics_of_old.common.block.entity.InfusionItemHolderBlockEntity;
import hojosa.relics_of_old.common.block.entity.InfusionLocusBlockEntity;
import hojosa.relics_of_old.lib.block.RelicsNormalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

//Invisible helper block at each infusion grid point — holds one ingredient item
public class InfusionItemHolderBlock extends RelicsNormalBlock implements EntityBlock {

	private static final VoxelShape SELECTION_SHAPE = Block.box(0, 0, 0, 16, 1, 16);

	public InfusionItemHolderBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).noCollission().noOcclusion().noLootTable().pushReaction(PushReaction.BLOCK).strength(-1.0f, 3600000.0f));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SELECTION_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	// Right-click to place/swap ingredient item
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (hand != InteractionHand.MAIN_HAND)
			return InteractionResult.PASS;

		if (level.getBlockEntity(pos) instanceof InfusionItemHolderBlockEntity holder) {
			if (!level.isClientSide) {
				ItemStack held = player.getItemInHand(hand);
				ItemStack current = holder.getItem(0);
				boolean hadItem = !current.isEmpty();
				boolean placingItem = !held.isEmpty();

				if (placingItem || hadItem) {
					holder.setItem(0, held.split(1));
					holder.itemRotation = player.getYRot();
					if (hadItem) {
						player.addItem(current);
					}
					level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0f, 1.0f);

					// Notify locus of item change for edge rebuilding
					notifyLocus(level, holder, placingItem && !hadItem);
				}
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}

	// Notify the parent locus when an item is placed or removed
	private void notifyLocus(Level level, InfusionItemHolderBlockEntity helper, boolean placed) {
		BlockPos locusPos = helper.getLocusPos();
		if (locusPos == null)
			return;

		if (level.getBlockEntity(locusPos) instanceof InfusionLocusBlockEntity locus) {
			// Find which grid point index this helper is
			if (locus.grid != null) {
				for (int i = 0; i < 8; i++) {
					if (locus.grid.places[i].equals(helper.getBlockPos())) {
						locus.onHelperItemChanged(i, placed);
						break;
					}
				}
			}
		}
	}

	// Drop stored item when the block is removed (by the locus, not by players)
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (!state.is(newState.getBlock())) {
			if (level.getBlockEntity(pos) instanceof InfusionItemHolderBlockEntity helper) {
				ItemStack item = helper.getItem(0);
				if (!item.isEmpty()) {
					Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, item);
				}
			}
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new InfusionItemHolderBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return null;
	}
}