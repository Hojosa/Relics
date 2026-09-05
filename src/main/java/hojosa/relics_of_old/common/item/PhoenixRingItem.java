package hojosa.relics_of_old.common.item;

import net.minecraft.world.item.ItemStack;

public class PhoenixRingItem extends MagicRingItem {
	public PhoenixRingItem() {
		super("Absorb fire");
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
}