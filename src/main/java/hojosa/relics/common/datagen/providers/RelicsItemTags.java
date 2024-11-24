package hojosa.relics.common.datagen.providers;


import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.common.init.RelicsTags;
import hojosa.relics.integration.curios.CuriosIntegration;
import hojosa.relics.lib.References;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.ModList;

public class RelicsItemTags extends ItemTagsProvider {

	public RelicsItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider pBlockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(packOutput, lookupProvider, pBlockTagsProvider.contentsGetter(), References.MODID, existingFileHelper);
	}

	@Override 
	protected void addTags(HolderLookup.Provider provider) {
		if(ModList.get().isLoaded("curios")){
			CuriosIntegration.generateItemTags(this::tag);
		}
		tag(ItemTags.SWORDS)
		.add(RelicsItems.FIRE_SWORD.get())
		.add(RelicsItems.MASTER_SWORD.get());
		tag(RelicsTags.Items.SWORD_PEDESTAL_INFUSEABLE)
		.add(Items.NETHER_STAR);
	}

    @Override
    public String getName() {
        return "Relics Item Tags";
    }
}
