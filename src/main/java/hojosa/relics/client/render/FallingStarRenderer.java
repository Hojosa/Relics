package hojosa.relics.client.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import hojosa.relics.common.entity.FallingStarEntity;
import hojosa.relics.lib.ModelHandle;
import hojosa.relics.lib.References;
import hojosa.relics.lib.RelicsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.NonNullLazy;

public class FallingStarRenderer extends EntityRenderer<FallingStarEntity> {

	public FallingStarRenderer(Context pContext) {
		super(pContext);
	}
	public static final NonNullLazy<ModelHandle> modelStarBit = NonNullLazy.of(() -> ModelHandle.of(References.MOD_ID + ":models/entity/starbit.obj"));
	public static final ResourceLocation BEAM = new ResourceLocation(References.MOD_ID, "textures/entity/beam.png");

	@Override
	public ResourceLocation getTextureLocation(FallingStarEntity pEntity) {
		return new ResourceLocation("minecraft:textures/block/white_stained_glass.png");
	}

	@Override
	public void render(FallingStarEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float scale = (float)pEntity.getAliveState() * 0.9f / FallingStarEntity.DWINDLE_TIME;
		pPoseStack.pushPose();
        pPoseStack.scale(scale, scale, scale);

        if(!pEntity.onGround())
        	renderStarTail(pPoseStack, pBuffer.getBuffer(RelicsRenderTypes.BEAM));

        float rotationAngle = (System.currentTimeMillis() % 3600) / 10f;
        pPoseStack.translate(0, 0.42, 0);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(rotationAngle));
        
        pPoseStack.pushPose();
        modelStarBit.get().render(pPoseStack, pBuffer, RenderType.entitySolid(getTextureLocation(pEntity)), 200, 0xFF96a2e1);
        pPoseStack.popPose();
        
        modelStarBit.get().render(pPoseStack, pBuffer, RenderType.entityTranslucent(getTextureLocation(pEntity)), 200, 0xFF767676);
        modelStarBit.get().render(pPoseStack, pBuffer, RelicsRenderTypes.ENTITY_GLINT_DIRECT_RAINBOW, 240, 0xFFffd619);

        pPoseStack.popPose();
	}
    
    private static void renderStarTail(PoseStack matrixStack, VertexConsumer builder) {
        float phase = (System.currentTimeMillis() % 1000L) / 1000.0f;
        float r = RelicsUtil.r(phase);
        float g = RelicsUtil.g(phase);
        float b = RelicsUtil.b(phase);
    	matrixStack.pushPose();
        Matrix4f matrix4f = matrixStack.last().pose();
        
	    matrixStack.mulPose(Axis.YN.rotationDegrees(Minecraft.getInstance().gameRenderer.getMainCamera().getYRot()));
	    matrixStack.translate(0, 15.5, 0);
      
		builder.vertex(matrix4f, -1.3f, -15, 0).color(r, g, b, 1).uv(0, 1).overlayCoords(0).uv2(240).normal(1, 0, 0).endVertex();
		builder.vertex(matrix4f, -1.3f, 15, 0).color(r, g, b, 1).uv(0, 0).overlayCoords(0).uv2(240).normal(1, 0, 0).endVertex();
		builder.vertex(matrix4f, 1.3f, 15, 0).color(r, g, b, 1).uv(1, 0).overlayCoords(0).uv2(240).normal(1, 0, 0).endVertex();
		builder.vertex(matrix4f, 1.3f, -15, 0).color(r, g, b, 1).uv(1 ,1).overlayCoords(0).uv2(240).normal(1, 0, 0).endVertex();

        matrixStack.popPose();
    }
    
    @Override
    public boolean shouldRender(FallingStarEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
    	if(!pLivingEntity.onGround()) {
    		return true;
    	}
    	else return super.shouldRender(pLivingEntity, pCamera, pCamX, pCamY, pCamZ);
    }
}
