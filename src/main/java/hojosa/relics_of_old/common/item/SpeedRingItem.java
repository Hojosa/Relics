package hojosa.relics_of_old.common.item;

import java.util.UUID;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class SpeedRingItem extends MagicRingItem {

	public SpeedRingItem() {
		super("Sprint faster");
	}

	private static final UUID SPEED_BOOST_UUID = UUID.fromString("9e93209c-4f07-4e26-a150-659bfc44feb1");
    private static final AttributeModifier SPEED_BOOST = new AttributeModifier(SPEED_BOOST_UUID,
            "Sprinting ring speed boost", 0.5, AttributeModifier.Operation.MULTIPLY_BASE);

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;

        AttributeInstance attr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr == null) return;

        // remove every tick, re-apply only if sprinting
        if (attr.getModifier(SPEED_BOOST_UUID) != null) {
            attr.removeModifier(SPEED_BOOST_UUID);
        }
        if (player.isSprinting()) {
            attr.addTransientModifier(SPEED_BOOST);
            player.setOnGround(true); // LG2 behavior: prevents sprint-jump from breaking
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (newStack.getItem() instanceof SpeedRingItem) return;
        AttributeInstance attr = slotContext.entity().getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr != null && attr.getModifier(SPEED_BOOST_UUID) != null) {
            attr.removeModifier(SPEED_BOOST_UUID);
        }
    }
}