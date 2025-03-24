package hojosa.relics.common.init;

import hojosa.relics.common.item.FireTablet;
import hojosa.relics.common.item.InfuseableItem;
import hojosa.relics.common.item.InfusedItem;
import hojosa.relics.common.item.RelicsItem;
import hojosa.relics.common.item.WaterTablet;
import hojosa.relics.lib.References;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RelicsItems{
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MOD_ID);
	
    public static final RegistryObject<FireTablet> FIRE_TABLET = ITEMS.register(
            References.UnlocalizedName.FIRE_PLATE, () -> new FireTablet(1, Rarity.EPIC)
    );
    
    public static final RegistryObject<WaterTablet> WATER_TABLET = ITEMS.register(
            References.UnlocalizedName.WATER_PLATE, () -> new WaterTablet(1, Rarity.EPIC)
    );
    
    public static final RegistryObject<RelicsItem> CLAY_BLUE = ITEMS.register(
    		References.UnlocalizedName.CLAY_BLUE, () -> new RelicsItem(64)
    );
    
    public static final RegistryObject<RelicsItem> BRICK_BLUE = ITEMS.register(
    		References.UnlocalizedName.BRICK_BLUE, () -> new RelicsItem(64)
    );
	
    public static final RegistryObject<SwordItem> FIRE_SWORD = ITEMS.register(
            References.UnlocalizedName.FIRE_SWORD, () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties())
    );
    
    public static final RegistryObject<SwordItem> MASTER_SWORD = ITEMS.register(
            References.UnlocalizedName.MASTER_SWORD, () -> new SwordItem(Tiers.DIAMOND, 5, -2.4F, new Item.Properties())
    );
    
    public static final RegistryObject<RelicsItem> INFUSED_STAR_PIECE = ITEMS.register(
    		References.UnlocalizedName.INFUSED_STAR_PIECE, () -> new InfusedItem(64, Rarity.EPIC)
    );
    
    public static final RegistryObject<RelicsItem> STAR_PIECE = ITEMS.register(
    		References.UnlocalizedName.STAR_PIECE, () -> new InfuseableItem(64, Rarity.UNCOMMON, INFUSED_STAR_PIECE)
    );
    
    public static final RegistryObject<RelicsItem> STAR_DUST = ITEMS.register(
    		References.UnlocalizedName.STAR_DUST, () -> new RelicsItem(64, Rarity.UNCOMMON)
    );
    
    public static final RegistryObject<RelicsItem> STAR_STONE = ITEMS.register(
    		References.UnlocalizedName.STAR_STONE, () -> new RelicsItem(64, Rarity.UNCOMMON)
    );
    
    public static final RegistryObject<RelicsItem> INFUSED_STAR_DUST = ITEMS.register(
    		References.UnlocalizedName.INFUSED_STAR_DUST, () -> new InfusedItem(64, Rarity.EPIC)
    );
    public static final RegistryObject<RelicsItem> INFUSED_STAR_STONE = ITEMS.register(
    		References.UnlocalizedName.INFUSED_STAR_STONE, () -> new InfusedItem(64, Rarity.EPIC)
    );
    //Make fire resistant
    public static final RegistryObject<RelicsItem> PHOENIX_FEATHER = ITEMS.register(
    		References.UnlocalizedName.PHOENIX_FEATHER, () -> new InfusedItem(new Item.Properties().stacksTo(64).rarity(Rarity.RARE).fireResistant())
    );
    
    public static final RegistryObject<RelicsItem> EMERALD_SHARD = ITEMS.register(
    		References.UnlocalizedName.EMERALD_SHARD, () -> new RelicsItem(64)
    );
    
    public static final RegistryObject<RelicsItem> EMERALD_PIECE = ITEMS.register(
    		References.UnlocalizedName.EMERALD_PIECE, () -> new RelicsItem(64)
    );
    
    public static final RegistryObject<RelicsItem> HEART = ITEMS.register(
    		References.UnlocalizedName.HEART, () -> new RelicsItem(64)
    );
    
    public static final RegistryObject<RelicsItem> TOTEM_DUST = ITEMS.register(
    		References.UnlocalizedName.TOTEM_DUST, () -> new RelicsItem(64, Rarity.UNCOMMON)
    );
    
    public static void addTabItems(ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
    	RelicsItems.ITEMS.getEntries().forEach(itemRegistryObject -> {
          output.accept(new ItemStack(itemRegistryObject.get()));
          });
    }
}
