package hojosa.relics_of_old.client.particle;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;

public class SpellIceParticle extends SpellBaseParticle {

	public SpellIceParticle(ClientLevel level, double x, double y, double z, double power, int maxLife, int hibernateTime) {
		super(level, x, y, z, power, maxLife, hibernateTime);
	}

	@Override
	protected void renderShape(PoseStack poseStack, Camera camera, double t, float partialTicks) {
		// Blue-white fading
		float fade = (float) (1.0 - Math.sin(t * Math.PI));
		float r = 0.5f + fade * 0.5f;
		float g = 0.5f * fade + 0.5f;
		float b = 1.0f;
		double size = power / 7.0;

		poseStack.pushPose();
		poseStack.mulPose(camera.rotation());
		Matrix4f matrix = poseStack.last().pose();

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();

		if (t < 0.5) {
			// Shrinking cross
			builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
			drawCrossQuads(builder, matrix, 3, size * (1.0 - t * 2.0) * 2.0, 0.007, r, g, b, 1.0f);
			tesselator.end();
		} else if (t < 0.75) {
			// Growing solid hexagon
			builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
			drawPolySolidQuads(builder, poseStack.last().pose(), 6, size * (t - 0.5) * 5.0, 90f, r, g, b, 1.0f);
			tesselator.end();
		} else {
			// Static hexagon outline
			builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
			drawPolyOutlineQuads(builder, matrix, 6, size, 0.007, r, g, b, 1.0f);
			tesselator.end();
		}
		poseStack.popPose();
	}
}