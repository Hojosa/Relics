package hojosa.relics_of_old.common.item;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity;
import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity.SpellType;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.common.mana.IMana;
import hojosa.relics_of_old.common.player.PlayerMana;
import hojosa.relics_of_old.common.player.PlayerSkyTracker;
import hojosa.relics_of_old.lib.item.RelicsItem;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SpellCastingItem extends RelicsItem implements IMana {

	private static final int CRIT_WINDOW = 5;
	@Getter
	private final SpellType spellType;
	@Getter
	private final double castRange;
	@Getter
	private final double castRadius;
	private final double basePower;
	private final double critBonus;
	@Getter
	private final float manaCost;
	private final double baseCastTime;
	@Getter
	private final boolean hitsWater;
	private final boolean isMelee;
	private final Multimap<Attribute, AttributeModifier> meleeAttributes;

	public SpellCastingItem(SpellType spellType, double basePower, double critBonus, double castRange, double castRadius, double castTicks, float manaCost, boolean hitsWater, int durability, boolean isMelee) {
		super(Rarity.UNCOMMON, durability);
		this.spellType = spellType;
		this.basePower = basePower;
		this.critBonus = critBonus;
		this.castRange = castRange;
		this.castRadius = castRadius;
		this.baseCastTime = castTicks;
		this.manaCost = manaCost;
		this.hitsWater = hitsWater;
		this.isMelee = isMelee;
		if (isMelee) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 3.0, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4, AttributeModifier.Operation.ADDITION));
			this.meleeAttributes = builder.build();
		} else {
			this.meleeAttributes = ImmutableMultimap.of();
		}
	}
	
	@Override
	  public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
	      super.appendHoverText(stack, level, tooltip, flag);
	      tooltip.add(Component.translatable("item.relics_of_old.spell.power", String.format("+%.0f", basePower)).withStyle(ChatFormatting.BLUE));
	      tooltip.add(Component.translatable("item.relics_of_old.spell.range", String.format("+%.0f", castRange)).withStyle(ChatFormatting.BLUE));
	      tooltip.add(Component.translatable("item.relics_of_old.spell.radius", String.format("+%.0f", castRadius)).withStyle(ChatFormatting.BLUE));
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

		double power = crit ? basePower + critBonus : basePower;

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

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (spellType == SpellEffectEntity.SpellType.EXIT && entity instanceof Player player && !level.isClientSide) {
			PlayerSkyTracker tracker = PlayerSkyTracker.get(player);
			if (tracker != null) {
				tracker.updateIfUnderSky(player);
			}
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (isMelee && !attacker.level().isClientSide && attacker instanceof Player player) {
			// Jump-crit: falling, not on ground, not on ladder, not in water, not blind, not riding
			boolean jumpCrit = attacker.fallDistance > 0.0f && !attacker.onGround() && !attacker.onClimbable() && !attacker.isInWater() && !attacker.hasEffect(MobEffects.BLINDNESS) && !attacker.isPassenger();
			if (jumpCrit) {
				stack.hurtAndBreak(1, attacker, e -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
				Vec3 center = target.position().add(0, target.getBbHeight() / 2.0, 0);
				SpellEffectEntity spell = new SpellEffectEntity(player.level(), spellType, player, center, castRadius / 2.0, basePower / 2.0, false);
				player.level().addFreshEntity(spell);
			}
		}
		stack.hurtAndBreak(2, attacker, e -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND && isMelee ? meleeAttributes : super.getDefaultAttributeModifiers(slot);
	}
}