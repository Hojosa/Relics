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

			poseStack.translate(0.32F, 1.1F, 0.12F);

			poseStack.mulPose(Axis.YP.rotationDegrees(switch (blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)) {
			case SOUTH -> 0;
			case EAST -> 90;
			case WEST -> 270;
			default -> 180;
			}));

			poseStack.mulPose(Axis.ZP.rotationDegrees(110));
			poseStack.mulPose(Axis.XP.rotationDegrees(-35));
			itemRenderer.renderStatic(itemSword, ItemDisplayContext.FIXED, light, overlay, poseStack, multiBufferSource, blockEntity.getLevel(), 1);
			poseStack.popPose();
		}
	}
}
