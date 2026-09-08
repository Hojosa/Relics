package hojosa.relics_of_old.network;

import hojosa.relics_of_old.common.player.PlayerGlideDataProvider;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;
import slimeknights.mantle.network.packet.IThreadsafePacket;

@AllArgsConstructor
public class GlideSyncPacket implements IThreadsafePacket {

	private final float glideCharge;

    public GlideSyncPacket(FriendlyByteBuf buffer) {
        glideCharge = buffer.readFloat();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeFloat(glideCharge);
    }

    @Override
    public void handleThreadsafe(Context context) {
        HandleClient.handle(this);
    }

    private static class HandleClient {
        private static void handle(GlideSyncPacket packet) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(PlayerGlideDataProvider.PLAYER_GLIDE_DATA).ifPresent(glide -> {
                    glide.setGlideCharge(packet.glideCharge);
                });
            }
        }
    }
}