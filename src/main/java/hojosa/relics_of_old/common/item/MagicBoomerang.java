package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.entity.MagicBoomerangEntity;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class MagicBoomerang extends RelicsItem {

	public MagicBoomerang() {
		super(1, Rarity.UNCOMMON, 256);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);
		if (!pLevel.isClientSide) {
			MagicBoomerangEntity boomerang = new MagicBoomerangEntity(pLevel, pPlayer, stack);
			boomerang.setThrownFromSlot(pPlayer.getInventory().selected);
			boomerang.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0f, MagicBoomerangEntity.BOOMERANG_SPEED, 1.0f);
			pLevel.addFreshEntity(boomerang);
		}
		pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.5f, 0.4f / (pLevel.getRandom().nextFloat() * 0.4f + 0.8f));

		stack.shrink(1);
		return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}
}