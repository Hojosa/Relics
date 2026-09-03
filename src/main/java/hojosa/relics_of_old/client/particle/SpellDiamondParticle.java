package hojosa.relics_of_old.client.particle;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;

public class SpellDiamondParticle extends SpellBaseParticle {

	private static final double DIAMOND_SIZE = 0.25;

	public SpellDiamondParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, int maxLife, int hibernateTime) {
		super(level, x, y, z, 0, maxLife, hibernateTime);
		this.xd = vx;
		this.yd = vy;
		this.zd = vz;
	}

	@Override
	protected void renderShape(PoseStack poseStack, Camera camera, double t, float partialTicks) {
		// Color: warm white → gold
		float r = 1.0f;
		float g = Math.min(1.0f, (float) (1.5 - t));
		float b = Math.min(1.0f, (float) (0.5 + t));

		// Diamond pinch: X shrinks, Y stretches over lifetime
		float sy = (float) (t * t * DIAMOND_SIZE);
		float sx = (float) ((1.0 - t) * (1.0 - t) * DIAMOND_SIZE);

		poseStack.pushPose();
		poseStack.mulPose(camera.rotation());
		Matrix4f matrix = poseStack.last().pose();

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
		poseStack.popPose();
	}
}