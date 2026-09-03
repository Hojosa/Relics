package hojosa.relics_of_old.common.entity.attacks;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import hojosa.relics_of_old.client.particle.SpellDiamondParticle;
import hojosa.relics_of_old.common.block.entity.RitualLocusBlockEntity;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsEntities;
import hojosa.relics_of_old.common.init.RelicsSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

//this class handels LG2 stlye spells (effects, interactions etc)
public class SpellEffectEntity extends Entity implements IEntityAdditionalSpawnData {

	private static final EntityDataAccessor<Integer> DATA_SPELL_TYPE = SynchedEntityData.defineId(SpellEffectEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DATA_LIFETIME = SynchedEntityData.defineId(SpellEffectEntity.class, EntityDataSerializers.INT);

	private static final float BLOCK_RADIUS_FUDGE = 0.25f;

	private UUID casterUUID;
	private Player cachedCaster;
	public double radius;
	public double power;
	public boolean isCrit;
	private final Set<Entity> hits = new HashSet<>();

	public SpellEffectEntity(EntityType<?> type, Level level) {
		super(type, level);
		this.noPhysics = true;
	}

	public SpellEffectEntity(Level level, SpellType type, Player caster, Vec3 pos, double radius, double power, boolean critical) {
		super(RelicsEntities.SPELL_EFFECT.get(), level);
		this.setPos(pos);
		this.noPhysics = true;
		this.radius = radius;
		this.power = power;
		this.isCrit = critical;
		setSpellType(type);
		if (caster != null) {
			this.casterUUID = caster.getUUID();
			this.cachedCaster = caster;
		}
	}

	@Override
	public void tick() {
		super.tick();
		int lifetime = getLifetime();
		SpellType type = getSpellType();

		// sound on spawn
		if (lifetime == 0 && !level().isClientSide) {
			type.onSpawn(this);
//			SpellDecoratorEntity decorator = new SpellDecoratorEntity(this);
//			level().addFreshEntity(decorator);
		}

		// affect blocks at sleepTime
		if (lifetime == type.sleepTime && !level().isClientSide && type.affectsBlocks) {
			affectBlocks();
		}

		// hit entities after sleepTime
		if (lifetime >= type.sleepTime && !level().isClientSide) {
			tryHittingEntities();
		}

		// client particles
		if (level().isClientSide) {
			type.clientTick(this, lifetime);
		}

		setLifetime(lifetime + 1);
		if (lifetime >= type.maxLife) {
			discard();
		}
	}

	private void affectBlocks() {
		double adjR = radius + BLOCK_RADIUS_FUDGE;
		BlockPos min = BlockPos.containing(getX() - adjR, getY() - adjR, getZ() - adjR);
		BlockPos max = BlockPos.containing(getX() + adjR, getY() + adjR, getZ() + adjR);

		for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
			Vec3 blockCenter = Vec3.atCenterOf(pos);
			if (blockCenter.distanceToSqr(position()) <= adjR * adjR) {
				getSpellType().affectBlock(this, pos);
			}
		}
	}

	private void tryHittingEntities() {
		AABB box = new AABB(getX() - radius, getY() - radius, getZ() - radius, getX() + radius, getY() + radius, getZ() + radius);
		List<Entity> entities = level().getEntities(this, box);

		for (Entity ent : entities) {
			if (hits.contains(ent))
				continue;
			// check actual distance to bounding box
			AABB entBox = ent.getBoundingBox();
			double cx = Math.min(entBox.maxX, Math.max(getX(), entBox.minX));
			double cy = Math.min(entBox.maxY, Math.max(getY(), entBox.minY));
			double cz = Math.min(entBox.maxZ, Math.max(getZ(), entBox.minZ));
			double dsq = (cx - getX()) * (cx - getX()) + (cy - getY()) * (cy - getY()) + (cz - getZ()) * (cz - getZ());

			if (dsq <= radius * radius) {
				if (ent instanceof LivingEntity living) {
					getSpellType().affectLiving(this, living);
				} else {
					getSpellType().affectInanimate(this, ent);
				}
				hits.add(ent);
			}
		}
	}

	public Player getCaster() {
		if (cachedCaster != null && !cachedCaster.isRemoved())
			return cachedCaster;
		if (casterUUID != null && level() instanceof ServerLevel serverLevel) {
			Entity e = serverLevel.getEntity(casterUUID);
			if (e instanceof Player player) {
				cachedCaster = player;
				return player;
			}
		}
		return null;
	}

	public void knockRadialOutward(Entity target, float force, float extraUp) {
		Vec3 away = target.position().subtract(position()).normalize();
		target.push(away.x * force, away.y * force + extraUp, away.z * force);
		target.hurtMarked = true;
	}

	public SpellType getSpellType() {
		return SpellType.values()[entityData.get(DATA_SPELL_TYPE)];
	}

	private void setSpellType(SpellType type) {
		entityData.set(DATA_SPELL_TYPE, type.ordinal());
	}

	public int getLifetime() {
		return entityData.get(DATA_LIFETIME);
	}

	private void setLifetime(int value) {
		entityData.set(DATA_LIFETIME, value);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(DATA_SPELL_TYPE, 0);
		entityData.define(DATA_LIFETIME, 0);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		setSpellType(SpellType.values()[tag.getInt("SpellType")]);
		setLifetime(tag.getInt("Lifetime"));
		radius = tag.getDouble("Radius");
		power = tag.getDouble("Power");
		isCrit = tag.getBoolean("IsCrit");
		if (tag.hasUUID("Caster"))
			casterUUID = tag.getUUID("Caster");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putInt("SpellType", getSpellType().ordinal());
		tag.putInt("Lifetime", getLifetime());
		tag.putDouble("Radius", radius);
		tag.putDouble("Power", power);
		tag.putBoolean("IsCrit", isCrit);
		if (casterUUID != null)
			tag.putUUID("Caster", casterUUID);
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeDouble(radius);
		buffer.writeDouble(power);
		buffer.writeBoolean(isCrit);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		radius = additionalData.readDouble();
		power = additionalData.readDouble();
		isCrit = additionalData.readBoolean();
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public enum SpellType {
		STAR_IMPACT(Element.STAR, 10, true), 
		ORB_EXPLOSION(Element.EXPLOSION, 2, true) {
			@Override
			public void affectBlock(SpellEffectEntity spell, BlockPos pos) {
				BlockState state = spell.level().getBlockState(pos);
				boolean breakable = state.is(Blocks.COBBLESTONE) || (!state.hasBlockEntity() && state.getDestroySpeed(spell.level(), pos) == 0.0f);
				if (breakable) {
					spell.level().destroyBlock(pos, true);
				}
			}

			@Override
			public void affectLiving(SpellEffectEntity spell, LivingEntity living) {
				float damage = (float) spell.power;
				Player caster = spell.getCaster();
				if (living.equals(caster))
					damage = 4.0f;
				living.hurt(spell.damageSources().indirectMagic(spell, caster), damage);
				spell.knockRadialOutward(living, 0.5f, 0.0f);
			}
			
			@Override
		      public void onSpawn(SpellEffectEntity spell) {
		          spell.level().playSound(null, spell.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0f, 1.0f);
		      }

			@Override
			public void clientTick(SpellEffectEntity spell, int lifetime) {
				if (lifetime == 0) {
		              spell.level().addParticle(ParticleTypes.EXPLOSION, spell.getX(), spell.getY(), spell.getZ(), 0, 0, 0);
		          }
			}
		},
		FIRE(Element.FIRE, 15, true) {
			@Override
			public void onSpawn(SpellEffectEntity spell) {
				spell.level().playSound(null, spell.blockPosition(), RelicsSounds.SPELL_FIRE.get(), SoundSource.PLAYERS, 2.5f, 1.0f);
			}

		},
		LIGHTNING(Element.LIGHTNING, 5, false) {
			@Override
			public void onSpawn(SpellEffectEntity spell) {
				spell.level().playSound(null, spell.blockPosition(), RelicsSounds.SPELL_LIGHTNING.get(), SoundSource.PLAYERS, 2.5f, 1.0f);
			}

		},
		ICE(Element.ICE, 10, true) {
			@Override
			public void onSpawn(SpellEffectEntity spell) {
				spell.level().playSound(null, spell.blockPosition(), RelicsSounds.SPELL_ICE.get(), SoundSource.PLAYERS, 2.5f, 1.0f);
			}

		},
		TWINKLE(Element.STAR, 10, false) {
			@Override
			public void onSpawn(SpellEffectEntity spell) {
				spell.level().playSound(null, spell.blockPosition(), RelicsSounds.SPELL_TWINKLE.get(), SoundSource.PLAYERS, 2.5f, 1.0f);
			}

		},
		SPRINKLE_STARDUST(Element.HARMLESS, 30, true) {
			@Override
			public void affectBlock(SpellEffectEntity spell, BlockPos pos) {
				Level level = spell.level();
				// Activate ritual locus
				if (level.getBlockEntity(pos) instanceof RitualLocusBlockEntity locus) {
					Player caster = spell.getCaster();
					if (caster != null) {
						locus.tryInvoke(caster);
					}
				}
			}

			@Override
			public void onSpawn(SpellEffectEntity spell) {
				spell.level().playSound(null, spell.blockPosition(), RelicsSounds.STARDUST.get(), SoundSource.PLAYERS, 2.5f, 1.0f);
			}

			@Override
			public void clientTick(SpellEffectEntity spell, int lifetime) {
				// Spawn diamond particles on first client tick
				if (lifetime == 0) {
					Random rand = new Random();
					ClientLevel clientLevel = (ClientLevel) spell.level();
					for (int i = 0; i < 30; i++) {
						Vec3 outward = new Vec3(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian()).normalize();
						double dist = rand.nextDouble() * spell.radius;
						double gx = outward.x * dist;
						double gy = outward.y * dist;
						double gz = outward.z * dist;
						int hibernateTime = rand.nextInt(8);

						SpellDiamondParticle diamond = new SpellDiamondParticle(clientLevel, spell.getX() + gx, spell.getY() + gy, spell.getZ() + gz, 0, 0, 0, 5, hibernateTime); // no velocity for stardust
						Minecraft.getInstance().particleEngine.add(diamond);
					}
				}
			}
		};

		public final Element element;
		public final int maxLife;
		public final boolean affectsBlocks;
		public final int sleepTime;

		SpellType(Element element, int maxLife, boolean affectsBlocks) {
			this(element, maxLife, affectsBlocks, 0);
		}

		SpellType(Element element, int maxLife, boolean affectsBlocks, int sleepTime) {
			this.element = element;
			this.maxLife = maxLife;
			this.affectsBlocks = affectsBlocks;
			this.sleepTime = sleepTime;
		}

		// -- Default block effects based on element --
		public void affectBlock(SpellEffectEntity spell, BlockPos pos) {
			BlockState state = spell.level().getBlockState(pos);
			if (element == Element.ICE && (state.is(Blocks.WATER))) {
				spell.level().setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
			}
			if (element == Element.FIRE && (state.is(Blocks.ICE))) {
				spell.level().setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
			}
			if (element == Element.STAR && state.is(Blocks.SAND)) {
				spell.level().setBlockAndUpdate(pos, RelicsBlocks.STARRY_SAND.get().defaultBlockState());
			}
		}

		// -- Default living effect: element-based damage --
		public void affectLiving(SpellEffectEntity spell, LivingEntity living) {
			if (element == Element.HARMLESS)
				return;
			float damage = (float) spell.power * element.getDamageMultiplier(living);
			if (damage == 0.0f)
				return;

			Player caster = spell.getCaster();
			float knockback = spell.isCrit ? 0.2f : 0.15f;

			if (spell.isCrit) {
				if (element == Element.FIRE)
					living.setRemainingFireTicks((int) spell.power * 20);
				if (element == Element.ICE) {
					// TODO: add slowness + jump penalty potion effects
				}
				if (element == Element.LIGHTNING)
					knockback = 0.5f;
			}
			if (element == Element.FIRE)
				living.setRemainingFireTicks(20);

			living.hurt(spell.damageSources().indirectMagic(spell, caster), damage);
			spell.knockRadialOutward(living, knockback, knockback);
		}

		public void affectInanimate(SpellEffectEntity spell, Entity target) {
			// override per type as needed
		}

		// -- Called on first tick, server side --
		public void onSpawn(SpellEffectEntity spell) {
			// override per type for custom spawn sounds
		}

		// -- Called every tick, client side --
		public void clientTick(SpellEffectEntity spell, int lifetime) {
			// override per type for custom particles
		}
	}

	// ========== Elements ==========

	public enum Element {
		HARMLESS, STAR, FIRE, ICE, LIGHTNING, RADIANT, EXPLOSION, WIND, TELEPORT;

		public float getDamageMultiplier(LivingEntity target) {
			if (this == FIRE && target.fireImmune())
				return 0.0f;
			if (this == LIGHTNING && target.isInWaterOrRain())
				return 2.0f;
			if (this == ICE && target instanceof SnowGolem)
				return 0.0f;
			if (this == ICE && target.fireImmune())
				return 2.0f;
			if (this == WIND && !target.onGround())
				return 2.0f;
			if (this == RADIANT && target.isInvertedHealAndHarm())
				return 2.0f;
			return 1.0f;
		}
	}
}