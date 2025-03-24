package hojosa.relics.common.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class InfusedItem extends RelicsItem{
	public InfusedItem(int stackSize, Rarity rarity) {
		super(stackSize, rarity);
	}

	public InfusedItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return true;
	}
}
