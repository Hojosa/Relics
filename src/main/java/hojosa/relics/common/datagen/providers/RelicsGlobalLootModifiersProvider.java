package hojosa.relics.common.datagen.providers;

import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.common.loot.AddItemModifier;
import hojosa.relics.lib.References;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class RelicsGlobalLootModifiersProvider extends GlobalLootModifierProvider {

	public RelicsGlobalLootModifiersProvider(PackOutput output) {
		super(output, References.MODID);
	}
	
	@Override
	protected void start() {
		add("fire_tablet_from_loot_chest", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.03f).build(),
				LootTableIdCondition.builder(BuiltInLootTables.END_CITY_TREASURE)
				.or(LootTableIdCondition.builder(BuiltInLootTables.RUINED_PORTAL))
				.or(LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_LIBRARY))
				.or(LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_TREASURE)).build()}, 
				RelicsItems.FIRE_TABLET.get()));
	}
}
