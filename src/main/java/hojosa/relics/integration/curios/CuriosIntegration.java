package hojosa.relics.integration.curios;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("removal")
public class CuriosIntegration {
	
    public static void init() {
		InterModComms.sendTo(CuriosApi.MODID,  SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
	}
}
