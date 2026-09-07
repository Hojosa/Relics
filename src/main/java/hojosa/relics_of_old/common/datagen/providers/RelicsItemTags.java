package hojosa.relics_of_old.common.datagen.providers;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsTags;
import hojosa.relics_of_old.lib.References;
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
		.add(RelicsItems.WATER_TABLET.get())
		.add(RelicsItems.PYRO_AMULET.get())
		.add(RelicsItems.GEO_AMULET.get())
		.add(RelicsItems.END_AMULET.get())
		.add(RelicsItems.AERO_AMULET.get())
		.add(RelicsItems.PHOENIX_CHARM.get())
		.add(RelicsItems.BLAST_CHARM.get())
		.add(RelicsItems.FEATHER_CHARM.get())
		.add(RelicsItems.AZURE_MANTLE.get())
		.add(RelicsItems.PHOENIX_MANTLE.get());
		tag(RelicsTags.Items.BELT_TAG)
		.add(RelicsItems.TITAN_BAND.get());
		tag(ItemTags.SWORDS)
		.add(RelicsItems.FIRE_SWORD.get())
		.add(RelicsItems.MASTER_SWORD.get())
		.add(RelicsItems.ENDER_SWORD.get())
		.add(RelicsItems.SLIME_SWORD.get());
		tag(RelicsTags.Items.SWORD_PEDESTAL_INFUSEABLE)
		.add(RelicsItems.INFUSED_STAR_STONE.get());
		tag(RelicsTags.Items.SWORD_PEDESTAL_GLOW)
		.add(Items.GLOWSTONE)
		.add(Items.GLOW_INK_SAC)
		.add(Items.GLOW_BERRIES);
		copy(RelicsTags.Blocks.SWORD_PEDESTAL_VARIANTS, RelicsTags.Items.SWORD_PEDESTAL_VARIANTS);
		tag(RelicsTags.Items.CLEANER)
		.add(Items.SPONGE).add(TagEntry.optionalElement(ResourceLocation.fromNamespaceAndPath("supplementaries", "soap")));
		tag(RelicsTags.Items.HEART)
		.add(RelicsItems.HEART.get());
		copy(RelicsTags.Blocks.CLAY_JAR_VARIANTS, RelicsTags.Items.CLAY_JAR_VARIANTS);
		tag(RelicsTags.Items.SUGAR_CUBES)
		.add(RelicsBlocks.SUGAR_CUBE.get().asItem())
		.add(TagEntry.optionalElement(ResourceLocation.fromNamespaceAndPath("supplementaries", "sugar_cube")));
		tag(ItemTags.ARROWS)
		.add(RelicsItems.BOMB_ARROW.get());
		tag(RelicsTags.Items.RING_TAG)
		.add(RelicsItems.SPEED_RING.get())
		.add(RelicsItems.CONVECTION_RING.get())
		.add(RelicsItems.SOFT_FALL_RING.get())
		.add(RelicsItems.COLD_FEET_RING.get())
		.add(RelicsItems.THIEF_RING.get())
		.add(RelicsItems.MAGE_RING.get()).
		add(RelicsItems.WARRIOR_RING.get())
		.add(RelicsItems.FORTUNE_RING.get())
		.add(RelicsItems.ARROWFIND_RING.get())
		.add(RelicsItems.AZUREFIND_RING.get())
		.add(RelicsItems.WISH_RING.get())
		.add(RelicsItems.RESONANCE_RING.get())
		.add(RelicsItems.PHOENIX_RING.get());
	}

	@Override
	public String getName() {
		return "Relics Item Tags";
	}
}
