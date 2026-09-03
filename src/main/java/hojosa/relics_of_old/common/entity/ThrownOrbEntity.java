package hojosa.relics_of_old.common.entity;

import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity;
import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity.SpellType;
import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownOrbEntity extends ThrowableItemProjectile {

	private static final EntityDataAccessor<Integer> DATA_ORB_TYPE = SynchedEntityData.defineId(ThrownOrbEntity.class, EntityDataSerializers.INT);

	public ThrownOrbEntity(EntityType<? extends ThrownOrbEntity> type, Level level) {
		super(type, level);
	}

	public ThrownOrbEntity(Level level, LivingEntity thrower) {
		super(RelicsEntities.THROWN_ORB.get(), thrower, level);
	}

	@Override
	protected Item getDefaultItem() {
		return RelicsItems.STARGLASS_SHELL.get();
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_ORB_TYPE, 0);
	}

	public void setOrbType(OrbType type) {
		entityData.set(DATA_ORB_TYPE, type.ordinal());
	}

	public OrbType getOrbType() {
		int index = entityData.get(DATA_ORB_TYPE);
		OrbType[] values = OrbType.values();
		return index >= 0 && index < values.length ? values[index] : OrbType.BLAST;
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);

		if (!level().isClientSide) {
			OrbType type = getOrbType();
			Vec3 hitPos = result.getLocation();

			if (type == OrbType.WATER || type == OrbType.LAVA) {
				// Place fluid directly
				BlockState fluid = type == OrbType.WATER ? Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, 1) : Blocks.LAVA.defaultBlockState().setValue(LiquidBlock.LEVEL, 1);
				double r = type.radius + 0.25;
				BlockPos center = BlockPos.containing(hitPos);

				BlockPos.betweenClosedStream(center.offset((int) -r, (int) -r, (int) -r), center.offset((int) r, (int) r, (int) r)).forEach(pos -> {
					if (pos.distToCenterSqr(hitPos) <= r * r && level().isEmptyBlock(pos)) {
						level().setBlockAndUpdate(pos, fluid);
					}
				});
			} else {
				Player thrower = null;
				if (getOwner() instanceof Player player) {
					thrower = player;
				}
				SpellEffectEntity spell = new SpellEffectEntity(level(), type.spellType, thrower, hitPos, type.radius, type.power, type.critical);
				level().addFreshEntity(spell);
			}
			level().playSound(null, getX(), getY(), getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0f, 1.0f);
			discard();
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("OrbType", entityData.get(DATA_ORB_TYPE));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		entityData.set(DATA_ORB_TYPE, tag.getInt("OrbType"));
	}

	// Maps orb variants to their spell effect parameters
	public enum OrbType {
		WATER(null, 1.5, 0.0, false), LAVA(null, 1.0, 0.0, false), BLAST(SpellType.ORB_EXPLOSION, 1.5, 6.0, true), TWINKLE(SpellType.TWINKLE, 6.0, 8.0, true), FIRE(SpellType.FIRE, 6.0, 10.0, true),
		ICE(SpellType.ICE, 6.0, 10.0, true), ZAP(SpellType.LIGHTNING, 6.0, 10.0, true);

		public final SpellType spellType;
		public final double radius;
		public final double power;
		public final boolean critical;

		OrbType(SpellType spellType, double radius, double power, boolean critical) {
			this.spellType = spellType;
			this.radius = radius;
			this.power = power;
			this.critical = critical;
		}
	}
}