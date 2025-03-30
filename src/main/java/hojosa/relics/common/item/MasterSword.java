package hojosa.relics.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class MasterSword extends SwordItem{

	public MasterSword() {
		super(Tiers.NETHERITE, 5, -2.4F, new Item.Properties());
	}

	@Override
	public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
		 if (pTarget.getMobType() == MobType.UNDEAD) {
			 pTarget.hurt(pAttacker.damageSources().playerAttack((Player) pAttacker), 15);
		 }
		return super.hurtEnemy(pStack, pTarget, pAttacker);
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if(enchantment == Enchantments.SMITE)
			return false;
		return super.canApplyAtEnchantingTable(stack, enchantment);
	}
}
