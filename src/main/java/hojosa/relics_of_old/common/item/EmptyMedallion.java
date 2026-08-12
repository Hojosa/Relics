package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.RelicsUtil.ElementType;
import hojosa.relics_of_old.lib.item.RelicsItem;
import lombok.Getter;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

public class EmptyMedallion extends RelicsItem {
	@Getter
	protected ElementType type;
	@Getter
	private RegistryObject<Medallion> chargedMedallion;

	public EmptyMedallion(ElementType medallionType, RegistryObject<Medallion> chargedMedallion) {
		super(Rarity.RARE, 50);
		this.type = medallionType;
		this.chargedMedallion = chargedMedallion;
	}

	public EmptyMedallion(ElementType medallionType, int charges) {
		super(Rarity.RARE, charges);
		this.type = medallionType;
	}

	@Override
	public boolean isRepairable(ItemStack stack) {
		return false;
	}

	public void charge(ItemStack item, int chargeAmount, Player player, Level level) {
		item.setDamageValue(Math.max(0, item.getDamageValue() - chargeAmount));
		if (item.getDamageValue() == 0) {
			Inventory inv = player.getInventory();
			int slot = inv.findSlotMatchingItem(item);
			inv.setItem(slot, new ItemStack(this.chargedMedallion.get()));
			level.playSound(null, player.blockPosition(), RelicsSounds.FULL_CHARGE.get(), SoundSource.PLAYERS, 0.2f, 1.0f);
		} else
			level.playSound(null, player.blockPosition(), RelicsSounds.PARTIAL_CHARGE.get(), SoundSource.PLAYERS, 0.2f, 1.0f);
	}

	public static ItemStack findEmptyMedallion(Player player, ElementType type) {
		for (ItemStack stack : player.getInventory().items) {
			if (stack.getItem().getClass() == EmptyMedallion.class && ((EmptyMedallion) stack.getItem()).type == type) {
				return stack;
			}
		}
		return null;
	}

	@Override
	public ItemStack getDefaultInstance() {
		var item = new ItemStack(this);
		item.setDamageValue(item.getMaxDamage());
		return item;
	}

	@Override
	public boolean isEnchantable(ItemStack pStack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
}