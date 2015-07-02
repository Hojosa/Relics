package mysticwater.init;

import mysticwater.lib.References;
import mysticwater.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class Models
{
	
	public static void registerModelBlock(Block block, int meta, String name)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(References.MODID + ":" + name, "inventory"));
	}
	
	public static void registerModelItem(Item item, int meta, String name)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(References.MODID + ":" + name, "inventory"));
	}
	
	public static void registerMetaVariantBlock(Block block, String name, String variant)//, String modelFile)
	{
		ModelBakery.addVariantName(Item.getItemFromBlock(block), References.MODID + ":" + name + variant);
	}
	
	
	public static void register()
	{
		registerModelBlock(ModBlocks.lapisBrick, 0, Strings.LapisBrickName);
		registerModelBlock(ModBlocks.glassSlab, 0, Strings.GlassSlabName);
		registerModelBlock(ModBlocks.lapisBrickSlab, 0, Strings.LapisBrickSlabName);
		registerModelBlock(ModBlocks.stainedGlassSlab, 0, Strings.StainedGlassSlabName);
		//registerModelBlock(ModBlocks.stainedGlassSlab2, 0, Strings.StainedGlassSlabName + "2");
		
		
		registerModelItem(ModItems.firePlate, 0, Strings.FirePlateName);
		registerModelItem(ModItems.flameSword, 0, Strings.FlameSwordName);
		registerModelItem(ModItems.leafSword, 0, Strings.LeafSwordName);
		registerModelItem(ModItems.iceCrystal, 0, Strings.IceCrystalName);
		
		//registerMetaVariantBlock(ModBlocks.stainedGlassSlab, Strings.StainedGlassSlabName, "white");
	
	}
	
	public static void init()
	{
		register();	
	}
}
