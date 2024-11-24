package hojosa.relics.common.init;

import hojosa.relics.lib.RelicsUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface RelicsTags {
	interface Blocks {
		TagKey<Block> SWORD_PEDESTAL_INFUSEABLE = tag("sword_pedestal_infuseable");;
		
		static TagKey<Block> tag(String name){
			return TagKey.create(BuiltInRegistries.BLOCK.key(), RelicsUtil.modLoc(name));
		}
	}
	
	interface Items {
        TagKey<Item> SWORD_PEDESTAL_SWORDS = tag("sword_pedestal_swords");
		TagKey<Item> SWORD_PEDESTAL_INFUSEABLE = tag("sword_pedestal_infuseable");
		
		static TagKey<Item> tag(String name){
			return TagKey.create(BuiltInRegistries.ITEM.key(), RelicsUtil.modLoc(name));
		}
	}

}
