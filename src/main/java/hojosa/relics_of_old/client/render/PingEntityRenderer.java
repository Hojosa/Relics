package hojosa.relics_of_old.client.render;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import hojosa.relics_of_old.common.entity.PingEntity;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class PingEntityRenderer extends EntityRenderer<PingEntity> {
	private static final ResourceLocation STAR_TEXTURE = ResourceLocation.fromNamespaceAndPath(References.MOD_ID, "textures/entity/add_ripple.png");

	public PingEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(PingEntity entity) {
		return STAR_TEXTURE;
	}

	@Override
	public void render(PingEntity entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		// Rainbow color cycling
		float phase = (float) (System.currentTimeMillis() % 1000L) / 1000.0f;
		float r = RelicsUtil.r(phase);
		float g = RelicsUtil.g(phase);
		float b = RelicsUtil.b(phase);

		// Flash white on spawn
		float fresh = (8.0f - (entity.getAge() + partialTick)) / 8.0f;
		if (fresh < 0.0f)
			fresh = 0.0f;

		// Scale grows with distance, with initial burst
		Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
		double distance = entity.position().distanceTo(camPos);
		float scale = 2.0f;
		if (distance / 20.0 > scale) {
			scale = (float) (distance / 20.0);
		}
		scale *= (1.0f - fresh) * (1.0f + fresh * fresh * 12.0f);

		// Fade near end of lifetime
		float fade = entity.getEnergy() * 0.01f;
		if (fade > 1.0f)
			fade = 1.0f;

		// Additive beam render type — same as ritual locus ripples
		VertexConsumer consumer = buffer.getBuffer(RelicsRenderTypes.additiveBeamNoDepth(STAR_TEXTURE));

		poseStack.pushPose();
		poseStack.scale(scale, scale, scale);

		// Main star sprite
		renderBillboard(poseStack, consumer, Math.min(r + fresh, 1.0f), Math.min(g + fresh, 1.0f), Math.min(b + fresh, 1.0f), fade);

		// Expanding ripple layer — faster cycle using system time
		float ripple = (float) (System.currentTimeMillis() % 1000L) / 1000.0f;
		poseStack.scale(ripple, ripple, ripple);
		renderBillboard(poseStack, consumer, Math.min(g + fresh, 1.0f), Math.min(b + fresh, 1.0f), Math.min(r + fresh, 1.0f), fade * (1.0f - ripple));

		poseStack.popPose();
	}

	// Billboarded quad facing the camera — BLOCK vertex format for additiveBeam
	private void renderBillboard(PoseStack poseStack, VertexConsumer consumer, float r, float g, float b, float a) {
		poseStack.pushPose();
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
		poseStack.translate(-0.5f, -0.5f, 0);

		PoseStack.Pose last = poseStack.last();
		Matrix4f m4 = last.pose();
		Matrix3f m3 = last.normal();

		consumer.vertex(m4, 0, 0, 0).color(r, g, b, a).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		consumer.vertex(m4, 1, 0, 0).color(r, g, b, a).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		consumer.vertex(m4, 1, 1, 0).color(r, g, b, a).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();
		consumer.vertex(m4, 0, 1, 0).color(r, g, b, a).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0, 1, 0).endVertex();

		poseStack.popPose();
	}

	// Always visible — extreme render distance
	@Override
	public boolean shouldRender(PingEntity entity, Frustum frustum, double x, double y, double z) {
		return true;
	}
}