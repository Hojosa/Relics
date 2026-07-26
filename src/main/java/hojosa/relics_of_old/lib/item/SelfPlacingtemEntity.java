package hojosa.relics_of_old.lib.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class SelfPlacingtemEntity extends ItemEntity {

	public SelfPlacingtemEntity(Level pLevel, double pPosX, double pPosY, double pPosZ, ItemStack pItemStack) {
		super(pLevel, pPosX, pPosY, pPosZ, pItemStack);
		this.setDefaultPickUpDelay();
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide && onGround()) {
			Block block = Block.byItem(getItem().getItem());
			if (block != Blocks.AIR) {
				BlockPos landed = blockPosition();
				BlockPos placePos = findPlaceablePos(block, landed);
				if (placePos != null) {
					BlockState state = block.defaultBlockState();
					level().setBlock(placePos, state, 3);
					SoundType sound = state.getSoundType();
					level().playSound(null, placePos, sound.getPlaceSound(), SoundSource.BLOCKS, 0.5f, sound.getPitch());
					getItem().shrink(1);
					if (getItem().isEmpty()) {
						discard();
					}
				}
			}
		}
	}

	private BlockPos findPlaceablePos(Block pBlock, BlockPos pCenter) {
		BlockState state = pBlock.defaultBlockState();
		if (canPlaceAt(state, pCenter))
			return pCenter;
		for (BlockPos pos : BlockPos.betweenClosed(pCenter.offset(-1, 0, -1), pCenter.offset(1, 0, 1))) {
			if (canPlaceAt(state, pos))
				return pos.immutable();
		}
		return null;
	}

	private boolean canPlaceAt(BlockState state, BlockPos pos) {
		BlockState existing = level().getBlockState(pos);
		return (existing.isAir() || existing.canBeReplaced()) && state.canSurvive(level(), pos);
	}
}