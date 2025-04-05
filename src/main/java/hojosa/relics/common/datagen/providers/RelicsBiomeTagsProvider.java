package hojosa.relics.common.datagen.providers;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import hojosa.relics.common.init.RelicsTags;
import hojosa.relics.lib.References;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RelicsBiomeTagsProvider extends BiomeTagsProvider{

	public RelicsBiomeTagsProvider(PackOutput pOutput, CompletableFuture<Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(pOutput, pProvider, References.MOD_ID, existingFileHelper);
	}
	
@Override
	protected void addTags(Provider pProvider) {
		tag(RelicsTags.Biomes.HasStructure.PEDESTAL_TIME).addTags(BiomeTags.IS_FOREST);
		tag(RelicsTags.Biomes.HasStructure.PEDESTAL_TWILIGHT).addTags(BiomeTags.IS_FOREST);
	}

	@Override
	public String getName() {
		return "Relics Biome Tags";
	}
}
