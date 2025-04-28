package hojosa.relics.common.datagen.providers;

import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.common.loot.AddItemModifier;
import hojosa.relics.lib.References;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class RelicsGlobalLootModifiersProvider extends GlobalLootModifierProvider {

	public RelicsGlobalLootModifiersProvider(PackOutput output) {
		super(output, References.MOD_ID);
	}

	@Override
	protected void start() {
		add("fire_tablet_from_loot_chest",
				new AddItemModifier(new LootItemCondition[] { 
						LootItemRandomChanceCondition.randomChance(0.1f).build(),
						LootTableIdCondition.builder(BuiltInLootTables.END_CITY_TREASURE)
						.or(LootTableIdCondition.builder(BuiltInLootTables.RUINED_PORTAL))
						.or(LootTableIdCondition.builder(BuiltInLootTables.PIGLIN_BARTERING))
						.or(LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_LIBRARY))
						.or(LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_TREASURE)).build() },
						RelicsItems.FIRE_TABLET.get()));

		add("water_tablet_from_loot_chest",
				new AddItemModifier(new LootItemCondition[] { 
						LootItemRandomChanceCondition.randomChance(0.1f).build(),
						LootTableIdCondition.builder(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY)
						.or(LootTableIdCondition.builder(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY))
						.or(LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY_ICE_BOX))
						.or(LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_BIG))
						.or(LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_TREASURE)).build() },
						RelicsItems.WATER_TABLET.get()));
		//todo, add supplemtaries urn
		add("emerald_shard_from_grass",
				new AddItemModifier(new LootItemCondition[] { 
						LootItemRandomChanceCondition.randomChance(0.5f).build(), 
						LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS).build() },
						RelicsItems.EMERALD_SHARD.get()));

		add("heart_from_grass",
				new AddItemModifier(new LootItemCondition[] { 
						LootItemRandomChanceCondition.randomChance(0.5f).build(), 
						LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS).build() },
						RelicsItems.HEART.get()));
		
		add("lost_page_1_from_loot_chest",
				new AddItemModifier(new LootItemCondition[] { 
						LootItemRandomChanceCondition.randomChance(0.20f).build(),
						LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_WEAPONSMITH)
						.or(LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_LIBRARY))
						.or(LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_TREASURE))
						.or(LootTableIdCondition.builder(BuiltInLootTables.WEAPONSMITH_GIFT))
						.or(LootTableIdCondition.builder(BuiltInLootTables.JUNGLE_TEMPLE)).build() },
						RelicsItems.LOST_PAGE_1.get()));
		
		add("lost_page_2_from_loot_chest",
				new AddItemModifier(new LootItemCondition[] { 
						LootItemRandomChanceCondition.randomChance(0.20f).build(),
						LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_WEAPONSMITH)
						.or(LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_WEAPONSMITH))
						.or(LootTableIdCondition.builder(BuiltInLootTables.RUINED_PORTAL))
						.or(LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_LIBRARY))
						.or(LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_DESERT_HOUSE)).build() },
						RelicsItems.LOST_PAGE_2.get()));
		
		add("lost_page_3_from_loot_chest",
				new AddItemModifier(new LootItemCondition[] { 
						LootItemRandomChanceCondition.randomChance(0.20f).build(),
						LootTableIdCondition.builder(BuiltInLootTables.LIBRARIAN_GIFT)
						.or(LootTableIdCondition.builder(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE))
						.or(LootTableIdCondition.builder(BuiltInLootTables.BASTION_TREASURE))
						.or(LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_LIBRARY))
						.or(LootTableIdCondition.builder(BuiltInLootTables.WOODLAND_MANSION)).build() },
						RelicsItems.LOST_PAGE_3.get()));
		
		add("lost_page_4_from_loot_chest",
				new AddItemModifier(new LootItemCondition[] { 
						LootItemRandomChanceCondition.randomChance(0.20f).build(),
						LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_MASON)
						.or(LootTableIdCondition.builder(BuiltInLootTables.MASON_GIFT))
						.or(LootTableIdCondition.builder(BuiltInLootTables.ABANDONED_MINESHAFT))
						.or(LootTableIdCondition.builder(BuiltInLootTables.SNIFFER_DIGGING))
						.or(LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY)).build() },
						RelicsItems.LOST_PAGE_4.get()));
		
		add("lost_page_5_from_loot_chest",
				new AddItemModifier(new LootItemCondition[] { 
						LootItemRandomChanceCondition.randomChance(0.20f).build(),
						LootTableIdCondition.builder(BuiltInLootTables.CLERIC_GIFT)
						.or(LootTableIdCondition.builder(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY))
						.or(LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_TREASURE))
						.or(LootTableIdCondition.builder(BuiltInLootTables.SNIFFER_DIGGING))
						.or(LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY)).build() },
						RelicsItems.LOST_PAGE_5.get()));
		
		add("lost_page_6_from_loot_chest",
				new AddItemModifier(new LootItemCondition[] { 
						LootItemRandomChanceCondition.randomChance(0.20f).build(),
						LootTableIdCondition.builder(BuiltInLootTables.BURIED_TREASURE)
						.or(LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_TEMPLE))
						.or(LootTableIdCondition.builder(BuiltInLootTables.DESERT_PYRAMID))
						.or(LootTableIdCondition.builder(BuiltInLootTables.SIMPLE_DUNGEON))
						.or(LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_SMALL)).build() },
						RelicsItems.LOST_PAGE_6.get()));
	}
}
