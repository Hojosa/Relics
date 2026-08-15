package hojosa.relics_of_old.client.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import hojosa.relics_of_old.common.entity.BombEntity;
import hojosa.relics_of_old.lib.References;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class BombEntityRenderer extends EntityRenderer<BombEntity> {

	private static final ResourceLocation BOMB_TEX = ResourceLocation.fromNamespaceAndPath(References.MOD_ID, "textures/item/bomb.png");
	private static final ResourceLocation BOMB_FLASH_TEX = ResourceLocation.fromNamespaceAndPath(References.MOD_ID, "textures/item/bomb_flash.png");

	public BombEntityRenderer(Context context) {
		super(context);
		this.shadowRadius = 0.125f;
	}

	@Override
	public void render(BombEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();

		// scale up in final 10 ticks (0.5 -> 0.75)
		int fuse = entity.getFuse();
		float scale = 0.5f;
		if (fuse <= 10 && fuse > 0) {
			scale += 0.25f * (1.0f - (float) fuse / 10.0f);
		}
		poseStack.translate(0, 0.25, 0);
		poseStack.scale(scale, scale, scale);

		// billboard — face camera
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());

		// pick texture based on pulse phase
		boolean flash = entity.getPulsePhase() == 1;
		ResourceLocation tex = flash ? BOMB_FLASH_TEX : BOMB_TEX;
		int light = flash ? 240 : packedLight;

		VertexConsumer builder = buffer.getBuffer(RenderType.entityCutout(tex));
		Matrix4f matrix = poseStack.last().pose();

		// billboard quad centered on entity
		float half = 0.5f;
		builder.vertex(matrix, -half, -half, 0).color(255, 255, 255, 255).uv(0, 1).overlayCoords(0).uv2(light).normal(0, 1, 0).endVertex();
		builder.vertex(matrix, -half, half, 0).color(255, 255, 255, 255).uv(0, 0).overlayCoords(0).uv2(light).normal(0, 1, 0).endVertex();
		builder.vertex(matrix, half, half, 0).color(255, 255, 255, 255).uv(1, 0).overlayCoords(0).uv2(light).normal(0, 1, 0).endVertex();
		builder.vertex(matrix, half, -half, 0).color(255, 255, 255, 255).uv(1, 1).overlayCoords(0).uv2(light).normal(0, 1, 0).endVertex();

		poseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(BombEntity entity) {
		return BOMB_TEX;
	}
}