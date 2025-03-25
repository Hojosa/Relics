package hojosa.relics.integration;

import hojosa.relics.integration.curios.CuriosIntegration;
import net.minecraftforge.fml.ModList;

public class RelicsIntegration {
	private RelicsIntegration() {
		// Private constructor to hide the implicit public one.
	}
	
	public static void load() {
		if(ModList.get().isLoaded("curios")) {
			CuriosIntegration.init();
		}
	}
}
