package hojosa.relics_of_old.client.render;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import hojosa.relics_of_old.common.block.entity.StarwellBlockEntity;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class StarwellBlockRenderer implements BlockEntityRenderer<StarwellBlockEntity> {
	private static final ResourceLocation BEAM_TEX = RelicsUtil.modLoc("textures/entity/beam.png");
	private static final ResourceLocation RIPPLE_TEX = RelicsUtil.modLoc("textures/entity/spikey_spark.png");

	@Override
	public void render(StarwellBlockEntity be, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		float height = be.beamHeight;
		if (height <= 0)
			return;

		boolean skylens = be.isSkylensMode();

		// Calculate time ONCE for the entire frame
		long period = skylens ? 3000L : 30000L;
		float t = (System.currentTimeMillis() % period) / (float) period;

		VertexConsumer vc = buffer.getBuffer(RelicsRenderTypes.additiveBeam(BEAM_TEX, true));

		// 8 orbiting beam quads — phase staggers color/height, t drives orbit
		float phase = t;
		for (int i = 0; i < 8; i++) {
			renderBeamQuad(pose, vc, i, t, phase, height, skylens, be);
			phase += 0.125f;
			if (phase > 1.0f)
				phase -= 1.0f;
		}

		// Skylens extras
		if (skylens) {
			renderSkylensRings(pose, buffer, be, partialTick);
			renderSkylensSparkle(pose, buffer, be, partialTick);
		}
	}

	private void renderBeamQuad(PoseStack pose, VertexConsumer vc, int index, float t, float phase, float height, boolean skylens, StarwellBlockEntity be) {
		// Orbit position — all beams use the same t
		float angle = (float) (2.356194490192345 * index + Math.PI * 2 * t);
		float width = 0.7f;
		float ox = (float) (Math.cos(angle) * 0.5 * width);
		float oz = (float) (Math.sin(angle) * 0.5 * width);

		float r, g, b, a;
		if (skylens) {
			if (be.flightCharge > 0) {
				float flash = 0.5f + 0.5f * (float) Math.sin(phase * Math.PI * 12);
				r = 0.2f + 0.8f * flash;
				g = 0.7f + 0.3f * flash;
				b = 1.0f;
				a = 1.0f - phase;
			} else {
				r = Math.max(0.0f, Math.min(1.0f, 1.2f - phase * 2.0f));
				g = Math.max(0.0f, Math.min(1.0f, 2.0f - phase * 2.0f));
				b = 1.0f;
				a = 0.5f * (1.0f - phase);
			}
		} else {
			r = RelicsUtil.r(phase * 2.0f);
			g = RelicsUtil.g(phase * 2.0f);
			b = RelicsUtil.b(phase * 2.0f);
			a = 0.5f * (1.0f - phase);
		}

		float quadW = skylens ? 3.0f : 2.0f;
		float h = phase * height;

		pose.pushPose();
		pose.translate(0.5f + ox, 0.5f, 0.5f + oz);
		pose.mulPose(Axis.YN.rotationDegrees(Minecraft.getInstance().gameRenderer.getMainCamera().getYRot()));

		PoseStack.Pose last = pose.last();
		Matrix4f m4 = last.pose();
		Matrix3f m3 = last.normal();

		vc.vertex(m4, -quadW, 0, 0).color(r, g, b, a).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		vc.vertex(m4, -quadW, h, 0).color(r, g, b, a).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		vc.vertex(m4, quadW, h, 0).color(r, g, b, a).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		vc.vertex(m4, quadW, 0, 0).color(r, g, b, a).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();

		pose.popPose();
	}

	// Expanding horizontal square rings above the sky lens
	private void renderSkylensRings(PoseStack pose, MultiBufferSource buffer, StarwellBlockEntity be, float partialTick) {
		VertexConsumer vc = buffer.getBuffer(RelicsRenderTypes.SPELL_TRIANGLES);
		float ringPhase = (System.currentTimeMillis() % 500L) / 500.0f;

		pose.pushPose();
		pose.translate(0.5, 0.5, 0.5);

		for (int i = 0; i < 4; i++) {
			float step = 0.25f * i + ringPhase * 0.25f;
			float alpha = (float) (Math.sin(step * Math.PI) * 0.15);
			float c = 1.0f - step;

			PoseStack.Pose last = pose.last();
			Matrix4f m4 = last.pose();
			float s = 1.5f;
			float y = 1.5f + step;

			// One horizontal quad
			vc.vertex(m4, -s, y, -s).color(c, 1.0f - step * 0.5f, 1.0f, alpha).normal(last.normal(), 0, 1, 0).endVertex();
			vc.vertex(m4, s, y, -s).color(c, 1.0f - step * 0.5f, 1.0f, alpha).normal(last.normal(), 0, 1, 0).endVertex();
			vc.vertex(m4, s, y, s).color(c, 1.0f - step * 0.5f, 1.0f, alpha).normal(last.normal(), 0, 1, 0).endVertex();
			vc.vertex(m4, -s, y, s).color(c, 1.0f - step * 0.5f, 1.0f, alpha).normal(last.normal(), 0, 1, 0).endVertex();
		}
		pose.popPose();
	}

	// Spinning sparkle billboard at the beam top
	private void renderSkylensSparkle(PoseStack pose, MultiBufferSource buffer, StarwellBlockEntity be, float partialTick) {
		VertexConsumer vc = buffer.getBuffer(RelicsRenderTypes.additiveBeam(RIPPLE_TEX, true));

		float phase = (System.currentTimeMillis() % 2000L) / 2000.0f;
		float scale = (float) Math.sin(phase * Math.PI * 2) + 5.0f;

		pose.pushPose();
		pose.translate(0.5, be.beamHeight / 1.5 - 2.0, 0.5);
		// Billboard face camera
		pose.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());
		// Spin
		pose.mulPose(Axis.ZP.rotationDegrees(phase * 180.0f));
		pose.scale(scale, scale, scale);

		float r, g, b;
		if (be.flightCharge > 0) {
			float flash = 0.5f + 0.5f * (float) Math.sin(phase * Math.PI * 12);
			r = 0.2f + 0.8f * flash;
			g = 0.7f + 0.3f * flash;
			b = 1.0f;
		} else {
			r = 0.2f;
			g = 0.7f;
			b = 1.0f;
		}

		PoseStack.Pose last = pose.last();
		Matrix4f m4 = last.pose();
		Matrix3f m3 = last.normal();

		vc.vertex(m4, -1, 1, 0).color(r, g, b, 0.5f).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		vc.vertex(m4, 1, 1, 0).color(r, g, b, 0.5f).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		vc.vertex(m4, 1, -1, 0).color(r, g, b, 0.5f).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		vc.vertex(m4, -1, -1, 0).color(r, g, b, 0.5f).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();

		// Back face
		vc.vertex(m4, -1, 1, 0).color(r, g, b, 0.5f).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		vc.vertex(m4, 1, 1, 0).color(r, g, b, 0.5f).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		vc.vertex(m4, 1, -1, 0).color(r, g, b, 0.5f).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		vc.vertex(m4, -1, -1, 0).color(r, g, b, 0.5f).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();

		pose.popPose();
	}

	@Override
	public boolean shouldRender(StarwellBlockEntity be, Vec3 cameraPos) {
		return Vec3.atCenterOf(be.getBlockPos()).multiply(1, 0, 1).closerThan(cameraPos.multiply(1, 0, 1), getViewDistance());
	}

	@Override
	public boolean shouldRenderOffScreen(StarwellBlockEntity be) {
		return true;
	}

	@Override
	public int getViewDistance() {
		return 256;
	}
}