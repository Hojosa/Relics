package hojosa.relics.common.init;

import hojosa.relics.common.loot.GlobalLootModifier;
import hojosa.relics.lib.References;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RelicsLootModifiers {
	
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, References.MODID);

    public static final RegistryObject<GlobalLootModifierSerializer<GlobalLootModifier>> GLOBAL_BLOCK_LOOT_MODIFIER =
            LOOT_MODIFIER_SERIALIZERS.register("global_block_loot_modifier", GlobalLootModifier.Serializer::new);

    public static void register(IEventBus bus) {
        LOOT_MODIFIER_SERIALIZERS.register(bus);
    }

}
