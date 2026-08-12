package hojosa.relics_of_old.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;

import hojosa.relics_of_old.common.entity.attacks.QuakeEntity;
import hojosa.relics_of_old.common.entity.attacks.WhirlwindEntity;
import hojosa.relics_of_old.common.init.RelicsEnchantments;
import hojosa.relics_of_old.common.init.RelicsParticles;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
import hojosa.relics_of_old.lib.RelicsUtil.ElementType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Mixin(SwordItem.class)
public abstract class SwordItemMixin extends TieredItem {

	private static final int CHARGE_TIME = 30;
	private static final int DISCHARGE_TIME = 15;
	private static final int RECALL_TIME = 50;

	private SwordItemMixin(Tier tier, Properties properties) {
		super(tier, properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!hasAugment(stack))
			return super.use(level, player, hand);

		// ender recall: right-click again to teleport back
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.getBoolean("enderRecall")) {
			long elapsed = level.getGameTime() - tag.getLong("enderRecallAt");
			if (elapsed <= RECALL_TIME) {
				if (!level.isClientSide) {
					player.fallDistance = 0.0f;
					player.teleportTo(tag.getDouble("recallX"), tag.getDouble("recallY"), tag.getDouble("recallZ"));
					level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
					tag.remove("enderRecall");
					tag.remove("enderRecallAt");
					tag.remove("recallX");
					tag.remove("recallY");
					tag.remove("recallZ");
				}
				return InteractionResultHolder.success(stack);
			}
			// recall expired, clean up and fall through to charge
			if (!level.isClientSide) {
				tag.remove("enderRecall");
				tag.remove("enderRecallAt");
				tag.remove("recallX");
				tag.remove("recallY");
				tag.remove("recallZ");
			}
		}

		player.startUsingItem(hand);
		return InteractionResultHolder.consume(stack);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		if (hasAugment(stack))
			return 72000;
		return super.getUseDuration(stack);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		if (stack.hasTag() && stack.getTag().contains("medallion")) {
			return UseAnim.BLOCK;
		}
		return super.getUseAnimation(stack);
	}

	@Override
	public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingTicks) {
		// normally, this check shouldnt be needed, as the sword can only be used when
		// it has an augment.
		// but you never know which other mixins might also do something, so out of
		// saftey, we double check
		if (!hasAugment(stack))
			return;

		int ticksUsed = getUseDuration(stack) - remainingTicks;
		int effectiveCharge = getEffectiveChargeTime(stack);

		if (ticksUsed > 4 && ticksUsed < effectiveCharge && level.isClientSide) {
			InteractionHand hand = entity.getUsedItemHand();
			boolean mainArmRight = entity.getMainArm() == HumanoidArm.RIGHT;
			boolean rightSide = (hand == InteractionHand.MAIN_HAND) == mainArmRight;
			spawnChargeParticles(entity, ticksUsed, rightSide, stack);
		}

		if (ticksUsed == effectiveCharge && !level.isClientSide) {
			int focusLevel = EnchantmentHelper.getTagEnchantmentLevel(RelicsEnchantments.FOCUS.get(), stack);
		      SoundEvent chargeSound = focusLevel > 0 ? RelicsSounds.HIGH_CHARGE.get() : RelicsSounds.SWORD_CHARGE.get();
		      level.playSound(null, entity.blockPosition(), chargeSound, SoundSource.PLAYERS, 0.8f, 1.0f);
		}
		// discharge sparkle while holding after full charge
		if (ticksUsed >= effectiveCharge && level.isClientSide) {
			InteractionHand hand = entity.getUsedItemHand();
			boolean mainArmRight = entity.getMainArm() == HumanoidArm.RIGHT;
			boolean rightSide = (hand == InteractionHand.MAIN_HAND) == mainArmRight;

			float yawRad = entity.getYRot() * Mth.DEG_TO_RAD;
			float pitchRad = entity.getXRot() * Mth.DEG_TO_RAD;
			double sideSign = rightSide ? 1.0 : -1.0;
			double sideX = -Mth.cos(yawRad) * sideSign;
			double sideZ = -Mth.sin(yawRad) * sideSign;

			double x = entity.getX() - Mth.sin(yawRad) * 0.5 * Mth.cos(pitchRad) + sideX * 0.6 + entity.getRandom().nextGaussian() * 0.05;
			double y = entity.getEyeY() - Mth.sin(pitchRad) * 0.5 + 0.1 + entity.getRandom().nextGaussian() * 0.05;
			double z = entity.getZ() + Mth.cos(yawRad) * 0.5 * Mth.cos(pitchRad) + sideZ * 0.6 + entity.getRandom().nextGaussian() * 0.05;

			level.addParticle(new RelicsParticleOptions(RelicsParticles.SPARKLE_PARTICLES, 15, 0.03f), x, y, z, 0.0, 0.0, 0.0);
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		if (!level.isClientSide || !hasAugment(stack) || !(entity instanceof Player player))
			return;

		CompoundTag tag = stack.getTag();
		if (tag == null)
			return;

		// ender recall portal overlay
		if (tag.getBoolean("enderRecall")) {
			long elapsed = level.getGameTime() - tag.getLong("enderRecallAt");
			if (elapsed <= RECALL_TIME) {
				float intensity = (float) (RECALL_TIME - elapsed) / RECALL_TIME * 0.25f;
				((LocalPlayer) player).spinningEffectIntensity = Math.max(intensity, ((LocalPlayer) player).spinningEffectIntensity);
			}
		}

		// discharge sparkle after release - ability ready indicator
		if (isSelected && tag.contains("chargedAt")) {
			long dischargeElapsed = level.getGameTime() - tag.getLong("chargedAt");
			if (dischargeElapsed <= DISCHARGE_TIME) {
				InteractionHand hand = player.getUsedItemHand();
				boolean mainArmRight = player.getMainArm() == HumanoidArm.RIGHT;
				boolean rightSide = (hand == InteractionHand.MAIN_HAND) == mainArmRight;

				float yawRad = player.getYRot() * Mth.DEG_TO_RAD;
				float pitchRad = player.getXRot() * Mth.DEG_TO_RAD;
				double sideSign = rightSide ? 1.0 : -1.0;
				double sideX = -Mth.cos(yawRad) * sideSign;
				double sideZ = -Mth.sin(yawRad) * sideSign;

				double x = player.getX() - Mth.sin(yawRad) * 0.5 * Mth.cos(pitchRad) + sideX * 0.6 + player.getRandom().nextGaussian() * 0.05;
				double y = player.getEyeY() - Mth.sin(pitchRad) * 0.5 + 0.1 + player.getRandom().nextGaussian() * 0.05;
				double z = player.getZ() + Mth.cos(yawRad) * 0.5 * Mth.cos(pitchRad) + sideZ * 0.6 + player.getRandom().nextGaussian() * 0.05;

				level.addParticle(new RelicsParticleOptions(RelicsParticles.SPARKLE_PARTICLES, 6, 0.03f), x, y, z, 0.0, 0.0, 0.0);
			}
		}
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remainingTicks) {
		if (!hasAugment(stack) || level.isClientSide)
			return;

		int ticksUsed = getUseDuration(stack) - remainingTicks;
		// only mark as charged, ability fires on next left-click via onEntitySwing
		if (ticksUsed >= getEffectiveChargeTime(stack)) {
			stack.getOrCreateTag().putLong("chargedAt", level.getGameTime());
		}
	}

	// fire ability on left-click within discharge window
	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
		if (!hasAugment(stack) || !(entity instanceof Player player))
			return false;

		CompoundTag tag = stack.getTag();
		if (tag == null || !tag.contains("chargedAt"))
			return false;

		long elapsed = player.level().getGameTime() - tag.getLong("chargedAt");
		tag.remove("chargedAt");

		if (elapsed > DISCHARGE_TIME)
			return false;

		if (!player.level().isClientSide) {
			doSpecialAbility(player, stack);
		} else {
			spawnAbilityParticles(player, stack);
		}
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		if (hasAugment(stack)) {
			String element = stack.getTag().getString("medallion");
			tooltip.add(Component.translatable("augment." + References.MOD_ID + "." + element).withStyle(ChatFormatting.GOLD));
		}
	}

	private static boolean hasAugment(ItemStack stack) {
		return stack.hasTag() && stack.getTag().contains("medallion");
	}

	private static int getEffectiveChargeTime(ItemStack stack) {
		int focusLevel = EnchantmentHelper.getTagEnchantmentLevel(RelicsEnchantments.FOCUS.get(), stack);
		return (int) (CHARGE_TIME / (1.0f + focusLevel * 0.5f));
	}

	private static void spawnChargeParticles(LivingEntity entity, int ticksUsed, boolean rightSide, ItemStack stack) {
		float yawRad = entity.getYRot() * Mth.DEG_TO_RAD;
		float pitchRad = entity.getXRot() * Mth.DEG_TO_RAD;

		// look direction (pitch-aware, follows crosshair)
		double lookX = -Mth.sin(yawRad) * Mth.cos(pitchRad);
		double lookY = -Mth.sin(pitchRad);
		double lookZ = Mth.cos(yawRad) * Mth.cos(pitchRad);

		// side vector for hand offset
		double sideSign = rightSide ? 1.0 : -1.0;
		double sideX = -Mth.cos(yawRad) * sideSign;
		double sideZ = -Mth.sin(yawRad) * sideSign;

		// sword tip: from eye, forward along look + sideways to hand
		double x = entity.getX() + lookX * 0.5 + sideX * 0.6;
		double y = entity.getEyeY() + lookY * 0.5 + 0.1;
		double z = entity.getZ() + lookZ * 0.5 + sideZ * 0.6;

		// converging spiral
		double r = (float) (getEffectiveChargeTime(stack) - ticksUsed) / getEffectiveChargeTime(stack);
		double theta = r * Math.PI * 3.0;
		double h = Math.cos(theta) * r;
		double offy = Math.sin(theta) * r;
		double sparkSpan = 0.3;

		entity.level().addParticle(new RelicsParticleOptions(RelicsParticles.SPARKLE_PARTICLES, 6, 0.03f), x + sideX * h * sparkSpan, y + offy * sparkSpan, z + sideZ * h * sparkSpan, 0.0, 0.0, 0.0);
		entity.level().addParticle(new RelicsParticleOptions(RelicsParticles.SPARKLE_PARTICLES, 6, 0.03f), x - sideX * h * sparkSpan, y - offy * sparkSpan, z - sideZ * h * sparkSpan, 0.0, 0.0, 0.0);
	}

	private static void spawnAbilityParticles(Player player, ItemStack sword) {
		String medallionName = sword.getTag().getString("medallion");
		Vec3 look = player.getLookAngle();

		// fire: burst of flame particles at cast point
		if ("fire".equalsIgnoreCase(medallionName)) {
			for (int i = 0; i < 10; i++) {
				player.level().addParticle(ParticleTypes.FLAME, player.getX() + look.x, player.getEyeY() + look.y, player.getZ() + look.z, player.getRandom().nextGaussian() * 0.04,
						player.getRandom().nextGaussian() * 0.04, player.getRandom().nextGaussian() * 0.04);
			}
		}

		// ender: beam scan particles along look direction
		if ("ender".equalsIgnoreCase(medallionName)) {
			Vec3 start = player.getEyePosition();
			Vec3 step = player.getLookAngle();
			for (int i = 0; i < 64; i++) {
				Vec3 pos = start.add(step.scale(i));
				player.level().addParticle(ParticleTypes.POOF, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
				// stop at first entity
				List<LivingEntity> hits = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos.subtract(0.5, 0.5, 0.5), pos.add(0.5, 0.5, 0.5)));
				for (LivingEntity hit : hits) {
					if (!hit.equals(player))
						return;
				}
			}
		}
	}

	private void doSpecialAbility(Player player, ItemStack sword) {
		String medallionName = sword.getTag().getString("medallion");
		ElementType type;
		try {
			type = ElementType.valueOf(medallionName.toUpperCase());
		} catch (IllegalArgumentException e) {
			return;
		}

		boolean success = false;
		Vec3 look = player.getLookAngle();
		Level level = player.level();

		switch (type) {
		case FIRE -> {
			SmallFireball fireball = new SmallFireball(level, player.getX() + look.x, player.getEyeY() + look.y, player.getZ() + look.z, look.x * 0.01, look.y * 0.01, look.z * 0.01);
			fireball.setOwner(player);
			level.addFreshEntity(fireball);
			level.playSound(null, player.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f);
			success = true;
		}
		case EARTH -> {
			level.addFreshEntity(new QuakeEntity(level, player.position(), player, 6.0, 5));
			// do we need that?
			level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0f, 1.2f);
			success = true;
		}
		case WIND -> {
			level.addFreshEntity(new WhirlwindEntity(level, player));
			success = true;
		}
		case ENDER -> {
			LivingEntity target = beamScan(player, 64.0);
			if (target != null) {
				CompoundTag tag = sword.getOrCreateTag();
				tag.putDouble("recallX", player.getX());
				tag.putDouble("recallY", player.getY());
				tag.putDouble("recallZ", player.getZ());
				tag.putBoolean("enderRecall", true);
				tag.putLong("enderRecallAt", level.getGameTime());

				double angle = Math.atan2(target.getZ() - player.getZ(), target.getX() - player.getX());
				double destX = target.getX() - Math.cos(angle) * 3.0;
				double destZ = target.getZ() - Math.sin(angle) * 3.0;

				// find safe 2-block air gap at destination
				BlockPos feet = BlockPos.containing(destX, target.getY(), destZ);
				for (int i = 0; i < 5; i++) {
					if (!level.getBlockState(feet).isSuffocating(level, feet) && !level.getBlockState(feet.above()).isSuffocating(level, feet.above())) {
						break;
					}
					feet = feet.above();
				}

				player.fallDistance = 0.0f;
				player.teleportTo(destX, feet.getY(), destZ);
				level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
				success = true;
			} else {
				level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.2f, 0.7f);
			}
		}
		default -> {
		}
		}

		// durability cost: maxDamage / (enchantability * 4), if overflow occures, save
		// it and accumulate until an extra point is reached.
		if (success) {
			int enchantability = Math.max(1, sword.getItem().getEnchantmentValue(sword));
			int uses = enchantability * 4;
			int maxDmg = sword.getMaxDamage();
			int damage = maxDmg / uses;

			CompoundTag tag = sword.getOrCreateTag();
			double overflow = tag.getDouble("damageOverflow") + (double) maxDmg / uses - damage;
			if (overflow >= 1.0) {
				damage++;
				overflow -= 1.0;
			}
			tag.putDouble("damageOverflow", overflow);

			if (damage > 0) {
				sword.hurtAndBreak(damage, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
			}
		}
	}

	// scan along look vector for first living entity
	private static LivingEntity beamScan(Player player, double range) {
		Vec3 start = player.getEyePosition();
		Vec3 step = player.getLookAngle();

		for (int i = 0; i < (int) range; i++) {
			Vec3 pos = start.add(step.scale(i));
			List<LivingEntity> hits = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos.subtract(0.5, 0.5, 0.5), pos.add(0.5, 0.5, 0.5)));
			for (LivingEntity hit : hits) {
				if (!hit.equals(player))
					return hit;
			}
		}
		return null;
	}
}