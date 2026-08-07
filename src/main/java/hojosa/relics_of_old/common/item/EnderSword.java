package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.init.RelicsEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class EnderSword extends SwordItem {
	
	public EnderSword() {
		super(Tiers.IRON, 3, -2.4F, new Item.Properties());
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (!attacker.level().isClientSide) {
			// 10 seconds of teleport suppression
			target.addEffect(new MobEffectInstance(RelicsEffects.ENDER_LOCK.get(), 200, 0, false, true));
		}
		return super.hurtEnemy(stack, target, attacker);
	}
}