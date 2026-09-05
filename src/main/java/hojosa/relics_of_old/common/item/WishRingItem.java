package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.player.StarFallChanceProvider;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class WishRingItem extends MagicRingItem {
	public WishRingItem() {
		super("Stars fall a bit more often");
	}

	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
		slotContext.entity().getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).ifPresent(star -> star.setWishRingActive(true));
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
		slotContext.entity().getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).ifPresent(star -> star.setWishRingActive(false));
	}
}