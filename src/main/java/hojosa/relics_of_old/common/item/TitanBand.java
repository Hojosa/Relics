package hojosa.relics_of_old.common.item;

import javax.annotation.Nullable;

import hojosa.relics_of_old.client.particle.RelicsParticles;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class TitanBand extends RelicsItem implements ICurioItem {

	public TitanBand() {
		super(Rarity.UNCOMMON, 50);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
		return true;
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		if (!(slotContext.entity() instanceof Player player))
			return;

		Entity passenger = player.getFirstPassenger();

		if (passenger != null) {
//			passenger.setPos(passenger.getX(), passenger.getY() + 0.5, passenger.getZ());
			// throw on sneak
			boolean isSneaking = player.isShiftKeyDown();
			if (isSneaking) {
				Vec3 look = player.getLookAngle();
				passenger.stopRiding();
				passenger.setDeltaMovement(look.x * 0.8, look.y * 0.8 + 0.2, look.z * 0.8);
				passenger.hurtMarked = true;

				if (!player.level().isClientSide) {
					if (passenger instanceof Mob mob) {
						mob.playAmbientSound();
					}
					player.level().playSound(null, player.blockPosition(), RelicsSounds.THROW.get(), SoundSource.PLAYERS, 0.3f, 1.0f);
					stack.getOrCreateTag().putBoolean("TitanLift", false);
				}
			}
			// chicken glide easter egg
			if (passenger instanceof Chicken && !player.onGround() && player.getDeltaMovement().y < 0) {
				player.setDeltaMovement(player.getDeltaMovement().multiply(1.0, 0.6, 1.0));
				player.fallDistance = 0.0f;
			}
			// rune particles around carried entity
			if (!player.level().isClientSide) {
				ServerLevel serverLevel = (ServerLevel) player.level();
				double ridingOffset = passenger.getMyRidingOffset();
				double particleY = passenger.getY() - Math.min(ridingOffset, 0.0) + (ridingOffset < 0 ? 0.1 : 0.0);
				serverLevel.sendParticles(new RelicsParticleOptions(RelicsParticles.RUNE_PARTICLE, 30, 0.15f), passenger.getX(), particleY + 0.5, passenger.getZ(), 1, 0.1, 0.1, 0.1, 0.0);
			}
		} else {
			// passenger gone (died, logged out, etc.) — clear carry state
			if (!player.level().isClientSide && stack.getOrCreateTag().getBoolean("TitanLift")) {
				stack.getOrCreateTag().putBoolean("TitanLift", false);
			}
		}
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
		// ignore curios data-change cycles (durability etc.)
		if (newStack.getItem() instanceof TitanBand)
			return;
		if (slotContext.entity() instanceof Player player && !player.level().isClientSide) {
			CompoundTag data = player.getPersistentData();
			Entity passenger = player.getFirstPassenger();
			if (passenger != null && data.getBoolean("TitanLift")) {
				passenger.stopRiding();
				player.level().playSound(null, player.blockPosition(), RelicsSounds.THROW.get(), SoundSource.PLAYERS, 0.3f, 1.0f);
				stack.getOrCreateTag().putBoolean("TitanLift", false);
			}
		}
	}

	@SuppressWarnings({ "deprecation", "removal" })
	public boolean isEquipped(@Nullable LivingEntity entity) {
		return entity != null && CuriosApi.getCuriosHelper().findFirstCurio(entity, this).isPresent();
	}

	@SuppressWarnings({ "deprecation", "removal" })
	@Nullable
	public ItemStack getEquippedStack(@Nullable LivingEntity entity) {
		if (entity == null)
			return null;
		return CuriosApi.getCuriosHelper().findFirstCurio(entity, this).map(slotResult -> slotResult.stack()).orElse(null);
	}
}