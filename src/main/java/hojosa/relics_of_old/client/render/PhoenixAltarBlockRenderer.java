package hojosa.relics_of_old.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import hojosa.relics_of_old.common.block.entity.PhoenixAltarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;

public class PhoenixAltarBlockRenderer implements BlockEntityRenderer<PhoenixAltarBlockEntity> {
	public PhoenixAltarBlockRenderer(Context context) {
	}

	@Override
	public void render(PhoenixAltarBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
		if (pBlockEntity.isEmpty())
			return;

		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

		// Convert stored direction to Y rotation angle
		float rotation = switch (pBlockEntity.getItemFacing()) {
		case SOUTH -> 0;
		case EAST -> 90;
		case NORTH -> 180;
		case WEST -> 270;
		default -> 0;
		};

		pPoseStack.pushPose();
		// Centered on top of the 3/4 height altar (12/16 = 0.75)
		pPoseStack.translate(0.5, 0.765, 0.5);
		pPoseStack.scale(0.4f, 0.4f, 0.4f);
		pPoseStack.mulPose(Axis.YP.rotationDegrees(rotation));
		pPoseStack.mulPose(Axis.XP.rotationDegrees(90));
		itemRenderer.renderStatic(pBlockEntity.getItem(0), ItemDisplayContext.FIXED, 200, pPackedOverlay, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
		pPoseStack.popPose();
	}
}