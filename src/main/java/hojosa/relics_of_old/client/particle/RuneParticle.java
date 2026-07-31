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

public class RuneParticle extends TextureSheetParticle {
	private double hue;

	protected RuneParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed, int lifetime, float size) {
		super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
		this.xd = pXSpeed;
		this.yd = pYSpeed;
		this.zd = pZSpeed;
		this.hasPhysics = false;
		this.quadSize = size;
		this.lifetime = lifetime;
//		this.quadSize = 0.15f;
//		this.lifetime = 45;
		this.hue = Math.random() * Math.PI * 2;
		this.pickSprite(spriteSet);
	}
	
    @Override
    public void tick() {
        super.tick();
        this.hue += 0.3;
        float r = (float) Math.sin(hue) / 2 + 0.5f;
        float g = (float) Math.sin(hue + Math.PI * 2 / 3) / 2 + 0.5f;
        float b = (float) Math.sin(hue - Math.PI * 2 / 3) / 2 + 0.5f;
        float freshness = 1f - (float) age / lifetime;
        this.alpha = freshness;
        this.rCol = freshness + (1 - freshness) * r;
        this.gCol = freshness + (1 - freshness) * g;
        this.bCol = freshness + (1 - freshness) * b;
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
	  public static class Provider implements ParticleProvider<RelicsParticleOptions> {
	      private final SpriteSet sprites;

	      public Provider(SpriteSet spriteSet) {
	          this.sprites = spriteSet;
	      }

	      public Particle createParticle(RelicsParticleOptions options, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
	          return new RuneParticle(level, x, y, z, this.sprites, dx, dy, dz, options.getLifetime(), options.getSize());
	      }
	  }

}