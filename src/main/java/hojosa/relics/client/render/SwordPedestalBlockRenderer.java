package hojosa.relics.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import hojosa.relics.common.block.SwordPedestalBlock;
import hojosa.relics.common.block.entity.SwordPedestalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

public class SwordPedestalBlockRenderer implements BlockEntityRenderer<SwordPedestalBlockEntity>{
	private final BlockEntityRendererProvider.Context context;
	
	public SwordPedestalBlockRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}
	
	@Override
	public void render(SwordPedestalBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {

		if(blockEntity.isStackInSlot()) {
			final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
			ItemStack itemSword = blockEntity.getSword();
			poseStack.pushPose();
			poseStack.translate(0.5F, 0.8F, 0.5F);

			switch(blockEntity.getBlockState().getValue(SwordPedestalBlock.FACING)) {
				case NORTH  -> {
					poseStack.mulPose(Vector3f.ZN.rotationDegrees(-45));
					poseStack.mulPose(Vector3f.XN.rotationDegrees(180));
					poseStack.mulPose(Vector3f.YN.rotationDegrees(0));
				}
				case SOUTH -> {
					poseStack.mulPose(Vector3f.ZN.rotationDegrees(45));
					poseStack.mulPose(Vector3f.XN.rotationDegrees(180));
					poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
				}
				case WEST  -> {
					poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
					poseStack.mulPose(Vector3f.XN.rotationDegrees(45));
					poseStack.mulPose(Vector3f.YN.rotationDegrees(270));
				}
				case EAST -> {
					poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
					poseStack.mulPose(Vector3f.XN.rotationDegrees(-45));
					poseStack.mulPose(Vector3f.YN.rotationDegrees(90));
				}
			}
			itemRenderer.renderStatic(itemSword, TransformType.FIXED, 250, overlay, poseStack, multiBufferSource, 1);
			poseStack.popPose();
		}
		
	}

}
