package hojosa.relics_of_old.common.entity;

import java.util.Optional;

import org.joml.Vector3f;

import hojosa.relics_of_old.common.block.StarBeamTorch;
import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsParticles;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class StarBeamEntity extends Entity {

	@Getter
	private static final EntityDataAccessor<BlockPos> DATA_ID_TARGET_POS = SynchedEntityData.defineId(FallingStarEntity.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<BlockPos> DATA_ID_START_POS = SynchedEntityData.defineId(FallingStarEntity.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<Vector3f> DATA_ID_MOTION = SynchedEntityData.defineId(FallingStarEntity.class, EntityDataSerializers.VECTOR3);
	private Player player;
	private static final double MIN_SPEED = 0.15;
	private static final double MAX_SPEED = 1.0;
	private static final double START_SPEED = 0.3;
	private static final double DRAG = 0.002;
	@Getter
	@Setter
	private double speed = START_SPEED;

	public StarBeamEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public StarBeamEntity(Level level, BlockPos start, BlockPos target, Player player) {
		super(RelicsEntities.STARBEAM.get(), level);
		this.setStartPos(start.immutable());
		this.setTargetPos(target.immutable());

		this.setPos(start.getX() + 0.5, start.getY() + 0.8, start.getZ() + 0.5);
		this.player = player;
		this.calcMotionVec();
		this.player.startRiding(this);
	}

	@Override
	public void load(CompoundTag pCompound) {
		super.load(pCompound);
		this.calcMotionVec();
	}

	@SuppressWarnings("resource")
	@Override
	public void tick() {
		if (!this.level().isClientSide && this.getPassengers().isEmpty()) {
			if (this.player == null)
				this.discard();
			else
				this.player.startRiding(this);
		}
		Vec3 dir = new Vec3(this.getMotion()).normalize();

		if (!this.getPassengers().isEmpty() && this.getFirstPassenger().isShiftKeyDown()) {
			speed += dir.y * -0.04;
//			this.move(MoverType.SELF, new Vec3(this.getMotion()).scale(0.5));
		} else
			speed += dir.y * -0.02;
//			this.move(MoverType.SELF, new Vec3(this.getMotion()));
		if (speed > MAX_SPEED) {
			speed = MAX_SPEED;
		}

		speed -= DRAG;
		if (speed < MIN_SPEED) {
			speed = MIN_SPEED;
		}

		this.setMotion(dir.toVector3f().mul((float) speed));
		this.move(MoverType.SELF, new Vec3(this.getMotion()));

		if (!this.level().isClientSide && this.tickCount % 5 == 0) {
			float speedlevel = (float) ((this.speed - MIN_SPEED) / (MAX_SPEED - MIN_SPEED));
			this.level().playSound(null, this.blockPosition(), RelicsSounds.GRIND.get(), SoundSource.PLAYERS, 0.3f, 0.5f + speedlevel * 1.5f);
		}

		if (this.level().isClientSide) {
			this.level().addParticle(RelicsParticles.SPARKLE_PARTICLES.get(), this.xo, this.yo, this.zo, random.nextGaussian() * 0.1, random.nextGaussian() * 0.1, random.nextGaussian() * 0.1);
			this.level().addParticle(new RelicsParticleOptions(RelicsParticles.RUNE_PARTICLE, 40, 0.15f), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
		}
		if (!this.level().isClientSide && this.position().distanceToSqr(this.entityData.get(DATA_ID_TARGET_POS).getCenter()) < 0.15) {
			BlockPos target = this.entityData.get(DATA_ID_TARGET_POS);
			this.setPos(target.getCenter().x, target.getCenter().y, target.getCenter().z);
			if (!getNextTarget(target)) {
				this.discard();
			}
		}
	}

	@Override
	public boolean shouldRiderSit() {
		return false;
	}

	@Override
	public double getPassengersRidingOffset() {
		return 0.3f;
	}

	private boolean getNextTarget(BlockPos checkPoint) {
		int radius = 6;
		Optional<BlockPos> matchingPos = BlockPos.withinManhattanStream(checkPoint, radius, radius, radius)
				.filter(pos -> level().getBlockState(pos).getBlock() instanceof StarBeamTorch && !pos.equals(checkPoint) && !pos.equals(this.getStartPos())).findFirst();
		if (matchingPos.isEmpty()) {
			return false;
		}

		if (hasClearPath(level(), checkPoint.getCenter(), matchingPos.get().getCenter())) {
			this.setStartPos(checkPoint.immutable());
			this.setTargetPos(matchingPos.get().immutable());
			this.calcMotionVec();
			return true;
		} else
			return false;
	}

	// calc the motion vector to travel from startPos to targetPos
	private void calcMotionVec() {
		Vec3 start = this.getStartPos().getCenter();
		Vec3 target = this.getTargetPos().getCenter();
		this.setMotion(new Vector3f((float) (target.x - start.x), (float) (target.y - start.y), (float) (target.z - start.z)).normalize().mul((float) speed));
	}

	private boolean hasClearPath(Level level, Vec3 start, Vec3 end) {
		ClipContext context = new ClipContext(start, end, ClipContext.Block.COLLIDER, // Check collision boxes
				ClipContext.Fluid.NONE, // Ignore fluids
				null);
		BlockHitResult result = level.clip(context);
		return result.getType() == HitResult.Type.MISS; // True if no obstacles
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_ID_TARGET_POS, BlockPos.ZERO);
		this.entityData.define(DATA_ID_START_POS, BlockPos.ZERO);
		this.entityData.define(DATA_ID_MOTION, new Vector3f(0));
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.setStartPos(BlockPos.of(tag.getLong("StartPos")));
		this.setTargetPos(BlockPos.of(tag.getLong("TargetPos")));
		this.setSpeed(tag.getDouble("Speed"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putLong("StartPos", getStartPos().asLong());
		tag.putLong("TargetPos", getTargetPos().asLong());
		tag.putDouble("Speed", this.getSpeed());
	}

	public BlockPos getStartPos() {
		return this.entityData.get(DATA_ID_START_POS);
	}

	public BlockPos getTargetPos() {
		return this.entityData.get(DATA_ID_TARGET_POS);
	}

	public Vector3f getMotion() {
		return this.entityData.get(DATA_ID_MOTION);
	}

	private void setStartPos(BlockPos pos) {
		this.entityData.set(DATA_ID_START_POS, pos);
	}

	private void setTargetPos(BlockPos pos) {
		this.entityData.set(DATA_ID_TARGET_POS, pos);
	}

	private void setMotion(Vector3f motion) {
		this.entityData.set(DATA_ID_MOTION, motion);
	}
}
