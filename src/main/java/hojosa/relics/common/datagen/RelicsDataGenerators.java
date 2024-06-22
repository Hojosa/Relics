package hojosa.relics.common.datagen;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import hojosa.relics.common.datagen.providers.RelicsBlockTags;
import hojosa.relics.common.datagen.providers.RelicsGlobalLootModifiersProvider;
import hojosa.relics.common.datagen.providers.RelicsItemTags;
import hojosa.relics.common.datagen.providers.RelicsLootTables;
import hojosa.relics.common.datagen.providers.RelicsRecipes;
import hojosa.relics.lib.References;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RelicsDataGenerators {
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		
		RelicsBlockTags blockTags = new RelicsBlockTags(packOutput, lookupProvider, existingFileHelper);
		generator.addProvider(event.includeServer(), blockTags);
		generator.addProvider(event.includeServer(), new RelicsItemTags(packOutput, lookupProvider, blockTags, existingFileHelper));
		generator.addProvider(event.includeServer(), new RelicsRecipes(packOutput));
		generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(RelicsLootTables::new, LootContextParamSets.BLOCK))));
		generator.addProvider(event.includeServer(), new RelicsGlobalLootModifiersProvider(packOutput));
	}

}
