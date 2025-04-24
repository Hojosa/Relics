package hojosa.relics.event;

import hojosa.relics.common.entity.StarBeamEntity;
import hojosa.relics.network.DismoutStarBeamPacket;
import hojosa.relics.network.RelicsNetwork;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EventBusSubscriber(Dist.CLIENT)
public class InputEvents {

    @SubscribeEvent
    public static void keyPress(InputEvent.Key event) {
        var minecraft = Minecraft.getInstance();
        if (minecraft.options.keyJump.isDown() && minecraft.player.getVehicle() instanceof StarBeamEntity starBeam) {
        	RelicsNetwork.getInstance().sendToServer(new DismoutStarBeamPacket());
        	minecraft.player.setDeltaMovement(starBeam.getMotion().x, starBeam.getMotion().y + 0.3, starBeam.getMotion().z);
        	starBeam.discard();
        }
    }
}
