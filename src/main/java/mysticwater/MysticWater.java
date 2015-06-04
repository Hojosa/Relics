package mysticwater;

import mysticwater.core.handler.CraftingHandler;
import mysticwater.core.proxy.CommonProxy;
import mysticwater.creativtab.MysticWaterTab;
import mysticwater.init.ModBlocks;
import mysticwater.init.ModItems;
import mysticwater.init.Models;
import mysticwater.lib.References;
import mysticwater.lib.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;


@Mod(modid = References.MODID, name = References.MODNAME, version = References.VERSION)
public class MysticWater
{	
	@Mod.Instance
	public static MysticWater instance;
	
	private static CreativeTabs mysticwaterTab = new MysticWaterTab(CreativeTabs.getNextID(), References.MODID);

	public static CreativeTabs getCreativTab()
	{
		return mysticwaterTab;
	}

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		ModBlocks.init();
		ModBlocks.register();
		
		ModItems.init();
		ModItems.register();

		CraftingHandler.init();
		
		
		//proxy.registerTileEntities();
		
		//proxy.registerRendering();
		
		//PacketHandler.init();
		
		//SwordPedestalUtil.preinit();
	}

	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		
		//NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
		if(event.getSide() == Side.CLIENT)
			{
				Models.init();
				//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ModItems.firePlate, 0, new ModelResourceLocation("mysticwater:firePlate", "inventory"));
				//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.lapisBrick), 0, new ModelResourceLocation(References.MODID + ":" + Strings.LapisBrickName , "inventory"));
			}
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		

	}

	@SidedProxy(clientSide = References.CLIENTPROXYLOCATION, serverSide = References.COMMONPROXYLOCATION)
	public static CommonProxy proxy;

}
