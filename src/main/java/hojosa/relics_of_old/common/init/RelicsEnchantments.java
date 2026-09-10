package hojosa.relics_of_old.common.init;

import hojosa.relics_of_old.common.enchantment.FocusEnchantment;
import hojosa.relics_of_old.common.enchantment.MagicProtectionEnchantment;
import hojosa.relics_of_old.common.enchantment.SoulTetherEnchantment;
import hojosa.relics_of_old.common.enchantment.SpellFortitudeEnchantment;
import hojosa.relics_of_old.common.enchantment.SpellReachEnchantment;
import hojosa.relics_of_old.common.enchantment.SpellSpreadEnchantment;
import hojosa.relics_of_old.common.item.SpellCastingItem;
import hojosa.relics_of_old.lib.References;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsEnchantments {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, References.MOD_ID);
	
	// Custom category for spell items (staffs, tomes)
    public static final EnchantmentCategory SPELL_CATEGORY = EnchantmentCategory.create("RELICS_SPELL", item -> item instanceof SpellCastingItem);

    public static final RegistryObject<Enchantment> FOCUS = ENCHANTMENTS.register("focus", FocusEnchantment::new);
    public static final RegistryObject<Enchantment> SPELL_REACH = ENCHANTMENTS.register("spell_reach", SpellReachEnchantment::new);
    public static final RegistryObject<Enchantment> SPELL_SPREAD = ENCHANTMENTS.register("spell_spread", SpellSpreadEnchantment::new);
    public static final RegistryObject<Enchantment> SPELL_FORTITUDE = ENCHANTMENTS.register("spell_fortitude", SpellFortitudeEnchantment::new);
    public static final RegistryObject<Enchantment> MAGIC_PROTECTION = ENCHANTMENTS.register("magic_protection", MagicProtectionEnchantment::new);
    public static final RegistryObject<Enchantment> SOUL_TETHER = ENCHANTMENTS.register("soul_tether", SoulTetherEnchantment::new);
}