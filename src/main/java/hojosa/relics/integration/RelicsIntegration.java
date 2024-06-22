package hojosa.relics.integration;

import hojosa.relics.integration.curios.CuriosIntegration;
import net.minecraftforge.fml.ModList;

public class RelicsIntegration {
	
	public static void load() {
		if(ModList.get().isLoaded("curios")) {
			CuriosIntegration.init();
		}
	}
}
