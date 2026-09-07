package hojosa.relics_of_old.client.render;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
//import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import hojosa.relics_of_old.common.item.SpellCastingItem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;

public class SpellReticleRenderer {

	private static final RenderType LINE_THIN = RelicsRenderTypes.RETICLE_LINE_THIN;
	private static final RenderType LINE_MEDIUM = RelicsRenderTypes.RETICLE_LINE_MEDIUM;
	private static final RenderType LINE_THICK = RelicsRenderTypes.RETICLE_LINE_THICK;
	private static final RenderType SEGMENT_THIN = RelicsRenderTypes.RETICLE_SEGMENT_THIN;

	public static void onRenderLevelStage(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
			return;

		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null)
			return;

		ItemStack held = player.getMainHandItem();
		if (!(held.getItem() instanceof SpellCastingItem staff))
			return;

		float partialTick = event.getPartialTick();
		float progress = staff.getCastingProgress(held, player, partialTick);

		// raycast to find target position
		Vec3 eye = player.getEyePosition(partialTick);
		Vec3 look = player.getViewVector(partialTick);
		double range = staff.getCastRange();
		Vec3 far = eye.add(look.scale(range));
		ClipContext.Fluid fluidMode = staff.isHitsWater() ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE;
		Level level = player.level();
		BlockHitResult hit = level.clip(new ClipContext(eye, far, ClipContext.Block.OUTLINE, fluidMode, player));

		Vec3 targetPos;
		boolean rayhit = hit.getType() != HitResult.Type.MISS;
		if (rayhit) {
			targetPos = hit.getLocation().add(look.scale(-0.015));
		} else {
			targetPos = far;
		}

		double radius = staff.getCastRadius();

		// translate to world position relative to camera
		Camera camera = event.getCamera();
		Vec3 camPos = camera.getPosition();
		PoseStack poseStack = event.getPoseStack();
		poseStack.pushPose();
		poseStack.translate(targetPos.x - camPos.x, targetPos.y - camPos.y, targetPos.z - camPos.z);

		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

		drawFancyReticle(poseStack, bufferSource, player, radius, progress, rayhit);

		poseStack.popPose();
	}

	private static void drawFancyReticle(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Player player, double radius, float progress, boolean rayhit) {
		float phase = (float) (System.currentTimeMillis() % 1000L) / 1000.0f;
		Matrix4f pose = poseStack.last().pose();
		Matrix3f normal = poseStack.last().normal();

		// always: small pentagons above and below center
		drawRing(bufferSource.getBuffer(LINE_THIN), pose, normal, 0, (float) (radius * 0.866), 0, radius / 2.0, 8, 0, 5, 1f, 1f, 1f, 0.25f);
		drawRing(bufferSource.getBuffer(LINE_THIN), pose, normal, 0, (float) (-radius * 0.866), 0, radius / 2.0, 8, 0, 5, 1f, 1f, 1f, 0.25f);

		// always: vertical center line
		drawLine(bufferSource.getBuffer(SEGMENT_THIN), pose, normal, 0, 0, 0, 0, (float) (-radius), 0, 1f, 1f, 1f, 0.25f);

		// flush thin lines
		bufferSource.endBatch(LINE_THIN);
		bufferSource.endBatch(SEGMENT_THIN);

		if (!player.isUsingItem()) {
			// idle: yellowish octagon when targeting a block
			float bb = rayhit ? 0.5f : 1f;

			// LEQUAL pass (in front of blocks)
			RenderSystem.depthFunc(515);
			drawRing(bufferSource.getBuffer(LINE_MEDIUM), pose, normal, 0, 0, 0, radius, 8, 0, 1, 1f, 1f, bb, 0.5f);
			bufferSource.endBatch(LINE_MEDIUM);

			// GREATER pass (behind blocks, faded)
			RenderSystem.depthFunc(516);
			drawRing(bufferSource.getBuffer(LINE_MEDIUM), pose, normal, 0, 0, 0, radius, 8, 0, 1, 1f, 1f, bb, 0.125f);
			bufferSource.endBatch(LINE_MEDIUM);
		} else if (progress < 1.0f) {
			// charging: orange outer octagon
			RenderSystem.depthFunc(515);
			drawRing(bufferSource.getBuffer(LINE_THICK), pose, normal, 0, 0, 0, radius, 8, 0, 1, 1f, progress, 0f, 1f);
			bufferSource.endBatch(LINE_THICK);
			RenderSystem.depthFunc(516);
			drawRing(bufferSource.getBuffer(LINE_THICK), pose, normal, 0, 0, 0, radius, 8, 0, 1, 1f, progress, 0f, 0.3f);
			bufferSource.endBatch(LINE_THICK);

			// shrinking twisted inner octagon
			RenderSystem.depthFunc(515);
			drawRing(bufferSource.getBuffer(LINE_MEDIUM), pose, normal, 0, 0, 0, radius * progress, 8, -progress, 1, 1f, progress, 0f, 1f);
			bufferSource.endBatch(LINE_MEDIUM);
			RenderSystem.depthFunc(516);
			drawRing(bufferSource.getBuffer(LINE_MEDIUM), pose, normal, 0, 0, 0, radius * progress, 8, -progress, 1, 1f, progress, 0f, 0.3f);
			bufferSource.endBatch(LINE_MEDIUM);
			// three swirling star pentagons converging on the target
			double flux1 = 1.0 - Math.cos(progress * Math.PI / 2.0);
			double flux2 = 1.0 - Math.cos(progress * Math.PI * 3.0 / 2.0);
			double flux3 = 1.0 - Math.cos(progress * Math.PI * 5.0 / 2.0);

			RenderSystem.depthFunc(515);
			drawRing(bufferSource.getBuffer(LINE_MEDIUM), pose, normal, 0, 0, 0, radius * flux1, 8, progress / 2.0f, 5, 1f, 1f, 1f, 0.5f * progress);
			drawRing(bufferSource.getBuffer(LINE_MEDIUM), pose, normal, 0, 0, 0, radius * flux2, 8, progress, 5, 1f, 1f, 1f, 0.5f * progress);
			drawRing(bufferSource.getBuffer(LINE_MEDIUM), pose, normal, 0, 0, 0, radius * flux3, 8, -progress / 2.0f, 5, 1f, 1f, 1f, 0.5f * progress);
			bufferSource.endBatch(LINE_MEDIUM);

		} else {
			// fully charged: pulsing octagon + star pentagon
			phase = (float) Math.sin(phase * Math.PI * 2.0 * 10.0) / 2.0f + 0.5f;
			float cr = 1f, cg = phase * 0.5f + 0.5f, cb = (1f - phase) * 0.5f + 0.5f;

			RenderSystem.depthFunc(515);
			drawRing(bufferSource.getBuffer(LINE_THICK), pose, normal, 0, 0, 0, radius, 8, 0, 1, cr, cg, cb, 1f);
			bufferSource.endBatch(LINE_THICK);
			drawRing(bufferSource.getBuffer(LINE_MEDIUM), pose, normal, 0, 0, 0, radius, 8, 0, 5, cr, cg, cb, 1f);
			bufferSource.endBatch(LINE_MEDIUM);

			// behind-blocks octagon
			RenderSystem.depthFunc(516);
			drawRing(bufferSource.getBuffer(LINE_THICK), pose, normal, 0, 0, 0, radius, 8, 0, 1, cr, cg, cb, 0.3f);
			bufferSource.endBatch(LINE_THICK);
		}

		// restore default depth function
		RenderSystem.depthFunc(515);
	}

	// ring with stride — stride=1 is octagon, stride=5 is star/pentagon
	private static void drawRing(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, float x, float y, float z, double radius, int segments, float twist, int stride, float r, float g, float b, float a) {
		for (int i = 0; i <= segments; i++) {
			int idx = (i % segments) * stride;
			int nextIdx = ((i + 1) % segments) * stride;
			double theta = Math.PI * 2 / segments * idx + Math.PI * 2 * (twist + 0.125);
			double nextTheta = Math.PI * 2 / segments * nextIdx + Math.PI * 2 * (twist + 0.125);

			float dx = (float) (Math.cos(theta) * radius);
			float dz = (float) (Math.sin(theta) * radius);

			// normal = direction to next vertex, normalized
			float ndx = (float) (Math.cos(nextTheta) * radius) - dx;
			float ndz = (float) (Math.sin(nextTheta) * radius) - dz;
			float len = Mth.sqrt(ndx * ndx + ndz * ndz);
			if (len > 0) {
				ndx /= len;
				ndz /= len;
			}

			consumer.vertex(pose, x + dx, y, z + dz).color(r, g, b, a).normal(normal, ndx, 0, ndz).endVertex();
		}
	}

	// single line segment
	private static void drawLine(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {
		float dx = x2 - x1, dy = y2 - y1, dz = z2 - z1;
		float len = Mth.sqrt(dx * dx + dy * dy + dz * dz);
		if (len > 0) {
			dx /= len;
			dy /= len;
			dz /= len;
		}

		consumer.vertex(pose, x1, y1, z1).color(r, g, b, a).normal(normal, dx, dy, dz).endVertex();
		consumer.vertex(pose, x2, y2, z2).color(r, g, b, a).normal(normal, dx, dy, dz).endVertex();
	}
}