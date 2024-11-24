package hojosa.relics.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import hojosa.relics.common.block.entity.SwordPedestalBlockEntityNew;
import hojosa.relics.lib.block.SwordPedestalBaseBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SwordPedestalBlockRendererNew implements BlockEntityRenderer<SwordPedestalBlockEntityNew>{
	
	@Override
	public void render(SwordPedestalBlockEntityNew blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
		if(!blockEntity.isEmpty()) {
			ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
			ItemStack itemSword = blockEntity.getItem(0);
			SwordPedestalBaseBlock block = (SwordPedestalBaseBlock) blockEntity.getBlockState().getBlock();
			poseStack.pushPose();
			poseStack.translate(0.5F, 0.8F + block.getRenderOffSet(), 0.5F);


			poseStack.mulPose(Axis.YP.rotationDegrees(switch (blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case SOUTH -> 0;
            case EAST -> 90;
            default -> 180;
            case WEST -> 270;
			}));

			poseStack.translate(0, 1 / 16d, 0);
			poseStack.mulPose(Axis.ZP.rotationDegrees(135));
			itemRenderer.renderStatic(itemSword, ItemDisplayContext.FIXED, 250, overlay, poseStack, multiBufferSource,blockEntity.getLevel(), 1);
			poseStack.popPose();
		}
	}
}
