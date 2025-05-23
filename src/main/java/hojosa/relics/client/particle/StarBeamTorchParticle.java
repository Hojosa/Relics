package hojosa.relics.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StarBeamTorchParticle extends TextureSheetParticle {

	protected StarBeamTorchParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
		super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
		this.friction = 0.8F;
		this.xd = pXSpeed;
		this.yd = pYSpeed;
		this.zd = pZSpeed;
		this.quadSize *= 0.5F;
		this.lifetime = 20;
		this.setSpriteFromAge(spriteSet);

		this.rCol = 1f;
		this.gCol = 1f;
		this.bCol = 1f;
	}
	
    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }

    private void fadeOut() {
        this.alpha = (-(1/(float)lifetime) * age + 1);
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
			return new StarBeamTorchParticle(level, x, y, z, this.sprites, dx, dy, dz);
		}
	}
}
