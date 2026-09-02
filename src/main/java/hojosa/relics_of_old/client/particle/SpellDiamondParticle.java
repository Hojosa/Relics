package hojosa.relics_of_old.client.particle;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;

public class SpellDiamondParticle extends Particle {

	private static final double DIAMOND_SIZE = 0.25;

	private int hibernateTime;
	private final int maxLife;

	public SpellDiamondParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, int maxLife, int hibernateTime) {
		super(level, x, y, z);
		this.xd = vx;
		this.yd = vy;
		this.zd = vz;
		this.maxLife = maxLife;
		this.lifetime = maxLife;
		this.hibernateTime = hibernateTime;
		this.age = 0;
		this.hasPhysics = false;
		this.gravity = 0;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		// Hibernate: stay in place, count down
		if (this.hibernateTime > 0) {
			this.hibernateTime--;
			return;
		}

		// Active: move and age
		this.x += this.xd;
		this.y += this.yd;
		this.z += this.zd;
		this.age++;

		if (this.age >= this.lifetime) {
			this.remove();
		}
	}

	@Override
	public void render(VertexConsumer pBuffer, Camera camera, float partialTicks) {
		if (this.hibernateTime > 0)
			return;

		// Interpolated age 0→1 for smooth shape animation
		double t = ((double) this.age + partialTicks) / (double) this.maxLife;
		if (t < 0 || t >= 1.0)
			return;

		// Camera-relative interpolated position
		double lerpX = (this.xo + (this.x - this.xo) * partialTicks) - camera.getPosition().x;
		double lerpY = (this.yo + (this.y - this.yo) * partialTicks) - camera.getPosition().y;
		double lerpZ = (this.zo + (this.z - this.zo) * partialTicks) - camera.getPosition().z;

		// Color: warm white → gold (matches LG2 stardust)
		float r = 1.0f;
		float g = Math.min(1.0f, (float) (1.5 - t));
		float b = Math.min(1.0f, (float) (0.5 + t));

		// Diamond pinch: X shrinks, Y stretches over lifetime
		float sy = (float) (t * t * DIAMOND_SIZE);
		float sx = (float) ((1.0 - t) * (1.0 - t) * DIAMOND_SIZE);

		PoseStack poseStack = new PoseStack();
		poseStack.translate(lerpX, lerpY, lerpZ);
		poseStack.mulPose(camera.rotation());
		Matrix4f matrix = poseStack.last().pose();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(true);
		RenderSystem.disableCull();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		// Top half (degenerate quad = triangle)
		builder.vertex(matrix, 0, sy, 0).color(r, g, b, 1.0f).endVertex();
		builder.vertex(matrix, -sx, 0, 0).color(r, g, b, 1.0f).endVertex();
		builder.vertex(matrix, sx, 0, 0).color(r, g, b, 1.0f).endVertex();
		builder.vertex(matrix, sx, 0, 0).color(r, g, b, 1.0f).endVertex();

		// Bottom half
		builder.vertex(matrix, 0, -sy, 0).color(r, g, b, 1.0f).endVertex();
		builder.vertex(matrix, sx, 0, 0).color(r, g, b, 1.0f).endVertex();
		builder.vertex(matrix, -sx, 0, 0).color(r, g, b, 1.0f).endVertex();
		builder.vertex(matrix, -sx, 0, 0).color(r, g, b, 1.0f).endVertex();

		tesselator.end();
		RenderSystem.enableCull();
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.CUSTOM;
	}

	@Override
	public boolean shouldCull() {
		return false;
	}
}