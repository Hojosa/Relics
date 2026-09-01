package hojosa.relics_of_old.common.entity.attacks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hojosa.relics_of_old.client.render.MiniParticle;
import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity.SpellType;
import hojosa.relics_of_old.common.init.RelicsEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

//this class handels LG2 stlye spell visuals
public class SpellDecoratorEntity extends Entity implements IEntityAdditionalSpawnData {

	public int spellType;
	public List<MiniParticle> particles;
	public double radius;
	public double power;
	public boolean isCrit;
	public int longLife = 0;
	public int maxAge = 200;

	public SpellDecoratorEntity(EntityType<?> type, Level level) {
		super(type, level);
		this.spellType = Integer.MIN_VALUE;
		this.noCulling = true;
	}

	// Spawned server-side by SpellEffectEntity on tick 0
	public SpellDecoratorEntity(SpellEffectEntity spell) {
		super(RelicsEntities.SPELL_DECORATOR.get(), spell.level());
		this.setPos(spell.getX(), spell.getY(), spell.getZ());
		this.radius = spell.radius;
		this.power = spell.power;
		this.isCrit = spell.isCrit;
		this.spellType = spell.getSpellType().ordinal();
		this.noCulling = true;
		generateParticles();
	}

	// Particle generation — mirrors LG2's SpellDecorator.generateParticles() with
	// original values
	protected void generateParticles() {
		this.particles = new ArrayList<>();
		Random rand = new Random();

		if (this.spellType == SpellType.STAR_IMPACT.ordinal()) {
			for (int i = 0; i < 50; i++) {
				MiniParticle p = MiniParticle.newRadial(rand, this.radius, 0.0, 0.0);
				p.maxLife = 5;
				p.hibernateTime = rand.nextInt(15);
				this.particles.add(p);
			}
		}

		if (this.spellType == SpellType.FIRE.ordinal()) {
			for (int i = 0; i < 30; i++) {
				MiniParticle p = MiniParticle.newRadial(rand, this.radius * 0.75, 0.1, -0.002);
				p.maxLife = 15 + rand.nextInt(10);
				p.ay = 0.03;
				p.hibernateTime = rand.nextInt(5);
				p.drag = 0.8;
				p.uniqueness = rand.nextDouble();
				this.particles.add(p);
			}
		}

		if (this.spellType == SpellType.LIGHTNING.ordinal()) {
			for (int i = 0; i < 30; i++) {
				MiniParticle p = MiniParticle.newRadial(rand, this.radius, 0.0, 0.0);
				p.uniqueness = rand.nextDouble();
				p.maxLife = 15;
				p.hibernateTime = rand.nextInt(5);
				this.particles.add(p);
			}
		}

		if (this.spellType == SpellType.ICE.ordinal()) {
			for (int i = 0; i < 20; i++) {
				MiniParticle p = MiniParticle.newRadial(rand, this.radius, 0.0, 0.0);
				p.maxLife = 15;
				p.hibernateTime = rand.nextInt(5);
				this.particles.add(p);
			}
		}

		if (this.spellType == SpellType.SPRINKLE_STARDUST.ordinal()) {
			for (int i = 0; i < 30; i++) {
				MiniParticle p = MiniParticle.newRadial(rand, this.radius, 0.0, 0.0);
				p.maxLife = 5;
				p.hibernateTime = rand.nextInt(8);
				this.particles.add(p);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		// Client-side fallback if particles weren't generated yet
		if (this.particles == null) {
			this.generateParticles();
		}

		// Tick particles only on client — natural 20 TPS, no gating needed
		if (level().isClientSide) {
			this.particles = MiniParticle.tickAll(this.particles);
		}

		this.longLife++;

		// Kill when all particles exhausted or max age safety
		if (level().isClientSide && this.particles.isEmpty()) {
			this.discard();
		}
		if (this.longLife > this.maxAge) {
			this.discard();
		}
	}

	@Override
	protected void defineSynchedData() {
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeInt(this.spellType);
		buffer.writeDouble(this.radius);
		buffer.writeDouble(this.power);
		buffer.writeBoolean(this.isCrit);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		this.spellType = additionalData.readInt();
		this.radius = additionalData.readDouble();
		this.power = additionalData.readDouble();
		this.isCrit = additionalData.readBoolean();
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}