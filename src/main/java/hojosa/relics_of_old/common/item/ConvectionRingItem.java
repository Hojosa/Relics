package hojosa.relics_of_old.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import top.theillusivec4.curios.api.SlotContext;

public class ConvectionRingItem extends MagicRingItem {
	public ConvectionRingItem() {
		super("Float above lava");
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		if (!(slotContext.entity() instanceof Player player))
			return;

		// check lava below player (expanded box offset downward, matching LG2)
		AABB checkBox = player.getBoundingBox().inflate(0.25, 0.5, 0.25).move(0.0, -2.5, 0.0);
		if (player.level().containsAnyLiquid(checkBox) && player.isInLava()) {
			player.setDeltaMovement(player.getDeltaMovement().multiply(1.0, 0.9, 1.0).add(0.0, 0.09, 0.0));
			player.fallDistance = 0.0f;
		}
	}
}