package hojosa.relics_of_old.common.entity;

import java.util.List;

import hojosa.relics_of_old.common.block.BombFlower;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class BombArrowEntity extends AbstractArrow {

	private static final double BLAST_RADIUS = 3.5;
	private static final int BLAST_DAMAGE = 5;

	public BombArrowEntity(EntityType<? extends BombArrowEntity> type, Level level) {
		super(type, level);
	}

	public BombArrowEntity(Level level, LivingEntity shooter) {
		super(RelicsEntities.BOMB_ARROW.get(), shooter, level);
	}

	@Override
	public void tick() {
		super.tick();
		// smoke trail while in flight
		if (level().isClientSide && !inGround) {
			level().addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0, 0, 0);
		}
	}
	
	//since a bomb arrow is heavier, we need to adjust the veloctiy
	@Override
	  public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
	      // 40% velocity reduction — heavier projectile (LG1 behavior)
	      super.shootFromRotation(shooter, x, y, z, velocity * 0.6f, inaccuracy);
	  }


	// explode on block hit
	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		detonate();
	}

	// explode on entity hit — deal arrow damage first, then explode
	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		detonate();
	}

	private void detonate() {
		if (!level().isClientSide) {
			// explosion sound
			level().playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 3.0f, 0.7f + (float) Math.random() * 0.2f);

			// damage + knockback (same as BombEntity)
			Entity owner = getOwner();
			List<Entity> entities = level().getEntities(this, getBoundingBox().inflate(4.0));

			for (Entity target : entities) {
				if (target instanceof BombEntity bomb && bomb.isUnblastable())
					continue;

				Vec3 away = new Vec3(target.getX() - getX(), target.getY() - getY(), target.getZ() - getZ());
				double dist = away.length();
				if (dist >= BLAST_RADIUS)
					continue;

				away = away.normalize();
				double ay = Math.max(away.y, 0.6);
				double blastForce = 4.0 / (2.0 + dist);

				if (target instanceof LivingEntity living) {
					living.hurt(damageSources().explosion(this, owner), BLAST_DAMAGE);
				}
				target.setDeltaMovement(away.x * blastForce, ay * blastForce, away.z * blastForce);
				target.hurtMarked = true;
				target.fallDistance = 0.0f;
			}

			// break bombable blocks
			breakBombableBlocks();

			// particles
			ServerLevel serverLevel = (ServerLevel) level();
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
						level().removeBlock(pos, false);
						TntBlock.explode(level(), pos);
					} else if (block == RelicsBlocks.BOMB_FLOWER.get() && state.getValue(BombFlower.STATE) == BombFlower.FlowerState.NORMAL) {
						level().setBlock(pos, state.setValue(BombFlower.STATE, BombFlower.FlowerState.CUT), Block.UPDATE_ALL);
						BombEntity chainBomb = new BombEntity(level(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, BombEntity.SHORT_FUSE_TIME);
						chainBomb.setDeltaMovement(0, 0.1, 0);
						level().addFreshEntity(chainBomb);
					} else if (isBombable(state)) {
						level().destroyBlock(pos, true);
					}
				}
			}
		}
	}

	private boolean isBombable(BlockState state) {
		return state.is(RelicsTags.Blocks.BOMBABLE);
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(RelicsItems.BOMB_ARROW.get());
	}
}