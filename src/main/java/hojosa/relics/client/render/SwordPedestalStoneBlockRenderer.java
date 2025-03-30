package hojosa.relics.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import hojosa.relics.common.block.entity.SwordPedestalStoneBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SwordPedestalStoneBlockRenderer implements BlockEntityRenderer<SwordPedestalStoneBlockEntity> {

	@Override
	public void render(SwordPedestalStoneBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
		if (!blockEntity.isEmpty()) {
			ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
			ItemStack itemSword = blockEntity.getItem(0);
			poseStack.pushPose();
			
			switch (blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)) {
				case NORTH:{
					poseStack.translate(0.32F, 1.1F, 0.12F);
					poseStack.mulPose(Axis.YP.rotationDegrees(180));
					break;
				}
				case EAST:{
					poseStack.translate(0.88F, 1.1F, 0.32F);
					poseStack.mulPose(Axis.YP.rotationDegrees(90));
					break;
				}
				case SOUTH:{
					poseStack.translate(0.68F, 1.1F, 0.88F);
					poseStack.mulPose(Axis.YP.rotationDegrees(0));
					break;
				}
				case WEST:{
					poseStack.translate(0.12F, 1.1F, 0.68F);
					poseStack.mulPose(Axis.YP.rotationDegrees(270));
					break;
				}
				default:
			}
			
			poseStack.mulPose(Axis.ZP.rotationDegrees(110));
			poseStack.mulPose(Axis.XP.rotationDegrees(-35));

			itemRenderer.renderStatic(itemSword, ItemDisplayContext.FIXED, light, overlay, poseStack, multiBufferSource, blockEntity.getLevel(), 1);
			poseStack.popPose();
		}
	}
}
