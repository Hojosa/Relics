package hojosa.relics.common.datagen.providers;

import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.common.loot.RelicsGlobalLootModifier;
import hojosa.relics.lib.References;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class RelicsGlobalLootModifiersProvider extends GlobalLootModifierProvider {

	public RelicsGlobalLootModifiersProvider(DataGenerator gen) {
		super(gen, References.MODID);
	}
	@Override
	protected void start() {
        ResourceLocation[] chests = new ResourceLocation[]{
                BuiltInLootTables.END_CITY_TREASURE,
                BuiltInLootTables.SHIPWRECK_TREASURE,
                BuiltInLootTables.RUINED_PORTAL,
                BuiltInLootTables.STRONGHOLD_LIBRARY,
        };
		
		for (ResourceLocation chest : chests) {
			add("fire_tablet_from_loot_chest", RelicsGlobalLootModifier.Serializer.INSTANCE, new RelicsGlobalLootModifier(new LootItemCondition[] {		
			LootItemRandomChanceCondition.randomChance(0.01f).build(),
			LootTableIdCondition.builder(chest).build()}, RelicsItems.FIRE_TABLET.get()));
		}
	}
}
