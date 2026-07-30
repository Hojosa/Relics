package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class EmptyMedallion extends RelicsItem {
	
	public enum MedallionType implements StringRepresentable {
		FIRE("fire"), EARTH("earth"), WIND("wind"), ENDER("ender"), ICE("ice"), WATER("water"), LIGHTNING("lightning"), MAGIC("magic"), GRAVITY("gravity"), FOREST("forest");

		private final String name;

		MedallionType(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
	
	protected MedallionType type;
	
	public EmptyMedallion(MedallionType medallionType) {
		super(Rarity.RARE, 50);
		this.type = medallionType;
	}
	
	public EmptyMedallion(MedallionType medallionType, int charges) {
		super(Rarity.RARE, charges);
		this.type = medallionType;
	}

	@Override
	public boolean isRepairable(ItemStack stack) {
		return false;
	}
	
	public void charge(ItemStack item, int chargeAmount, Player player) {
		item.setDamageValue(item.getDamageValue() + chargeAmount);
		if(item.getDamageValue() == 50) {
			
		}
	}
	
	@Override
	public ItemStack getDefaultInstance() {
		var item = new ItemStack(this);
		item.setDamageValue(0);
		return item;
	}

}
