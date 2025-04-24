package hojosa.relics.event;

import hojosa.relics.client.particle.CustomParticle;
import hojosa.relics.client.particle.RelicsParticles;
import hojosa.relics.client.render.FallingStarRenderer;
import hojosa.relics.client.render.GlintBlockRenderer;
import hojosa.relics.client.render.InfusedStarstoneBlockRenderer;
import hojosa.relics.client.render.SkybeamBlockRenderer;
import hojosa.relics.client.render.StarBeamRenderer;
import hojosa.relics.client.render.SwordPedestalBlockRenderer;
import hojosa.relics.client.render.SwordPedestalStoneBlockRenderer;
import hojosa.relics.common.entity.StarBeamEntity;
import hojosa.relics.common.init.RelicsBlockEntities;
import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.common.init.RelicsEntities;
import hojosa.relics.lib.References;
import hojosa.relics.lib.RelicsBlockColor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
		event.registerBlockEntityRenderer(RelicsBlockEntities.GLINT_BLOCK_ENTITY.get(), GlintBlockRenderer::new);
		event.registerBlockEntityRenderer(RelicsBlockEntities.INFUSED_STARSTONE_BLOCK_ENTITY.get(), InfusedStarstoneBlockRenderer::new);
		event.registerBlockEntityRenderer(RelicsBlockEntities.SKYBEAM_BLOCK_ENTITY.get(), context -> new SkybeamBlockRenderer());
		event.registerEntityRenderer(RelicsEntities.FALLING_STAR.get(), FallingStarRenderer::new);
		event.registerEntityRenderer(RelicsEntities.STARBEAM.get(), StarBeamRenderer::new);
	}
	
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
    	event.registerSpriteSet(RelicsParticles.FLAME_PATTICLES.get(), CustomParticle.Provider::new);
    }
}
