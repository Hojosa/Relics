package hojosa.relics.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import hojosa.relics.common.entity.StarBeamEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class StarBeamRenderer extends EntityRenderer<StarBeamEntity> {

	public StarBeamRenderer(Context pContext) {
		super(pContext);
	}

	@Override
	public void render(StarBeamEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

	}


	@Override
	public ResourceLocation getTextureLocation(StarBeamEntity pEntity) {
		return null;
	}
}
