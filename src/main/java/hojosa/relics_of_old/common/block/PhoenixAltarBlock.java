package hojosa.relics_of_old.common.block;

import hojosa.relics_of_old.common.block.entity.PhoenixAltarBlockEntity;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.lib.block.RelicsBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PhoenixAltarBlock extends RelicsBlock implements EntityBlock {
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 12, 16);

	public PhoenixAltarBlock() {
		super(BlockBehaviour.Properties.of().strength(50.0f, 2000.0f).requiresCorrectToolForDrops().sound(SoundType.STONE).lightLevel(state -> 10).noOcclusion());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new PhoenixAltarBlockEntity(pPos, pState);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (!pLevel.isClientSide() && pHand == InteractionHand.MAIN_HAND && pHit.getDirection() == Direction.UP) {
			ItemStack itemInHand = pPlayer.getItemInHand(pHand);

			// Let infused star dust activate its own right-click interaction
			if (itemInHand.is(RelicsItems.INFUSED_STAR_DUST.get()))
				return InteractionResult.PASS;

			BlockEntity be = pLevel.getBlockEntity(pPos);
			if (be instanceof PhoenixAltarBlockEntity altar) {
				ItemStack slotStack = altar.getItem(0);

				if (!itemInHand.isEmpty() || !slotStack.isEmpty()) {
					altar.setItem(0, itemInHand.copy());
					altar.setItemFacing(pPlayer.getDirection());
					itemInHand.setCount(0);
					pPlayer.addItem(slotStack);
					pLevel.playSound(null, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
			}
		}
		return InteractionResult.SUCCESS;
	}
}