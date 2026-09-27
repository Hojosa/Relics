package hojosa.relics_of_old.client.render;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import hojosa.relics_of_old.common.block.entity.InfusionLocusBlockEntity;
import hojosa.relics_of_old.common.ritual.Edge;
import hojosa.relics_of_old.common.ritual.RitualGrid;
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

public class InfusionLocusBlockRenderer implements BlockEntityRenderer<InfusionLocusBlockEntity> {
	private static final ResourceLocation BEAM_TEX = RelicsUtil.modLoc("textures/entity/beam.png");
	private static final ResourceLocation RINGFLARE_TEX = RelicsUtil.modLoc("textures/entity/ringflare.png");

	public InfusionLocusBlockRenderer(Context context) {
	}

	@Override
	public void render(InfusionLocusBlockEntity be, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		if (!be.active || be.grid == null)
			return;

		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		RitualGrid grid = be.grid;

		// Render target item on the locus
		ItemStack targetItem = be.getTargetItem();
		if (!targetItem.isEmpty() && !be.isCrafting()) {
			pose.pushPose();
			pose.translate(0.5, 1.05, 0.5);
			pose.scale(0.5f, 0.5f, 0.5f);
			pose.mulPose(Axis.YP.rotationDegrees(-be.itemRotation));
			pose.mulPose(Axis.XP.rotationDegrees(90));
			itemRenderer.renderStatic(targetItem, ItemDisplayContext.FIXED, 200, packedOverlay, pose, buffer, be.getLevel(), 1);
			pose.popPose();
		}
		if (be.isCrafting()) {
			float floatProgress = Math.min((float) be.getProgress() / InfusionLocusBlockEntity.FLOAT_DURATION, 1.0f);
			float y = 1.05f + floatProgress * 1.0f;

            drawRingFlare(pose, buffer, 0.5, y, 0.5, 1.2f, 1.0f, 0.95f, 0.7f, 1.0f);

			if (!targetItem.isEmpty()) {
				pose.pushPose();
				pose.translate(0.5, y, 0.5);

				// Slow rotation
				float angle = (System.currentTimeMillis() % 4000L) / 4000.0f * 360.0f;
				pose.mulPose(Axis.YP.rotationDegrees(angle));

				// Flip from lying flat (90°) to upright (0°) during float-up
				float tiltAngle = 90.0f * (1.0f - floatProgress);
				pose.mulPose(Axis.XP.rotationDegrees(tiltAngle));

				pose.scale(0.5f, 0.5f, 0.5f);
				itemRenderer.renderStatic(targetItem, ItemDisplayContext.FIXED, 200, packedOverlay, pose, buffer, be.getLevel(), 1);
				pose.popPose();
			}
		}

		// Render edge beams connecting occupied helper points
		if (!grid.edges.isEmpty()) {
			renderEdges(pose, buffer, be, grid, partialTick);
		}
	}

	// Beam lines connecting edge points (same pattern as RitualLocusBlockRenderer)
	private void renderEdges(PoseStack pose, MultiBufferSource buffer, InfusionLocusBlockEntity be, RitualGrid grid, float partialTick) {
		VertexConsumer vc = buffer.getBuffer(RelicsRenderTypes.additiveBeam(BEAM_TEX, true));
		float beamWidth = 0.15f;
		float wakeupFade = clerp(be.awakeTicks + partialTick, 20.0f);

		// Moon brightness factor
		float darkness = 1.0f - be.getLevel().getMoonBrightness();
		float moonAngle = (float) (-Math.cos(be.getLevel().getSunAngle(0)));
		float moonlight = clerp(moonAngle - darkness, 1.0f);

		if (moonlight <= 0)
			return;

		pose.pushPose();
		pose.translate(0.5, 0.5, 0.5);

		for (Edge edge : grid.edges) {
			BlockPos p1 = grid.places[edge.first];
			BlockPos p2 = grid.places[edge.second];

			double bx = p1.getX() - be.getBlockPos().getX();
			double by = p1.getY() - be.getBlockPos().getY() - 0.45;
			double bz = p1.getZ() - be.getBlockPos().getZ();
			double ex = p2.getX() - be.getBlockPos().getX();
			double ey = p2.getY() - be.getBlockPos().getY() - 0.45;
			double ez = p2.getZ() - be.getBlockPos().getZ();

			// Fade-in per endpoint — beam draws from each end over 10 ticks
			float v1 = clerp(be.ticksSinceOccupied[edge.first], 10);
			float v2 = clerp(be.ticksSinceOccupied[edge.second], 10) * 0.99f;

			// Cross product with Y-up for sideways offset
			double dx = ex - bx, dz = ez - bz;
			double len = Math.sqrt(dx * dx + dz * dz);
			if (len < 0.001)
				continue;
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
		pose.popPose();
	}

	private float clerp(float input, float scale) {
		float t = input / scale;
		return Math.max(0, Math.min(t, 1));
	}

	// Camera-facing ring flare — always billboarded toward the player
	private void drawRingFlare(PoseStack pose, MultiBufferSource buffer, double x, double y, double z, float scale, float r, float g, float b, float a) {
		VertexConsumer vc = buffer.getBuffer(RelicsRenderTypes.additiveBeam(RINGFLARE_TEX, true));

		pose.pushPose();
		pose.translate(x, y, z);
		pose.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().camera.rotation());
		pose.scale(scale, scale, scale);
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

	@Override
	public boolean shouldRenderOffScreen(InfusionLocusBlockEntity be) {
		return true;
	}

	@Override
	public int getViewDistance() {
		return 128;
	}
}