package hojosa.relics.common.datagen.providers;


import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.common.init.RelicsTags;
import hojosa.relics.lib.References;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RelicsItemTags extends ItemTagsProvider {

	public RelicsItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider pBlockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(packOutput, lookupProvider, pBlockTagsProvider.contentsGetter(), References.MOD_ID, existingFileHelper);
	}

	@Override 
	protected void addTags(HolderLookup.Provider provider) {
		tag(RelicsTags.Items.CHARM_TAG)
		.add(RelicsItems.FIRE_TABLET.get())
		.add(RelicsItems.WATER_TABLET.get());
		tag(ItemTags.SWORDS)
		.add(RelicsItems.FIRE_SWORD.get())
		.add(RelicsItems.MASTER_SWORD.get());
		tag(RelicsTags.Items.SWORD_PEDESTAL_INFUSEABLE)
		.add(Items.NETHER_STAR);
		tag(RelicsTags.Items.SWORD_PEDESTAL_GLOW)
		.add(Items.GLOWSTONE)
		.add(Items.GLOW_INK_SAC)
		.add(Items.GLOW_BERRIES);
		copy(RelicsTags.Blocks.SWORD_PEDESTAL_VARIANTS, RelicsTags.Items.SWORD_PEDESTAL_VARIANTS);
		tag(RelicsTags.Items.CLEANER)
		.add(Items.SPONGE)
		.add(TagEntry.optionalElement(new ResourceLocation("supplementaries", "soap")));
	}

    @Override
    public String getName() {
        return "Relics Item Tags";
    }
}
