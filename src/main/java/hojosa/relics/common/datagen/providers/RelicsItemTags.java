package hojosa.relics.common.datagen.providers;


import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import hojosa.relics.integration.curios.CuriosIntegration;
import hojosa.relics.lib.References;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.ModList;

public class RelicsItemTags extends ItemTagsProvider {

	public RelicsItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider pBlockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(packOutput, lookupProvider, pBlockTagsProvider.contentsGetter(), References.MODID, existingFileHelper);
	}

	@Override 
	protected void addTags(HolderLookup.Provider provider) {
		System.out.println(ModList.get().isLoaded("curios"));
		if(ModList.get().isLoaded("curios")){
			CuriosIntegration.generateItemTags(this::tag);
		}
	}

    @Override
    public String getName() {
        return "Relics Item Tags";
    }
}
