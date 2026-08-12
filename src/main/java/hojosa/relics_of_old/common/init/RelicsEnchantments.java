package hojosa.relics_of_old.common.init;

import hojosa.relics_of_old.common.FocusEnchantment;
import hojosa.relics_of_old.lib.References;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsEnchantments {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, References.MOD_ID);

    public static final RegistryObject<Enchantment> FOCUS = ENCHANTMENTS.register("focus", FocusEnchantment::new);
		
}