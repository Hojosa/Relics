package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.entity.PingEntity;
import hojosa.relics_of_old.common.mana.IMana;
import hojosa.relics_of_old.common.player.PlayerMana;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SpottingScopeItem extends RelicsItem implements IMana {

	private static final float RANGE = 192.0f;
	private static final int QUICK_RELEASE_TICKS = 5;

	public SpottingScopeItem() {
		super(1);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 60000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.SPYGLASS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(player.getItemInHand(hand));
	}

	// Quick-release: if released within 5 ticks, fire the ping
	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		if (level.isClientSide || !(entity instanceof Player player))
			return;

		int ticksUsed = getUseDuration(stack) - timeLeft;
		if (ticksUsed > QUICK_RELEASE_TICKS)
			return;

		PlayerMana mana = PlayerMana.get(player);
		if (mana == null || mana.getAvailableMana() <= 0.0f)
			return;

		// Raycast from eyes
		Vec3 eye = player.getEyePosition(1.0f);
		Vec3 look = player.getLookAngle().normalize().scale(RANGE);
		Vec3 far = eye.add(look);
		BlockHitResult hit = level.clip(new ClipContext(eye, far, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

		if (hit.getType() != HitResult.Type.MISS) {
			Vec3 hitPos = hit.getLocation();
			PingEntity ping = new PingEntity(level, hitPos.x, hitPos.y, hitPos.z, 500, 0);
			level.addFreshEntity(ping);

			if (!player.isCreative()) {
				mana.expendMana(player, getManaCost());
			}
		}
	}

	@Override
	public float getManaCost() {
		return 10.0f;
	}
}