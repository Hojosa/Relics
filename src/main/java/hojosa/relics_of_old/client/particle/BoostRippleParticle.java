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

public class BoostRippleParticle extends TextureSheetParticle {

	private final float baseScale;

	protected BoostRippleParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, float scale) {
		super(level, x, y, z);
		this.xd = 0;
		this.yd = 0;
		this.zd = 0;
		this.hasPhysics = false;
		this.lifetime = 6;
		this.baseScale = scale;
		this.quadSize = 0.1f * scale * 0.3f;
		this.pickSprite(spriteSet);
	}

	// LG2 ripple: expands from 30% to full size, fades cyan→blue
	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
			return;
		}
		float freshness = 1.0f - (float) this.age / (float) this.lifetime;
		this.alpha = (float) Math.sin(freshness * Math.PI);
		this.rCol = Math.max(-0.5f + freshness, 0.0f);
		this.gCol = 0.5f + 0.5f * freshness;
		this.bCol = 1.0f;
		this.quadSize = 0.1f * (1.0f - freshness * 0.7f) * baseScale;
	}

	@Override
	protected int getLightColor(float partialTick) {
		return 240;
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

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			// dx encodes the base scale (default 30.0f from LG2)
			float scale = dx > 0 ? (float) dx : 30.0f;
			return new BoostRippleParticle(level, x, y, z, this.sprites, scale);
		}
	}
}