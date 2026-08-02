package hojosa.relics_of_old.network;

import hojosa.relics_of_old.client.particle.RelicsParticles;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;
import slimeknights.mantle.network.packet.IThreadsafePacket;
import net.minecraft.world.level.Level;

@AllArgsConstructor
public class MagicScrambleParticlePacket implements IThreadsafePacket {
	private final double posX;
	private final double posY;
	private final double posZ;
	private final double velX;
	private final double velY;
	private final double velZ;

	public MagicScrambleParticlePacket(FriendlyByteBuf buffer) {
		posX = buffer.readDouble();
		posY = buffer.readDouble();
		posZ = buffer.readDouble();
		velX = buffer.readDouble();
		velY = buffer.readDouble();
		velZ = buffer.readDouble();
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeDouble(posX);
		buffer.writeDouble(posY);
		buffer.writeDouble(posZ);
		buffer.writeDouble(velX);
		buffer.writeDouble(velY);
		buffer.writeDouble(velZ);
	}

	@Override
	public void handleThreadsafe(Context context) {
		HandleClient.handle(this);
	}

	private static class HandleClient {
		private static void handle(MagicScrambleParticlePacket packet) {
			Level level = Minecraft.getInstance().level;
			level.addParticle(new RelicsParticleOptions(RelicsParticles.MAGIC_SCRAMBLE_PARTICLE, 15, 0.4f), packet.posX, packet.posY, packet.posZ, packet.velX, packet.velY, packet.velZ);
		}
	}
}