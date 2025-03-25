package hojosa.relics.integration;

import hojosa.relics.integration.curios.CuriosIntegration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.fml.ModList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsIntegration {
	
	public static void load() {
		if(ModList.get().isLoaded("curios")) {
			CuriosIntegration.init();
		}
	}
}
