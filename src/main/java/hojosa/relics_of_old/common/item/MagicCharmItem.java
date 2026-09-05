package hojosa.relics_of_old.common.item;

import javax.annotation.Nullable;

import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class MagicCharmItem extends RelicsItem implements ICurioItem {

	public MagicCharmItem(String description) {
		super(1, description);
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

	// consume the charm from the curios slot
	public void consumeCharm(LivingEntity entity) {
		CuriosApi.getCuriosInventory(entity).ifPresent(handler -> {
			handler.findFirstCurio(this).ifPresent(slotResult -> {
				slotResult.stack().shrink(1);
			});
		});
	}
}