package hojosa.relics.integration.curios;

import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;


@SuppressWarnings("removal")
public class CuriosIntegration {
//    public static final TagKey<Item> CHARM_TAG = ItemTags.create(new ResourceLocation(CuriosApi.MODID, SlotTypePreset.CHARM.getIdentifier()));
	
    public static void init() {
		InterModComms.sendTo(CuriosApi.MODID,  SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
	}
	
    //datagen
//	public static void generateItemTags(Function<TagKey<Item>, TagsProvider.TagAppender> builder) {
//		builder.apply(CHARM_TAG)
//		.add(RelicsItems.FIRE_TABLET.getKey())
//		.add(RelicsItems.WATER_TABLET.getKey());
//	}
}
