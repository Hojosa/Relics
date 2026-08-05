package hojosa.relics_of_old.common.entity;

import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class CaptureEggEntity extends ThrowableItemProjectile {

	public CaptureEggEntity(EntityType<? extends CaptureEggEntity> type, Level level) {
        super(type, level);
    }

    public CaptureEggEntity(Level level, LivingEntity thrower) {
        super(RelicsEntities.CAPTURE_EGG.get(), thrower, level);
    }

    @Override
    protected Item getDefaultItem() {
        return RelicsItems.CAPTURE_EGG.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!(result.getEntity() instanceof Mob target)) return;

        // if the hit killed the mob and a spawn egg exists, drop it
        if (target.getHealth() <= 3) {
            SpawnEggItem spawnEgg = ForgeSpawnEggItem.fromEntityType(target.getType());
            if (spawnEgg != null) {
                target.spawnAtLocation(new ItemStack(spawnEgg));
            }
            target.discard();
        } else {
            target.hurt(damageSources().thrown(this, getOwner()), 3);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (level().isClientSide) {
            for (int i = 0; i < 8; i++) {
                level().addParticle(
                        new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.EGG)),
                        getX(), getY(), getZ(), 0.0, 0.0, 0.0);
            }
        }
        if (!level().isClientSide) {
            discard();
        }
    }
}