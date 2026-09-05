package hojosa.relics_of_old.network;

import java.util.Random;

import hojosa.relics_of_old.client.particle.SpellFireParticle;
import hojosa.relics_of_old.common.init.RelicsParticles;
import hojosa.relics_of_old.common.init.RelicsSounds;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import slimeknights.mantle.network.packet.IThreadsafePacket;

@AllArgsConstructor
public class PhoenixParticlePacket implements IThreadsafePacket {
	private final double posX;
	private final double posY;
	private final double posZ;

	public PhoenixParticlePacket(FriendlyByteBuf buffer) {
		posX = buffer.readDouble();
		posY = buffer.readDouble();
		posZ = buffer.readDouble();
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeDouble(posX);
		buffer.writeDouble(posY);
		buffer.writeDouble(posZ);
	}

	@Override
	public void handleThreadsafe(Context context) {
		HandleClient.handle(this);
	}

	private static class HandleClient {
		private static void handle(PhoenixParticlePacket packet) {
			Level level = Minecraft.getInstance().level;
			Player player = Minecraft.getInstance().player;
			level.playSound(player, player.blockPosition(), RelicsSounds.REVIVE.get(), SoundSource.BLOCKS, 1f, 1f);
			for (int i = 0; i < 360; i++) {
				if (i % 20 == 0) {
					level.addParticle(RelicsParticles.FLAME_PATTICLES.get(), packet.posX, packet.posY + 0.4d, packet.posZ, Math.cos(i) * 0.15d, 0.0d, Math.sin(i) * 0.15d);
				}
			}
			Random rand = new Random();
			  for (int i = 0; i < 20; i++) {
			      double angle = rand.nextDouble() * Math.PI * 2;
			      double dist = rand.nextDouble() * 1.0; // within 1 block radius
			      double gx = Math.cos(angle) * dist;
			      double gz = Math.sin(angle) * dist;
			      SpellFireParticle p = new SpellFireParticle((ClientLevel) level,
			          packet.posX + gx, packet.posY + 0.1, packet.posZ + gz,
			          gx * 0.02, 0.05, gz * 0.02,  // gentle outward + upward velocity
			          2.0,              // power: small (max size = 2.0/8.0 = 0.25 blocks)
			          20 + rand.nextInt(10),  // lifetime
			          rand.nextInt(8),        // stagger
			          0.85,                   // drag
			          0.0, 0.02, 0.0);            // upward acceleration only
			      Minecraft.getInstance().particleEngine.add(p);
			  }
		}
	}
}