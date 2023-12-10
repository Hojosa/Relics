package hojosa.relics.common.item;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;


public class FireTablet extends RelicsItem implements ICurioItem
{
	public FireTablet(int stackSize, Rarity raity)
	{
		super(stackSize, raity);
	}
	
	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		slotContext.entity().addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20));
	}
	
	@Override
	public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
		return true;
	}
	
	public boolean isEquipped(@Nullable LivingEntity entity) {
		return entity != null && CuriosApi.getCuriosHelper().findFirstCurio(entity, this).isPresent();
	}
}

