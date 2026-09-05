package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.player.PlayerMana;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class SoftFallRingItem extends MagicRingItem {

	public SoftFallRingItem() {
		super("Gentle landings", 1.5f);
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		if (!(slotContext.entity() instanceof Player player))
			return;
		PlayerMana mana = PlayerMana.get(player);
		if (player.fallDistance > 3.0f && mana != null && mana.getAvailableMana() > 0.0f) {
			float excess = player.fallDistance - 3.0f;
			PlayerMana.spendRingMana(player, excess * this.getManaCost(), hasResonance(player));
			player.fallDistance = 3.0f;
		}
	}
}