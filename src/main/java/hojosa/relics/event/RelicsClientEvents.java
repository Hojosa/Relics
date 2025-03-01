package hojosa.relics.client.init;

import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.lib.References;
import hojosa.relics.lib.RelicsBlockColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RelicsBlockColorHandler {

	@SubscribeEvent
	public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
		event.register(new RelicsBlockColor(), RelicsBlocks.SWORD_PEDESTAL_NORMAL.get(), RelicsBlocks.SWORD_PEDESTAL_RELIC.get(), RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS.get(),
				RelicsBlocks.SWORD_PEDESTAL_TIME.get(), RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get());
	}
}
