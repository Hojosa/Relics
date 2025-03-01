package hojosa.relics.common.entity;

import java.util.Random;

import hojosa.relics.common.init.RelicsEntities;
import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.common.init.RelicsSounds;
import hojosa.relics.common.player.StarFallChanceProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class FallingStarEntity extends Entity {

	public static final String AIR_TAG = "Air";
	public static final String ALIVE_TAG = "Alive";
	private static final EntityDataAccessor<Boolean> DATA_ID_AIR = SynchedEntityData.defineId(FallingStarEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> DATA_ID_ALIVE = SynchedEntityData.defineId(FallingStarEntity.class, EntityDataSerializers.INT);
	public static int DWINDLE_TIME = 440;
	private double movementY;


	public FallingStarEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public FallingStarEntity(Player player) {
		this(RelicsEntities.FALLING_STAR.get(), player.level());
		double theta = this.random.nextDouble() * Math.PI * 2.0;
		double radius = 42.0;
		this.setPos(player.position().x + Math.cos(theta) * radius, player.position().y + 100, player.position().z + Math.sin(theta) * radius);
	}
	
    public static AttributeSupplier.Builder createAttributes() {
        return AttributeSupplier.builder()
                .add(ForgeMod.ENTITY_GRAVITY.get(), 1D)
                .add(Attributes.FLYING_SPEED, 2D);
    }

	@Override
	public void playerTouch(Player pPlayer) {
		if (!this.level().isClientSide()) {
			this.playSound(RelicsSounds.STAR_CAUGHT_SOUND.get(), 1f, 1f);
			pPlayer.addItem(new ItemStack(RelicsItems.STAR_PIECE.get(), 1));
			ExperienceOrb.award((ServerLevel) this.level(), this.position(), 3);
			// add achivement maybe?
			this.remove(RemovalReason.DISCARDED);
			
			//a little bonus for star collectors:
			pPlayer.getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).ifPresent(star -> {
				if(star.getStarsCollected() % 7 == 0 ) {
					pPlayer.level().addFreshEntity(new FallingStarEntity(pPlayer));
				}
			});
		}
	}

	@Override
	public void tick() {
		// star is flying
		if (this.getAirState()) {
			if(this.tickCount == 6 && this.level().isClientSide()) {
				this.level().playLocalSound(this.blockPosition(), RelicsSounds.STAR_FALL_SOUND.get(), getSoundSource(), 0.5f, 1.0f, false);
			}
			
			this.movementY -= 0.03;
			this.move(MoverType.SELF, new Vec3(0, this.movementY, 0));

			if (!this.level().isClientSide()) {
				if ((this.horizontalCollision || this.verticalCollision)) {

					this.level().explode((Entity) this, this.getX(), this.getY(), this.getZ(), 0.3F, Level.ExplosionInteraction.NONE);

					if (this.isInWater()) {
						this.playSound(this.getSwimSplashSound(), 2F, 2.0F);
					}
					this.setAirState(false);
					this.level().setBlockAndUpdate(this.blockPosition(), Blocks.LIGHT.defaultBlockState());
				}
			}
		}
		// landed
		else {
			if (this.getAliveState() == 0) {
				if (!this.level().isClientSide()) {
					this.spawnAtLocation(new ItemStack(RelicsItems.STAR_PIECE.get(), new Random().nextInt(2)));
					ExperienceOrb.award((ServerLevel) this.level(), this.position(), 1);
					this.remove(RemovalReason.DISCARDED);
				} else {
					// particles?
				}
			} else {
				if (!this.level().isClientSide()) {
					if (this.getAliveState() % 25 == 0)
						this.playSound(RelicsSounds.STAR_TWINKLE_SOUND.get(), 0.1f, 0.5f);
					
					this.setAliveState(this.getAliveState() -1);
				}
			}
		}
		
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_ID_AIR, true);
		this.entityData.define(DATA_ID_ALIVE, 380);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound) {
//		pCompound.putBoolean(AIR_TAG, isInAir);
//		pCompound.putInt(ALIVE_TAG, aliveTimer);

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {
//		if (pCompound.contains(AIR_TAG)) {
//			this.isInAir = pCompound.getBoolean(AIR_TAG);
//		}
//		if (pCompound.contains(ALIVE_TAG)) {
//			this.aliveTimer = pCompound.getInt(ALIVE_TAG);
//		}
	}

	public Boolean getAirState() {
		return this.entityData.get(DATA_ID_AIR);
	}

	public void setAirState(boolean state) {
		this.entityData.set(DATA_ID_AIR, state);
	}

	public Integer getAliveState() {
		return this.entityData.get(DATA_ID_ALIVE);
	}

	public void setAliveState(int state) {
		this.entityData.set(DATA_ID_ALIVE, state);
	}
	
	@Override
	public void remove(RemovalReason pReason) {
		this.level().setBlockAndUpdate(this.blockPosition(), Blocks.AIR.defaultBlockState());
		super.remove(pReason);
	}
}
