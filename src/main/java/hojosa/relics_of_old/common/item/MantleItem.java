package hojosa.relics_of_old.common.item;

import javax.annotation.Nullable;

import hojosa.relics_of_old.lib.item.RelicsItem;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class MantleItem extends RelicsItem implements ICurioItem {
	@Getter
	private final float glideRatio;

	public MantleItem(float glideRatio, Rarity rarity) {
		super(rarity, 0);
		this.glideRatio = glideRatio;
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return glideRatio > 4.0f;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
		return CuriosApi.createCurioProvider(new ICurio() {
			@Override
			public ItemStack getStack() {
				return stack;
			}

			@Override
			public boolean canEquipFromUse(SlotContext slotContext) {
				return true;
			}
		});
	}

	@SuppressWarnings({ "deprecation", "removal" })
	public boolean isEquipped(@Nullable LivingEntity entity) {
		return entity != null && CuriosApi.getCuriosHelper().findFirstCurio(entity, this).isPresent();
	}

	@SuppressWarnings({ "deprecation", "removal" })
	@Nullable
	public ItemStack getEquippedStack(@Nullable LivingEntity entity) {
		if (entity == null)
			return null;
		return CuriosApi.getCuriosHelper().findFirstCurio(entity, this).map(slotResult -> slotResult.stack()).orElse(null);
	}
}