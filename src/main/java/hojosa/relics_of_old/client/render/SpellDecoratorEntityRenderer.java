package hojosa.relics_of_old.client.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import hojosa.relics_of_old.common.entity.attacks.SpellDecoratorEntity;
import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity.SpellType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SpellDecoratorEntityRenderer extends EntityRenderer<SpellDecoratorEntity> {

	public SpellDecoratorEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(SpellDecoratorEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		if (entity.particles == null || entity.particles.isEmpty())
			return;

		SpellType type = SpellType.values()[entity.spellType];

		poseStack.pushPose();

		VertexConsumer lineBuilder = buffer.getBuffer(RenderType.lines());
		VertexConsumer triBuilder = buffer.getBuffer(RelicsRenderTypes.SPELL_TRIANGLES);

		for (MiniParticle mip : entity.particles) {
			if (mip.hibernateTime > 0)
				continue;

			// interpolated position
			float px = (float) (mip.x + mip.vx * partialTick);
			float py = (float) (mip.y + mip.vy * partialTick);
			float pz = (float) (mip.z + mip.vz * partialTick);

			// interpolated age for smooth shape animation (LG2 does this)
			double age = mip.age + (double) (partialTick / (float) mip.maxLife);

			poseStack.pushPose();
			poseStack.translate(px, py, pz);

			renderMiniParticle(poseStack, lineBuilder, triBuilder, mip, type, entity.power, age);

			poseStack.popPose();
		}

		// crit flash: expanding rings
		if (entity.isCrit) {
			float burstTime = entity.longLife + partialTick;
			float phase = burstTime / 10.0f;
			if (phase <= 1.0f) {
				float alpha = 1.0f - phase;
				drawHorizontalRing(poseStack, lineBuilder, 0, phase * 0.3f, 0, (float) entity.radius, 8, 1.0f, 1.0f, 1.0f, alpha);
				drawHorizontalRing(poseStack, lineBuilder, 0, -phase * 0.3f, 0, (float) entity.radius, 8, 1.0f, 1.0f, 1.0f, alpha);
			}
		}

		poseStack.popPose();
	}

	private void renderMiniParticle(PoseStack poseStack, VertexConsumer lineBuilder, VertexConsumer triBuilder, MiniParticle mip, SpellType type, double power, double age) {

		switch (type) {
		case STAR_IMPACT -> {
			float r = 1.0f;
			float g = Math.min(1.0f, (float) (1.5 - age));
			float b = Math.min(1.0f, (float) (0.5 + age));
			float a = 1.0f;
			double size = Math.sin(age * Math.PI) * (power - 1.0) / 4.0;
			drawCross(poseStack, lineBuilder, 2, size, r, g, b, a);
		}
		case FIRE -> {
			float r = 1.0f;
			float g = Math.min(1.0f, (float) (2.0 - age * 2.0));
			float b = Math.min(1.0f, (float) (1.0 - age * 2.0));
			float a = 1.0f;
			double size = Math.sin(age * Math.PI * 3.0 / 4.0 + Math.PI / 4.0) * power / 8.0;
			float rot = (float) (age * 360.0);
			drawPolySolid(poseStack, triBuilder, 4, size, rot, r, g, b, a);
		}
		case LIGHTNING -> {
			if (age < 0.5) {
				double f = Math.sin((age + mip.uniqueness) * 30.0) * 0.5 + 0.5;
				float r = Math.min(1.0f, (float) (f * 2.0));
				float g = 1.0f;
				float b = Math.min(1.0f, (float) (2.0 - f * 2.0));
				float a = 1.0f;
				double s = power * 0.15;
				double phase = Math.sin(age * Math.PI * 4.0);
				drawDiamondZig(poseStack, lineBuilder, s, s * 2.0, phase, r, g, b, a);
			} else {
				double f = Math.sin((age + mip.uniqueness) * 30.0) * 0.5 + 0.5;
				float r = Math.min(1.0f, (float) (f * 2.0));
				float g = 1.0f;
				float b = Math.min(1.0f, (float) (2.0 - f * 2.0));
				float a = 1.0f;
				double phase = age * 2.0 - 1.0;
				double h = Math.sin(phase * Math.PI) * 2.5;
				drawLine(poseStack, lineBuilder, (float) -mip.x, (float) -mip.y, (float) -mip.z, (float) (mip.x * h - mip.x), (float) (mip.y * h - mip.y), (float) (mip.z * h - mip.z), r, g, b, a);
			}
		}
		case ICE -> {
			float fade = (float) (1.0 - Math.sin(age * Math.PI));
			float r = 0.5f + fade * 0.5f;
			float g = 0.5f * fade + 0.5f;
			float b = 1.0f;
			float a = 1.0f;
			double size = power / 4.0;
			if (age < 0.5) {
				drawCross(poseStack, lineBuilder, 3, size * (1.0 - age * 2.0) * 2.0, r, g, b, a);
			} else if (age < 0.75) {
				drawPolySolid(poseStack, triBuilder, 6, size * (age - 0.5) * 4.0, 90f, r, g, b, a);
			} else {
				drawPolyOutline(poseStack, lineBuilder, 6, size, r, g, b, a);
			}
		}
		case SPRINKLE_STARDUST -> {
			float r = 1.0f;
			float g = Math.min(1.0f, (float) (1.5 - age));
			float b = Math.min(1.0f, (float) (0.5 + age));
			float a = 1.0f;
			drawPinchDiamond(poseStack, triBuilder, age, 0.25, r, g, b, a);
		}
		default -> {
		}
		}
	}
	
	//========== Geometry helpers ==========
	private void drawCross(PoseStack poseStack, VertexConsumer builder, int spikes, double size, float r, float g, float b, float a) {
		poseStack.pushPose();
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		Matrix4f matrix = poseStack.last().pose();

		double dTh = Math.PI / spikes;
		for (int i = 0; i < spikes; i++) {
			double theta = i * dTh + Math.PI / 2.0;
			float x = (float) (Math.cos(theta) * size);
			float y = (float) (Math.sin(theta) * size);
			vertex(builder, matrix, x, y, 0, r, g, b, a);
			vertex(builder, matrix, -x, -y, 0, r, g, b, a);
		}
		poseStack.popPose();
	}

	private void drawPolySolid(PoseStack poseStack, VertexConsumer builder, int sides, double size, float rotation, float r, float g, float b, float a) {
		poseStack.pushPose();
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		Matrix4f matrix = poseStack.last().pose();

		double dTh = Math.PI * 2.0 / sides;
		for (int i = 0; i < sides; i++) {
			double th1 = i * dTh + Math.toRadians(rotation);
			double th2 = (i + 1) * dTh + Math.toRadians(rotation);
			vertex(builder, matrix, 0, 0, 0, r, g, b, a);
			vertex(builder, matrix, (float) (Math.cos(th1) * size), (float) (Math.sin(th1) * size), 0, r, g, b, a);
			vertex(builder, matrix, (float) (Math.cos(th2) * size), (float) (Math.sin(th2) * size), 0, r, g, b, a);
			vertex(builder, matrix, (float) (Math.cos(th2) * size), (float) (Math.sin(th2) * size), 0, r, g, b, a);
		}
		poseStack.popPose();
	}

	private void drawPolyOutline(PoseStack poseStack, VertexConsumer builder, int sides, double size, float r, float g, float b, float a) {
		poseStack.pushPose();
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		Matrix4f matrix = poseStack.last().pose();

		double dTh = Math.PI * 2.0 / sides;
		for (int i = 0; i < sides; i++) {
			double th1 = i * dTh + Math.PI / 2.0;
			double th2 = (i + 1) * dTh + Math.PI / 2.0;
			vertex(builder, matrix, (float) (Math.cos(th1) * size), (float) (Math.sin(th1) * size), 0, r, g, b, a);
			vertex(builder, matrix, (float) (Math.cos(th2) * size), (float) (Math.sin(th2) * size), 0, r, g, b, a);
		}
		poseStack.popPose();
	}

	private void drawDiamondZig(PoseStack poseStack, VertexConsumer builder, double w, double h, double phase, float r, float g, float b, float a) {
		poseStack.pushPose();
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		Matrix4f matrix = poseStack.last().pose();

		float top = (float) ((1.0 - Math.abs(phase)) * h);
		float mid = (float) (-phase * w);
		float bot = (float) (-(1.0 - Math.abs(phase)) * h);

		vertex(builder, matrix, 0, top, 0, r, g, b, a);
		vertex(builder, matrix, mid, 0, 0, r, g, b, a);

		vertex(builder, matrix, mid, 0, 0, r, g, b, a);
		vertex(builder, matrix, -mid, 0, 0, r, g, b, a);

		vertex(builder, matrix, -mid, 0, 0, r, g, b, a);
		vertex(builder, matrix, 0, bot, 0, r, g, b, a);

		poseStack.popPose();
	}

	private void drawLine(PoseStack poseStack, VertexConsumer builder, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {
		Matrix4f matrix = poseStack.last().pose();
		float dx = x2 - x1, dy = y2 - y1, dz = z2 - z1;
		float len = Mth.sqrt(dx * dx + dy * dy + dz * dz);
		if (len < 0.001f)
			return;
		float nx = dx / len, ny = dy / len, nz = dz / len;
		builder.vertex(matrix, x1, y1, z1).color(r, g, b, a).normal(poseStack.last().normal(), nx, ny, nz).endVertex();
		builder.vertex(matrix, x2, y2, z2).color(r, g, b, a).normal(poseStack.last().normal(), nx, ny, nz).endVertex();
	}

	private void drawHorizontalRing(PoseStack poseStack, VertexConsumer builder, float cx, float cy, float cz, float radius, int segments, float r, float g, float b, float a) {
		Matrix4f matrix = poseStack.last().pose();
		double dTh = Math.PI * 2.0 / segments;
		for (int i = 0; i < segments; i++) {
			double th1 = i * dTh;
			double th2 = (i + 1) * dTh;
			float x1 = cx + (float) (Math.cos(th1) * radius);
			float z1 = cz + (float) (Math.sin(th1) * radius);
			float x2 = cx + (float) (Math.cos(th2) * radius);
			float z2 = cz + (float) (Math.sin(th2) * radius);
			float dx = x2 - x1, dz = z2 - z1;
			float len = Mth.sqrt(dx * dx + dz * dz);
			builder.vertex(matrix, x1, cy, z1).color(r, g, b, a).normal(poseStack.last().normal(), dx / len, 0, dz / len).endVertex();
			builder.vertex(matrix, x2, cy, z2).color(r, g, b, a).normal(poseStack.last().normal(), dx / len, 0, dz / len).endVertex();
		}
	}

	private void drawPinchDiamond(PoseStack poseStack, VertexConsumer builder, double phase, double size, float r, float g, float b, float a) {
		poseStack.pushPose();
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		Matrix4f matrix = poseStack.last().pose();

		// X shrinks, Y stretches over lifetime
		float sy = (float) (phase * phase * size);
		float sx = (float) ((1.0 - phase) * (1.0 - phase) * size);

		// Two triangles forming a diamond (QUADS with degenerate 4th vertex)
		vertex(builder, matrix, 0, sy, 0, r, g, b, a);
		vertex(builder, matrix, -sx, 0, 0, r, g, b, a);
		vertex(builder, matrix, sx, 0, 0, r, g, b, a);
		vertex(builder, matrix, sx, 0, 0, r, g, b, a);

		vertex(builder, matrix, 0, -sy, 0, r, g, b, a);
		vertex(builder, matrix, sx, 0, 0, r, g, b, a);
		vertex(builder, matrix, -sx, 0, 0, r, g, b, a);
		vertex(builder, matrix, -sx, 0, 0, r, g, b, a);

		poseStack.popPose();
	}

	private void vertex(VertexConsumer builder, Matrix4f matrix, float x, float y, float z, float r, float g, float b, float a) {
		builder.vertex(matrix, x, y, z).color(r, g, b, a).normal(0, 1, 0).endVertex();
	}

	@Override
	public ResourceLocation getTextureLocation(SpellDecoratorEntity entity) {
		return null;
	}
}