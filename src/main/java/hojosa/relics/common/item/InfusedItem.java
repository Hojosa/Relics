package hojosa.relics.common.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class InfusedItem extends RelicsItem{
	public InfusedItem(int stackSize, Rarity raity) {
		super(stackSize, raity);
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return true;
	}
}
