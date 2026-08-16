package hojosa.relics_of_old.client.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import hojosa.relics_of_old.common.entity.BombArrowEntity;
import hojosa.relics_of_old.lib.References;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BombArrowEntityRenderer extends ArrowRenderer<BombArrowEntity> {
	private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/projectiles/arrow.png");
	private static final ResourceLocation BOMB_TEX = ResourceLocation.fromNamespaceAndPath(References.MOD_ID, "textures/item/bomb.png");
	private static final ResourceLocation BOMB_FLASH_TEX = ResourceLocation.fromNamespaceAndPath(References.MOD_ID, "textures/item/bomb_flash.png");

	public BombArrowEntityRenderer(Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(BombArrowEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(BombArrowEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);

		poseStack.pushPose();

		float interpYaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
		float interpPitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());

		// rotate into arrow-local space (same rotations as ArrowRenderer)
		poseStack.mulPose(Axis.YP.rotationDegrees(interpYaw - 90.0f));
		poseStack.mulPose(Axis.ZP.rotationDegrees(interpPitch));

		// move along the arrow shaft — positive X = toward the tip
		poseStack.translate(0.1, 0.0, 0.0);

		// rotate back to world space
		poseStack.mulPose(Axis.ZP.rotationDegrees(-interpPitch));
		poseStack.mulPose(Axis.YP.rotationDegrees(-(interpYaw - 90.0f)));

		// billboard — face the camera
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());

		poseStack.scale(0.35f, 0.35f, 0.35f);

		// flash bomb texture based on tick count (same pulse rate as bomb entity)
		boolean flash = (entity.tickCount / 5) % 2 == 1;
		ResourceLocation tex = flash ? BOMB_FLASH_TEX : BOMB_TEX;
		int light = flash ? 240 : packedLight;

		VertexConsumer builder = buffer.getBuffer(RenderType.entityCutout(tex));
		Matrix4f matrix = poseStack.last().pose();

		float half = 0.5f;
		builder.vertex(matrix, -half, -half, 0).color(255, 255, 255, 255).uv(0, 1).overlayCoords(0).uv2(light).normal(0, 1, 0).endVertex();
		builder.vertex(matrix, -half, half, 0).color(255, 255, 255, 255).uv(0, 0).overlayCoords(0).uv2(light).normal(0, 1, 0).endVertex();
		builder.vertex(matrix, half, half, 0).color(255, 255, 255, 255).uv(1, 0).overlayCoords(0).uv2(light).normal(0, 1, 0).endVertex();
		builder.vertex(matrix, half, -half, 0).color(255, 255, 255, 255).uv(1, 1).overlayCoords(0).uv2(light).normal(0, 1, 0).endVertex();

		poseStack.popPose();
	}
}