package hojosa.relics.common.init;

import hojosa.relics.common.item.EmeraldShard;
import hojosa.relics.common.item.FireTablet;
import hojosa.relics.common.item.FlameSword;
import hojosa.relics.common.item.Heart;
import hojosa.relics.common.item.InfuseableItem;
import hojosa.relics.common.item.InfusedItem;
import hojosa.relics.common.item.LostPage;
import hojosa.relics.common.item.MagicPowder;
import hojosa.relics.common.item.MasterSword;
import hojosa.relics.common.item.RelicsItem;
import hojosa.relics.common.item.WaterTablet;
import hojosa.relics.lib.References;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MOD_ID);

	public static final RegistryObject<FireTablet> FIRE_TABLET = ITEMS.register(References.UnlocalizedName.FIRE_PLATE, () -> new FireTablet(1, Rarity.EPIC));
	public static final RegistryObject<WaterTablet> WATER_TABLET = ITEMS.register(References.UnlocalizedName.WATER_PLATE, () -> new WaterTablet(1, Rarity.EPIC));
	public static final RegistryObject<RelicsItem> BLANK_TABLET = ITEMS.register(References.UnlocalizedName.BLANK_TABLET, () -> new RelicsItem(64, Rarity.UNCOMMON));
	public static final RegistryObject<SwordItem> FIRE_SWORD = ITEMS.register(References.UnlocalizedName.FIRE_SWORD, FlameSword::new);
	public static final RegistryObject<SwordItem> MASTER_SWORD = ITEMS.register(References.UnlocalizedName.MASTER_SWORD, MasterSword::new);
	public static final RegistryObject<RelicsItem> INFUSED_STAR_PIECE = ITEMS.register(References.UnlocalizedName.INFUSED_STAR_PIECE, () -> new InfusedItem(64, Rarity.EPIC));
	public static final RegistryObject<RelicsItem> STAR_PIECE = ITEMS.register(References.UnlocalizedName.STAR_PIECE, () -> new InfuseableItem(64, Rarity.UNCOMMON, INFUSED_STAR_PIECE));
	public static final RegistryObject<RelicsItem> STAR_DUST = ITEMS.register(References.UnlocalizedName.STAR_DUST, () -> new RelicsItem(64, Rarity.UNCOMMON));
	public static final RegistryObject<RelicsItem> STAR_STONE = ITEMS.register(References.UnlocalizedName.STAR_STONE, () -> new RelicsItem(64, Rarity.UNCOMMON));
	public static final RegistryObject<RelicsItem> INFUSED_STAR_DUST = ITEMS.register(References.UnlocalizedName.INFUSED_STAR_DUST, () -> new InfusedItem(64, Rarity.EPIC));
	public static final RegistryObject<RelicsItem> INFUSED_STAR_STONE = ITEMS.register(References.UnlocalizedName.INFUSED_STAR_STONE, () -> new InfusedItem(64, Rarity.EPIC));
	public static final RegistryObject<RelicsItem> PHOENIX_FEATHER = ITEMS.register(References.UnlocalizedName.PHOENIX_FEATHER,
			() -> new InfusedItem(new Item.Properties().stacksTo(64).rarity(Rarity.RARE).fireResistant()));
	public static final RegistryObject<RelicsItem> EMERALD_SHARD = ITEMS.register(References.UnlocalizedName.EMERALD_SHARD, EmeraldShard::new);
	public static final RegistryObject<RelicsItem> EMERALD_PIECE = ITEMS.register(References.UnlocalizedName.EMERALD_PIECE, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> HEART = ITEMS.register(References.UnlocalizedName.HEART, Heart::new);
	public static final RegistryObject<RelicsItem> TOTEM_DUST = ITEMS.register(References.UnlocalizedName.TOTEM_DUST, () -> new RelicsItem(64, Rarity.UNCOMMON));
	public static final RegistryObject<LostPage> LOST_PAGE_1 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_1, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_1));
	public static final RegistryObject<LostPage> LOST_PAGE_2 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_2, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_2));
	public static final RegistryObject<LostPage> LOST_PAGE_3 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_3, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_3));
	public static final RegistryObject<LostPage> LOST_PAGE_4 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_4, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_4));
	public static final RegistryObject<LostPage> LOST_PAGE_5 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_5, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_5));
	public static final RegistryObject<LostPage> LOST_PAGE_6 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_6, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_6));
	public static final RegistryObject<LostPage> LOST_PAGE_7 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_7, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_7));
	public static final RegistryObject<MagicPowder> MAGIC_POWDER = ITEMS.register(References.UnlocalizedName.MAGIC_POWDER, MagicPowder::new);
	
	public static void addTabItems(ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
		RelicsItems.ITEMS.getEntries().forEach(itemRegistryObject -> output.accept(new ItemStack(itemRegistryObject.get())));
	}
}
