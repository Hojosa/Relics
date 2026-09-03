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

public class SpellCritRingParticle extends Particle {

	private final double radius;
	private static final int SEGMENTS = 8;
	private static final int DURATION = 10;

	public SpellCritRingParticle(ClientLevel level, double x, double y, double z, double radius) {
		super(level, x, y, z);
		this.radius = radius;
		this.lifetime = DURATION;
		this.age = 0;
		this.hasPhysics = false;
		this.gravity = 0;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		this.age++;
		if (this.age >= this.lifetime) {
			this.remove();
		}
	}

	@Override
	public void render(VertexConsumer pBuffer, Camera camera, float partialTicks) {
		float phase = ((float) this.age + partialTicks) / (float) DURATION;
		if (phase >= 1.0f)
			return;

		double lerpX = this.x - camera.getPosition().x;
		double lerpY = this.y - camera.getPosition().y;
		double lerpZ = this.z - camera.getPosition().z;

		float alpha = 1.0f - phase;
		float yOffset = phase * 0.3f;

		PoseStack poseStack = new PoseStack();
		poseStack.translate(lerpX, lerpY, lerpZ);
		Matrix4f matrix = poseStack.last().pose();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(true);
		RenderSystem.disableCull();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		builder.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

		// Upper ring
		drawHorizontalRing(builder, matrix, yOffset, alpha);
		// Lower ring
		drawHorizontalRing(builder, matrix, -yOffset, alpha);

		tesselator.end();
		RenderSystem.enableCull();
	}

	private void drawHorizontalRing(BufferBuilder builder, Matrix4f matrix, float cy, float alpha) {
		double dTh = Math.PI * 2.0 / SEGMENTS;
		for (int i = 0; i < SEGMENTS; i++) {
			double th1 = i * dTh;
			double th2 = (i + 1) * dTh;
			float x1 = (float) (Math.cos(th1) * radius);
			float z1 = (float) (Math.sin(th1) * radius);
			float x2 = (float) (Math.cos(th2) * radius);
			float z2 = (float) (Math.sin(th2) * radius);
			builder.vertex(matrix, x1, cy, z1).color(1.0f, 1.0f, 1.0f, alpha).endVertex();
			builder.vertex(matrix, x2, cy, z2).color(1.0f, 1.0f, 1.0f, alpha).endVertex();
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