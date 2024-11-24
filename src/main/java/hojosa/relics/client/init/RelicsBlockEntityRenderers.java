package hojosa.relics.client.init;

import hojosa.relics.client.render.SwordPedestalBlockRenderer;
import hojosa.relics.client.render.SwordPedestalBlockRendererNew;
import hojosa.relics.common.init.RelicsBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RelicsBlockEntityRenderers {
	
	@SubscribeEvent
	public static void register(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), context -> new SwordPedestalBlockRendererNew());
	}	
}
