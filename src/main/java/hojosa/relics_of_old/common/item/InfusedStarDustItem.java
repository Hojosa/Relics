package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity;
import hojosa.relics_of_old.lib.item.InfusedItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class InfusedStarDustItem extends InfusedItem {
	public InfusedStarDustItem(int stackSize, Rarity rarity) {
		super(stackSize, rarity);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide) {
			Vec3 eye = player.getEyePosition();
			Vec3 look = player.getLookAngle();
			float range = 3.0f;
			Vec3 target = eye.add(look.scale(range));
			SpellEffectEntity spell = new SpellEffectEntity(level, SpellEffectEntity.SpellType.SPRINKLE_STARDUST, player, target, 2.0, 1.0, false);
			level.addFreshEntity(spell);
		}
		player.swing(hand);
		if (!player.getAbilities().instabuild) {
			stack.shrink(1);
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
}