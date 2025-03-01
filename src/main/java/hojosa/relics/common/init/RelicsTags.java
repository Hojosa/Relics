package hojosa.relics.common.init;

import hojosa.relics.lib.RelicsUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;

@SuppressWarnings({ "deprecation", "removal" })
public interface RelicsTags {
	interface Blocks {
		TagKey<Block> SWORD_PEDESTAL_VARIANTS = tag("sword_pedestal_variants");

		static TagKey<Block> tag(String name) {
			return TagKey.create(BuiltInRegistries.BLOCK.key(), RelicsUtil.modLoc(name));
		}
	}

	interface Items {
		TagKey<Item> SWORD_PEDESTAL_SWORDS = tag("sword_pedestal_swords");
		TagKey<Item> SWORD_PEDESTAL_INFUSEABLE = tag("sword_pedestal_infuseable");
		TagKey<Item> SWORD_PEDESTAL_VARIANTS = tag("sword_pedestal_variants");
		TagKey<Item> SWORD_PEDESTAL_GLOW = tag("sword_pedestal_coloring");
		TagKey<Item> SWORD_PEDESTAL_COLORING = tag("sword_pedestal_coloring");
		TagKey<Item> CLEANER = tag("cleaner");
		TagKey<Item> HEART = tag("heart");
		TagKey<Item> CHARM_TAG = ItemTags.create(new ResourceLocation(CuriosApi.MODID, SlotTypePreset.CHARM.getIdentifier()));


		static TagKey<Item> tag(String name) {
			return TagKey.create(BuiltInRegistries.ITEM.key(), RelicsUtil.modLoc(name));
		}
	}
}
