package hojosa.relics.common.datagen.providers;


import org.jetbrains.annotations.Nullable;

import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.lib.References;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;

public class RelicsItemTags extends ItemTagsProvider {
	
    public static final TagKey<Item> CHARM_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(CuriosApi.MODID, SlotTypePreset.CHARM.getIdentifier()));

	public RelicsItemTags(DataGenerator pGenerator, BlockTagsProvider pBlockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(pGenerator, pBlockTagsProvider, References.MODID, existingFileHelper);
	}

	@Override 
	protected void addTags() {
		tag(CHARM_TAG)
			.add(RelicsItems.FIRE_PLATE.get());

	}

    @Override
    public String getName() {
        return "Relics Item Tags";
    }
}
