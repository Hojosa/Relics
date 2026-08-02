package hojosa.relics_of_old.client.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import hojosa.relics_of_old.common.entity.MedallionEntity;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.RelicsUtil.ElementType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class MedallionEntityRenderer extends EntityRenderer<MedallionEntity>   {
	
	private static final ResourceLocation STAR_TEX = ResourceLocation.fromNamespaceAndPath(References.MOD_ID, "textures/entity/star.png");

    public MedallionEntityRenderer(Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(MedallionEntity pEntity) {
        return STAR_TEX;
    }

    @Override
    public void render(MedallionEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float phase = (float) (Minecraft.getInstance().level.getGameTime() + pPartialTick) / 20.0f;
        int color = getColor(pEntity.getMedallionType());
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        VertexConsumer builder = pBuffer.getBuffer(RenderType.beaconBeam(STAR_TEX, true));
        pPoseStack.pushPose();

        // first star — clockwise
        billboard(pPoseStack, builder, 180.0f * phase, r, g, b, 1.0f);

        // second star — counter-clockwise, slightly smaller
        pPoseStack.scale(0.8f, 0.8f, 0.8f);
        billboard(pPoseStack, builder, -270.0f * phase, r * 0.8f, g * 0.8f, b * 0.8f, 1.0f);

        pPoseStack.popPose();
    }
    
    private void billboard(PoseStack poseStack, VertexConsumer builder, float angle, float r, float g, float b, float size) {
        float cameraYRot = Minecraft.getInstance().gameRenderer.getMainCamera().getYRot();
        float cameraXRot = Minecraft.getInstance().gameRenderer.getMainCamera().getXRot();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - cameraYRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(-cameraXRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(angle)); 

        float half = size / 2.0f;
        Matrix4f matrix = poseStack.last().pose();

        builder.vertex(matrix, -half, -half, 0).color(r, g, b, 1.0f).uv(0, 1).overlayCoords(0).uv2(240).normal(0, 1, 0).endVertex();
        builder.vertex(matrix,  half, -half, 0).color(r, g, b, 1.0f).uv(1, 1).overlayCoords(0).uv2(240).normal(0, 1, 0).endVertex();
        builder.vertex(matrix,  half,  half, 0).color(r, g, b, 1.0f).uv(1, 0).overlayCoords(0).uv2(240).normal(0, 1, 0).endVertex();
        builder.vertex(matrix, -half,  half, 0).color(r, g, b, 1.0f).uv(0, 0).overlayCoords(0).uv2(240).normal(0, 1, 0).endVertex();

        poseStack.popPose();
    }

    private static int getColor(ElementType type) {
        return switch (type) {
            case FIRE      -> 0xFF4400;
            case EARTH     -> 0x8B6914;
            case WIND      -> 0xCCEECC;
            case ENDER     -> 0x8800CC;
            case ICE       -> 0x88CCFF;
            case WATER     -> 0x2266FF;
            case LIGHTNING -> 0xFFEE00;
            case MAGIC     -> 0xFF44FF;
            case GRAVITY   -> 0x6622AA;
            case FOREST    -> 0x22AA22;
            default 	   -> 0xFFFFFF;
        };
    }
}