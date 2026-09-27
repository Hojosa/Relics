package hojosa.relics_of_old.client.render;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import hojosa.relics_of_old.common.block.entity.InfusionItemHolderBlockEntity;
import hojosa.relics_of_old.common.block.entity.InfusionLocusBlockEntity;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class InfusionItemHolderBlockRenderer implements BlockEntityRenderer<InfusionItemHolderBlockEntity> {
	private static final ResourceLocation SPARK_TEX = RelicsUtil.modLoc("textures/entity/spikey_spark.png");
	private static final ResourceLocation RIPPLE_TEX = RelicsUtil.modLoc("textures/entity/add_ripple.png");
	private static final int PULSE_DURATION = 10;

	public InfusionItemHolderBlockRenderer(Context context) {}

	private float clerp(float input, float scale) {
		float t = input / scale;
		return Math.max(0, Math.min(t, 1));
	}

	@Override
	public void render(InfusionItemHolderBlockEntity be, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		if (be.getLevel() == null || be.getLocusPos() == null)
			return;

		// Get locus for shared state (awakeTicks, pulseStartTime)
		InfusionLocusBlockEntity locus = null;
		if (be.getLevel().getBlockEntity(be.getLocusPos()) instanceof InfusionLocusBlockEntity l) {
			locus = l;
		}
		if (locus == null || !locus.active)
			return;

		float wakeupFade = clerp(locus.awakeTicks + partialTick, 20.0f);

		// Moon brightness factor
		float darkness = 1.0f - be.getLevel().getMoonBrightness();
		float moonAngle = (float) (-Math.cos(be.getLevel().getSunAngle(0)));
		float moonlight = clerp(moonAngle - darkness, 1.0f);

		float phase1 = 1.0f - (System.currentTimeMillis() % 2000L) / 2000.0f;
		float phase2 = 1.0f - ((1000L + System.currentTimeMillis()) % 2000L) / 2000.0f;
		float fade1 = (float) Math.sin(phase1 * Math.PI) * 0.5f;
		float fade2 = (float) Math.sin(phase2 * Math.PI) * 0.5f;

		// Sparkle orb
		if (moonlight > 0) {
			float orbScale = 0.8f + (float) Math.sin(phase1 * Math.PI * 2) * 0.1f;
			drawSparkleOrb(pose, buffer, 0.5, 0.01, 0.5, orbScale, 0.8f, 0.8f, 1.0f, 0.75f * moonlight * wakeupFade);
		}
		// Two staggered ripples
		drawRipple(pose, buffer, 0.5, 0.0, 0.5, 1.0f * phase1 + 0.3f, 0.9f, 0.7f, 1.0f, fade1 * wakeupFade);
		drawRipple(pose, buffer, 0.5, 0.0, 0.5, 1.0f * phase2 + 0.3f, 0.7f, 0.9f, 1.0f, fade2 * wakeupFade);
		// Pulse from placement
		if (locus != null) {
			int pointIndex = findPointIndex(be, locus);
			if (pointIndex >= 0) {
				long now = be.getLevel().getGameTime();
				if (locus.pulseStartTime[pointIndex] > now - PULSE_DURATION && locus.pulseStartTime[pointIndex] <= now) {
					float diff = (now - locus.pulseStartTime[pointIndex]) + partialTick;
					float pulsePhase = clerp(diff, PULSE_DURATION);
					drawRipple(pose, buffer, 0.5, 0.0, 0.5, pulsePhase * 3.0f, 0.3f, 0.4f, 0.5f, 1.0f - pulsePhase);
				}
			}
		}
		// Render held item floating slightly above ground
		ItemStack stack = be.getItem(0);
		if (!stack.isEmpty()) {
			ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
			pose.pushPose();
			pose.translate(0.5, 0.05, 0.5);
			pose.scale(0.4f, 0.4f, 0.4f);
			pose.mulPose(Axis.YP.rotationDegrees(-be.itemRotation));
			pose.mulPose(Axis.XP.rotationDegrees(90));
			itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 200, packedOverlay, pose, buffer, be.getLevel(), 1);
			pose.popPose();
		}
	}

	// Find which grid point index this helper occupies
	private int findPointIndex(InfusionItemHolderBlockEntity be, InfusionLocusBlockEntity locus) {
		if (locus.grid == null)
			return -1;
		BlockPos pos = be.getBlockPos();
		for (int i = 0; i < 8; i++) {
			if (locus.grid.places[i].equals(pos))
				return i;
		}
		return -1;
	}

	// Billboard sparkle — 3 cross-planes
	private void drawSparkleOrb(PoseStack pose, MultiBufferSource buffer, double x, double y, double z, float scale, float r, float g, float b, float a) {
		VertexConsumer vc = buffer.getBuffer(RelicsRenderTypes.additiveBeam(SPARK_TEX, true));

		pose.pushPose();
		pose.translate(x, y, z);

		float[] angles = { (float) (System.currentTimeMillis() * 0.05), (float) (System.currentTimeMillis() * -0.0072 + z), (float) (System.currentTimeMillis() * 0.0113 + x * 2.7) };
		float[] scales = { scale, scale * 0.9f, scale * 0.8f };

		for (int j = 0; j < 3; j++) {
			pose.pushPose();
			pose.scale(scales[j], scales[j], scales[j]);
			pose.mulPose(Axis.XP.rotationDegrees(90));
			pose.mulPose(Axis.ZP.rotationDegrees(angles[j]));
			pose.translate(-0.5, -0.5, 0);

			PoseStack.Pose last = pose.last();
			Matrix4f m4 = last.pose();
			Matrix3f m3 = last.normal();

			vc.vertex(m4, 0, 0, 0).color(r, g, b, a).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
			vc.vertex(m4, 1, 0, 0).color(r, g, b, a).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
			vc.vertex(m4, 1, 1, 0).color(r, g, b, a).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
			vc.vertex(m4, 0, 1, 0).color(r, g, b, a).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
			pose.popPose();
		}
		pose.popPose();
	}

	// Horizontal expanding ring
	private void drawRipple(PoseStack pose, MultiBufferSource buffer, double x, double y, double z, float scale, float r, float g, float b, float a) {
		VertexConsumer vc = buffer.getBuffer(RelicsRenderTypes.additiveBeam(RIPPLE_TEX, true));
		pose.pushPose();
		pose.translate(x, y + 0.015, z);
		pose.scale(scale, scale, scale);

		PoseStack.Pose last = pose.last();
		Matrix4f m4 = last.pose();
		Matrix3f m3 = last.normal();

		vc.vertex(m4, -1, 0, -1).color(r, g, b, a).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		vc.vertex(m4, 1, 0, -1).color(r, g, b, a).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		vc.vertex(m4, 1, 0, 1).color(r, g, b, a).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		vc.vertex(m4, -1, 0, 1).color(r, g, b, a).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();

		pose.popPose();
	}
}