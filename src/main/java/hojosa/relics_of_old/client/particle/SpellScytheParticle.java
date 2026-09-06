package hojosa.relics_of_old.client.particle;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;

public class SpellScytheParticle extends SpellBaseParticle {

	// Offset from spell center in XZ plane
	private final double offsetX, offsetZ;

	public SpellScytheParticle(ClientLevel level, double x, double y, double z, double power, int maxLife, double offsetX, double offsetZ) {
		super(level, x, y, z, power, maxLife, 0);
		this.offsetX = offsetX;
		this.offsetZ = offsetZ;
	}

	@Override
	protected void renderShape(PoseStack poseStack, Camera camera, double t, float partialTicks) {
		double waxwane = Math.sin(t * Math.PI);
		float c = (float) (0.7 + 0.3 * waxwane);
		float r = c;
		float g = c;
		float b = (float) (0.8 + 0.2 * waxwane);

		// Move to Y-axis (center column) and draw horizontal scythe
		double dist = Math.sqrt(offsetX * offsetX + offsetZ * offsetZ);
		double size = dist * 1.2 * waxwane;
		float yaw = (float) (Math.atan2(offsetZ, offsetX) * 180.0 / Math.PI + t * 360.0 * 4.0);

		poseStack.pushPose();
		// Translate to center column (remove radial offset)
		poseStack.translate(-offsetX, 0, -offsetZ);
		poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
		poseStack.scale((float) size, (float) size, (float) size);
		Matrix4f matrix = poseStack.last().pose();

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		builder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

		float phase = (float) (1.0 - waxwane * 0.3);
		// Two triangles forming scythe blade (horizontal XZ plane)
		builder.vertex(matrix, 1.0f, 0, 0).color(r, g, b, 1.0f).endVertex();
		builder.vertex(matrix, 0.7f, 0, 0.7f).color(r, g, b, 1.0f).endVertex();
		builder.vertex(matrix, phase, 0, 0).color(r, g, b, 1.0f).endVertex();

		builder.vertex(matrix, phase, 0, 0).color(r, g, b, 1.0f).endVertex();
		builder.vertex(matrix, 0.7f, 0, -0.7f).color(r, g, b, 1.0f).endVertex();
		builder.vertex(matrix, 1.0f, 0, 0).color(r, g, b, 1.0f).endVertex();

		tesselator.end();
		poseStack.popPose();
	}
}