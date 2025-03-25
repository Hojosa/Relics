package hojosa.relics.client.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import hojosa.relics.common.block.entity.SkybeamBlockEntity;
import hojosa.relics.lib.RelicsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.phys.Vec3;

public class SkybeamBlockRenderer implements BlockEntityRenderer<SkybeamBlockEntity> {

	@Override
	public void render(SkybeamBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
		float power = pBlockEntity.beamStrenght * 1.0f / SkybeamBlockEntity.MAX_BEAM_STRENGHT;
		if (pBlockEntity.beamStrenght > 0) {
			float phase = (System.currentTimeMillis() % 2000L) / 2000.0f;

			for (int num = 0; num < 8; ++num) {
				renderLightBeam(pPoseStack, pBuffer.getBuffer(RelicsRenderTypes.BEAM), num, phase, power);
				phase = phase + 0.125f;
                if (phase > 1.0f) {
                    phase -= 1.0f;
                }
			}
		}
	}

	private static void renderLightBeam(PoseStack matrixStack, VertexConsumer builder, float num, float phase, float power) {
		float r = RelicsUtil.r(phase*1.2f);
		float g = RelicsUtil.g(phase*1.2f);
		float b = RelicsUtil.b(phase*1.2f);
		matrixStack.pushPose();
		Matrix4f matrix4f = matrixStack.last().pose();

		matrixStack.translate(0.5f, 0.5f, 0.5f);
		matrixStack.translate(Math.cos(2.356194490192345 * num) * 0.5f * power, 1, Math.sin(2.356194490192345 * num) * 0.5f * power);
		matrixStack.mulPose(Axis.YN.rotationDegrees(Minecraft.getInstance().gameRenderer.getMainCamera().getYRot()));
		
		builder.vertex(matrix4f, -1.5f, -1f, 0).color(r, g, b, 0.8f * (1.0f - phase)).uv(0, 1).overlayCoords(0).uv2(251662080).normal(2.0f, 2.0f, 2.0f).endVertex();
		builder.vertex(matrix4f, -1.5f, 30 * power*(phase*1.8f), 0).color(r, g, b, 0.8f * (1.0f - phase)).uv(0, 0).overlayCoords(0).uv2(251662080).normal(2.0f, 2.0f, 2.0f).endVertex();
		builder.vertex(matrix4f, 1.5f, 30 * power*(phase*1.8f), 0).color(r, g, b, 0.8f * (1.0f - phase)).uv(1, 0).overlayCoords(0).uv2(251662080).normal(2.0f, 2.0f, 2.0f).endVertex();
		builder.vertex(matrix4f, 1.5f, -1f, 0).color(r, g, b, 0.8f * (1.0f - phase)).uv(1, 1).overlayCoords(0).uv2(251662080).normal(2.0f, 2.0f, 2.0f).endVertex();
		matrixStack.popPose();
	}
	
	@Override
	public boolean shouldRender(SkybeamBlockEntity pBlockEntity, Vec3 pCameraPos) {
		return Vec3.atCenterOf(pBlockEntity.getBlockPos()).multiply(1.0D, 0.0D, 1.0D).closerThan(pCameraPos.multiply(1.0D, 0.0D, 1.0D), this.getViewDistance());
	}
	
	@Override
	public boolean shouldRenderOffScreen(SkybeamBlockEntity pBlockEntity) {
		return true;
	}
	
	@Override
	public int getViewDistance() {
		return 256;
	}
}
