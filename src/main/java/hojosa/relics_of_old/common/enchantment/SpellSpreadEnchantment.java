package hojosa.relics_of_old.common.enchantment;

import hojosa.relics_of_old.common.init.RelicsEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

//Increases spell cast radius by 15% per level
public class SpellSpreadEnchantment extends Enchantment {
	public SpellSpreadEnchantment() {
		super(Rarity.COMMON, RelicsEnchantments.SPELL_CATEGORY, new EquipmentSlot[] { EquipmentSlot.MAINHAND });
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public int getMinCost(int level) {
		return 1 + (level - 1) * 10;
	}

	@Override
	public int getMaxCost(int level) {
		return getMinCost(level) + 16;
	}
}