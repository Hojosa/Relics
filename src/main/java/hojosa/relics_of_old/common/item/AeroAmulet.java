package hojosa.relics_of_old.common.item;

import java.util.List;

import hojosa.relics_of_old.client.particle.RelicsParticles;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
import hojosa.relics_of_old.lib.RelicsUtil.ElementType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class AeroAmulet extends RelicsAmulet {

	private int orbitTimer = 0;

	public AeroAmulet(int charges) {
		super(charges, ElementType.WIND);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
		return CuriosApi.createCurioProvider(new ICurio() {

			@Override
			public ItemStack getStack() {
				return stack;
			}

			@Override
			public boolean canEquipFromUse(SlotContext slotContext) {
				return true;
			}

			@Override
			public void curioTick(SlotContext slotContext) {
				if (slotContext.entity().level().isClientSide)
					return;
				if (!hasCharges(stack))
					return;

				if (orbitTimer > 0) {
					ServerLevel serverLevel = (ServerLevel) slotContext.entity().level();
					int ticksElapsed = 60 - orbitTimer;
					double r = 3.0;
					for (int i = 0; i < 3; i++) {
						double azimuth = ticksElapsed * 0.3 + i * Math.PI * 2.0 / 3.0;
						double elevation = Math.sin(ticksElapsed * 0.13 + i * Math.PI * 2.0 / 3.0);
						double y = Math.sin(elevation) * r + slotContext.entity().getY();
						double out = Math.cos(elevation);
						double x = Math.sin(azimuth) * out * r + slotContext.entity().getX();
						double z = Math.cos(azimuth) * out * r + slotContext.entity().getZ();
						serverLevel.sendParticles(new RelicsParticleOptions(RelicsParticles.RUNE_PARTICLE, 30, 0.15f), x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
					}
					orbitTimer--;
				}

				List<AbstractArrow> arrows = slotContext.entity().level().getEntitiesOfClass(AbstractArrow.class, slotContext.entity().getBoundingBox().inflate(2.0),
						arrow -> arrow.getOwner() != slotContext.entity() && arrow.getDeltaMovement().length() > 0.1);

				for (AbstractArrow arrow : arrows) {
					if (!hasCharges(stack))
						break;

					Vec3 motion = arrow.getDeltaMovement();
					arrow.setDeltaMovement(motion.reverse());
					arrow.setOwner(slotContext.entity());
					arrow.setBaseDamage(arrow.getBaseDamage() * 1.5);
					arrow.setCritArrow(true);

					int cost = (int) Math.ceil(arrow.getBaseDamage());
					consumeCharge(stack, cost);

					slotContext.entity().level().playSound(null, arrow.blockPosition(), RelicsSounds.REPEL.get(), SoundSource.PLAYERS, 1.0f, 0.8f + slotContext.entity().getRandom().nextFloat() * 0.4f);
					ServerLevel serverLevel = (ServerLevel) slotContext.entity().level();
					serverLevel.sendParticles(new RelicsParticleOptions(RelicsParticles.MAGIC_SCRAMBLE_PARTICLE, 15, 0.4f), arrow.getX(), arrow.getY(), arrow.getZ(), 0, arrow.getDeltaMovement().x * 0.2,
							arrow.getDeltaMovement().y * 0.2, arrow.getDeltaMovement().z * 0.2, 1.0);
					orbitTimer = 60;
				}
			}
		});
	}
}