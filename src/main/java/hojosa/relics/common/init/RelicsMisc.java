package hojosa.relics.common.init;

import hojosa.relics.lib.ConfigEnabledCondition;
import hojosa.relics.lib.OptionalLootItem;
import hojosa.relics.lib.References;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsMisc {
//collections of ... deferred registers we dont need or want to have in a sperate class

	  protected static final SynchronizedDeferredRegister<LootItemConditionType> LOOT_CONDITIONS = SynchronizedDeferredRegister.create(Registries.LOOT_CONDITION_TYPE, References.MOD_ID);
	  public static final SynchronizedDeferredRegister<LootPoolEntryType> LOOT_POOL_ENTRY_TYPES = SynchronizedDeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, References.MOD_ID);
	    
	    
	  public static final RegistryObject<LootItemConditionType> lootConfig = LOOT_CONDITIONS.register(ConfigEnabledCondition.ID.getPath(), () -> new LootItemConditionType(ConfigEnabledCondition.SERIALIZER));
	  public static final RegistryObject<LootPoolEntryType> OPTIONAL_ITEM = LOOT_POOL_ENTRY_TYPES.register("optional_item", () -> new LootPoolEntryType(new OptionalLootItem.Serializer()));
}
