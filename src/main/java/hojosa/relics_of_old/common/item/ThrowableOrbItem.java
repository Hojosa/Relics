package hojosa.relics_of_old.common.item;

import java.util.List;

import hojosa.relics_of_old.common.entity.ThrownOrbEntity;
import hojosa.relics_of_old.common.entity.ThrownOrbEntity.OrbType;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ThrowableOrbItem extends RelicsItem {

	private final OrbType orbType;

	public ThrowableOrbItem(OrbType orbType, String customHoverText) {
		super(16, customHoverText);
		this.orbType = orbType;
	}
	
	public ThrowableOrbItem(OrbType orbType) {
        super(16);
        this.orbType = orbType;
    }

	@Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal("Throwable"));
    }

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));

		if (!level.isClientSide) {
			ThrownOrbEntity orb = new ThrownOrbEntity(level, player);
			orb.setItem(stack);
			orb.setOrbType(orbType);
			orb.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.5f, 1.0f);
			level.addFreshEntity(orb);
		}

		if (!player.getAbilities().instabuild) {
			stack.shrink(1);
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
}