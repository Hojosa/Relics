package hojosa.relics_of_old.client.render;

import java.util.Random;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import hojosa.relics_of_old.common.entity.attacks.EnderBombEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class EnderBombEntityRenderer extends EntityRenderer<EnderBombEntity> {

	private static final int SEGMENTS = 12;
	private static final int[][] COLORS = { { 0x1E, 0x0E, 0x35 }, // dark purple
			{ 0x4E, 0x20, 0x6A }, // medium purple
			{ 0x90, 0x49, 0xFF } // bright purple
	};

	private final Random rand = new Random();

	public EnderBombEntityRenderer(Context pContext) {
		super(pContext);
	}

	@Override
	public ResourceLocation getTextureLocation(EnderBombEntity pEntity) {
		return null;
	}

	@Override
	public void render(EnderBombEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
		int lifetime = pEntity.getLifetime();
		double radius = computeRadius(lifetime, pPartialTick);

		if (radius <= 0.0)
			return;

		pPoseStack.pushPose();

		Matrix4f matrix = pPoseStack.last().pose();

		for (int[] color : COLORS) {
			float r = color[0] / 255.0f;
			float g = color[1] / 255.0f;
			float b = color[2] / 255.0f;

			VertexConsumer builder = pBuffer.getBuffer(RenderType.lineStrip());
			ringVertices(builder, matrix, radius, r, g, b);
		}

		pPoseStack.popPose();
	}

	private void ringVertices(VertexConsumer builder, Matrix4f matrix, double radius, float r, float g, float b) {
		double fuzz = 0.05 * radius;
		float firstX = 0, firstY = 0, firstZ = 0;

		for (int i = 0; i < SEGMENTS; i++) {
			double th = Math.PI * 2.0 * i / SEGMENTS + radius;
			float x = (float) (Math.cos(th) * radius + rand.nextGaussian() * fuzz);
			float y = (float) (rand.nextGaussian() * fuzz);
			float z = (float) (Math.sin(th) * radius + rand.nextGaussian() * fuzz);

			if (i == 0) {
				firstX = x;
				firstY = y;
				firstZ = z;
			}
			builder.vertex(matrix, x, y, z).color(r, g, b, 1.0f).normal(0, 1, 0).endVertex();
		}
		// close the loop
		builder.vertex(matrix, firstX, firstY, firstZ).color(r, g, b, 1.0f).normal(0, 1, 0).endVertex();
	}

	private double computeRadius(int lifetime, float partialTick) {
		if (lifetime < EnderBombEntity.EXPAND_TIME) {
			return (lifetime + partialTick) / EnderBombEntity.EXPAND_TIME * EnderBombEntity.MAX_RADIUS;
		} else {
			int collapseProgress = lifetime - EnderBombEntity.EXPAND_TIME;
			return EnderBombEntity.MAX_RADIUS * (1.0 - (collapseProgress + partialTick) / EnderBombEntity.COLLAPSE_TIME);
		}
	}
}