package com.hojosa.relics;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import mysticwater.lib.References;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import com.hojosa.relics.common.util.CreativeTabRelics;
import com.hojosa.relics.proxy.CommonProxy;


@Mod(modid = Relics.MOD_ID, name = "Relics", version = "@MOD_VERSION@")
public class Relics
{
	public final String FINGERPRINT = "@FINGERPRINT@";
	public static final String MOD_ID = "relics";
	public static final Logger LOGGER = (Logger) LogManager.getLogger(Relics.MOD_ID);
	
	@Mod.Instance(Relics.MOD_ID)
	public static Relics instance;
	
	@SidedProxy(clientSide = "com.hojosa.relics.proxy.ClientProxy", serverSide = "com.hojosa.relics.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) 
	{
		proxy.onServerStarting(event);
	}
	
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		System.out.println("CALL?");
		proxy.onPreInit(event);
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
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event)
	{	
		proxy.onInit(event);
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
	}

	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event)
	{
		proxy.onPostInit(event);
	}
	
	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent event)
	{
		proxy.onServerStopping(event);
	}

//	@SidedProxy(clientSide = References.CLIENTPROXYLOCATION, serverSide = References.COMMONPROXYLOCATION)
//	public static CommonProxy proxy;

}
