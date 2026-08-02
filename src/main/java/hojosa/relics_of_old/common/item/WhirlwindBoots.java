package hojosa.relics_of_old.common.item;

import java.util.UUID;

import hojosa.relics_of_old.common.init.RelicsSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class WhirlwindBoots extends ArmorItem {
	
	private static final UUID SPEED_UUID = UUID.fromString("a3d5739e-77a4-4b67-b3c0-e01f5c8e2b77");
    private static final AttributeModifier SPEED_MODIFIER = new AttributeModifier(
        SPEED_UUID, "Whirlwind sprint boost", 1.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private int dashTicks = 0;
    
    public WhirlwindBoots(ArmorMaterial material) {
    	super(material, Type.BOOTS, new Properties().rarity(Rarity.UNCOMMON));
	}

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (level.isClientSide) return;

        if (player.isSprinting() && !player.isInWater()) {
            // speed boost
            if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(SPEED_MODIFIER)) {
                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(SPEED_MODIFIER);
            }

            // water walking while sprinting
            BlockPos below = player.blockPosition().below();
            if (!player.onGround()
                && player.level().getBlockState(below).getFluidState().isSource()
                && player.getDeltaMovement().y <= 0
                && player.getDeltaMovement().y >= -0.2) {
                player.setDeltaMovement(player.getDeltaMovement().x, 0.0, player.getDeltaMovement().z);
                player.setOnGround(true);
                player.fallDistance = 0.0f;
            }

            // durability
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.FEET));

            if (player.onGround()) {
                // particles
                ((ServerLevel) level).sendParticles(ParticleTypes.POOF,
                    player.getX(), player.getY(), player.getZ(),
                    1, 0.0, 0.0, 0.0, 0.0);

                // dash sound every 3 ticks
                if (dashTicks % 3 == 0) {
                    level.playSound(null, player.blockPosition(),
                        RelicsSounds.DASH.get(), SoundSource.PLAYERS,
                        0.2f, 0.9f + player.getRandom().nextFloat() * 0.2f);
                }
            }
            dashTicks++;
        } else {
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_UUID);
            dashTicks = 0;
        }
    }
}