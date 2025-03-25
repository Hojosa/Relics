package hojosa.relics.lib.block;

import hojosa.relics.common.block.entity.SwordPedestalBlockEntity;
import hojosa.relics.common.init.RelicsSounds;
import hojosa.relics.common.init.RelicsTags;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;

public abstract class SwordPedestalBaseBlock extends RelicsFacingEntityBlock {
	public static final BooleanProperty SWORD = BooleanProperty.create("sword");
	public static final BooleanProperty REPAIR = BooleanProperty.create("repair");
	public static final BooleanProperty GLOW = BooleanProperty.create("glow");
	protected SoundEvent placeSound = RelicsSounds.SWORD_PLACE_SOUND.get();
	protected SoundEvent drawSound = RelicsSounds.SWORD_DRAW_SOUND.get();

	@Getter
	private double renderOffSet;

	public SwordPedestalBaseBlock(Properties builder, double renderOffSet) {
		super(builder);
		this.renderOffSet = renderOffSet;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
		super.createBlockStateDefinition(blockStateBuilder.add(SWORD).add(REPAIR).add(GLOW));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(SWORD, Boolean.FALSE).setValue(REPAIR, false).setValue(GLOW, false);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SwordPedestalBlockEntity(pos, state);
	}

	@Override
	public abstract VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		SwordPedestalBlockEntity blockEntity = (SwordPedestalBlockEntity) level.getBlockEntity(pos);
		ItemStack itemInHand = player.getItemInHand(hand);
		if (!level.isClientSide) {

			int slot = 0;
			ItemStack slotStack = blockEntity.getItem(slot);

			if ((!itemInHand.isEmpty() && blockEntity.canPlaceItem(slot, itemInHand)) || !slotStack.isEmpty() && (itemInHand.isEmpty() || blockEntity.canPlaceItem(slot, itemInHand))) {
				blockEntity.setItem(slot, itemInHand);
				player.setItemInHand(hand, slotStack);
				level.playSound(null, pos, blockEntity.isEmpty() ? drawSound : placeSound, SoundSource.BLOCKS, 1.0F, 1.0F);
				state = state.setValue(SWORD, !blockEntity.isEmpty()).setValue(REPAIR, blockEntity.isSwordDamaged() && blockEntity.isInfused());
				level.setBlock(pos, state, UPDATE_ALL);
				return InteractionResult.SUCCESS;
			}
			// pedestal upgrade
			else if (itemInHand.is(RelicsTags.Items.SWORD_PEDESTAL_INFUSEABLE) && !(blockEntity.isInfused())) {
				blockEntity.infusePedestal();
				player.getItemInHand(hand).setCount(itemInHand.getCount() - 1);
				level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
				return InteractionResult.SUCCESS;
			}
			// glow upgrade
			else if (itemInHand.is(RelicsTags.Items.SWORD_PEDESTAL_GLOW) && !blockEntity.isGlowing()) {
				level.playSound(null, pos, SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
				player.getItemInHand(hand).setCount(itemInHand.getCount() - 1);
				state = state.setValue(GLOW, true);
				blockEntity.glowPedestal();
				level.setBlock(pos, state, UPDATE_ALL);
				return InteractionResult.SUCCESS;
			}
		}
		if (itemInHand.is(Tags.Items.DYES) && ((blockEntity.isGlowing() || blockEntity.isInfused()))) {
			DyeItem dye = (DyeItem) itemInHand.getItem();

			if (dye.getDyeColor().getTextColor() != blockEntity.getGlowColor()) {
				blockEntity.setGlowColor(dye.getDyeColor().getTextColor());
				player.getItemInHand(hand).setCount(itemInHand.getCount() - 1);
				level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.markAndNotifyBlock(pos, level.getChunkAt(pos), state, state, UPDATE_CLIENTS, 1);
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.FAIL;
		} else if (itemInHand.is(RelicsTags.Items.CLEANER) && blockEntity.getGlowColor() != 0x6699cc) {
			blockEntity.setGlowColor(0x6699cc);
			level.markAndNotifyBlock(pos, level.getChunkAt(pos), state, state, UPDATE_CLIENTS, 1);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return state.getValue(SWORD);
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
		if (pBlockState.getValue(SWORD)) {
			SwordPedestalBlockEntity blockEntity = (SwordPedestalBlockEntity) pBlockAccess.getBlockEntity(pPos);
			SwordItem sword = (SwordItem) blockEntity.getItem(0).getItem();
			return sword.getTier().getLevel() + 5;
		} else
			return 0;
	}

	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		SwordPedestalBlockEntity blockEntity = (SwordPedestalBlockEntity) pLevel.getBlockEntity(pPos);
		blockEntity.repairSword();
	}

	@Override
	public boolean isRandomlyTicking(BlockState pState) {
		return pState.getValue(REPAIR);
	}

	public int getColor(int pTintIndex, BlockEntity blockEntity) {
		return pTintIndex == 1 ? ((SwordPedestalBlockEntity) blockEntity).getGlowColor() : -1;
	}
}
