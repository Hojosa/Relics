package hojosa.relics_of_old.common.entity;

import java.util.ArrayList;
import java.util.List;

import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity;
import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.common.player.StarFallChanceProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class FallingStarEntity extends Entity {

	public static final String ALIVE_TAG = "Alive";
	private static final EntityDataAccessor<Integer> DATA_ID_ALIVE = SynchedEntityData.defineId(FallingStarEntity.class, EntityDataSerializers.INT);
	public static final int DWINDLE_TIME = 440;
	private double movementY;
	private boolean special = false;
	private final List<ItemStack> contents = new ArrayList<>();

	public FallingStarEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public FallingStarEntity(Player player) {
		this(RelicsEntities.FALLING_STAR.get(), player.level());
		double theta = this.random.nextDouble() * Math.PI * 2.0;
		double radius = 42.0;
		this.setPos(player.position().x + Math.cos(theta) * radius, player.position().y + 110, player.position().z + Math.sin(theta) * radius);
	}

	// Reward star — spawns directly above a position for ritual delivery
	public FallingStarEntity(Level level, double x, double z, double baseY) {
		this(RelicsEntities.FALLING_STAR.get(), level);
		this.setPos(x + 0.5, baseY + 150, z + 0.5);
		this.special = true;
	}

	public void addItem(ItemStack stack) {
		if (!stack.isEmpty()) {
			contents.add(stack.copy());
		}
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	@Override
	public void playerTouch(Player pPlayer) {
		if (!this.level().isClientSide()) {
			pPlayer.addItem(new ItemStack(RelicsItems.STAR_PIECE.get(), 1));
			ExperienceOrb.award((ServerLevel) this.level(), this.position(), 3);
			pPlayer.getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).ifPresent(star -> star.setStarsCollected(star.getStarsCollected() + 1));
			// add achivement maybe?
			this.remove(RemovalReason.DISCARDED);

			// a little bonus for star collectors:
			pPlayer.getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).ifPresent(star -> {
				if (star.getStarsCollected() != 0 && star.getStarsCollected() % 7 == 0) {
					this.playSound(RelicsSounds.STAR_CAUGHT_X_SOUND.get(), 1f, 1f);
					pPlayer.level().addFreshEntity(new FallingStarEntity(pPlayer));
				} else
					this.playSound(RelicsSounds.STAR_CAUGHT_SOUND.get(), 1f, 1f);
			});
		}
	}

	@Override
	public void tick() {
		// star is flying
		if (!this.onGround()) {
			if (this.tickCount == 5 && !this.level().isClientSide()) {
				this.level().playSound(null, this.blockPosition(), RelicsSounds.STAR_FALL_SOUND.get(), getSoundSource(), 10.0f, 1.0f);
			}
			this.movementY -= 0.03;
			this.move(MoverType.SELF, new Vec3(0, this.movementY, 0));

			if (!this.level().isClientSide() && (this.horizontalCollision || this.verticalCollision)) {
				if (this.special) {
					// Reward star: small explosion, drop contents, die
					this.level().explode(this, this.getX(), this.getY(), this.getZ(), 0.3F, Level.ExplosionInteraction.NONE);
					for (ItemStack stack : this.contents) {
						ItemEntity item = new ItemEntity(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), stack);
						item.setDeltaMovement(0, 0.2, 0);
						this.level().addFreshEntity(item);
					}
					this.contents.clear();
					if (this.level() instanceof ServerLevel serverLevel) {
						ExperienceOrb.award(serverLevel, this.position(), 7);
					}
					this.remove(RemovalReason.DISCARDED);
					return;
				}

				// Normal star: explosion + star impact effect
				this.level().explode(this, this.getX(), this.getY(), this.getZ(), 0.3F, Level.ExplosionInteraction.NONE);
				if (this.isInWater()) {
					this.playSound(this.getSwimSplashSound(), 2F, 2.0F);
				}
				SpellEffectEntity spell = new SpellEffectEntity(this.level(), SpellEffectEntity.SpellType.STAR_IMPACT, null, this.position(), 2.0, 1.0, false);
				this.level().addFreshEntity(spell);
				this.level().setBlockAndUpdate(this.blockPosition(), Blocks.LIGHT.defaultBlockState());
			}
		}
		// landed
		else {
			if (this.getAliveState() == 0) {
				if (!this.level().isClientSide()) {
					this.spawnAtLocation(new ItemStack(RelicsItems.STAR_DUST.get(), random.nextInt(2)));
					ExperienceOrb.award((ServerLevel) this.level(), this.position(), 1);
					this.remove(RemovalReason.DISCARDED);
				} else {
					// particles?
				}
			} else {
				if (!this.level().isClientSide()) {
					if (this.getAliveState() % 25 == 0)
						this.playSound(RelicsSounds.STAR_TWINKLE_SOUND.get(), 0.1f, 0.5f);

					this.setAliveState(this.getAliveState() - 1);
				}
			}
		}
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_ID_ALIVE, 380);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound) {
		this.special = pCompound.getBoolean("special");
        this.setAliveState(pCompound.getInt("aliveState"));
        this.movementY = pCompound.getDouble("movementY");
        if (pCompound.contains("items")) {
                ListTag items = pCompound.getList("items", Tag.TAG_COMPOUND);
                this.contents.clear();
                for (int i = 0; i < items.size(); i++) {
                        ItemStack stack = ItemStack.of(items.getCompound(i));
                        if (!stack.isEmpty()) this.contents.add(stack);
                }
        }
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {
		pCompound.putBoolean("special", this.special);
        pCompound.putInt("aliveState", this.getAliveState());
        pCompound.putDouble("movementY", this.movementY);
        ListTag items = new ListTag();
        for (ItemStack stack : this.contents) {
                items.add(stack.save(new CompoundTag()));
        }
        pCompound.put("items", items);
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
