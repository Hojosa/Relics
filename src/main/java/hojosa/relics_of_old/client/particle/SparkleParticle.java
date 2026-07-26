package hojosa.relics_of_old.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SparkleParticle extends TextureSheetParticle {
	private final float baseSize;

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
		this.bCol = 0.4f;
	}
	
    @Override
    public void tick() {
        super.tick();
        float freshness = 1f - (float) age / lifetime;
        this.bCol = bCol * freshness;// * 0.6f;
        this.quadSize = freshness * baseSize;
    }

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet spriteSet) {
			this.sprites = spriteSet;
		}

		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new SparkleParticle(level, x, y, z, this.sprites, dx, dy, dz);
		}
	}
}