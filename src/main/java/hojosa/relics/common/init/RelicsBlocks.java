package com.hojosa.relics.common.init;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.hojosa.relics.Relics;
import com.hojosa.relics.common.block.LapisBrick;
import com.hojosa.relics.common.block.SwordPedestalBlock;
import com.hojosa.relics.common.util.CreativeTabRelics;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@GameRegistry.ObjectHolder(Relics.MOD_ID)
@Mod.EventBusSubscriber
public class ModBlocks
{
	private static final List <Block> BLOCKS = new ArrayList<>();
	
	public static final Block lapis_brick = new LapisBrick(Material.ROCK, "lapis_brick");
	public static final Block sword_pedestal = new SwordPedestalBlock(Material.ROCK, "sword_pedestal");

	public static Collection<Block> getBlocks()
	{
		return BLOCKS;
	}
	
	public static void register(Block block) 
	{
		BLOCKS.add(block);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		for(Block blockRelics : BLOCKS)
		{
			event.getRegistry().register(blockRelics.setCreativeTab(CreativeTabRelics.instance));
		}
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event)
	{
		for(Block itemBlockRelics : BLOCKS)
		{
			event.getRegistry().register(new ItemBlock(itemBlockRelics).setRegistryName(itemBlockRelics.getRegistryName()));
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		for(Block modelBlock : BLOCKS)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(modelBlock), 0, new ModelResourceLocation(modelBlock.getRegistryName(), "inventory"));
		}
	}
	
	
	
	
	
	
	
	

//  public static final Block lapisBrick;
//	public static Block swordPedestal;
//	public static Block stainedGlassSlab;
//	public static Block glassSlab;
//	public static Block doubleGlassSlab;
//	public static Block doubleStainedGlassSlab;
//	public static Block stainedGlassSlab2;
//	public static Block doubleStainedGlassSlab2;
//	public static Block otherSlabs;
//	public static Block doubleOtherSlabs;
//	public static Block portalBlock;
//	public static Block customModelBlock;
//	public static Block basicSwordPedestal;
//
//
//
//	public static void init()
//	{
//		lapisBrick = new LapisBrick();
//
//		stainedGlassSlab = new SingleSlab(Material.GLASS, Category.SLABCOLOR1GLASS, Strings.StainedGlassSlabName, false);
//		doubleStainedGlassSlab = new DoubleSlab(Material.GLASS, Category.SLABCOLOR1GLASS, Strings.StainedDoubleGlassSlabName, true);
//		stainedGlassSlab2 = new SingleSlab(Material.GLASS, Category.SLABCOLOR2GLASS, Strings.StainedGlassSlabName + "2", false);
//		doubleStainedGlassSlab2 = new DoubleSlab(Material.GLASS, Category.SLABCOLOR2GLASS, Strings.StainedDoubleGlassSlabName + "2", true);
//		
//		glassSlab = new SingleSlab(Material.GLASS, Category.GLASS, Strings.GlassSlabName, false);
//		doubleGlassSlab = new DoubleSlab(Material.GLASS, Category.GLASS, Strings.DoubleGlassName, true);
//		
//		otherSlabs = new SingleSlab(Material.ROCK, Category.OTHER, Strings.OtherSlabName, false);//, Category.LAPIS);
//		doubleOtherSlabs = new DoubleSlab(Material.ROCK, Category.OTHER, Strings.DoubleOtherSlabName, true);//, Category.LAPIS);
//		
//		portalBlock = new PortalBlock();
//		swordPedestal = new SwordPedestalBlock(Material.ROCK, Strings.SwordPedestalName);
//		basicSwordPedestal = new SwordPedestalBlock(Material.ROCK, Strings.BasicSwordPedestalName);
//		
//		//customModelBlock = CustomModelBlock.instance;
//	
//	}
//	
//	public static void register()
//	{
//		registerBlock(lapisBrick, Strings.LapisBrickName);
//		
//		registerBlockSlab(stainedGlassSlab, ItemBlockSlab.class, Strings.StainedGlassSlabName, stainedGlassSlab, doubleStainedGlassSlab);
//		registerBlockSlab(doubleStainedGlassSlab, ItemBlockSlab.class, Strings.StainedDoubleGlassSlabName, stainedGlassSlab, doubleStainedGlassSlab);
//		
//		registerBlockSlab(stainedGlassSlab2, ItemBlockSlab.class, Strings.StainedGlassSlabName + "2", stainedGlassSlab2, doubleStainedGlassSlab2);
//		registerBlockSlab(doubleStainedGlassSlab2, ItemBlockSlab.class, Strings.StainedDoubleGlassSlabName + "2", stainedGlassSlab2, doubleStainedGlassSlab2);
//		
//		registerBlockSlab(otherSlabs, ItemBlockSlab.class, Strings.OtherSlabName, otherSlabs, doubleOtherSlabs);
//		registerBlockSlab(doubleOtherSlabs, ItemBlockSlab.class, Strings.DoubleOtherSlabName, otherSlabs, doubleOtherSlabs);
//		
//		registerBlockSlab(glassSlab, ItemBlockSlab.class, Strings.GlassSlabName, glassSlab, doubleGlassSlab);
//		registerBlockSlab(doubleGlassSlab, ItemBlockSlab.class, Strings.DoubleGlassName, glassSlab, doubleGlassSlab);
//		
//		registerBlock(portalBlock, Strings.PortalBlock);
//		registerBlock(swordPedestal, Strings.SwordPedestalName);
//		
//		registerBlock(basicSwordPedestal, Strings.BasicSwordPedestalName);
//		
//		//registerBlock(customModelBlock, References.MODID + ":"+ "CustomModelBlock");
//		List<Block> blocks = Lists.newArrayList();
//		blocks.add(CustomModelBlock.instance);
//		 for(Block block : blocks)
//	        {
//	            GameRegistry.register(block);
//	            GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
//	}
//		
//		
//	}
//	
//	
//	public static void registerBlock(Block block, String name)
//	{
//		GameRegistry.register(block.setRegistryName(name));
//		GameRegistry.register(new ItemBlock(block).setRegistryName(name));
//		//GameRegistry.registerBlock(block, name);
//	}
//	
//	
//	public static void registerBlockSlab(Block block, Class<?extends ItemBlock>itemclass, String name, Object... itemCtorArgs)
//	{
//		GameRegistry.register(block.setRegistryName(name));
//		GameRegistry.register(new ItemBlockSlab(block, (SingleSlab)itemCtorArgs[0], (DoubleSlab)itemCtorArgs[1]).setRegistryName(name));
//	
//		//GameRegistry.registerBlock(block, itemclass, name, itemCtorArgs);
//	}
//	
}
