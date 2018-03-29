package mysticwater.init;

import mysticwater.core.handler.EnumHandler;
import mysticwater.items.FirePlate;
import mysticwater.items.FlameSword;
import mysticwater.items.IceCrystal;
import mysticwater.items.LeafSword;
import mysticwater.lib.Strings;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class ModItems
{
	public static Item iceCrystal;
	public static Item flameSword;
	public static Item leafSword;
	public static Item itemGlassSlabs;
	public static Item firePlate;
	
	
	public static void init()
	{
		iceCrystal = new IceCrystal();
		flameSword = new FlameSword(EnumHandler.flame, "flame");
		leafSword = new LeafSword(EnumHandler.leaf, "leaf");
		firePlate = new FirePlate();
		
	}
	
	public static void register()
	{
		registerItem(iceCrystal, Strings.IceCrystalName);
		registerItem(flameSword, Strings.FlameSwordName);
		registerItem(leafSword, Strings.LeafSwordName);
		registerItem(firePlate, Strings.FirePlateName);
	}
	
	

	public static void registerItem(Item item, String name)
	{
		GameRegistry.register(item.setRegistryName(name));
		//GameRegistry.registerItem(item, name);
	}
	
	

}
