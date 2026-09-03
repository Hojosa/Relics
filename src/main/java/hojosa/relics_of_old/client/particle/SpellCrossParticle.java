package hojosa.relics_of_old.client.particle;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;

public class SpellCrossParticle extends SpellBaseParticle {

	private final int spikes;

	public SpellCrossParticle(ClientLevel level, double x, double y, double z, double power, int maxLife, int hibernateTime, int spikes) {
		super(level, x, y, z, power, maxLife, hibernateTime);
		this.spikes = spikes;
	}

	@Override
	protected void renderShape(PoseStack poseStack, Camera camera, double t, float partialTicks) {
		// Warm white → gold
		float r = 1.0f;
		float g = Math.min(1.0f, (float) (1.5 - t));
		float b = Math.min(1.0f, (float) (0.5 + t));
		double size = Math.sin(t * Math.PI) * (power - 1.0) / 4.0;

		poseStack.pushPose();
		poseStack.mulPose(camera.rotation());
		Matrix4f matrix = poseStack.last().pose();

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		builder.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
		drawCrossLines(builder, matrix, spikes, size, r, g, b, 1.0f);
		tesselator.end();

		poseStack.popPose();
	}
}