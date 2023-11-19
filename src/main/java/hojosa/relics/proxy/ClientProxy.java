package hojosa.relics.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;


public class ClientProxy extends CommonProxy
{
	public void onServerStarting(FMLServerStartingEvent event)
	{
		super.onServerStarting(event);
	}
	
	public void onPreInit(FMLPreInitializationEvent event)
	{
		super.onPreInit(event);
		//MinecraftForge.EVENT_BUS.register(new ModBlocks());
	}
	
	public void onInit(FMLInitializationEvent event)
	{
		super.onInit(event);
	}
	
	public void onPostInit(FMLPostInitializationEvent event)
	{
		super.onPostInit(event);
	}
	
	public void onServerStopping(FMLServerStoppingEvent event)
	{
		super.onServerStopping(event);
	}
	
	public ClientProxy getClientProxy()
	{
		return null;
	}
	
	
	
	@Override
	public void registerRendering()
	{
		
		
		
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySwordPedestal.class, new TESwordPedestalSpecialRenderer());
	
		//RenderingRegistry.registerBlockHandler(SwordPedestalUtil.PEDESTAL_RENDERER_ID, new RenderBlockPedestal());
		//SwordPedestalUtil var10000 = SwordPedestalUtil.instance;
        //MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.swordPedestal), new RenderItemPedestal(new TileEntitySwordPedestalRenderer(), new SwordPedestalTE()));
       // RenderingRegistry.registerBlockHandler(SwordPedestalUtil.PEDESTAL_RENDERER_ID, new RenderBlockPedestal());
		//ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.swordPedestal), 0, TileEntitySwordPedestal.class);
	}

}
