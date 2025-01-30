package hojosa.relics.lib.block;

import java.util.List;

import javax.annotation.Nullable;

import hojosa.relics.common.block.entity.RetexturedSwordPedestalEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import slimeknights.mantle.block.RetexturedBlock;
import slimeknights.mantle.util.RetexturedHelper;

//to be used with retextured pedestal TE
public abstract class RetexturedSwordPedestal extends SwordPedestalBaseBlock {

	public RetexturedSwordPedestal(Properties builder, double renderOffSet) {
		super(builder, renderOffSet);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter pLevel, List<Component> tooltip, TooltipFlag pFlag) {
		RetexturedHelper.addTooltip(stack, tooltip);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(world, pos, state, placer, stack);
		RetexturedBlock.updateTextureBlock(world, pos, stack);
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return RetexturedBlock.getPickBlock(level, pos, state);
	}

	// override common methods to match the correct BE

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RetexturedSwordPedestalEntity(pos, state);
	}
}
