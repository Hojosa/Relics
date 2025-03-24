package hojosa.relics.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class RelicsItem extends Item {
	public RelicsItem(int stackSize, Rarity raity) {
		super(getInitProperties(stackSize).rarity(raity));
	}

	public RelicsItem(int stackSize) {
		super(getInitProperties(stackSize));
	}

	public RelicsItem(Properties properties) {
		super(properties);
	}

	private static Properties getInitProperties(int maxStackSize) {
		Properties properties = new Properties();
		properties.stacksTo(maxStackSize);
		return properties;
	}
}
