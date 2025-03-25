package hojosa.relics.integration.curios;

import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;


@SuppressWarnings("removal")
public class CuriosIntegration {
	private CuriosIntegration() {
		// Private constructor to hide the implicit public one.
	}
	
    public static void init() {
		InterModComms.sendTo(CuriosApi.MODID,  SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
	}
}
