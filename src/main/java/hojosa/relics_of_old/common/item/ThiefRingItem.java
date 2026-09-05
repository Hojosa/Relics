package hojosa.relics_of_old.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class ThiefRingItem extends MagicRingItem {
	public ThiefRingItem() {
		super("Invisible while sneaking");
	}
	
	@Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;
        if (player.isShiftKeyDown()) {
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 4, 0, true, false));
        }
    }
}