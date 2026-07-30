package hojosa.relics_of_old.lib.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class RelicsItem extends Item {
	public RelicsItem(int stackSize, Rarity raity) {
		super(getInitProperties(stackSize).rarity(raity));
	}

	public RelicsItem(int stackSize) {
		super(getInitProperties(stackSize));
	}
	
	public RelicsItem() {
		super(getInitProperties(64));
	}

	public RelicsItem(Properties properties) {
		super(properties);
	}

	public RelicsItem(Rarity rarity, int durability) {
		super(getInitProperties().rarity(rarity).durability(durability));
	}

	private static Properties getInitProperties(int maxStackSize) {
		Properties properties = new Properties();
		properties.stacksTo(maxStackSize);
		return properties;
	}
	
	private static Properties getInitProperties() {
		Properties properties = new Properties();
		return properties;
	}
}