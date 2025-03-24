package hojosa.relics.network;

import hojosa.relics.client.particle.RelicsParticles;
import hojosa.relics.common.init.RelicsSounds;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import slimeknights.mantle.network.packet.IThreadsafePacket;

@AllArgsConstructor
public class PhoenixParticlePacket implements IThreadsafePacket{
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
		    level.playSound(player, player.blockPosition(),RelicsSounds.REVIVE.get(), SoundSource.BLOCKS, 1f, 1f);
	 		for(int i = 0; i < 360; i++) {
	            if(i % 20 == 0) {
					level.addParticle(RelicsParticles.FLAME_PATTICLES.get(),
							packet.posX, packet.posY+0.4d, packet.posZ,
				    Math.cos(i) * 0.15d, 0.0d, Math.sin(i) * 0.15d);
	            }
			}
	    }
  }
}
