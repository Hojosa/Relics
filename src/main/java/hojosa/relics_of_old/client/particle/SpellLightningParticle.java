package hojosa.relics_of_old.client.particle;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;

//Two-phase lightning particle: diamond zig-zag → bolt line (LIGHTNING)
public class SpellLightningParticle extends SpellBaseParticle {

	private final double uniqueness;
	// Offset from spell center for bolt line phase
	private final double offsetX, offsetY, offsetZ;

	public SpellLightningParticle(ClientLevel level, double x, double y, double z, double power, int maxLife, int hibernateTime, double uniqueness, double offsetX, double offsetY, double offsetZ) {
		super(level, x, y, z, power, maxLife, hibernateTime);
		this.uniqueness = uniqueness;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
	}

	@Override
	protected void renderShape(PoseStack poseStack, Camera camera, double t, float partialTicks) {
		// Flickering cyan-green
		double f = Math.sin((t + uniqueness) * 30.0) * 0.5 + 0.5;
		float r = Math.min(1.0f, (float) (f * 2.0));
		float g = 1.0f;
		float b = Math.min(1.0f, (float) (2.0 - f * 2.0));
		double s = power * 0.15;

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		builder.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

		if (t < 0.5) {
			// Billboarded diamond zig-zag
			poseStack.pushPose();
			poseStack.mulPose(camera.rotation());
			Matrix4f matrix = poseStack.last().pose();

			double phase = Math.sin(t * Math.PI * 4.0);
			float top = (float) ((1.0 - Math.abs(phase)) * s * 2.0);
			float mid = (float) (-phase * s);

			// LINE_STRIP equivalent: top → mid-left → mid-right → bottom
			builder.vertex(matrix, 0, top, 0).color(r, g, b, 1.0f).endVertex();
			builder.vertex(matrix, mid, 0, 0).color(r, g, b, 1.0f).endVertex();
			builder.vertex(matrix, mid, 0, 0).color(r, g, b, 1.0f).endVertex();
			builder.vertex(matrix, -mid, 0, 0).color(r, g, b, 1.0f).endVertex();
			builder.vertex(matrix, -mid, 0, 0).color(r, g, b, 1.0f).endVertex();
			builder.vertex(matrix, 0, -top, 0).color(r, g, b, 1.0f).endVertex();

			poseStack.popPose();
		} else {
			// World-space bolt line from center to extended position
			Matrix4f matrix = poseStack.last().pose();
			double phase = t * 2.0 - 1.0;
			double h = Math.sin(phase * Math.PI) * 2.5;

			builder.vertex(matrix, (float) -offsetX, (float) -offsetY, (float) -offsetZ).color(r, g, b, 1.0f).endVertex();
			builder.vertex(matrix, (float) (offsetX * h - offsetX), (float) (offsetY * h - offsetY), (float) (offsetZ * h - offsetZ)).color(r, g, b, 1.0f).endVertex();
		}

		tesselator.end();
	}
}
