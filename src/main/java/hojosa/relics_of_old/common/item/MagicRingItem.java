package hojosa.relics_of_old.common.item;

import javax.annotation.Nullable;

import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class MagicRingItem extends RelicsItem implements ICurioItem {

	private final float manaCost;

	public MagicRingItem(String description) {
		this(description, 0.0f);
	}

	public MagicRingItem(String description, float manaCost) {
		super(1, description);
		this.manaCost = manaCost;
	}

	public float getManaCost() {
		return manaCost;
	}

	@Override
	public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
		return true;
	}

	public boolean isEquipped(@Nullable LivingEntity entity) {
		if (entity == null)
			return false;
		return CuriosApi.getCuriosInventory(entity).map(handler -> handler.isEquipped(this)).orElse(false);
	}

	@Nullable
	public ItemStack getEquippedStack(@Nullable LivingEntity entity) {
		if (entity == null)
			return null;
		return CuriosApi.getCuriosInventory(entity).map(handler -> handler.findFirstCurio(this).map(slotResult -> slotResult.stack()).orElse(null)).orElse(null);
	}

	// Add to MagicRingItem.java
	protected boolean hasResonance(Player player) {
		return RelicsItems.RESONANCE_RING.get().isEquipped(player);
	}

}