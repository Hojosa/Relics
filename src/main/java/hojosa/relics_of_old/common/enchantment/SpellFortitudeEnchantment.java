package hojosa.relics_of_old.common.enchantment;

import hojosa.relics_of_old.common.init.RelicsEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

//Reduces mana recharge delay penalty by level/3 (at max level 3, penalty eliminated)
public class SpellFortitudeEnchantment extends Enchantment {
	public SpellFortitudeEnchantment() {
		super(Rarity.UNCOMMON, RelicsEnchantments.SPELL_CATEGORY, new EquipmentSlot[] { EquipmentSlot.MAINHAND });
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinCost(int level) {
		return 10 + (level - 1) * 20;
	}

	@Override
	public int getMaxCost(int level) {
		return getMinCost(level) + 30;
	}
}