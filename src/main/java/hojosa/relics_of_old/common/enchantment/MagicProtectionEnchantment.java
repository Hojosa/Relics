package hojosa.relics_of_old.common.enchantment;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

//Reduces magic damage — compatible with feather falling, conflicts with other protection types
public class MagicProtectionEnchantment extends Enchantment {

	private static final EquipmentSlot[] ARMOR_SLOTS = { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };

	public MagicProtectionEnchantment() {
		super(Rarity.RARE, EnchantmentCategory.ARMOR, ARMOR_SLOTS);
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public int getMinCost(int level) {
		return 5 + level * 8;
	}

	@Override
	public int getMaxCost(int level) {
		return getMinCost(level) + 12;
	}

	// LG2 formula: (6 + level²) / 3 * 1.5, only on magic damage
	@Override
	public int getDamageProtection(int level, DamageSource source) {
		if (source.is(DamageTypes.MAGIC) || source.is(DamageTypes.INDIRECT_MAGIC)) {
			return Mth.floor((6.0f + level * level) / 3.0f * 1.5f);
		}
		return 0;
	}

	// Compatible with feather falling, conflicts with other protection enchantments
	@Override
	protected boolean checkCompatibility(Enchantment other) {
		if (other instanceof ProtectionEnchantment prot) {
			return prot.type == ProtectionEnchantment.Type.FALL;
		}
		return super.checkCompatibility(other);
	}
}