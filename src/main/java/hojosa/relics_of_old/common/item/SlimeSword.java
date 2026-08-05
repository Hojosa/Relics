package hojosa.relics_of_old.common.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class SlimeSword extends SwordItem {

	public SlimeSword() {
		super(Tiers.WOOD, 0, -2.4F, new Item.Properties().durability(131));
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hurtEnemy(stack, target, attacker);
		if (!attacker.level().isClientSide) {
			target.level().playSound(null, target.blockPosition(), SoundEvents.SLIME_SQUISH_SMALL, SoundSource.PLAYERS, 1.0f, 0.7f + attacker.getRandom().nextFloat() * 0.5f);
			target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20));
			target.heal(1);
			target.invulnerableTime = target.invulnerableTime;
		}
		return result;
	}
}