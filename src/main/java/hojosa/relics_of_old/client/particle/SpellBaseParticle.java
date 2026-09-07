package hojosa.relics_of_old.client.particle;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;

public abstract class SpellBaseParticle extends Particle {

	protected int hibernateTime;
	protected final int maxLife;
	protected final double power;
	protected double drag = 1.0;
	protected double ax, ay, az;

	protected SpellBaseParticle(ClientLevel level, double x, double y, double z, double power, int maxLife, int hibernateTime) {
		super(level, x, y, z);
		this.power = power;
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

		if (this.hibernateTime > 0) {
			this.hibernateTime--;
			return;
		}

		// Accelerate, drag, move
		this.xd += this.ax;
		this.yd += this.ay;
		this.zd += this.az;
		this.xd *= this.drag;
		this.yd *= this.drag;
		this.zd *= this.drag;
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

		double t = ((double) this.age + partialTicks) / (double) this.maxLife;
		if (t < 0 || t >= 1.0)
			return;

		// Camera-relative interpolated position
		double lerpX = (this.xo + (this.x - this.xo) * partialTicks) - camera.getPosition().x;
		double lerpY = (this.yo + (this.y - this.yo) * partialTicks) - camera.getPosition().y;
		double lerpZ = (this.zo + (this.z - this.zo) * partialTicks) - camera.getPosition().z;

		PoseStack poseStack = new PoseStack();
		poseStack.translate(lerpX, lerpY, lerpZ);

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(true);
		RenderSystem.disableCull();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		renderShape(poseStack, camera, t, partialTicks);
		RenderSystem.enableCull();
	}

	protected abstract void renderShape(PoseStack poseStack, Camera camera, double t, float partialTicks);

	// ========== Shared geometry helpers ==========

	// Draws line spikes through center (billboarded)
	protected static void drawCrossQuads(BufferBuilder builder, Matrix4f matrix, int spikes, double size, double thickness, float r, float g, float b, float a) {
		double dTh = Math.PI / spikes;
		for (int i = 0; i < spikes; i++) {
			double theta = i * dTh + Math.PI / 2.0;
			float cx = (float) Math.cos(theta);
			float cy = (float) Math.sin(theta);
			// Perpendicular for thickness
			float nx = (float) (-cy * thickness);
			float ny = (float) (cx * thickness);
			float ex = (float) (cx * size);
			float ey = (float) (cy * size);

			builder.vertex(matrix, ex + nx, ey + ny, 0).color(r, g, b, a).endVertex();
			builder.vertex(matrix, ex - nx, ey - ny, 0).color(r, g, b, a).endVertex();
			builder.vertex(matrix, -ex - nx, -ey - ny, 0).color(r, g, b, a).endVertex();
			builder.vertex(matrix, -ex + nx, -ey + ny, 0).color(r, g, b, a).endVertex();
		}
	}

	// Draws a single 2D line segment as a thin quad (billboarded)
	protected static void drawLineQuad(BufferBuilder builder, Matrix4f matrix, float x1, float y1, float x2, float y2, float thickness, float r, float g, float b, float a) {
		float dx = x2 - x1;
		float dy = y2 - y1;
		float len = (float) Math.sqrt(dx * dx + dy * dy);
		if (len < 0.001f)
			return;
		float nx = -dy / len * thickness;
		float ny = dx / len * thickness;
		builder.vertex(matrix, x1 + nx, y1 + ny, 0).color(r, g, b, a).endVertex();
		builder.vertex(matrix, x1 - nx, y1 - ny, 0).color(r, g, b, a).endVertex();
		builder.vertex(matrix, x2 - nx, y2 - ny, 0).color(r, g, b, a).endVertex();
		builder.vertex(matrix, x2 + nx, y2 + ny, 0).color(r, g, b, a).endVertex();
	}

	// Draws filled polygon as degenerate quads (billboarded), used by fire/ice
	protected static void drawPolySolidQuads(BufferBuilder builder, Matrix4f matrix, int sides, double size, float rotation, float r, float g, float b, float a) {
		double dTh = Math.PI * 2.0 / sides;
		for (int i = 0; i < sides; i++) {
			double th1 = i * dTh + Math.toRadians(rotation);
			double th2 = (i + 1) * dTh + Math.toRadians(rotation);
			builder.vertex(matrix, 0, 0, 0).color(r, g, b, a).endVertex();
			builder.vertex(matrix, (float) (Math.cos(th1) * size), (float) (Math.sin(th1) * size), 0).color(r, g, b, a).endVertex();
			builder.vertex(matrix, (float) (Math.cos(th2) * size), (float) (Math.sin(th2) * size), 0).color(r, g, b, a).endVertex();
			builder.vertex(matrix, (float) (Math.cos(th2) * size), (float) (Math.sin(th2) * size), 0).color(r, g, b, a).endVertex();
		}
	}

	// Draws polygon outline as line segments (billboarded)
	protected static void drawPolyOutlineLines(BufferBuilder builder, Matrix4f matrix, int sides, double size, float r, float g, float b, float a) {
		double dTh = Math.PI * 2.0 / sides;
		for (int i = 0; i < sides; i++) {
			double th1 = i * dTh + Math.PI / 2.0;
			double th2 = (i + 1) * dTh + Math.PI / 2.0;
			builder.vertex(matrix, (float) (Math.cos(th1) * size), (float) (Math.sin(th1) * size), 0).color(r, g, b, a).normal(0, 0, 1).endVertex();
			builder.vertex(matrix, (float) (Math.cos(th2) * size), (float) (Math.sin(th2) * size), 0).color(r, g, b, a).normal(0, 0, 1).endVertex();
		}
	}

	protected static void drawPolyOutlineQuads(BufferBuilder builder, Matrix4f matrix, int sides, double size, double thickness, float r, float g, float b, float a) {
		double dTh = Math.PI * 2.0 / sides;
		for (int i = 0; i < sides; i++) {
			double th1 = i * dTh + Math.PI / 2.0;
			double th2 = (i + 1) * dTh + Math.PI / 2.0;
			float x1 = (float) (Math.cos(th1) * size);
			float y1 = (float) (Math.sin(th1) * size);
			float x2 = (float) (Math.cos(th2) * size);
			float y2 = (float) (Math.sin(th2) * size);

			float dx = x2 - x1;
			float dy = y2 - y1;
			float len = (float) Math.sqrt(dx * dx + dy * dy);
			if (len < 0.001f)
				continue;
			float nx = (float) (-dy / len * thickness);
			float ny = (float) (dx / len * thickness);

			builder.vertex(matrix, x1 + nx, y1 + ny, 0).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x1 - nx, y1 - ny, 0).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x2 - nx, y2 - ny, 0).color(r, g, b, a).endVertex();
			builder.vertex(matrix, x2 + nx, y2 + ny, 0).color(r, g, b, a).endVertex();
		}
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