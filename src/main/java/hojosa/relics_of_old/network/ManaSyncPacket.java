package hojosa.relics_of_old.network;

import hojosa.relics_of_old.common.player.PlayerManaProvider;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;
import slimeknights.mantle.network.packet.IThreadsafePacket;

@AllArgsConstructor
public class ManaSyncPacket implements IThreadsafePacket {

	private final float fatigue;

    public ManaSyncPacket(FriendlyByteBuf buffer) {
        fatigue = buffer.readFloat();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeFloat(fatigue);
    }

    @Override
    public void handleThreadsafe(Context context) {
        HandleClient.handle(this);
    }

    private static class HandleClient {
        private static void handle(ManaSyncPacket packet) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                    mana.setFatigueClient(packet.fatigue);
                });
            }
        }
    }
}