package hojosa.relics.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class FlameSword extends SwordItem{

	public FlameSword() {
		super(Tiers.IRON, 3, -2.4F, new Item.Properties().fireResistant());
	}

	@Override
	public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
		if(!pTarget.isOnFire()) {
			pTarget.setSecondsOnFire(5);
		}
		return super.hurtEnemy(pStack, pTarget, pAttacker);
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if(enchantment == Enchantments.FIRE_ASPECT)
			return false;
		return super.canApplyAtEnchantingTable(stack, enchantment);
	}
}
