package com.hojosa.relics.proxy;

import com.hojosa.relics.common.tileentity.TileEntitySwordPedestal;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class CommonProxy
{
	public void onServerStarting(FMLServerStartingEvent event)
	{
		
	}
	
	public void onPreInit(FMLPreInitializationEvent event)
	{
		GameRegistry.registerTileEntity(TileEntitySwordPedestal.class, "sword_pedestal");
		//MinecraftForge.EVENT_BUS.register(new ModBlocks());
	}
	
	public void onInit(FMLInitializationEvent event)
	{
		
	}
	
	public void onPostInit(FMLPostInitializationEvent event)
	{
		
	}
	
	public void onServerStopping(FMLServerStoppingEvent event)
	{
		
	}
	
	public ClientProxy getClientProxy()
	{
		return null;
	}
	
	public void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntitySwordPedestal.class, "SwordPedestalTE");
	}
	public void registerRendering(){}

}
