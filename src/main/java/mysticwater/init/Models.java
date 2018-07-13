package mysticwater.init;

import mysticwater.blocks.DoubleSlab;
import mysticwater.core.handler.EnumHandler;
import mysticwater.core.handler.EnumHandler.Category;
import mysticwater.core.handler.EnumHandler.ColorSet;
import mysticwater.lib.References;
import mysticwater.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import relics.common.tileentity.TileEntitySwordPedestal;

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
	
	public static void registerMetaVariantBlock(Block block, String name)
	{
		ModelBakery.registerItemVariants(Item.getItemFromBlock(block), new ResourceLocation(References.MODID + ":" + name));
	}
	
	public static void registerBlockModelWithoutProperties(Block block, IProperty property)
	{
		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().registerBlockWithStateMapper(block, new StateMap.Builder().ignore(new IProperty[] {property}).build());
	}
	
	
	public static void register()
	{
		
		//registerModelBlock(ModBlocks.lapisBrick, 0, Strings.LapisBrickName);	
		//registerModelBlock(ModBlocks.basicSwordPedestal, 0, "test");
		
		//registerModelBlock(ModBlocks.swordPedestal, 0, Strings.SwordPedestalName);
		//ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.swordPedestal), 0, TileEntitySwordPedestal.class);
//		
//
//		for(int meta = 0; meta < 8; ++meta)
//		{
//			registerMetaVariantBlock(ModBlocks.stainedGlassSlab, Strings.StainedGlassSlabName + "_" + EnumHandler.Color1[meta]);
//			registerModelBlock(ModBlocks.stainedGlassSlab, meta, Strings.StainedGlassSlabName + "_" + EnumHandler.Color1[meta]);
//			registerMetaVariantBlock(ModBlocks.stainedGlassSlab2, Strings.StainedGlassSlabName + "_" + EnumHandler.Color2[meta]);
//			registerModelBlock(ModBlocks.stainedGlassSlab2, meta, Strings.StainedGlassSlabName + "_" + EnumHandler.Color2[meta]);
//			//System.out.println(meta + Strings.StainedGlassSlabName + 2 + "_" + EnumHandler.Color2[meta]);
//		}
//		registerBlockModelWithoutProperties(ModBlocks.doubleStainedGlassSlab, DoubleSlab.HALF);
//		registerBlockModelWithoutProperties(ModBlocks.doubleStainedGlassSlab2, DoubleSlab.HALF);
//		registerBlockModelWithoutProperties(ModBlocks.doubleGlassSlab, DoubleSlab.HALF);
//		registerBlockModelWithoutProperties(ModBlocks.doubleOtherSlabs, DoubleSlab.HALF);
//		
//		registerMetaVariantBlock(ModBlocks.otherSlabs, Strings.OtherSlabName + "_" + ColorSet.getStateList(Category.OTHER, false).get(0));
//		registerModelBlock(ModBlocks.otherSlabs, 0, Strings.OtherSlabName + "_" + ColorSet.getStateList(Category.OTHER, false).get(0));
//		registerModelBlock(ModBlocks.glassSlab, 0, Strings.GlassSlabName);
//		registerModelBlock(ModBlocks.portalBlock, 0, Strings.PortalBlock);
//		
//		
//		registerModelItem(ModItems.firePlate, 0, Strings.FirePlateName);
//		registerModelItem(ModItems.flameSword, 0, Strings.FlameSwordName);
//		registerModelItem(ModItems.leafSword, 0, Strings.LeafSwordName);
//		registerModelItem(ModItems.iceCrystal, 0, Strings.IceCrystalName);
//		
		//registerMetaVariantBlock(ModBlocks.stainedGlassSlab, Strings.StainedGlassSlabName, "white");
	
	}
	
	public static void init()
	{
		register();	
	}
}
