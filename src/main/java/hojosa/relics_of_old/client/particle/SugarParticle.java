package hojosa.relics_of_old.client.particle;

import hojosa.relics_of_old.common.init.RelicsBlocks;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SugarParticle extends TerrainParticle {

	public SugarParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, RelicsBlocks.SUGAR_CUBE.get().defaultBlockState());
		this.lifetime = (int) (40.0f / (this.random.nextFloat() * 0.7f + 0.3f));
		this.gravity = 1.0f;
		this.setColor(1f, 1f, 1f);
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		public Factory(SpriteSet set) {
		}

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new SugarParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
		}
	}
}