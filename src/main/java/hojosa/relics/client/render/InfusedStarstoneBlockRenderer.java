package hojosa.relics.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import hojosa.relics.common.block.entity.InfusedStarstoneBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.data.ModelData;

public class InfusedStarstoneBlockRenderer implements BlockEntityRenderer<InfusedStarstoneBlockEntity> {
	private static final RandomSource RANDOM = RandomSource.create();
	private final BlockRenderDispatcher blockRenderDispatcher;

	public InfusedStarstoneBlockRenderer(Context context) {
		this.blockRenderDispatcher = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(InfusedStarstoneBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
		pPoseStack.pushPose();
		PoseStack.Pose pose = pPoseStack.last();
		VertexConsumer consumer = new SheetedDecalTextureGenerator(pBuffer.getBuffer(RelicsRenderTypes.BLOCK_GLINT_RAINBOW), pose.pose(), pose.normal(), 0.0078125F);
		blockRenderDispatcher.renderBatched(pBlockEntity.getBlockState(), pBlockEntity.getBlockPos(), pBlockEntity.getLevel(), pPoseStack, consumer, true, RANDOM, ModelData.EMPTY, null);
		pPoseStack.popPose();

		if (!pBlockEntity.isEmpty()) {
			ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
			for (int i = 0; i < pBlockEntity.getContainerSize(); i++) {
				
				if (pBlockEntity.isStackInSlot(i)) {
					pPoseStack.pushPose();
					
				    double angle = Math.toRadians(270D + i * 90);
				    double xOffset = 0.3 * Math.cos(angle) + 0.5;
				    double zOffset = 0.3 * Math.sin(angle) + 0.5;
				    pPoseStack.translate(xOffset, 1.015F, zOffset);
					pPoseStack.scale(0.4f, 0.4f, 0.4f);
					pPoseStack.mulPose(Axis.XP.rotationDegrees(90));
					itemRenderer.renderStatic(pBlockEntity.getItem(i), ItemDisplayContext.FIXED, 200, pPackedOverlay, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
					pPoseStack.popPose();
				}
			}
		}
	}
}