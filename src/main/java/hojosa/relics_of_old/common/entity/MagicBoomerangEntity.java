package hojosa.relics_of_old.common.entity;

import java.util.List;

import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsSounds;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class MagicBoomerangEntity extends ThrowableItemProjectile {

	public static final int MAX_THROW_TIME = 10;
	private static final int SAFETY_TIMEOUT = -100;

	private int returnTimer = MAX_THROW_TIME;
	@Getter
	private float speed = 1.5f;
	public int damage = 6;
	private int thrownFromSlot;
	private ItemStack boomerangItem = ItemStack.EMPTY;
	private int maxItemPickup;

	public MagicBoomerangEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
		super(type, level);
	}

	public MagicBoomerangEntity(Level level, LivingEntity shooter, ItemStack stack, float speed, int damage, int maxItemPickup) {
		super(RelicsEntities.MAGIC_BOOMERANG.get(), shooter, level);
		this.boomerangItem = stack.copy();
		this.setItem(stack);
		this.speed = speed;
		this.damage = damage;
		this.maxItemPickup = maxItemPickup;
	}

	@Override
	protected Item getDefaultItem() {
		return RelicsItems.MAGIC_BOOMERANG.get();
	}

	@Override
	protected float getGravity() {
		return 0.0f;
	}

	public void setThrownFromSlot(int slot) {
		this.thrownFromSlot = slot;
	}

	@Override
	public void tick() {
		Entity owner = getOwner();

		// sound every 3 ticks
		if (owner != null && returnTimer % 3 == 0) {
			playSound(RelicsSounds.MAGIC_BOOMERANG.get(), 2.0f, 1.0f + 1.0f / (1.0f + distanceTo(owner) / 16.0f));
		}

		// crit particles on client
		if (level().isClientSide()) {
			Vec3 delta = getDeltaMovement();
			for (int i = 0; i < 4; i++) {
				level().addParticle(ParticleTypes.CRIT, getX() + delta.x * i / 4.0, getY() + delta.y * i / 4.0, getZ() + delta.z * i / 4.0, -delta.x, -delta.y + 0.2, -delta.z);
			}
		}

		// pick up nearby items/entities
		if (!level().isClientSide() && getPassengers().isEmpty()) {
			List<Entity> nearby = level().getEntitiesOfClass(Entity.class, getBoundingBox().inflate(1.0), e -> e instanceof ItemEntity);
			for (Entity e : nearby) {
				e.startRiding(this);
				break;
			}
		}
		
		if (!level().isClientSide()) {
		      BlockPos pos = blockPosition();
		      BlockState state = level().getBlockState(pos);
		      if (!state.isAir() && state.getDestroySpeed(level(), pos) == 0.0f
		              && state.getCollisionShape(level(), pos).isEmpty()) {
		          level().destroyBlock(pos, true, getOwner());
		      }
		  }

		// return heading logic
		returnTimer--;
		if (returnTimer <= 0 && owner != null) {
			Vec3 delta = getDeltaMovement();
			float currentHeading = (float) Math.atan2(delta.z, delta.x);
			float headingToOwner = (float) Math.atan2(owner.getZ() - getZ(), owner.getX() - getX());
			float curveScale = (float) (-returnTimer) * 0.007f;
			float newHeading = lerpAngle(currentHeading, headingToOwner, curveScale);

			double currentPitch = Math.atan2(delta.y, Math.sqrt(delta.x * delta.x + delta.z * delta.z));
			double dx = owner.getX() - getX();
			double dz = owner.getZ() - getZ();
			double targetPitch = Math.atan2(owner.getEyeY() - getY(), Math.sqrt(dx * dx + dz * dz));
			float newPitch = lerpAngle((float) currentPitch, (float) targetPitch, curveScale * 0.3f);

			double motionX = Math.cos(newHeading) * Math.cos(newPitch);
			double motionZ = Math.sin(newHeading) * Math.cos(newPitch);
			double motionY = Math.sin(newPitch);
			shoot(motionX, motionY, motionZ, speed, 0.0f);
		}

		// safety timeout — drop item if it can't reach the player
		if (returnTimer < SAFETY_TIMEOUT && !level().isClientSide()) {
			if (!boomerangItem.isEmpty()) {
				spawnAtLocation(boomerangItem);
			}
			ejectPassengers();
			discard();
			return;
		}

		super.tick();
	}

	private float lerpAngle(float current, float target, float maxStep) {
		float diff = target - current;
		diff %= (float) (Math.PI * 2);
		if (diff >= Math.PI)
			diff -= (float) (Math.PI * 2);
		if (diff < -Math.PI)
			diff += (float) (Math.PI * 2);
		if (diff > maxStep)
			diff = maxStep;
		if (diff < -maxStep)
			diff = -maxStep;
		return current + diff;
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (level().isClientSide())
			return;

		Entity hit = result.getEntity();
		Entity owner = getOwner();

		// catch by owner when returning
		if (hit == owner && returnTimer <= 0 && hit instanceof Player player) {
			catchBoomerang(player);
			return;
		}

		// damage non-owner living entities
		if (hit != owner && hit instanceof LivingEntity) {
			hit.hurt(damageSources().thrown(this, owner), damage);

			if (!boomerangItem.isEmpty()) {
				ServerPlayer serverPlayer = owner instanceof ServerPlayer sp ? sp : null;
				if (boomerangItem.hurt(1, level().getRandom(), serverPlayer)) {
					playSound(SoundEvents.ITEM_BREAK, 1.0f, 0.8f);
					boomerangItem = ItemStack.EMPTY;
					ejectPassengers();
					discard();
				}
			}
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		if (noPhysics)
			return;

		BlockState state = level().getBlockState(result.getBlockPos());
		float hardness = state.getDestroySpeed(level(), result.getBlockPos());

		// break fragile blocks (tallgrass, flowers, dead bushes, etc.)
		if (hardness == 0.0f && !state.isAir()) {
			if (!level().isClientSide()) {
				level().destroyBlock(result.getBlockPos(), true, getOwner());
			}
			return;
		}

		// bounce off solid blocks — trigger block reactions (bell, target, etc.)
		super.onHitBlock(result);
		returnTimer = Math.min(returnTimer, 0);
		noPhysics = true;
		setDeltaMovement(getDeltaMovement().reverse());
	}

	@Override
	public void playerTouch(Player player) {
		if (!level().isClientSide() && player == getOwner() && returnTimer <= 0) {
			catchBoomerang(player);
		}
	}

	private void catchBoomerang(Player player) {
		if (isRemoved())
			return;

		// return item to original slot if possible
		if (!boomerangItem.isEmpty()) {
			ItemStack existing = player.getInventory().getItem(thrownFromSlot);
			if (existing.isEmpty()) {
				player.getInventory().setItem(thrownFromSlot, boomerangItem);
			} else if (!player.getInventory().add(boomerangItem)) {
				player.drop(boomerangItem, false);
			}
		}

		// drop passengers at player position
		List<Entity> passengers = List.copyOf(getPassengers());
		ejectPassengers();
		for (Entity passenger : passengers) {
			passenger.moveTo(player.getX(), player.getY(), player.getZ());
		}

		player.take(this, 1);
		playSound(SoundEvents.ITEM_PICKUP, 0.2f, ((random.nextFloat() - random.nextFloat()) * 0.7f + 1.0f) * 2.0f);
		discard();
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return this.getPassengers().size() < this.maxItemPickup;
	}

	@Override
	public double getPassengersRidingOffset() {
		return 0;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("ReturnTimer", returnTimer);
		tag.putInt("ThrownFromSlot", thrownFromSlot);
		if (!boomerangItem.isEmpty()) {
			tag.put("BoomerangItem", boomerangItem.save(new CompoundTag()));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		returnTimer = tag.getInt("ReturnTimer");
		thrownFromSlot = tag.getInt("ThrownFromSlot");
		if (tag.contains("BoomerangItem", 10)) {
			boomerangItem = ItemStack.of(tag.getCompound("BoomerangItem"));
		}
	}
}