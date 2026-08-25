package hojosa.relics_of_old.client.render;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import hojosa.relics_of_old.common.block.entity.RitualLocusBlockEntity;
import hojosa.relics_of_old.common.ritual.Edge;
import hojosa.relics_of_old.common.ritual.RitualGrid;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class RitualLocusBlockRenderer implements BlockEntityRenderer<RitualLocusBlockEntity> {
	private static final ResourceLocation BEAM_TEX = RelicsUtil.modLoc("textures/entity/beam.png");
	private static final ResourceLocation SPARK_TEX = RelicsUtil.modLoc("textures/entity/spikey_spark.png");
	private static final ResourceLocation RIPPLE_TEX = RelicsUtil.modLoc("textures/entity/add_ripple.png");
	private static final ResourceLocation RAINBOW_TEX = RelicsUtil.modLoc("textures/entity/rainbow_fade.png");

	private static final int PULSE_DURATION = 10;

	private float clerp(float input, float scale) {
		float t = input / scale;
		return Math.max(0, Math.min(t, 1));
	}

	@Override
	public void render(RitualLocusBlockEntity be, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		if (!be.active || be.grid == null)
			return;

		RitualGrid grid = be.grid;
		float wakeupFade = clerp(be.awakeTicks + partialTick, 20.0f);

		// Moon brightness factor
		float darkness = 1.0f - be.getLevel().getMoonBrightness();
		float moonAngle = (float) (-Math.cos(be.getLevel().getSunAngle(0)));
		float moonlight = clerp(moonAngle - darkness, 1.0f);

		pose.pushPose();
		pose.translate(0.5, 0.5, 0.5);

		// Stable grid: pulsing rainbow box
		if (grid.isGridStable(be.getLevel())) {
			renderRainbowBoxes(pose, buffer, partialTick);
		}
		// Success effect: expanding rainbow
		if (be.successGoing) {
			float phase = (be.successEffectTimer + partialTick) / (float) RitualLocusBlockEntity.SUCCESS_EFFECT_DURATION;
			float alpha = (float) Math.min(1.5 - phase * 1.5, 1.0);
			renderRainbowBox(pose, buffer, 0, 0.5, 0, 0.5625, Math.min(phase * 32, 16), alpha);
			renderRainbowBox(pose, buffer, 0, 0.5, 0, 0.5625 + (1 - phase) * (1 - phase), 8 * phase * phase, alpha);
		}
		// Edge beams connecting filled points
		if (moonlight > 0) {
			renderEdges(pose, buffer, be, grid, moonlight, wakeupFade, partialTick);
		}

		// Orbs and ripples at each grid point
		if (!be.dirty) {
			renderGridPoints(pose, buffer, be, grid, moonlight, wakeupFade, partialTick);
		}

		pose.popPose();
	}

	private void renderRainbowBoxes(PoseStack pose, MultiBufferSource buffer, float partialTick) {
		float phase1 = (System.currentTimeMillis() % 2000L) / 2000.0f;
		float phase2 = ((1000L + System.currentTimeMillis()) % 2000L) / 2000.0f;
		float fade1 = (float) Math.sin(phase1 * Math.PI) * 0.5f;
		float fade2 = (float) Math.sin(phase2 * Math.PI) * 0.5f;

		renderRainbowBox(pose, buffer, 0, 0, 0, 0.75 - 0.25 * phase1, 1 + phase1, fade1);
		renderRainbowBox(pose, buffer, 0, 0, 0, 0.75 - 0.25 * phase2, 1 + phase2, fade2);
	}

	// Renders a textured column (4 sides) with rainbow scrolling UVs
	private void renderRainbowBox(PoseStack pose, MultiBufferSource buffer, double x, double y, double z, double radius, double height, float alpha) {
		VertexConsumer vc = buffer.getBuffer(RelicsRenderTypes.additiveBeam(RAINBOW_TEX, true));
		float uShift = 1.0f - (System.currentTimeMillis() % 1000L) / 1000.0f;

		pose.pushPose();
		pose.translate(x, y, z);
		pose.scale((float) radius, (float) height, (float) radius);

		PoseStack.Pose last = pose.last();
		Matrix4f m4 = last.pose();
		Matrix3f m3 = last.normal();

		for (int i = 0; i < 4; i++) {
			vc.vertex(m4, -1, 1, -1).color(1f, 1f, 1f, alpha).uv(uShift, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, -1, 0, 0).endVertex();
			vc.vertex(m4, -1, 1, 1).color(1f, 1f, 1f, alpha).uv(1 + uShift, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, -1, 0, 0).endVertex();
			vc.vertex(m4, -1, 0, 1).color(1f, 1f, 1f, alpha).uv(1 + uShift, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, -1, 0, 0).endVertex();
			vc.vertex(m4, -1, 0, -1).color(1f, 1f, 1f, alpha).uv(uShift, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, -1, 0, 0).endVertex();
			pose.mulPose(Axis.YP.rotationDegrees(90));
			last = pose.last();
			m4 = last.pose();
			m3 = last.normal();
		}
		pose.popPose();
	}

	// Beam lines connecting edge points
	private void renderEdges(PoseStack pose, MultiBufferSource buffer, RitualLocusBlockEntity be, RitualGrid grid, float moonlight, float wakeupFade, float partialTick) {
		VertexConsumer vc = buffer.getBuffer(RelicsRenderTypes.additiveBeam(BEAM_TEX, true));
		float beamWidth = 0.25f;
		float sink = -0.45f;

		for (Edge edge : grid.edges) {
			BlockPos p1 = grid.places[edge.first];
			BlockPos p2 = grid.places[edge.second];

			double bx = p1.getX() - be.getBlockPos().getX();
			double by = p1.getY() - be.getBlockPos().getY() + sink;
			double bz = p1.getZ() - be.getBlockPos().getZ();
			double ex = p2.getX() - be.getBlockPos().getX();
			double ey = p2.getY() - be.getBlockPos().getY() + sink;
			double ez = p2.getZ() - be.getBlockPos().getZ();

			float v1 = !be.getLevel().getBlockState(p1).isAir() ? clerp(be.ticksSinceEmpty[edge.first], 10) : 0.01f;
			float v2 = !be.getLevel().getBlockState(p2).isAir() ? clerp(be.ticksSinceEmpty[edge.second], 10) * 0.99f : 0.01f;

			// Cross product with Y-up for sideways offset
			double dx = ex - bx, dz = ez - bz;
			double len = Math.sqrt(dx * dx + dz * dz);
			double sx = (-dz / len) * beamWidth;
			double sz = (dx / len) * beamWidth;

			float alpha = moonlight * wakeupFade;

			PoseStack.Pose last = pose.last();
			Matrix4f m4 = last.pose();
			Matrix3f m3 = last.normal();

			vc.vertex(m4, (float) (bx - sx), (float) by, (float) (bz - sz)).color(0.85f, 0.9f, 1f, alpha).uv(0, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
			vc.vertex(m4, (float) (bx + sx), (float) by, (float) (bz + sz)).color(0.85f, 0.9f, 1f, alpha).uv(1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
			vc.vertex(m4, (float) (ex + sx), (float) ey, (float) (ez + sz)).color(0.85f, 0.9f, 1f, alpha).uv(1, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
			vc.vertex(m4, (float) (ex - sx), (float) ey, (float) (ez - sz)).color(0.85f, 0.9f, 1f, alpha).uv(0, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		}
	}

	// Orbs and ripples at each of the 8 grid positions
	private void renderGridPoints(PoseStack pose, MultiBufferSource buffer, RitualLocusBlockEntity be, RitualGrid grid, float moonlight, float wakeupFade, float partialTick) {
		float phase1 = 1.0f - (System.currentTimeMillis() % 2000L) / 2000.0f;
		float phase2 = 1.0f - ((1000L + System.currentTimeMillis()) % 2000L) / 2000.0f;
		float fade1 = (float) Math.sin(phase1 * Math.PI) * 0.5f;
		float fade2 = (float) Math.sin(phase2 * Math.PI) * 0.5f;
		float sink = -0.45f;

		for (int i = 0; i < 8; i++) {
			double dx = grid.places[i].getX() - be.getBlockPos().getX();
			double dy = grid.places[i].getY() - be.getBlockPos().getY();
			double dz = grid.places[i].getZ() - be.getBlockPos().getZ();

			// Sparkle orb
			if (moonlight > 0) {
				float orbScale = 0.8f + (float) Math.sin(phase1 * Math.PI * 2) * 0.1f;
				drawSparkleOrb(pose, buffer, dx, dy + sink, dz, orbScale, 0.8f, 0.8f, 1.0f, 0.75f * moonlight * wakeupFade);
			}

			// Two staggered ripples
			drawRipple(pose, buffer, dx, dy, dz, 1.0f * phase1 + 0.3f, 0.9f, 0.7f, 1.0f, fade1 * wakeupFade);
			drawRipple(pose, buffer, dx, dy, dz, 1.0f * phase2 + 0.3f, 0.7f, 0.9f, 1.0f, fade2 * wakeupFade);

			// Pulse from placement
			long now = be.getLevel().getGameTime();
			if (be.pulseStartTime[i] > now - PULSE_DURATION && be.pulseStartTime[i] <= now) {
				float diff = (now - be.pulseStartTime[i]) + partialTick;
				float pulsePhase = clerp(diff, PULSE_DURATION);
				drawRipple(pose, buffer, dx, dy, dz, pulsePhase * 3.0f, 0.3f, 0.4f, 0.5f, 1.0f - pulsePhase);
			}
		}
	}

	// Billboard sparkle at a position
	private void drawSparkleOrb(PoseStack pose, MultiBufferSource buffer, double x, double y, double z, float scale, float r, float g, float b, float a) {
		VertexConsumer vc = buffer.getBuffer(RelicsRenderTypes.additiveBeam(SPARK_TEX, true));

		pose.pushPose();
		pose.translate(x, y, z);

		// 3 cross-planes for the sparkle
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

	// Horizontal expanding ring at a grid point
	private void drawRipple(PoseStack pose, MultiBufferSource buffer, double x, double y, double z, float scale, float r, float g, float b, float a) {
		VertexConsumer vc = buffer.getBuffer(RelicsRenderTypes.additiveBeam(RIPPLE_TEX, true));
		pose.pushPose();
		pose.translate(x, y - 0.485, z);
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

	@Override
	public boolean shouldRenderOffScreen(RitualLocusBlockEntity be) {
		return true;
	}

	@Override
	public int getViewDistance() {
		return 128;
	}
}