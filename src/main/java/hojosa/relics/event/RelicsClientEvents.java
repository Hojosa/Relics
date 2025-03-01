package hojosa.relics.event;

import hojosa.relics.client.particle.RelicsParticles;
import hojosa.relics.client.particle.StarParticles;
import hojosa.relics.client.render.FallingStarRenderer;
import hojosa.relics.client.render.GlintBlockRenderer;
import hojosa.relics.client.render.SkybeamBlockRenderer;
import hojosa.relics.client.render.SwordPedestalBlockRenderer;
import hojosa.relics.client.render.SwordPedestalStoneBlockRenderer;
import hojosa.relics.common.init.RelicsBlockEntities;
import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.common.init.RelicsEntities;
import hojosa.relics.lib.References;
import hojosa.relics.lib.RelicsBlockColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RelicsClientEvents {

	@SubscribeEvent
	public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
		event.register(new RelicsBlockColor(), RelicsBlocks.SWORD_PEDESTAL_NORMAL.get(), RelicsBlocks.SWORD_PEDESTAL_RELIC.get(), RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS.get(),
				RelicsBlocks.SWORD_PEDESTAL_TIME.get(), RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get());
	}
	
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), context -> new SwordPedestalBlockRenderer());
		event.registerBlockEntityRenderer(RelicsBlockEntities.REXTURED_SWORD_PEDESTAL_BLOCK_ENTITY.get(), context -> new SwordPedestalBlockRenderer());
		event.registerBlockEntityRenderer(RelicsBlockEntities.SWORD_PEDESTAL_STONE_BLOCK_ENTITY.get(), context -> new SwordPedestalStoneBlockRenderer());
		event.registerBlockEntityRenderer(RelicsBlockEntities.GLINT_BLOCK.get(), context -> new GlintBlockRenderer(context));
		event.registerBlockEntityRenderer(RelicsBlockEntities.SKYBEAM_BLOCK.get(), context -> new SkybeamBlockRenderer());
		event.registerEntityRenderer(RelicsEntities.FALLING_STAR.get(), context -> new FallingStarRenderer(context));
	}
	
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent  event) {
    	event.registerSpriteSet(RelicsParticles.STAR_PATTICLES.get(), StarParticles.Provider::new);
    }
}
