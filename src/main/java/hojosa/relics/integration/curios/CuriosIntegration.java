package hojosa.relics.integration.curios;

import java.util.function.Function;

import hojosa.relics.common.init.RelicsItems;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;


public class CuriosIntegration {
    public static final TagKey<Item> CHARM_TAG = ItemTags.create(new ResourceLocation(CuriosApi.MODID, SlotTypePreset.CHARM.getIdentifier()));
	
    public static void init() {
		InterModComms.sendTo("curios",  SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
	}
	
    //datagen
	public static void generateItemTags(Function<TagKey<Item>, TagsProvider.TagAppender> builder) {
		builder.apply(CHARM_TAG)
		.add(RelicsItems.FIRE_TABLET.getKey())
		.add(RelicsItems.WATER_TABLET.getKey());
	}
}
