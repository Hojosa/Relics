package hojosa.relics_of_old.common.init;

import hojosa.relics_of_old.lib.RelicsUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsItemProperties {

	public static void setupItemProperties() {
		ItemProperties.register(RelicsItems.MYSTIC_SEED.get(), RelicsUtil.modLoc("thundering"), (stack, level, entity, seed) -> {
			if (level == null)
				level = Minecraft.getInstance().level;
			return level != null && level.isThundering() ? 1.0f : 0.0f;
		});
		
		ItemProperties.register(RelicsItems.MAGIC_MIRROR.get(), RelicsUtil.modLoc("active"), (stack, level, entity, seed) -> {
			return stack.hasTag() && stack.getOrCreateTag().getBoolean("outdoors") ? 0.0f : 1.0f;
		});
		
		ItemProperties.register(RelicsItems.MAGIC_MIRROR.get(), RelicsUtil.modLoc("using"), (stack, level, entity, seed) -> {
		      if (entity == null || !entity.isUsingItem() || entity.getUseItem() != stack)
		          return 0f;
		      int remaining = entity.getUseItemRemainingTicks();
		      return ((remaining % 4) +1) / 4.0f;
		  });
	}
}