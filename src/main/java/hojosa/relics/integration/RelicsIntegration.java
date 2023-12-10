package hojosa.relics.integration;

import hojosa.relics.Relics;
import hojosa.relics.integration.curios.RelicsCuriosIntegration;
import net.minecraftforge.fml.ModList;

public class RelicsIntegration {
	
	public static void load() {
		if(ModList.get().isLoaded("curios")) {
			Relics.curiosPresent = true;
			RelicsCuriosIntegration.init();
		}
	}

}
