package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity;
import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity.SpellType;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.common.player.PlayerMana;
import hojosa.relics_of_old.lib.item.RelicsItem;
import lombok.Getter;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SpellStaffItem extends RelicsItem {

	private static final int CRIT_WINDOW = 5;
	@Getter
	private final SpellType spellType;
	@Getter
	private final double castRange;
	@Getter
	private final double castRadius;
	private final double basePower;
	private final float manaCost;
	private final double baseCastTime;
	@Getter
	private final boolean hitsWater;

	public SpellStaffItem(SpellType spellType, double basePower, double castRange, double castRadius, double castTicks, float manaCost, boolean hitsWater, int durability) {
		super(Rarity.UNCOMMON, durability);
		this.spellType = spellType;
		this.basePower = basePower;
		this.castRange = castRange;
		this.castRadius = castRadius;
		this.baseCastTime = castTicks;
		this.manaCost = manaCost;
		this.hitsWater = hitsWater;
	}

	// cast time scales with mana fatigue — fatigued 2x, exhausted 4x
	public int getCastingTicks(Player player) {
		double time = baseCastTime;
		PlayerMana mana = PlayerMana.get(player);
		if (mana != null) {
			int fatigue = mana.getFatigueLevel(player);
			if (fatigue == PlayerMana.FATIGUED) {
				time *= 2.0;
			} else if (fatigue == PlayerMana.EXHAUSTED) {
				time *= 4.0;
			}
		}
		return (int) (time * 20.0);
	}

	// casting progress for the reticle renderer (0.0 to 1.0)
	public float getCastingProgress(ItemStack stack, Player player, float partialTick) {
		if (!player.isUsingItem())
			return 0.0f;
		return Math.min((player.getTicksUsingItem() + partialTick) / getCastingTicks(player), 1.0f);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(player.getItemInHand(hand));
	}

	// charge sounds at 1/3, 2/3, and full charge
	@Override
	public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
		if (level.isClientSide || !(entity instanceof Player player))
			return;
		int ticks = getUseDuration(stack) - remainingUseDuration;
		int castTime = getCastingTicks(player);
		if (ticks == castTime / 3 || ticks == castTime * 2 / 3) {
			level.playSound(null, entity.blockPosition(), RelicsSounds.HIGH_CHARGE.get(), SoundSource.PLAYERS, 0.3f, 0.5f + 0.25f * ticks / castTime);
		}
		if (ticks == castTime) {
			level.playSound(null, entity.blockPosition(), RelicsSounds.HIGH_CHARGE.get(), SoundSource.PLAYERS, 0.3f, 1.0f);
		}
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		if (level.isClientSide || !(entity instanceof Player player))
			return;

		int ticksUsed = getUseDuration(stack) - timeLeft;
		int totalTicks = getCastingTicks(player);
		if (ticksUsed < totalTicks)
			return;

		// crit if released within CRIT_WINDOW ticks of full charge
		boolean crit = (ticksUsed - totalTicks) <= CRIT_WINDOW;

		// raycast to target
		Vec3 eye = player.getEyePosition(1.0f);
		Vec3 look = player.getLookAngle();
		Vec3 far = eye.add(look.scale(castRange));
		ClipContext.Fluid fluidMode = hitsWater ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE;
		BlockHitResult hitResult = level.clip(new ClipContext(eye, far, ClipContext.Block.OUTLINE, fluidMode, player));

		Vec3 castPos;
		if (hitResult.getType() != HitResult.Type.MISS) {
			// pull back slightly to avoid spawning inside block
			castPos = hitResult.getLocation().add(look.scale(-0.015));
		} else {
			castPos = far;
		}

		double power = crit ? basePower * 1.5 : basePower;

		// spawn spell effect
		SpellEffectEntity spell = new SpellEffectEntity(level, spellType, player, castPos, castRadius, power, crit);
		level.addFreshEntity(spell);

		// durability and mana cost
		if (!player.isCreative()) {
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
			PlayerMana mana = PlayerMana.get(player);
			if (mana != null) {
				mana.expendMana(player, manaCost);
			}
		}
		player.swing(player.getUsedItemHand());
	}
}