package hojosa.relics_of_old.client.particle;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;

//Rotating solid polygon particle (FIRE)
public class SpellFireParticle extends SpellBaseParticle {

	public SpellFireParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, double power, int maxLife, int hibernateTime, double drag, double ax, double ay, double az) {
		super(level, x, y, z, power, maxLife, hibernateTime);
		this.xd = vx;
		this.yd = vy;
		this.zd = vz;
		this.drag = drag;
		this.ax = ax;
		this.ay = ay;
		this.az = az;
	}

	@Override
	protected void renderShape(PoseStack poseStack, Camera camera, double t, float partialTicks) {
		// Orange → red gradient
		float r = 1.0f;
		float g = Math.min(1.0f, (float) (2.0 - t * 2.0));
		float b = Math.min(1.0f, (float) (1.0 - t * 2.0));
		double size = Math.sin(t * Math.PI * 3.0 / 4.0 + Math.PI / 4.0) * power / 8.0;
		float rot = (float) (t * 360.0);

		poseStack.pushPose();
		poseStack.mulPose(camera.rotation());
		Matrix4f matrix = poseStack.last().pose();

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		drawPolySolidQuads(builder, matrix, 4, size, rot, r, g, b, 1.0f);
		tesselator.end();

		poseStack.popPose();
	}
}