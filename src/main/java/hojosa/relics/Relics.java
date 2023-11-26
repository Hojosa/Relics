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
import hojosa.relics.lib.References;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(References.MODID)
@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Relics {
    public static final Logger LOGGER = LogManager.getLogger(References.MODID);
    private static boolean curiosPresent;// = false;
	
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
        curiosPresent = ModList.get().isLoaded("curios");
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
    	if (curiosPresent) {
    		InterModComms.sendTo("curios",  SlotTypeMessage.REGISTER_TYPE, 
    				() -> SlotTypePreset.CHARM.getMessageBuilder().build());
    	}
    }
    
    
    
//	@Mod.Instance(Relics.MOD_ID)
//	public static Relics instance;
//	
//	@SidedProxy(clientSide = "com.hojosa.relics.proxy.ClientProxy", serverSide = "com.hojosa.relics.proxy.CommonProxy")
//	public static CommonProxy proxy;
//
//	@EventHandler
//	public void onServerStarting(FMLServerStartingEvent event) 
//	{
//		proxy.onServerStarting(event);
//	}
//	
//	@EventHandler
//	public void onPreInit(FMLPreInitializationEvent event)
//	{
//		System.out.println("CALL?");
//		proxy.onPreInit(event);
//		BlockPropertyHelper.init();
//		ModBlocks.init();
//		ModBlocks.register();
//		
//		ModItems.init();
//		ModItems.register();
//
//		CraftingHandler.init();
//		
//		
//		if(event.getSide() == Side.CLIENT)
//		{
//			OBJLoader.INSTANCE.addDomain(References.MODID);
//			B3DLoader.INSTANCE.addDomain(References.MODID);
//			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.swordPedestal), 0, new ModelResourceLocation(References.MODID + ":" + Strings.SwordPedestalName, "inventory"));
//		
//			
//			
//			Models.init();
//		}
		
		
		
		//proxy.registerRendering();
		
		//PacketHandler.init();
		
		//SwordPedestalUtil.preinit();
//	}
//
//	@EventHandler
//	public void onInit(FMLInitializationEvent event)
//	{	
//		proxy.onInit(event);
		//NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
//		if(event.getSide() == Side.CLIENT)
//			{
//				Models.init();
//				//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ModItems.firePlate, 0, new ModelResourceLocation("mysticwater:firePlate", "inventory"));
//				//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.lapisBrick), 0, new ModelResourceLocation(References.MODID + ":" + Strings.LapisBrickName , "inventory"));
//			}
//		proxy1.registerTileEntities();
//		proxy1.registerRendering();
//		
		//ModWorld.init();
//	}
//
//	@EventHandler
//	public void onPostInit(FMLPostInitializationEvent event)
//	{
//		proxy.onPostInit(event);
//	}
//	
//	@EventHandler
//	public void onServerStopping(FMLServerStoppingEvent event)
//	{
//		proxy.onServerStopping(event);
//	}

//	@SidedProxy(clientSide = References.CLIENTPROXYLOCATION, serverSide = References.COMMONPROXYLOCATION)
//	public static CommonProxy proxy;

}
