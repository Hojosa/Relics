package hojosa.relics.common.init;

import hojosa.relics.lib.References;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RelicsItems{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MODID);
	
//	private static final List <Item> ITEMS = new ArrayList<>();
//	//public static final Item iceCrystal;
//	public static final Item flame_sword  = new SwordRelic(EnumHandler.flame, "flame_sword");
//	//public static Item leafSword;
//	public static final Item fire_plate = new FirePlate("fire_plate");
//	
//	public static Collection<Item> getItems()
//	{
//		return ITEMS;
//	}
//	
//	public static void register(Item item)
//	{
//		ITEMS.add(item);
//	}
//	
////	@SubscribeEvent
//	public static void registerItems(RegistryEvent.Register<Item> event)
//	{
//		for(Item itemRelics : ITEMS)
//		{
//			event.getRegistry().register(itemRelics.setCreativeTab(CreativeTabRelics.instance));
//		}
//	}
////	
////	@SideOnly(Side.CLIENT)
////	@SubscribeEvent
//	public static void registerModels(ModelRegistryEvent event)
//	{
//		for(Item itemModel : ITEMS)
//		{
//			ModelLoader.setCustomModelResourceLocation(itemModel, 0, new ModelResourceLocation(itemModel.getRegistryName(), "inventory"));
//		}
//	}
	
//	public static void init()
//	{
//		iceCrystal = new IceCrystal();
//		flameSword = new FlameSword(EnumHandler.flame, "flame");
//		//leafSword = new LeafSword(EnumHandler.leaf, "leaf");
//		firePlate = new FirePlate();
//		
//	}
//	
//	public static void register()
//	{
//		registerItem(iceCrystal, Strings.IceCrystalName);
//		registerItem(flameSword, Strings.FlameSwordName);
//		registerItem(leafSword, Strings.LeafSwordName);
//		registerItem(firePlate, Strings.FirePlateName);
//	}
//	
//	
//
//	public static void registerItem(Item item, String name)
//	{
//		//GameRegistry.register(item.setRegistryName(name));
//		//GameRegistry.registerItem(item, name);
//	}
//	
	

}
