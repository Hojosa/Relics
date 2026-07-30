package hojosa.relics_of_old.common.init;

import hojosa.relics_of_old.common.item.Medallion;
import hojosa.relics_of_old.common.item.EmeraldPiece;
import hojosa.relics_of_old.common.item.EmeraldShard;
import hojosa.relics_of_old.common.item.FireTablet;
import hojosa.relics_of_old.common.item.FlameSword;
import hojosa.relics_of_old.common.item.Heart;
import hojosa.relics_of_old.common.item.LostPage;
import hojosa.relics_of_old.common.item.MagicBoomerang;
import hojosa.relics_of_old.common.item.MagicMirror;
import hojosa.relics_of_old.common.item.MagicPowder;
import hojosa.relics_of_old.common.item.MasterSword;
import hojosa.relics_of_old.common.item.EmptyMedallion;
import hojosa.relics_of_old.common.item.EmptyMedallion.MedallionType;
import hojosa.relics_of_old.common.item.MilkChocolate;
import hojosa.relics_of_old.common.item.MysticSeed;
import hojosa.relics_of_old.common.item.WaterTablet;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.item.InfuseableItem;
import hojosa.relics_of_old.lib.item.InfusedItem;
import hojosa.relics_of_old.lib.item.RelicsItem;
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
	public static final RegistryObject<RelicsItem> EMERALD_PIECE = ITEMS.register(References.UnlocalizedName.EMERALD_PIECE, EmeraldPiece::new);
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
	public static final RegistryObject<MysticSeed> MYSTIC_SEED = ITEMS.register(References.UnlocalizedName.MYSTIC_SEED, () -> new MysticSeed(RelicsBlocks.MYSTIC_SHRUB.get()));
	public static final RegistryObject<MilkChocolate> MILK_CHOCOLATE = ITEMS.register(References.UnlocalizedName.MILK_CHOCOLATE, MilkChocolate::new);
	public static final RegistryObject<MagicMirror> MAGIC_MIRROR = ITEMS.register(References.UnlocalizedName.MAGIC_MIRROR, () -> new MagicMirror(Rarity.UNCOMMON, 4));
	public static final RegistryObject<MagicMirror> FLAWLESS_MAGIC_MIRROR = ITEMS.register(References.UnlocalizedName.FLAWLESS_MAGIC_MIRROR, () -> new MagicMirror(Rarity.EPIC));
	public static final RegistryObject<MagicBoomerang> MAGIC_BOOMERANG = ITEMS.register(References.UnlocalizedName.MAGIC_BOOMERANG, MagicBoomerang::new);
	public static final RegistryObject<EmptyMedallion> EMPTY_FIRE_MEDALLION = ITEMS.register("empty_" + References.UnlocalizedName.FIRE_MEDALLION, () -> new EmptyMedallion(MedallionType.FIRE));
	public static final RegistryObject<Medallion> FIRE_MEDALLION = ITEMS.register(References.UnlocalizedName.FIRE_MEDALLION, () -> new Medallion(MedallionType.FIRE, 3));

	public static void addTabItems(ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
		RelicsItems.ITEMS.getEntries().forEach(itemRegistryObject -> output.accept(new ItemStack(itemRegistryObject.get())));
	}
}