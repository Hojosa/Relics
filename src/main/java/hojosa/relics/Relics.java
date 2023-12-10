package hojosa.relics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hojosa.relics.client.init.RelicsBlockEntityRenderers;
import hojosa.relics.client.render.RelicsBlockRenders;
import hojosa.relics.common.init.RelicsBlockEntities;
import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.common.init.RelicsLootModifiers;
import hojosa.relics.common.init.RelicsSounds;
import hojosa.relics.integration.RelicsIntegration;
import hojosa.relics.lib.References;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(References.MODID)
@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Relics {
    public static final Logger LOGGER = LogManager.getLogger(References.MODID);
    public static boolean curiosPresent = false;
	
    public Relics() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetupEvent);
        modEventBus.addListener(this::onRegisterRenderers);
        modEventBus.addListener(this::enqueueIMC);
        
        RelicsBlocks.BLOCKS.register(modEventBus);
        RelicsItems.ITEMS.register(modEventBus);
        RelicsBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        RelicsSounds.SOUNDS.register(modEventBus);
        RelicsLootModifiers.register(modEventBus);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void clientSetupEvent(final FMLClientSetupEvent event) {
        RelicsBlockRenders.setRenderLayers();
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Do nothing
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do nothing
    }
    
    public void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
    	RelicsBlockEntityRenderers.register(event);
    }
    
    private void enqueueIMC(final InterModEnqueueEvent event) {
        RelicsIntegration.load();
    }
}
