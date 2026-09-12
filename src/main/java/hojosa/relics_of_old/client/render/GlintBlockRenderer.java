package hojosa.relics_of_old.client.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import hojosa.relics_of_old.lib.block.entity.GlintBlockEntity;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public class GlintBlockRenderer implements BlockEntityRenderer<GlintBlockEntity> {
	private static final RandomSource RANDOM = RandomSource.create();
	private final BlockRenderDispatcher blockRenderDispatcher;
	private static final float DSU = 0.03125f;  // 1/32
    private static final float DS  = 0.015625f; // 1/64
    private static final int SP = 64;

	public GlintBlockRenderer(BlockEntityRendererProvider.Context context) {
		this.blockRenderDispatcher = context.getBlockRenderDispatcher();
	}

//	@Override
//	public void render(GlintBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
//		pPoseStack.pushPose();
//		PoseStack.Pose pose = pPoseStack.last();
//		VertexConsumer consumer = new SheetedDecalTextureGenerator(pBuffer.getBuffer(RelicsRenderTypes.BLOCK_CHAOS_RAINBOW), pose.pose(), pose.normal(), 0.0078125F);
//		blockRenderDispatcher.renderBatched(pBlockEntity.getBlockState(), pBlockEntity.getBlockPos(), pBlockEntity.getLevel(), pPoseStack, consumer, true, RANDOM, ModelData.EMPTY, null);
//		pPoseStack.popPose();
//	}
	@Override
    public void render(GlintBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = be.getLevel();
        BlockPos pos = be.getBlockPos();

        // LG2 time-based UV scroll
        long time = Util.getMillis();
        float phase  = (float)(time % 8000L)  / 8000.0f;
        float uphase = (float)(time % 11000L) / 11000.0f;

        // per-block position offset (all blocks visually sync, subtle UV shift)
        float posOffset = (float)(((pos.getX() + pos.getZ() + pos.getY()) % SP + SP) % SP) / (float) SP;
        float u = posOffset - uphase;
        float v = posOffset + phase;

        VertexConsumer vc = buffer.getBuffer(RelicsRenderTypes.BLOCK_CHAOS_RAINBOW);
        Matrix4f mat = poseStack.last().pose();

        // LG2 vertex color: RGB dimmed to 60%, alpha 30%
        float cr = 0.6f, cg = 0.6f, cb = 0.6f, ca = 0.3f;

        // top face — skip if adjacent block is opaque
        if (!level.getBlockState(pos.above()).canOcclude()) {
            vc.vertex(mat, 0, 1, 0).color(cr, cg, cb, ca).uv(u + DSU,     v + DS    ).endVertex();
            vc.vertex(mat, 0, 1, 1).color(cr, cg, cb, ca).uv(u + DSU * 2, v + DS * 2).endVertex();
            vc.vertex(mat, 1, 1, 1).color(cr, cg, cb, ca).uv(u + DSU * 3, v + DS * 3).endVertex();
            vc.vertex(mat, 1, 1, 0).color(cr, cg, cb, ca).uv(u + DSU * 2, v + DS * 2).endVertex();
        }
        // bottom face
        if (!level.getBlockState(pos.below()).canOcclude()) {
            vc.vertex(mat, 0, 0, 0).color(cr, cg, cb, ca).uv(u,           v         ).endVertex();
            vc.vertex(mat, 1, 0, 0).color(cr, cg, cb, ca).uv(u + DSU,     v + DS    ).endVertex();
            vc.vertex(mat, 1, 0, 1).color(cr, cg, cb, ca).uv(u + DSU * 2, v + DS * 2).endVertex();
            vc.vertex(mat, 0, 0, 1).color(cr, cg, cb, ca).uv(u + DSU,     v + DS    ).endVertex();
        }
        // west face (x-1)
        if (!level.getBlockState(pos.west()).canOcclude()) {
            vc.vertex(mat, 0, 0, 1).color(cr, cg, cb, ca).uv(u + DSU,     v + DS    ).endVertex();
            vc.vertex(mat, 0, 1, 1).color(cr, cg, cb, ca).uv(u + DSU * 2, v + DS * 2).endVertex();
            vc.vertex(mat, 0, 1, 0).color(cr, cg, cb, ca).uv(u + DSU,     v + DS    ).endVertex();
            vc.vertex(mat, 0, 0, 0).color(cr, cg, cb, ca).uv(u,           v         ).endVertex();
        }
        // east face (x+1)
        if (!level.getBlockState(pos.east()).canOcclude()) {
            vc.vertex(mat, 1, 1, 0).color(cr, cg, cb, ca).uv(u + DSU * 2, v + DS * 2).endVertex();
            vc.vertex(mat, 1, 1, 1).color(cr, cg, cb, ca).uv(u + DSU * 3, v + DS * 3).endVertex();
            vc.vertex(mat, 1, 0, 1).color(cr, cg, cb, ca).uv(u + DSU * 2, v + DS * 2).endVertex();
            vc.vertex(mat, 1, 0, 0).color(cr, cg, cb, ca).uv(u + DSU,     v + DS    ).endVertex();
        }
        // south face (z+1)
        if (!level.getBlockState(pos.south()).canOcclude()) {
            vc.vertex(mat, 1, 0, 1).color(cr, cg, cb, ca).uv(u + DSU * 2, v + DS * 2).endVertex();
            vc.vertex(mat, 1, 1, 1).color(cr, cg, cb, ca).uv(u + DSU * 3, v + DS * 3).endVertex();
            vc.vertex(mat, 0, 1, 1).color(cr, cg, cb, ca).uv(u + DSU * 2, v + DS * 2).endVertex();
            vc.vertex(mat, 0, 0, 1).color(cr, cg, cb, ca).uv(u + DSU,     v + DS    ).endVertex();
        }
        // north face (z-1)
        if (!level.getBlockState(pos.north()).canOcclude()) {
            vc.vertex(mat, 0, 1, 0).color(cr, cg, cb, ca).uv(u + DSU,     v + DS    ).endVertex();
            vc.vertex(mat, 1, 1, 0).color(cr, cg, cb, ca).uv(u + DSU * 2, v + DS * 2).endVertex();
            vc.vertex(mat, 1, 0, 0).color(cr, cg, cb, ca).uv(u + DSU,     v + DS    ).endVertex();
            vc.vertex(mat, 0, 0, 0).color(cr, cg, cb, ca).uv(u,           v         ).endVertex();
        }
    }

}
