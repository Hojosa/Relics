package hojosa.relics.client.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import hojosa.relics.common.entity.StarBeamEntity;
import hojosa.relics.lib.References;
import hojosa.relics.lib.RelicsUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class StarBeamRenderer extends EntityRenderer<StarBeamEntity> {

	public StarBeamRenderer(Context pContext) {
		super(pContext);
	}

	@Override
	public void render(StarBeamEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
		renderStarBeam(pPoseStack, pBuffer.getBuffer(RenderType.lines()), pEntity.getStartPos(), pEntity.getTargetPos());
	}
	
    private static void renderStarBeam(PoseStack matrixStack, VertexConsumer builder, BlockPos startPos, BlockPos endPos) {
		float phase = (System.currentTimeMillis() % 2000L) / 2000.0f;
        int r = (int) (RelicsUtil.r(phase)*255);
        int g = (int) (RelicsUtil.g(phase)*255);
        int b = (int) (RelicsUtil.b(phase)*255);
    	
        matrixStack.pushPose();
        
    	Matrix4f matrix4f = matrixStack.last().pose();
	    matrixStack.translate(0, 1.3f, 0);
	    
	    // 1. Get camera position
	    Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
	    Vec3 cameraPos = camera.getPosition();
	    
	    // 2. setup start and end position
	    Vec3 start = Vec3.atCenterOf(startPos).subtract(cameraPos);
	    Vec3 end = Vec3.atCenterOf(endPos).subtract(cameraPos);
	    
	    // 3. Draw line
	    builder.vertex(matrix4f, (float)start.x, (float)start.y, (float)start.z)
	        .color(r, g, b, 255)
	        .normal(0, 1, 0)
	        .endVertex();
    
	    builder.vertex(matrix4f, (float)end.x, (float)end.y, (float)end.z)
	        .color(r, g, b, 255)
	        .normal(0, 1, 0)
	        .endVertex();

        matrixStack.popPose();
    }

	@Override
	public ResourceLocation getTextureLocation(StarBeamEntity pEntity) {
		return new ResourceLocation(References.MOD_ID, "textures/entity/beam.png");
	}
}
