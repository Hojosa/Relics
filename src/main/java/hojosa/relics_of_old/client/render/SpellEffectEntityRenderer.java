package hojosa.relics_of_old.client.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity;
import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity.SpellType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SpellEffectEntityRenderer extends EntityRenderer<SpellEffectEntity> {

	// per-entity particle lists, keyed by entity ID
	private final Map<Integer, List<MiniParticle>> particleMap = new HashMap<>();
	private final Random rand = new Random();

	public SpellEffectEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(SpellEffectEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

		SpellType type = entity.getSpellType();
		int id = entity.getId();

		// initialize particles on first render
		if (!particleMap.containsKey(id)) {
			particleMap.put(id, generateParticles(type, entity.radius, entity.power));
		}

		List<MiniParticle> particles = particleMap.get(id);

		// tick the particle sim
		particles = MiniParticle.tickAll(particles);
		particleMap.put(id, particles);

		// clean up when entity is gone
		if (entity.isRemoved()) {
			particleMap.remove(id);
			return;
		}
		if (particles.isEmpty()) {
			particleMap.remove(id);
			return;
		}

		poseStack.pushPose();

		// full brightness, no texture
		VertexConsumer lineBuilder = buffer.getBuffer(RenderType.lines());
		VertexConsumer triBuilder = buffer.getBuffer(RelicsRenderTypes.SPELL_TRIANGLES);

		for (MiniParticle mip : particles) {
			if (mip.hibernateTime > 0)
				continue;
			// interpolated position
			float px = (float) (mip.x + mip.vx * partialTick);
			float py = (float) (mip.y + mip.vy * partialTick);
			float pz = (float) (mip.z + mip.vz * partialTick);

			poseStack.pushPose();
			poseStack.translate(px, py, pz);

			renderMiniParticle(poseStack, lineBuilder, triBuilder, mip, type, entity.power);

			poseStack.popPose();
		}

		// crit flash: expanding rings
		if (entity.isCrit) {
			float burstTime = entity.getLifetime() + partialTick;
			float phase = burstTime / 10.0f;
			if (phase <= 1.0f) {
				float alpha = 1.0f - phase;
				drawHorizontalRing(poseStack, lineBuilder, 0, phase * 0.3f, 0, (float) entity.radius, 8, 1.0f, 1.0f, 1.0f, alpha);
				drawHorizontalRing(poseStack, lineBuilder, 0, -phase * 0.3f, 0, (float) entity.radius, 8, 1.0f, 1.0f, 1.0f, alpha);
			}
		}

		poseStack.popPose();
	}

	// -- Particle generation per spell type --

	private List<MiniParticle> generateParticles(SpellType type, double radius, double power) {
		List<MiniParticle> particles = new ArrayList<>();

		switch (type) {
		case ICE -> {
			for (int i = 0; i < 20; i++) {
				MiniParticle p = MiniParticle.newRadial(rand, radius, 0.0, 0.0);
				p.maxLife = 15;
				p.hibernateTime = rand.nextInt(5);
				particles.add(p);
			}
		}
		case FIRE -> {
			for (int i = 0; i < 30; i++) {
				MiniParticle p = MiniParticle.newRadial(rand, radius * 0.75, 0.1, -0.002);
				p.maxLife = 15 + rand.nextInt(10);
				p.ay = 0.03;
				p.hibernateTime = rand.nextInt(5);
				p.drag = 0.8;
				p.uniqueness = rand.nextDouble();
				particles.add(p);
			}
		}
		case LIGHTNING -> {
			for (int i = 0; i < 30; i++) {
				MiniParticle p = MiniParticle.newRadial(rand, radius, 0.0, 0.0);
				p.uniqueness = rand.nextDouble();
				p.maxLife = 15;
				p.hibernateTime = rand.nextInt(5);
				particles.add(p);
			}
		}
		case STAR_IMPACT -> {
			// no custom geometry, just block conversion
			for (int i = 0; i < 50; i++) {
				MiniParticle p = MiniParticle.newRadial(rand, radius, 0.0, 0.0);
				p.maxLife = 5;
				p.hibernateTime = rand.nextInt(15);
				particles.add(p);
			}
		}
		case ORB_EXPLOSION -> {
			// minimal — mostly handled by vanilla explosion particle via clientTick
		}
		default -> {
		}
		}

		return particles;
	}

	// -- Per-type shape rendering --

	private void renderMiniParticle(PoseStack poseStack, VertexConsumer lineBuilder, VertexConsumer triBuilder, MiniParticle mip, SpellType type, double power) {
		double age = mip.age;

		switch (type) {
		case STAR_IMPACT -> {
			// twinkle crosses
			float r = 1.0f, g = (float) (1.5 - age), b = (float) (0.5 + age), a = 1.0f;
			double size = Math.sin(age * Math.PI) * (power - 1.0) / 4.0;
			drawCross(poseStack, lineBuilder, 2, size, r, g, b, a);
		}
		case FIRE -> {
			float r = 1.0f, g = (float) (2.0 - age * 2.0), b = (float) (1.0 - age * 2.0), a = 1.0f;
			double size = Math.sin(age * Math.PI * 3.0 / 4.0 + Math.PI / 4.0) * power / 8.0;
			float rot = (float) (age * 360.0);
			drawPolySolid(poseStack, triBuilder, 4, size, rot, r, g, b, a);
		}
		case LIGHTNING -> {
			if (age < 0.5) {
				double f = Math.sin((age + mip.uniqueness) * 30.0) * 0.5 + 0.5;
				float r = (float) (f * 2.0), g = 1.0f, b = (float) (2.0 - f * 2.0), a = 1.0f;
				double s = power * 0.15;
				double phase = Math.sin(age * Math.PI * 4.0);
				drawDiamondZig(poseStack, lineBuilder, s, s * 2.0, phase, r, g, b, a);
			} else {
				double f = Math.sin((age + mip.uniqueness) * 30.0) * 0.5 + 0.5;
				float r = (float) (f * 2.0), g = 1.0f, b = (float) (2.0 - f * 2.0), a = 1.0f;
				double phase = age * 2.0 - 1.0;
				double h = Math.sin(phase * Math.PI) * 2.5;
				// beam from origin through particle position
				drawLine(poseStack, lineBuilder, (float) -mip.x, (float) -mip.y, (float) -mip.z, (float) (mip.x * h - mip.x), (float) (mip.y * h - mip.y), (float) (mip.z * h - mip.z), r, g, b, a);
			}
		}
		case ICE -> {
			float fade = (float) (1.0 - Math.sin(age * Math.PI));
			float r = 0.5f + fade * 0.5f, g = 0.5f * fade + 0.5f, b = 1.0f, a = 1.0f;
			double size = power / 4.0;
			if (age < 0.5) {
				drawCross(poseStack, lineBuilder, 3, size * (1.0 - age * 2.0) * 2.0, r, g, b, a);
			} else if (age < 0.75) {
				drawPolySolid(poseStack, triBuilder, 6, size * (age - 0.5) * 4.0, 90f, r, g, b, a);
			} else {
				drawPolyOutline(poseStack, lineBuilder, 6, size, r, g, b, a);
			}
		}
		default -> {
		}
		}
	}

	// ========== Geometry helpers ==========

	// -- Lines: cross pattern --
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

	// -- Solid polygon (triangle fan emulated as triangles) --
	private void drawPolySolid(PoseStack poseStack, VertexConsumer builder, int sides, double size, float rotation, float r, float g, float b, float a) {
		poseStack.pushPose();
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		Matrix4f matrix = poseStack.last().pose();

		double dTh = Math.PI * 2.0 / sides;
		for (int i = 0; i < sides; i++) {
			double th1 = i * dTh + Math.toRadians(rotation);
			double th2 = (i + 1) * dTh + Math.toRadians(rotation);
			// triangle: center, vertex i, vertex i+1
			vertex(builder, matrix, 0, 0, 0, r, g, b, a);
			vertex(builder, matrix, (float) (Math.cos(th1) * size), (float) (Math.sin(th1) * size), 0, r, g, b, a);
			vertex(builder, matrix, (float) (Math.cos(th2) * size), (float) (Math.sin(th2) * size), 0, r, g, b, a);
			// degenerate 4th vertex for QUADS if needed, or use TRIANGLES render type
			vertex(builder, matrix, (float) (Math.cos(th2) * size), (float) (Math.sin(th2) * size), 0, r, g, b, a);
		}
		poseStack.popPose();
	}

	// -- Polygon outline --
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

	// -- Diamond zigzag (lightning) --
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

	// -- Simple line segment --
	private void drawLine(PoseStack poseStack, VertexConsumer builder, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {
		Matrix4f matrix = poseStack.last().pose();
		// compute normal for line rendering
		float dx = x2 - x1, dy = y2 - y1, dz = z2 - z1;
		float len = Mth.sqrt(dx * dx + dy * dy + dz * dz);
		if (len < 0.001f)
			return;
		float nx = dx / len, ny = dy / len, nz = dz / len;
		builder.vertex(matrix, x1, y1, z1).color(r, g, b, a).normal(poseStack.last().normal(), nx, ny, nz).endVertex();
		builder.vertex(matrix, x2, y2, z2).color(r, g, b, a).normal(poseStack.last().normal(), nx, ny, nz).endVertex();
	}

	// -- Horizontal ring (for crit flash) --
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

	// -- Vertex shorthand for lines --
	private void vertex(VertexConsumer builder, Matrix4f matrix, float x, float y, float z, float r, float g, float b, float a) {
		builder.vertex(matrix, x, y, z).color(r, g, b, a).normal(0, 1, 0).endVertex();
	}

	@Override
	public ResourceLocation getTextureLocation(SpellEffectEntity entity) {
		return null;
	}
}