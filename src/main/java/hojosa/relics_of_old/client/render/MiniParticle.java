package hojosa.relics_of_old.client.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.world.phys.Vec3;

//Lightweight animation anchor point for spell effect geometry
@Deprecated
public class MiniParticle {
	public double x, y, z;
	public double vx, vy, vz;
	public double ax, ay, az;
	public int hibernateTime;
	public int maxLife = 20;
	public int lifeTicks;
	public double age;
	public double drag = 1.0;
	public double uniqueness;

	public MiniParticle() {
	}

	public MiniParticle(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	// create a particle at a random radial position within spread
	public static MiniParticle newRadial(Random rand, double spread, double velScale, double accScale) {
		Vec3 outward = new Vec3(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian()).normalize();
		double dist = rand.nextDouble() * spread;
		double gx = outward.x * dist;
		double gy = outward.y * dist;
		double gz = outward.z * dist;

		MiniParticle p = new MiniParticle();
		p.x = gx;
		p.y = gy;
		p.z = gz;
		p.vx = gx * velScale;
		p.vy = gy * velScale;
		p.vz = gz * velScale;
		p.ax = gx * accScale;
		p.ay = gy * accScale;
		p.az = gz * accScale;
		p.uniqueness = rand.nextDouble();
		return p;
	}

	// advance physics, returns false when dead
	public boolean tick() {
		if (hibernateTime-- > 0)
			return true;
		vx += ax;
		vy += ay;
		vz += az;
		vx *= drag;
		vy *= drag;
		vz *= drag;
		x += vx;
		y += vy;
		z += vz;
		lifeTicks++;
		age = (double) lifeTicks / (double) maxLife;
		return age < 1.0;
	}

	// tick all particles, remove dead ones
	public static List<MiniParticle> tickAll(List<MiniParticle> input) {
		ArrayList<MiniParticle> output = new ArrayList<>(input.size());
		for (MiniParticle p : input) {
			if (p.tick())
				output.add(p);
		}
		return output;
	}
}