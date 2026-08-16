package hojosa.relics_of_old.common.entity;

import java.util.List;
import java.util.UUID;

import hojosa.relics_of_old.common.block.BombFlower;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsTags;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BombEntity extends Entity {

	public static final int LONG_FUSE_TIME = 80;
	public static final int SHORT_FUSE_TIME = 10;
	public static final double BLAST_RADIUS = 3.5;
	public static final int BLAST_DAMAGE = 5;
	public static final double SLOW_PULSE = 0.1;
	public static final double FAST_PULSE = 1.0;

	private static final EntityDataAccessor<Integer> DATA_FUSE = SynchedEntityData.defineId(BombEntity.class, EntityDataSerializers.INT);

	private UUID throwerUUID;
	private double pulse = 0.0;
	private boolean primed = false;
	@Getter
	private boolean unblastable;

	public BombEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	public BombEntity(Level level, LivingEntity thrower, int fuseTime) {
		super(RelicsEntities.BOMB.get(), level);
		this.throwerUUID = thrower.getUUID();
		this.unblastable = fuseTime <= SHORT_FUSE_TIME;
		setFuse(fuseTime);

		// position at thrower's eye, offset like vanilla throwables
		float yawRad = thrower.getYRot() / 180.0f * (float) Math.PI;
		setPos(thrower.getX() - Mth.cos(yawRad) * 0.16f, thrower.getEyeY() - 0.1, thrower.getZ() - Mth.sin(yawRad) * 0.16f);

		// throw velocity (matches LG1: speed 0.5 in look direction)
		Vec3 look = thrower.getLookAngle();
		setDeltaMovement(look.scale(0.5));
	}
	
	public BombEntity(Level level, double posX, double posY, double posZ, int fuseTime) {
		super(RelicsEntities.BOMB.get(), level);
		setFuse(fuseTime);
		setPos(posX, posY, posZ);
	}

	@Override
	public void tick() {
		super.tick();

		// water fizzle
		if (isInWater()) {
			fizzle();
			return;
		}

		// smoke trail
		if (level().isClientSide) {
			level().addParticle(ParticleTypes.SMOKE, getX(), getY() + 0.6, getZ(), 0, 0, 0);
		}

		// gravity + friction (matches LG1 physics)
		Vec3 motion = getDeltaMovement();
		double my = motion.y - 0.04;
		double friction = 0.98;
		if (onGround()) {
			BlockPos below = BlockPos.containing(getX(), getBoundingBox().minY - 1, getZ());
			BlockState belowState = level().getBlockState(below);
			friction = belowState.getBlock().getFriction() * 0.98;
		}
		setDeltaMovement(motion.x * friction, my * 0.98, motion.z * friction);
		move(MoverType.SELF, getDeltaMovement());

		// fuse ignition sound (first tick only)
		if (!primed) {
			primed = true;
			if (!level().isClientSide) {
				level().playSound(null, blockPosition(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0f, 1.0f);
			}
		}

		// fuse countdown + pulse
		int fuse = getFuse();
		fuse--;
		setFuse(fuse);
		pulse += fuse < 10 ? FAST_PULSE : SLOW_PULSE;

		// detonate on fuse expiry or fire
		if (fuse <= 0 || isOnFire()) {
			detonate();
		}
	}

	private void fizzle() {
		if (!level().isClientSide) {
			ItemEntity drop = new ItemEntity(level(), getX(), getY(), getZ(), new ItemStack(RelicsItems.BOMB.get()));
			level().addFreshEntity(drop);
			level().playSound(null, blockPosition(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0f, 1.7f);
			// fizzle smoke
			((ServerLevel) level()).sendParticles(ParticleTypes.SMOKE, getX(), getY() + 0.5, getZ(), 3, 0.1, 0.1, 0.1, 0);
		}
		discard();
	}

	private void detonate() {
		if (!level().isClientSide) {
			// explosion sound
			level().playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 3.0f, 0.7f + (float) Math.random() * 0.2f);

			// damage + knockback
			Entity thrower = getThrower();
			List<Entity> entities = level().getEntities(this, getBoundingBox().inflate(4.0));

			for (Entity target : entities) {
				// skip unblastable bombs (chain reaction protection)
				if (target instanceof BombEntity bomb && bomb.unblastable)
					continue;

				Vec3 away = new Vec3(target.getX() - getX(), target.getY() - getY(), target.getZ() - getZ());
				double dist = away.length();
				if (dist >= BLAST_RADIUS)
					continue;

				away = away.normalize();
				// minimum upward knockback component
				double ay = Math.max(away.y, 0.6);
				double blastForce = 4.0 / (2.0 + dist);

				if (target instanceof LivingEntity living) {
					living.hurt(damageSources().explosion(this, thrower), BLAST_DAMAGE);
				}
				target.setDeltaMovement(away.x * blastForce, ay * blastForce, away.z * blastForce);
				target.hurtMarked = true;
				target.fallDistance = 0.0f;
			}

			// break bombable blocks in 1-block radius
			breakBombableBlocks();
			ServerLevel serverLevel = (ServerLevel) level();
			// explosion particles (sent to all clients via network)
			serverLevel.sendParticles(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 8, 1.0, 1.0, 1.0, 0);
			serverLevel.sendParticles(ParticleTypes.LAVA, getX(), getY() + 0.25, getZ(), 8, 0.5, 0.5, 0.5, 1.0);
			serverLevel.sendParticles(ParticleTypes.POOF, getX(), getY(), getZ(), 8, 1.0, 1.0, 1.0, 0);
		}
		discard();
	}

	private void breakBombableBlocks() {
		int r = 1;
		BlockPos center = blockPosition();
		for (int x = -r; x <= r; x++) {
			for (int y = -r; y <= r; y++) {
				for (int z = -r; z <= r; z++) {
					BlockPos pos = center.offset(x, y, z);
					BlockState state = level().getBlockState(pos);
					Block block = state.getBlock();

					if (block == Blocks.TNT) {
						// ignite TNT like LG1
						level().removeBlock(pos, false);
						TntBlock.explode(level(), pos);
					} else if (isBombable(state)) {
						level().destroyBlock(pos, true);
					}
					if (block == RelicsBlocks.BOMB_FLOWER.get() && state.getValue(BombFlower.STATE) == BombFlower.FlowerState.NORMAL) {
					      level().setBlock(pos, state.setValue(BombFlower.STATE, BombFlower.FlowerState.CUT), 3);
					      // chain reaction — short fuse bomb
					      BombEntity chainBomb = new BombEntity(level(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, BombEntity.SHORT_FUSE_TIME);
					      chainBomb.setDeltaMovement(0, 0.1, 0);
					      level().addFreshEntity(chainBomb);
					  }
				}
			}
		}
	}

	private boolean isBombable(BlockState state) {
		return state.is(RelicsTags.Blocks.BOMBABLE);
	}

	private Entity getThrower() {
		if (throwerUUID != null && level() instanceof ServerLevel serverLevel) {
			return serverLevel.getEntity(throwerUUID);
		}
		return null;
	}

	// used by renderer for flash effect
	public int getPulsePhase() {
		return (int) pulse % 2;
	}

	public int getFuse() {
		return entityData.get(DATA_FUSE);
	}

	private void setFuse(int value) {
		entityData.set(DATA_FUSE, value);
	}

	@Override
	public boolean canBeCollidedWith() {
		return !isRemoved() && onGround();
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(DATA_FUSE, LONG_FUSE_TIME);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		setFuse(tag.getInt("Fuse"));
		unblastable = tag.getBoolean("Unblastable");
		if (tag.hasUUID("Thrower"))
			throwerUUID = tag.getUUID("Thrower");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putInt("Fuse", getFuse());
		tag.putBoolean("Unblastable", unblastable);
		if (throwerUUID != null)
			tag.putUUID("Thrower", throwerUUID);
	}
}