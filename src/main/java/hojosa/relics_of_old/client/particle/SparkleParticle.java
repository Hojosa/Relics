package hojosa.relics_of_old.client.particle;

import hojosa.relics_of_old.lib.RelicsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SparkleParticle extends TextureSheetParticle {
	private float baseSize;
	private float initialBCol;

	protected SparkleParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
		super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
		this.xd = pXSpeed;
		this.yd = pYSpeed;
		this.zd = pZSpeed;
		this.baseSize = 0.1f;
		this.quadSize = baseSize;
		this.lifetime = 6;
		this.pickSprite(spriteSet);

		this.rCol = 1f;
		this.gCol = 1f;
		this.initialBCol = 0.4f;
		this.bCol = initialBCol;
	}

	@Override
	public void tick() {
		super.tick();
		float freshness = 1f - (float) age / lifetime;
//		this.bCol = bCol * freshness;// * 0.6f;
		this.quadSize = freshness * baseSize;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<RelicsParticleOptions> {
		private final SpriteSet sprites;

		public Provider(SpriteSet spriteSet) {
			this.sprites = spriteSet;
		}

		public Particle createParticle(RelicsParticleOptions options, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
	        SparkleParticle particle = new SparkleParticle(level, x, y, z, this.sprites, dx, dy, dz);
	        particle.lifetime = options.getLifetime();
	        particle.baseSize = options.getSize();
	        particle.quadSize = options.getSize();
	        particle.rCol = options.getR();
	        particle.gCol = options.getG();
	        particle.initialBCol = options.getB();
	        particle.bCol = options.getB();
	        return particle;
	  }
	}
}