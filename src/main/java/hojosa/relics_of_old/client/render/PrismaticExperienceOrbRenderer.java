package hojosa.relics_of_old.client.render;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import hojosa.relics_of_old.common.init.RelicsConfig;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ExperienceOrbRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ExperienceOrb;

public class PrismaticExperienceOrbRenderer extends EntityRenderer<ExperienceOrb> {
	private static final ResourceLocation EXPERIENCE_ORB_LOCATION = RelicsUtil.modLoc("textures/entity/xp_orb.png");
	// Vanilla renderer for fallback when config is disabled
	private final ExperienceOrbRenderer vanillaRenderer;

	public PrismaticExperienceOrbRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.15f;
		this.shadowStrength = 0.75f;
		this.vanillaRenderer = new ExperienceOrbRenderer(context);
	}

	@Override
	public ResourceLocation getTextureLocation(ExperienceOrb entity) {
		if (!RelicsConfig.CLIENT.prismaticXpOrbs.get()) {
			return vanillaRenderer.getTextureLocation(entity);
		}
		return EXPERIENCE_ORB_LOCATION;
	}

	@Override
	public void render(ExperienceOrb entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		// Delegate to vanilla renderer when disabled
		if (!RelicsConfig.CLIENT.prismaticXpOrbs.get()) {
			vanillaRenderer.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
			return;
		}
		poseStack.pushPose();
		poseStack.translate(0.0F, 0.05F, 0.0F);

		// Texture atlas: 4x4 grid of 16px tiles in a 64px texture
		int textureIndex = entity.getIcon();
		float u0 = (textureIndex % 4 * 16) / 64.0f;
		float u1 = (textureIndex % 4 * 16 + 16) / 64.0f;
		float v0 = (textureIndex / 4 * 16) / 64.0f;
		float v1 = (textureIndex / 4 * 16 + 16) / 64.0f;

		// Rainbow color based on time + position (LG2-faithful phase calculation)
		float phase = (float) ((System.currentTimeMillis() % 1000L) / 1000.0) + (float) ((entity.getX() + entity.getZ()) * 0.05);
		int r = (int) (RelicsUtil.r(phase) * 255);
		int g = (int) (RelicsUtil.g(phase) * 255);
		int b = (int) (RelicsUtil.b(phase) * 255);
		int a = 255;

		// Billboard facing camera
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		float scale = 0.3f;
		poseStack.scale(scale, scale, scale);

		// Full brightness
		int light = 0xF000F0;

		VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(EXPERIENCE_ORB_LOCATION));
		Matrix4f pose = poseStack.last().pose();
		Matrix3f normal = poseStack.last().normal();

		// Quad vertices (billboard)
		vertexConsumer.vertex(pose, -0.5f, -0.25f, 0.0f).color(r, g, b, a).uv(u0, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 1, 0).endVertex();
		vertexConsumer.vertex(pose, 0.5f, -0.25f, 0.0f).color(r, g, b, a).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 1, 0).endVertex();
		vertexConsumer.vertex(pose, 0.5f, 0.75f, 0.0f).color(r, g, b, a).uv(u1, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 1, 0).endVertex();
		vertexConsumer.vertex(pose, -0.5f, 0.75f, 0.0f).color(r, g, b, a).uv(u0, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 1, 0).endVertex();

		poseStack.popPose();
		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
	}
}