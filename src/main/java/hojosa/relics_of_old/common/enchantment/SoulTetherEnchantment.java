package hojosa.relics_of_old.common.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SoulTetherEnchantment extends Enchantment {
	public SoulTetherEnchantment() {
		super(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public int getMinCost(int level) {
		return 20;
	}

	@Override
	public int getMaxCost(int level) {
		return 50;
	}
}