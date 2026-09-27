package hojosa.relics_of_old.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.world.item.ItemStack;

public class InfusionItemParticle extends BreakingItemParticle {
	private final double startX, startY, startZ;
	private final double targetX, targetY, targetZ;

	public InfusionItemParticle(ClientLevel level, double x, double y, double z, double targetX, double targetY, double targetZ, ItemStack stack) {
		super(level, x, y, z, stack);
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetZ = targetZ;
		this.lifetime = 8 + this.random.nextInt(5); // 8-12 ticks
		this.gravity = 0;

		// Small perpendicular offset so the stream has depth
		double offX = (this.random.nextDouble() - 0.5) * 0.45;
		double offY = (this.random.nextDouble() - 0.5) * 0.45;
		double offZ = (this.random.nextDouble() - 0.5) * 0.45;
		this.startX = x + offX;
		this.startY = y + offY;
		this.startZ = z + offZ;

		// Snap to offset start position
		this.setPos(this.startX, this.startY, this.startZ);
		this.xo = this.startX;
		this.yo = this.startY;
		this.zo = this.startZ;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if (this.age++ >= this.lifetime) {
			this.remove();
			return;
		}

		// Straight line interpolation from start to target
		float t = (float) this.age / this.lifetime;
		this.setPos(startX + (targetX - startX) * t, startY + (targetY - startY) * t, startZ + (targetZ - startZ) * t);
	}

	// Spawns directly into the particle engine — no registration needed
	public static void spawn(ItemStack stack, double x, double y, double z, double targetX, double targetY, double targetZ) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null)
			return;
		Minecraft.getInstance().particleEngine.add(new InfusionItemParticle(level, x, y, z, targetX, targetY, targetZ, stack));
	}
}