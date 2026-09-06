package hojosa.relics_of_old.client.particle;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;

public class SpellRayfireParticle extends SpellBaseParticle {

	private static final double DIAMOND_SIZE = 1.5;

	public SpellRayfireParticle(ClientLevel level, double x, double y, double z, double power, int maxLife, int hibernateTime) {
		super(level, x, y, z, power, maxLife, hibernateTime);
	}

	@Override
	protected void renderShape(PoseStack poseStack, Camera camera, double t, float partialTicks) {
		// White-yellow → purple
		float r = 1.0f;
		float g = Math.min(1.0f, (float) (1.5 - t * 1.5));
		float b = Math.min(1.0f, (float) (t * 1.5));

		// Diamond pinch, scaled 8x tall
		float sy = (float) (t * t * DIAMOND_SIZE);
		float sx = (float) ((1.0 - t) * (1.0 - t) * DIAMOND_SIZE);
		// Apply 8x Y stretch
		sy *= 8.0f;

		poseStack.pushPose();
		poseStack.mulPose(camera.rotation());
		Matrix4f matrix = poseStack.last().pose();

		// Additive blending
		RenderSystem.blendFunc(com.mojang.blaze3d.platform.GlStateManager.SourceFactor.SRC_ALPHA, com.mojang.blaze3d.platform.GlStateManager.DestFactor.ONE);

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		// Top half
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

		// Restore default blending
		RenderSystem.defaultBlendFunc();
		poseStack.popPose();
	}
}