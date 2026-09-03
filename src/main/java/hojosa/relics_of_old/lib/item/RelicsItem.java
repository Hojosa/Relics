package hojosa.relics_of_old.lib.item;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class RelicsItem extends Item {
	private boolean hasGlint = false;
	private String customHoverText = "";

	public RelicsItem(int stackSize, Rarity raity) {
		super(getInitProperties(stackSize).rarity(raity));
	}

	public RelicsItem(int stackSize) {
		super(getInitProperties(stackSize));
	}
	
	public RelicsItem(int stackSize, boolean hasGlint) {
		super(getInitProperties(stackSize));
		this.hasGlint = hasGlint;
	}
	
	public RelicsItem(int stackSize, String customHoverText) {
		super(getInitProperties(stackSize));
		this.customHoverText = customHoverText;
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

	@Override
	public boolean isFoil(ItemStack pStack) {
		return hasGlint;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		if (!customHoverText.isEmpty()) {
			tooltip.add(Component.literal(this.customHoverText));
		}
	}
}