package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.entity.MedallionEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class Medallion extends EmptyMedallion {

	public Medallion(MedallionType medallionType, int charges) {
		super(medallionType, charges);
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return true;
	}

	@Override
	public boolean isRepairable(ItemStack stack) {
		return false;
	}

	@Override
	public int getUseDuration(ItemStack pStack) {
		return 15;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.BOW;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		var item = pPlayer.getItemInHand(pUsedHand);
		if (item.getDamageValue() > 0) {
			item.setDamageValue(item.getDamageValue() - 1);
			return InteractionResultHolder.success(item);
		}
		pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 0.5f, 1.0F);
		return InteractionResultHolder.fail(item);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
		pLevel.playSound(null, pLivingEntity.blockPosition(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 0.5f, 1.0F);
		MedallionEntity boomerang = new MedallionEntity(pLevel, pLivingEntity, pStack, this.type);
		pLevel.addFreshEntity(boomerang);

		return pStack;
	}
	
	@Override
	public ItemStack getDefaultInstance() {
		return new ItemStack(this);
	}
}