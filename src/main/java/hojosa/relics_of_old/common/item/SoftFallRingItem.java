package hojosa.relics_of_old.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class SoftFallRingItem extends MagicRingItem {

	public SoftFallRingItem() {
		super("Gentle landings");
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		if (!(slotContext.entity() instanceof Player player))
			return;
		if (player.fallDistance > 3.0f) {
			player.fallDistance = 3.0f;
		}
	}
}