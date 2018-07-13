package relics.common.init;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mysticwater.core.handler.EnumHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import relics.Relics;
import relics.common.item.FirePlate;
import relics.common.item.SwordRelic;
import relics.common.util.CreativeTabRelics;

@GameRegistry.ObjectHolder(Relics.MOD_ID)
@Mod.EventBusSubscriber
public class ModItems
{
	private static final List <Item> ITEMS = new ArrayList<>();
	//public static final Item iceCrystal;
	public static final Item flame_sword  = new SwordRelic(EnumHandler.flame, "flame_sword");
	//public static Item leafSword;
	public static final Item fire_plate = new FirePlate("fire_plate");
	
	public static Collection<Item> getItems()
	{
		return ITEMS;
	}
	
	public static void register(Item item)
	{
		ITEMS.add(item);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		for(Item itemRelics : ITEMS)
		{
			event.getRegistry().register(itemRelics.setCreativeTab(CreativeTabRelics.instance));
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		for(Item itemModel : ITEMS)
		{
			ModelLoader.setCustomModelResourceLocation(itemModel, 0, new ModelResourceLocation(itemModel.getRegistryName(), "inventory"));
		}
	}
	
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
