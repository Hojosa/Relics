package hojosa.relics.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;

import hojosa.relics.lib.block.entity.GlintBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraftforge.client.model.data.ModelData;

public class GlintBlockRenderer implements BlockEntityRenderer<GlintBlockEntity> {
	private static final RandomSource RANDOM = RandomSource.create();
	private final BlockRenderDispatcher blockRenderDispatcher;

	public GlintBlockRenderer(BlockEntityRendererProvider.Context context) {
		this.blockRenderDispatcher = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(GlintBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
		pPoseStack.pushPose();
		PoseStack.Pose pose = pPoseStack.last();
		VertexConsumer consumer = new SheetedDecalTextureGenerator(pBuffer.getBuffer(RelicsRenderTypes.BLOCK_GLINT_RAINBOW), pose.pose(), pose.normal(), 0.0078125F);
		blockRenderDispatcher.renderBatched(pBlockEntity.getBlockState(), pBlockEntity.getBlockPos(), pBlockEntity.getLevel(), pPoseStack, consumer, true, RANDOM, ModelData.EMPTY, null);
		pPoseStack.popPose();
	}
}
