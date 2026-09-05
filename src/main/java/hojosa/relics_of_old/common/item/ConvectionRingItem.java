package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.player.PlayerMana;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import top.theillusivec4.curios.api.SlotContext;

public class ConvectionRingItem extends MagicRingItem {
	public ConvectionRingItem() {
		super("Float above lava", 0.075f);
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		if (!(slotContext.entity() instanceof Player player))
			return;

		// detect lava up to 2.5 blocks below the player
		AABB checkBox = player.getBoundingBox().inflate(0.25, 0.5, 0.25).move(0.0, -0.5, 0.0);
		PlayerMana mana = PlayerMana.get(player);
		if (containsLava(player.level(), checkBox) && mana != null && mana.getAvailableMana() > 0.0f) {
			player.setDeltaMovement(player.getDeltaMovement().multiply(1.0, 0.9, 1.0).add(0.0, 0.09, 0.0));
			player.fallDistance = 0.0f;
			PlayerMana.spendRingMana(player, getManaCost(), hasResonance(player));
		}
	}

	private static boolean containsLava(Level level, AABB box) {
		for (int x = (int) Math.floor(box.minX); x <= (int) Math.floor(box.maxX); x++) {
			for (int y = (int) Math.floor(box.minY); y <= (int) Math.floor(box.maxY); y++) {
				for (int z = (int) Math.floor(box.minZ); z <= (int) Math.floor(box.maxZ); z++) {
					if (level.getFluidState(new BlockPos(x, y, z)).is(FluidTags.LAVA)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}