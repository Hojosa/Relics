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

public class FlareParticle extends TextureSheetParticle {
	
	private final SpriteSet spriteSet;

	protected FlareParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
		super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
		this.xd = pXSpeed;
		this.yd = pYSpeed;
		this.zd = pZSpeed;
		this.hasPhysics = false;
		this.quadSize = 1.0f;
		this.lifetime = 7;
		this.pickSprite(spriteSet);
		this.spriteSet = spriteSet;
	}
	
    @Override
    public void tick() {
    	super.tick();
    	this.setSpriteFromAge(this.spriteSet);

        float freshness = 1.0f - (float) age / lifetime;
        this.alpha = Math.min(freshness * 2.0f, 1.0f);
        this.rCol = Math.min(freshness * 3.0f, 1.0f);
        this.gCol = Math.max(Math.min(freshness * 3.0f - 1.0f, 1.0f), 0.0f);
        this.bCol = Math.max(Math.min(freshness * 3.0f - 2.0f, 1.0f), 0.0f);
    }
    
    @Override
    protected int getLightColor(float pPartialTick) {
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

		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new FlareParticle(level, x, y, z, this.sprites, dx, dy, dz);
		}
	}
}