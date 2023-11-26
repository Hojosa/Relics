package hojosa.relics.common.datagen;

import hojosa.relics.common.datagen.providers.RelicsBlockTags;
import hojosa.relics.common.datagen.providers.RelicsItemTags;
import hojosa.relics.common.datagen.providers.RelicsLootTables;
import hojosa.relics.common.datagen.providers.RelicsRecipes;
import hojosa.relics.lib.References;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RelicsDataGenerators {
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		
		
		if(event.includeServer()) {
			RelicsBlockTags blockTags = new RelicsBlockTags(generator, existingFileHelper);
			generator.addProvider(blockTags);
			generator.addProvider(new RelicsItemTags(generator, blockTags, existingFileHelper));
			generator.addProvider(new RelicsRecipes(generator));
			generator.addProvider(new RelicsLootTables(generator));
		}
		if(event.includeClient()) {
			
		}
	}

}
