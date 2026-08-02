package hojosa.relics_of_old.client.particle;

import hojosa.relics_of_old.lib.RelicsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MagicScrambleParticle extends RuneParticle {

	private final SpriteSet spriteSet;

	protected MagicScrambleParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed, int lifetime, float size) {
		super(pLevel, pX, pY, pZ, spriteSet, pXSpeed, pYSpeed, pZSpeed, lifetime, size);
		this.spriteSet = spriteSet;
	}

	@Override
	public void tick() {
		super.tick();
		this.pickSprite(spriteSet);

		float freshness = 1f - (float) age / lifetime;
		this.rCol = Math.max(freshness * 2.0f - 1.0f, 0.0f);
		this.gCol = Math.min(freshness * 2.0f, 1.0f);
		this.bCol = 1.0f;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<RelicsParticleOptions> {
		private final SpriteSet sprites;

		public Provider(SpriteSet spriteSet) {
			this.sprites = spriteSet;
		}

		public Particle createParticle(RelicsParticleOptions options, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new MagicScrambleParticle(level, x, y, z, this.sprites, dx, dy, dz, options.getLifetime(), options.getSize());
		}
	}
}