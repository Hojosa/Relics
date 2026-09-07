package hojosa.relics_of_old.client.particle;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;

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

		if (t < 0.5) {
			// Billboarded diamond zig-zag
			poseStack.pushPose();
			poseStack.mulPose(camera.rotation());
			Matrix4f matrix = poseStack.last().pose();

			double phase = Math.sin(t * Math.PI * 4.0);
			float top = (float) ((1.0 - Math.abs(phase)) * s * 2.0);
			float mid = (float) (-phase * s);
			float thick = 0.005f;

			builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
			// top → mid-left
			drawLineQuad(builder, matrix, 0, top, mid, 0, thick, r, g, b, 1.0f);
			// mid-left → mid-right
			drawLineQuad(builder, matrix, mid, 0, -mid, 0, thick, r, g, b, 1.0f);
			// mid-right → bottom
			drawLineQuad(builder, matrix, -mid, 0, 0, -top, thick, r, g, b, 1.0f);
			tesselator.end();

			poseStack.popPose();
		} else {
			// World-space bolt line from center to extended position
			RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
			RenderSystem.lineWidth(3.0f);
			Matrix4f matrix = poseStack.last().pose();
			Matrix3f normal = poseStack.last().normal();
			builder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);

			double phase = t * 2.0 - 1.0;
			double h = Math.sin(phase * Math.PI) * 2.5;

			float x1 = (float) -offsetX;
			float y1 = (float) -offsetY;
			float z1 = (float) -offsetZ;
			float x2 = (float) (offsetX * h - offsetX);
			float y2 = (float) (offsetY * h - offsetY);
			float z2 = (float) (offsetZ * h - offsetZ);

			// Normal perpendicular to the bolt direction
			float dx = x2 - x1;
			float dy = y2 - y1;
			float dz = z2 - z1;
			float len = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
			// Cross with up vector for a perpendicular
			float nx, ny, nz;
			if (len > 0.001f) {
				nx = -dz / len;
				ny = 0;
				nz = dx / len;
			} else {
				nx = 1;
				ny = 0;
				nz = 0;
			}
			builder.vertex(matrix, x1, y1, z1).color(r, g, b, 1.0f).normal(normal, nx, ny, nz).endVertex();
			builder.vertex(matrix, x2, y2, z2).color(r, g, b, 1.0f).normal(normal, nx, ny, nz).endVertex();
			tesselator.end();
		}
	}
}