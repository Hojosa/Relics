package mysticwater.init;

import mysticwater.blocks.DoubleMetaSlab;
import mysticwater.blocks.DoubleSlab;
import mysticwater.blocks.LapisBrick;
import mysticwater.blocks.SingleMetaSlab;
import mysticwater.blocks.SingleSlab;
import mysticwater.core.handler.EnumHandler;
import mysticwater.core.handler.EnumHandler.Category;
import mysticwater.items.block.ItemBlockLapisBrickSlab;
import mysticwater.items.block.ItemBlockSlab;
import mysticwater.items.block.ItemBlockStainedGlassSlab;
import mysticwater.items.block.ItemBlockStainedGlassSlab2;
import mysticwater.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class ModBlocks
{
	public static Block lapisBrick;
	public static Block swordPedestal;
	public static Block stainedGlassSlab;
	public static Block glassSlab;
	public static Block doubleGlassSlab;
	public static Block doubleStainedGlassSlab;
	public static Block stainedGlassSlab2;
	public static Block doubleStainedGlassSlab2;
	
	public static Block testSlab;
	public static Block testdoubleSlab;
	public static Block lapisBrickSlab;
	public static Block doubleLapisBrickSlab;
	public static Block tür;

	public static void init()
	{
		glassSlab= new SingleSlab(Material.glass, Category.GLASS);
		doubleGlassSlab = new DoubleSlab(Material.glass, Category.GLASS);
		
		lapisBrick = new LapisBrick();
		//swordPedestal = new SwordPedestal(Material.rock);
		stainedGlassSlab = new SingleMetaSlab(Material.glass, EnumHandler.Category.COLOR1);
		doubleStainedGlassSlab = new DoubleMetaSlab(Material.glass, EnumHandler.Category.COLOR1);

		stainedGlassSlab2 = new SingleMetaSlab(Material.glass, EnumHandler.Category.COLOR2);
		doubleStainedGlassSlab2 = new DoubleMetaSlab(Material.glass, EnumHandler.Category.COLOR2);
		
		lapisBrickSlab = new SingleSlab(Material.rock, Category.LAPIS);//, Category.LAPIS);
		doubleLapisBrickSlab = new DoubleSlab(Material.rock, Category.LAPIS);//, Category.LAPIS);
	
	}
	
	public static void register()
	{
		registerBlock(lapisBrick, Strings.LapisBrickName);
		
		//registerBlock(swordPedestal, Strings.SwordPedestalName);
		
		registerBlock(glassSlab, ItemBlockSlab.class,  Strings.GlassSlabName);//, glassSlab, doubleGlassSlab, false);
		registerBlock(doubleGlassSlab, ItemBlockSlab.class, Strings.DoubleGlassName);//, glassSlab, doubleGlassSlab, true);
		
		
		registerBlock(stainedGlassSlab, ItemBlockStainedGlassSlab.class, Strings.StainedGlassSlabName);
		registerBlock(doubleStainedGlassSlab, ItemBlockStainedGlassSlab.class, Strings.StainedDoubleGlassSlabName);
		
		registerBlock(stainedGlassSlab2, ItemBlockStainedGlassSlab2.class, Strings.StainedGlassSlabName + "2");
		registerBlock(doubleStainedGlassSlab2, ItemBlockStainedGlassSlab2.class, Strings.StainedDoubleGlassSlabName + "2");
		
		registerBlock(lapisBrickSlab, ItemBlockLapisBrickSlab.class, Strings.LapisBrickSlabName);
		registerBlock(doubleLapisBrickSlab, ItemBlockLapisBrickSlab.class, Strings.DoubleLapisBrickSlabName);
	}
	
	
	public static void registerBlock(Block block, String name)
	{
		GameRegistry.registerBlock(block, name);
	}
	
	
	public static void registerBlock(Block block, Class<?extends ItemBlock>itemclass, String name)
	{
		GameRegistry.registerBlock(block, itemclass, name);
	}
	
}
