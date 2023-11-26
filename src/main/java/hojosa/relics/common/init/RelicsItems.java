package hojosa.relics.common.init;

import hojosa.relics.common.item.FirePlate;
import hojosa.relics.common.item.RelicsItem;
import hojosa.relics.lib.References;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RelicsItems{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MODID);
	
    public static final RegistryObject<FirePlate> FIRE_PLATE = ITEMS.register(
            References.UnlocalizedName.FIRE_PLATE, () -> new FirePlate(1, Rarity.EPIC)
    );
    
    public static final RegistryObject<RelicsItem> WATER_PLATE = ITEMS.register(
    		References.UnlocalizedName.WATER_PLATE, () -> new RelicsItem(1, Rarity.EPIC)
    );
    
    public static final RegistryObject<RelicsItem> CLAY_BLUE = ITEMS.register(
    		References.UnlocalizedName.CLAY_BLUE, () -> new RelicsItem(64)
    );
    
    public static final RegistryObject<RelicsItem> BRICK_BLUE = ITEMS.register(
    		References.UnlocalizedName.BRICK_BLUE, () -> new RelicsItem(64)
    );
	
    public static final RegistryObject<SwordItem> FIRE_SWORD = ITEMS.register(
            References.UnlocalizedName.FIRE_SWORD, () -> new SwordItem(Tiers.IRON, 3, -2.4F, (new Item.Properties().tab(References.ITEM_GROUP)))
    );
    
    
    
    
    
    
    
    
}
