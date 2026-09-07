package hojosa.relics_of_old.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;

public class SpellExitParticle extends SpellBaseParticle {

	public SpellExitParticle(ClientLevel level, double x, double y, double z, double power, int maxLife, int hibernateTime) {
		super(level, x, y, z, power, maxLife, hibernateTime);
	}

	@Override
	protected void renderShape(PoseStack poseStack, Camera camera, double t, float partialTicks) {
		// White → blue-green
		float r = Math.min(1.0f, (float) (1.5 - t * 1.5));
		float g = Math.min(1.0f, (float) (t * 1.5));
		float b = 1.0f;
		double size = Math.sin(t * Math.PI) * 5.0;

		poseStack.pushPose();
		poseStack.mulPose(camera.rotation());

		RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
		RenderSystem.lineWidth(5.0f);

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		builder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
		drawPolyOutlineLines(builder, poseStack.last().pose(), 4, size, r, g, b, 1.0f);
		tesselator.end();

		poseStack.popPose();
	}
}