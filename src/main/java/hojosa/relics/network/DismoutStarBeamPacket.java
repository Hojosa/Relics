package hojosa.relics.network;

import lombok.AllArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;
import slimeknights.mantle.network.packet.IThreadsafePacket;

@AllArgsConstructor
public class DismoutStarBeamPacket implements IThreadsafePacket{
	
	public DismoutStarBeamPacket(FriendlyByteBuf buffer) {
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		//we dont need to encode anything, but we are forced to overwrite, since we use the threadsafe packet from mantel
	}

	@Override
	public void handleThreadsafe(Context context) {
		ServerPlayer sender =context.getSender();
		sender.getVehicle().discard();
	}
}
