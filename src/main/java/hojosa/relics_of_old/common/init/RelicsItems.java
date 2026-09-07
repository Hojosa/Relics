package hojosa.relics_of_old.common.init;

import hojosa.relics_of_old.common.entity.ThrownOrbEntity.OrbType;
import hojosa.relics_of_old.common.entity.attacks.SpellEffectEntity;
import hojosa.relics_of_old.common.item.AeroAmulet;
import hojosa.relics_of_old.common.item.AzureFeatherItem;
import hojosa.relics_of_old.common.item.MantleItem;
import hojosa.relics_of_old.common.item.BombArrowItem;
import hojosa.relics_of_old.common.item.BombBagItem;
import hojosa.relics_of_old.common.item.BombItem;
import hojosa.relics_of_old.common.item.CaptureEgg;
import hojosa.relics_of_old.common.item.ColdFeetRingItem;
import hojosa.relics_of_old.common.item.ConvectionRingItem;
import hojosa.relics_of_old.common.item.DimensionalCatalystItem;
import hojosa.relics_of_old.common.item.EmeraldPiece;
import hojosa.relics_of_old.common.item.EmeraldShard;
import hojosa.relics_of_old.common.item.EmptyMedallion;
import hojosa.relics_of_old.common.item.EnderSword;
import hojosa.relics_of_old.common.item.FireTablet;
import hojosa.relics_of_old.common.item.FlameSword;
import hojosa.relics_of_old.common.item.HeadbandOfValor;
import hojosa.relics_of_old.common.item.Heart;
import hojosa.relics_of_old.common.item.InfusedStarDustItem;
import hojosa.relics_of_old.common.item.LostPage;
import hojosa.relics_of_old.common.item.MagicBoomerang;
import hojosa.relics_of_old.common.item.MagicCharmItem;
import hojosa.relics_of_old.common.item.MagicMirror;
import hojosa.relics_of_old.common.item.MagicPowder;
import hojosa.relics_of_old.common.item.MagicRingItem;
import hojosa.relics_of_old.common.item.MasterSword;
import hojosa.relics_of_old.common.item.Medallion;
import hojosa.relics_of_old.common.item.MilkChocolate;
import hojosa.relics_of_old.common.item.MysticSeed;
import hojosa.relics_of_old.common.item.PhoenixRingItem;
import hojosa.relics_of_old.common.item.ReedPipes;
import hojosa.relics_of_old.common.item.RelicsAmulet;
import hojosa.relics_of_old.common.item.RockCandyItem;
import hojosa.relics_of_old.common.item.SlimeSword;
import hojosa.relics_of_old.common.item.SoftFallRingItem;
import hojosa.relics_of_old.common.item.SpeedRingItem;
import hojosa.relics_of_old.common.item.SpellCastingItem;
import hojosa.relics_of_old.common.item.SpottingScopeItem;
import hojosa.relics_of_old.common.item.ThiefRingItem;
import hojosa.relics_of_old.common.item.ThrowableOrbItem;
import hojosa.relics_of_old.common.item.TitanBand;
import hojosa.relics_of_old.common.item.TuningForkItem;
import hojosa.relics_of_old.common.item.WaterTablet;
import hojosa.relics_of_old.common.item.WhirlwindBoots;
import hojosa.relics_of_old.common.item.WishRingItem;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.RelicsUtil.ElementType;
import hojosa.relics_of_old.lib.item.InfuseableItem;
import hojosa.relics_of_old.lib.item.InfusedItem;
import hojosa.relics_of_old.lib.item.RelicsArmorMaterials;
import hojosa.relics_of_old.lib.item.RelicsItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MOD_ID);

	// relics
	public static final RegistryObject<FireTablet> FIRE_TABLET = ITEMS.register(References.UnlocalizedName.FIRE_PLATE, () -> new FireTablet(1, Rarity.EPIC));
	public static final RegistryObject<WaterTablet> WATER_TABLET = ITEMS.register(References.UnlocalizedName.WATER_PLATE, () -> new WaterTablet(1, Rarity.EPIC));
	public static final RegistryObject<RelicsItem> BLANK_TABLET = ITEMS.register(References.UnlocalizedName.BLANK_TABLET, () -> new RelicsItem(64, Rarity.UNCOMMON));
	public static final RegistryObject<RelicsItem> PHOENIX_FEATHER = ITEMS.register(References.UnlocalizedName.PHOENIX_FEATHER,
			() -> new InfusedItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant()));
	public static final RegistryObject<MagicMirror> MAGIC_MIRROR = ITEMS.register(References.UnlocalizedName.MAGIC_MIRROR, () -> new MagicMirror(Rarity.UNCOMMON, 4));
	public static final RegistryObject<MagicMirror> FLAWLESS_MAGIC_MIRROR = ITEMS.register(References.UnlocalizedName.FLAWLESS_MAGIC_MIRROR, () -> new MagicMirror(Rarity.EPIC));
	public static final RegistryObject<MagicCharmItem> PHOENIX_CHARM = ITEMS.register(References.UnlocalizedName.PHOENIX_CHARM, () -> new MagicCharmItem("Revives on death"));
	public static final RegistryObject<MagicCharmItem> BLAST_CHARM = ITEMS.register(References.UnlocalizedName.BLAST_CHARM, () -> new MagicCharmItem("Saves from lethal explosions"));
	public static final RegistryObject<MagicCharmItem> FEATHER_CHARM = ITEMS.register(References.UnlocalizedName.FEATHER_CHARM, () -> new MagicCharmItem("Saves from lethal falls"));
	public static final RegistryObject<MantleItem> AZURE_MANTLE = ITEMS.register(References.UnlocalizedName.AZURE_MANTLE, () -> new MantleItem(4.0f, Rarity.RARE));
	public static final RegistryObject<MantleItem> PHOENIX_MANTLE = ITEMS.register(References.UnlocalizedName.PHOENIX_MANTLE, () -> new MantleItem(7.0f, Rarity.EPIC));

	// weapons
	public static final RegistryObject<SwordItem> FIRE_SWORD = ITEMS.register(References.UnlocalizedName.FIRE_SWORD, FlameSword::new);
	public static final RegistryObject<SwordItem> MASTER_SWORD = ITEMS.register(References.UnlocalizedName.MASTER_SWORD, MasterSword::new);
	public static final RegistryObject<SwordItem> SLIME_SWORD = ITEMS.register(References.UnlocalizedName.SLIME_SWORD, SlimeSword::new);
	public static final RegistryObject<SwordItem> ENDER_SWORD = ITEMS.register(References.UnlocalizedName.ENDER_SWORD, EnderSword::new);

	public static final RegistryObject<MagicBoomerang> WOODEN_BOOMERANG = ITEMS.register(References.UnlocalizedName.WOODEN_BOOMERANG, () -> new MagicBoomerang(1.0f, 3, 4, 0));
	public static final RegistryObject<MagicBoomerang> MAGIC_BOOMERANG = ITEMS.register(References.UnlocalizedName.MAGIC_BOOMERANG, () -> new MagicBoomerang(1.1f, 5, 5, 1));
	public static final RegistryObject<MagicBoomerang> STARSTEEL_BOOMERANG = ITEMS.register(References.UnlocalizedName.STARSTEEL_BOOMERANG, () -> new MagicBoomerang(1.3f, 6, 7, 3));

	public static final RegistryObject<BombItem> BOMB = ITEMS.register(References.UnlocalizedName.BOMB, BombItem::new);
	public static final RegistryObject<BombArrowItem> BOMB_ARROW = ITEMS.register(References.UnlocalizedName.BOMB_ARROW, BombArrowItem::new);
	public static final RegistryObject<BombBagItem> BOMB_BAG = ITEMS.register(References.UnlocalizedName.BOMB_BAG, BombBagItem::new);

	// staffs
	public static final RegistryObject<SpellCastingItem> FIRE_STAFF = ITEMS.register(References.UnlocalizedName.FIRE_STAFF,
			() -> new SpellCastingItem(SpellEffectEntity.SpellType.FIRE, 10.0, 0.0, 7.0, 3.0, 0.75, 5.0f, false, 128, true));
	public static final RegistryObject<SpellCastingItem> ICE_STAFF = ITEMS.register(References.UnlocalizedName.ICE_STAFF,
			() -> new SpellCastingItem(SpellEffectEntity.SpellType.ICE, 10.0, 0.0, 7.0, 3.0, 0.75, 5.0f, true, 128, true));
	public static final RegistryObject<SpellCastingItem> LIGHTNING_STAFF = ITEMS.register(References.UnlocalizedName.LIGHTNING_STAFF,
			() -> new SpellCastingItem(SpellEffectEntity.SpellType.LIGHTNING, 10.0, 0.0, 7.0, 3.0, 0.75, 5.0f, true, 128, true));
	public static final RegistryObject<SpellCastingItem> TWINKLE_STAFF = ITEMS.register(References.UnlocalizedName.TWINKLE_STAFF,
			() -> new SpellCastingItem(SpellEffectEntity.SpellType.TWINKLE, 8.0, 0.0, 7.0, 3.0, 0.75d, 4.0f, false, 128, true));
	// tomes
	public static final RegistryObject<RelicsItem> BLANK_SPELLBOOK = ITEMS.register(References.UnlocalizedName.BLANK_SPELLBOOK, () -> new RelicsItem(1, Rarity.UNCOMMON));
	public static final RegistryObject<SpellCastingItem> TOME_SCYTHEWIND = ITEMS.register(References.UnlocalizedName.TOME_SCYTHEWIND,
			() -> new SpellCastingItem(SpellEffectEntity.SpellType.SCYTHEWIND, 12.0, 0.0, 9.0, 7.0, 3.0, 8.0f, false, 16, false));
	public static final RegistryObject<SpellCastingItem> TOME_RAYFIRE = ITEMS.register(References.UnlocalizedName.TOME_RAYFIRE,
			() -> new SpellCastingItem(SpellEffectEntity.SpellType.RAYFIRE, 12.0, 0.0, 9.0, 7.0, 3.0, 8.0f, false, 16, false));
	public static final RegistryObject<SpellCastingItem> TOME_EXEUNT = ITEMS.register(References.UnlocalizedName.TOME_EXEUNT,
			() -> new SpellCastingItem(SpellEffectEntity.SpellType.EXIT, 5.0, 0.0, 9.0, 7.0, 3.0, 8.0f, false, 16, false));

	// normal items
	public static final RegistryObject<RelicsItem> INFUSED_STAR_PIECE = ITEMS.register(References.UnlocalizedName.INFUSED_STAR_PIECE, () -> new InfusedItem(64, Rarity.EPIC));
	public static final RegistryObject<RelicsItem> STAR_PIECE = ITEMS.register(References.UnlocalizedName.STAR_PIECE, () -> new InfuseableItem(64, Rarity.UNCOMMON, INFUSED_STAR_PIECE));
	public static final RegistryObject<RelicsItem> STAR_DUST = ITEMS.register(References.UnlocalizedName.STAR_DUST, () -> new RelicsItem(64, Rarity.UNCOMMON));
	public static final RegistryObject<RelicsItem> STAR_STONE = ITEMS.register(References.UnlocalizedName.STAR_STONE, () -> new RelicsItem(64, Rarity.UNCOMMON));
	public static final RegistryObject<RelicsItem> INFUSED_STAR_DUST = ITEMS.register(References.UnlocalizedName.INFUSED_STAR_DUST, () -> new InfusedStarDustItem(64, Rarity.EPIC));
	public static final RegistryObject<RelicsItem> INFUSED_STAR_STONE = ITEMS.register(References.UnlocalizedName.INFUSED_STAR_STONE, () -> new InfusedItem(64, Rarity.EPIC));
	public static final RegistryObject<RelicsItem> EMERALD_SHARD = ITEMS.register(References.UnlocalizedName.EMERALD_SHARD, EmeraldShard::new);
	public static final RegistryObject<RelicsItem> EMERALD_PIECE = ITEMS.register(References.UnlocalizedName.EMERALD_PIECE, EmeraldPiece::new);
	public static final RegistryObject<RelicsItem> HEART = ITEMS.register(References.UnlocalizedName.HEART, Heart::new);
	public static final RegistryObject<RelicsItem> TOTEM_DUST = ITEMS.register(References.UnlocalizedName.TOTEM_DUST, () -> new RelicsItem(64, Rarity.UNCOMMON));
	public static final RegistryObject<MagicPowder> MAGIC_POWDER = ITEMS.register(References.UnlocalizedName.MAGIC_POWDER, MagicPowder::new);
	public static final RegistryObject<MysticSeed> MYSTIC_SEED = ITEMS.register(References.UnlocalizedName.MYSTIC_SEED, () -> new MysticSeed(RelicsBlocks.MYSTIC_SHRUB.get()));
	public static final RegistryObject<MilkChocolate> MILK_CHOCOLATE = ITEMS.register(References.UnlocalizedName.MILK_CHOCOLATE, MilkChocolate::new);
	public static final RegistryObject<ReedPipes> REED_PIPES = ITEMS.register(References.UnlocalizedName.REED_PIPES, ReedPipes::new);
	public static final RegistryObject<CaptureEgg> CAPTURE_EGG = ITEMS.register(References.UnlocalizedName.CAPTURE_EGG, CaptureEgg::new);
	public static final RegistryObject<RockCandyItem> ROCK_CANDY_REDSTONE = ITEMS.register(References.UnlocalizedName.ROCK_CANDY_REDSTONE, () -> new RockCandyItem(MobEffects.DAMAGE_BOOST, 0)); // Strength
																																																	// I
	public static final RegistryObject<RockCandyItem> ROCK_CANDY_LAPIS = ITEMS.register(References.UnlocalizedName.ROCK_CANDY_LAPIS, () -> new RockCandyItem(MobEffects.JUMP, 1)); // Jump
																																													// Boost
																																													// II
	public static final RegistryObject<RockCandyItem> ROCK_CANDY_EMERALD = ITEMS.register(References.UnlocalizedName.ROCK_CANDY_EMERALD, () -> new RockCandyItem(MobEffects.DIG_SPEED, 3)); // Haste
																																															// IV
	public static final RegistryObject<RockCandyItem> ROCK_CANDY_DIAMOND = ITEMS.register(References.UnlocalizedName.ROCK_CANDY_DIAMOND, () -> new RockCandyItem(MobEffects.DAMAGE_RESISTANCE, 3)); // Resistance
																																																	// IV
	public static final RegistryObject<RelicsItem> FULGURITE = ITEMS.register(References.UnlocalizedName.FULGURITE, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> TUNING_FORK = ITEMS.register(References.UnlocalizedName.TUNING_FORK, TuningForkItem::new);
	public static final RegistryObject<RelicsItem> STARGLASS_LUMP = ITEMS.register(References.UnlocalizedName.STARGLASS_LUMP, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> STARSTEEL_INGOT = ITEMS.register(References.UnlocalizedName.STARSTEEL_INGOT, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> STARSTEEL_DUST = ITEMS.register(References.UnlocalizedName.STARSTEEL_DUST, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> AZURITE_DUST = ITEMS.register(References.UnlocalizedName.AZURITE_DUST, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> AZURITE_DOT = ITEMS.register(References.UnlocalizedName.AZURITE_DOT, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> AZURITE_SPHERE = ITEMS.register(References.UnlocalizedName.AZURITE_SPHERE, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> ABSTRACTION_GEL = ITEMS.register(References.UnlocalizedName.ABSTRACTION_GEL, () -> new InfusedItem(64, Rarity.COMMON));
	public static final RegistryObject<DimensionalCatalystItem> DIMENSIONAL_CATALYST = ITEMS.register(References.UnlocalizedName.DIMENSIONAL_CATALYST, DimensionalCatalystItem::new);
	public static final RegistryObject<RecordItem> MUSIC_DISC_DRAGONDOT = ITEMS.register(References.UnlocalizedName.MUSIC_DISC_DRAGONDOT,
			() -> new RecordItem(15, RelicsSounds.DRAGONDOT, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 1720));
	public static final RegistryObject<SpottingScopeItem> SPOTTING_SCOPE = ITEMS.register(References.UnlocalizedName.SPOTTING_SCOPE, SpottingScopeItem::new);
	public static final RegistryObject<AzureFeatherItem> AZURE_FEATHER = ITEMS.register(References.UnlocalizedName.AZURE_FEATHER, AzureFeatherItem::new);
	public static final RegistryObject<RelicsItem> SUNFIRE_DIAMOND = ITEMS.register(References.UnlocalizedName.SUNFIRE_DIAMOND, () -> new InfusedItem(64, Rarity.EPIC));

	// lost pages
	public static final RegistryObject<LostPage> LOST_PAGE_1 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_1, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_1));
	public static final RegistryObject<LostPage> LOST_PAGE_2 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_2, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_2));
	public static final RegistryObject<LostPage> LOST_PAGE_3 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_3, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_3));
	public static final RegistryObject<LostPage> LOST_PAGE_4 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_4, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_4));
	public static final RegistryObject<LostPage> LOST_PAGE_5 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_5, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_5));
	public static final RegistryObject<LostPage> LOST_PAGE_6 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_6, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_6));
	public static final RegistryObject<LostPage> LOST_PAGE_7 = ITEMS.register(References.UnlocalizedName.LOST_PAGE_7, () -> new LostPage(1, References.LostPagesText.LOST_PAGE_7));

	// anything medallions related
	public static final RegistryObject<Medallion> FIRE_MEDALLION = ITEMS.register(References.UnlocalizedName.FIRE_MEDALLION, () -> new Medallion(ElementType.FIRE, 3));
	public static final RegistryObject<EmptyMedallion> EMPTY_FIRE_MEDALLION = ITEMS.register("empty_" + References.UnlocalizedName.FIRE_MEDALLION, () -> new EmptyMedallion(ElementType.FIRE, FIRE_MEDALLION));
	public static final RegistryObject<Medallion> EARTH_MEDALLION = ITEMS.register(References.UnlocalizedName.EARTH_MEDALLION, () -> new Medallion(ElementType.EARTH, 3));
	public static final RegistryObject<EmptyMedallion> EMPTY_EARTH_MEDALLION = ITEMS.register("empty_" + References.UnlocalizedName.EARTH_MEDALLION, () -> new EmptyMedallion(ElementType.EARTH, EARTH_MEDALLION));
	public static final RegistryObject<Medallion> WIND_MEDALLION = ITEMS.register(References.UnlocalizedName.WIND_MEDALLION, () -> new Medallion(ElementType.WIND, 3));
	public static final RegistryObject<EmptyMedallion> EMPTY_WIND_MEDALLION = ITEMS.register("empty_" + References.UnlocalizedName.WIND_MEDALLION, () -> new EmptyMedallion(ElementType.WIND, WIND_MEDALLION));
	public static final RegistryObject<Medallion> ENDER_MEDALLION = ITEMS.register(References.UnlocalizedName.ENDER_MEDALLION, () -> new Medallion(ElementType.ENDER, 3));
	public static final RegistryObject<EmptyMedallion> EMPTY_ENDER_MEDALLION = ITEMS.register("empty_" + References.UnlocalizedName.ENDER_MEDALLION, () -> new EmptyMedallion(ElementType.ENDER, ENDER_MEDALLION));
	public static final RegistryObject<Medallion> ICE_MEDALLION = ITEMS.register(References.UnlocalizedName.ICE_MEDALLION, () -> new Medallion(ElementType.ICE, 3));
	public static final RegistryObject<EmptyMedallion> EMPTY_ICE_MEDALLION = ITEMS.register("empty_" + References.UnlocalizedName.ICE_MEDALLION, () -> new EmptyMedallion(ElementType.ICE, ICE_MEDALLION));
	public static final RegistryObject<Medallion> WATER_MEDALLION = ITEMS.register(References.UnlocalizedName.WATER_MEDALLION, () -> new Medallion(ElementType.WATER, 3));
	public static final RegistryObject<EmptyMedallion> EMPTY_WATER_MEDALLION = ITEMS.register("empty_" + References.UnlocalizedName.WATER_MEDALLION, () -> new EmptyMedallion(ElementType.WATER, WATER_MEDALLION));
	public static final RegistryObject<Medallion> FOREST_MEDALLION = ITEMS.register(References.UnlocalizedName.FOREST_MEDALLION, () -> new Medallion(ElementType.FOREST, 3));
	public static final RegistryObject<EmptyMedallion> EMPTY_FOREST_MEDALLION = ITEMS.register("empty_" + References.UnlocalizedName.FOREST_MEDALLION, () -> new EmptyMedallion(ElementType.FOREST, FOREST_MEDALLION));
	public static final RegistryObject<Medallion> MAGIC_MEDALLION = ITEMS.register(References.UnlocalizedName.MAGIC_MEDALLION, () -> new Medallion(ElementType.MAGIC, 3));
	public static final RegistryObject<EmptyMedallion> EMPTY_MAGIC_MEDALLION = ITEMS.register("empty_" + References.UnlocalizedName.MAGIC_MEDALLION, () -> new EmptyMedallion(ElementType.MAGIC, MAGIC_MEDALLION));
	public static final RegistryObject<Medallion> SHADOW_MEDALLION = ITEMS.register(References.UnlocalizedName.SHADOW_MEDALLION, () -> new Medallion(ElementType.SHADOW, 3));
	public static final RegistryObject<EmptyMedallion> EMPTY_SHADOW_MEDALLION = ITEMS.register("empty_" + References.UnlocalizedName.SHADOW_MEDALLION, () -> new EmptyMedallion(ElementType.SHADOW, SHADOW_MEDALLION));
	public static final RegistryObject<Medallion> LIGHTNING_MEDALLION = ITEMS.register(References.UnlocalizedName.LIGHTNING_MEDALLION, () -> new Medallion(ElementType.LIGHTNING, 3));
	public static final RegistryObject<EmptyMedallion> EMPTY_LIGHTNING_MEDALLION = ITEMS.register("empty_" + References.UnlocalizedName.LIGHTNING_MEDALLION,
			() -> new EmptyMedallion(ElementType.LIGHTNING, LIGHTNING_MEDALLION));
	public static final RegistryObject<Medallion> STAR_MEDALLION = ITEMS.register(References.UnlocalizedName.STAR_MEDALLION, () -> new Medallion(ElementType.STAR, 3));
	public static final RegistryObject<EmptyMedallion> EMPTY_STAR_MEDALLION = ITEMS.register("empty_" + References.UnlocalizedName.STAR_MEDALLION, () -> new EmptyMedallion(ElementType.STAR, STAR_MEDALLION));
	// amulets
	public static final RegistryObject<RelicsAmulet> PYRO_AMULET = ITEMS.register(References.UnlocalizedName.PYRO_AMULET, () -> new RelicsAmulet(50, ElementType.FIRE));
	public static final RegistryObject<RelicsAmulet> GEO_AMULET = ITEMS.register(References.UnlocalizedName.GEO_AMULET, () -> new RelicsAmulet(50, ElementType.EARTH));
	public static final RegistryObject<RelicsAmulet> AERO_AMULET = ITEMS.register(References.UnlocalizedName.AERO_AMULET, () -> new AeroAmulet(50));
	public static final RegistryObject<RelicsAmulet> END_AMULET = ITEMS.register(References.UnlocalizedName.END_AMULET, () -> new RelicsAmulet(50, ElementType.ENDER));
	// things made from medallions
	public static final RegistryObject<WhirlwindBoots> WHIRLWIND_BOOTS = ITEMS.register(References.UnlocalizedName.WHIRLWIND_BOOTS, () -> new WhirlwindBoots(RelicsArmorMaterials.WHIRLWIND));
	public static final RegistryObject<TitanBand> TITAN_BAND = ITEMS.register(References.UnlocalizedName.TITAN_BAND, TitanBand::new);
	public static final RegistryObject<HeadbandOfValor> HEADBAND_OF_VALOR = ITEMS.register(References.UnlocalizedName.HEADBAND_OF_VALOR, () -> new HeadbandOfValor(RelicsArmorMaterials.HEADBAND));

	// nucleus
	public static final RegistryObject<RelicsItem> NUCLEUS_FIRE = ITEMS.register(References.UnlocalizedName.NUCLEUS_FIRE, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> NUCLEUS_ICE = ITEMS.register(References.UnlocalizedName.NUCLEUS_ICE, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> NUCLEUS_LIGHTNING = ITEMS.register(References.UnlocalizedName.NUCLEUS_LIGHTNING, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> NUCLEUS_CUT = ITEMS.register(References.UnlocalizedName.NUCLEUS_CUT, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> NUCLEUS_SKY = ITEMS.register(References.UnlocalizedName.NUCLEUS_SKY, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> NUCLEUS_SUN = ITEMS.register(References.UnlocalizedName.NUCLEUS_SUN, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> NUCLEUS_NAVIGATE = ITEMS.register(References.UnlocalizedName.NUCLEUS_NAVIGATE, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> NUCLEUS_DARK = ITEMS.register(References.UnlocalizedName.NUCLEUS_DARK, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> NUCLEUS_STAR = ITEMS.register(References.UnlocalizedName.NUCLEUS_STAR, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> NUCLEUS_HEALTH = ITEMS.register(References.UnlocalizedName.NUCLEUS_HEALTH, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> NUCLEUS_WEAPON = ITEMS.register(References.UnlocalizedName.NUCLEUS_WEAPON, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> NUCLEUS_WEALTH = ITEMS.register(References.UnlocalizedName.NUCLEUS_WEALTH, () -> new RelicsItem(64));

	// gems
	public static final RegistryObject<RelicsItem> GEM_FIRE = ITEMS.register(References.UnlocalizedName.GEM_FIRE, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> GEM_ICE = ITEMS.register(References.UnlocalizedName.GEM_ICE, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> GEM_LIGHTNING = ITEMS.register(References.UnlocalizedName.GEM_LIGHTNING, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> GEM_CUT = ITEMS.register(References.UnlocalizedName.GEM_CUT, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> GEM_SKY = ITEMS.register(References.UnlocalizedName.GEM_SKY, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> GEM_SUN = ITEMS.register(References.UnlocalizedName.GEM_SUN, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> GEM_NAVIGATE = ITEMS.register(References.UnlocalizedName.GEM_NAVIGATE, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> GEM_DARK = ITEMS.register(References.UnlocalizedName.GEM_DARK, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> GEM_STAR = ITEMS.register(References.UnlocalizedName.GEM_STAR, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> GEM_HEALTH = ITEMS.register(References.UnlocalizedName.GEM_HEALTH, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> GEM_WEAPON = ITEMS.register(References.UnlocalizedName.GEM_WEAPON, () -> new RelicsItem(64));
	public static final RegistryObject<RelicsItem> GEM_WEALTH = ITEMS.register(References.UnlocalizedName.GEM_WEALTH, () -> new RelicsItem(64));

	// shells
	public static final RegistryObject<RelicsItem> STARGLASS_SHELL = ITEMS.register(References.UnlocalizedName.STARGLASS_SHELL, () -> new RelicsItem(16, "Usable in crafting"));
	// throwable shells
	public static final RegistryObject<ThrowableOrbItem> WATER_SHELL = ITEMS.register(References.UnlocalizedName.WATER_SHELL, () -> new ThrowableOrbItem(OrbType.WATER));
	public static final RegistryObject<ThrowableOrbItem> LAVA_SHELL = ITEMS.register(References.UnlocalizedName.LAVA_SHELL, () -> new ThrowableOrbItem(OrbType.LAVA));
	public static final RegistryObject<ThrowableOrbItem> BLAST_SHELL = ITEMS.register(References.UnlocalizedName.BLAST_SHELL, () -> new ThrowableOrbItem(OrbType.BLAST));
	// orbs
	public static final RegistryObject<ThrowableOrbItem> GLITTERING_ORB = ITEMS.register(References.UnlocalizedName.GLITTERING_ORB, () -> new ThrowableOrbItem(OrbType.TWINKLE, "Usable in crafting"));
	public static final RegistryObject<ThrowableOrbItem> BURNING_ORB = ITEMS.register(References.UnlocalizedName.BURNING_ORB, () -> new ThrowableOrbItem(OrbType.FIRE, "Usable in crafting"));
	public static final RegistryObject<ThrowableOrbItem> FREEZING_ORB = ITEMS.register(References.UnlocalizedName.FREEZING_ORB, () -> new ThrowableOrbItem(OrbType.ICE, "Usable in crafting"));
	public static final RegistryObject<ThrowableOrbItem> SHOCKING_ORB = ITEMS.register(References.UnlocalizedName.SHOCKING_ORB, () -> new ThrowableOrbItem(OrbType.ZAP, "Usable in crafting"));

	// rings
	public static final RegistryObject<SpeedRingItem> SPEED_RING = ITEMS.register(References.UnlocalizedName.SPEED_RING, SpeedRingItem::new);
	public static final RegistryObject<ConvectionRingItem> CONVECTION_RING = ITEMS.register(References.UnlocalizedName.CONVECTION_RING, ConvectionRingItem::new);
	public static final RegistryObject<SoftFallRingItem> SOFT_FALL_RING = ITEMS.register(References.UnlocalizedName.SOFT_FALL_RING, SoftFallRingItem::new);
	public static final RegistryObject<ColdFeetRingItem> COLD_FEET_RING = ITEMS.register(References.UnlocalizedName.COLD_FEET_RING, ColdFeetRingItem::new);
	public static final RegistryObject<ThiefRingItem> THIEF_RING = ITEMS.register(References.UnlocalizedName.THIEF_RING, ThiefRingItem::new);
	public static final RegistryObject<MagicRingItem> MAGE_RING = ITEMS.register(References.UnlocalizedName.MAGE_RING, () -> new MagicRingItem("Reduce magic cost of spells"));
	public static final RegistryObject<MagicRingItem> WARRIOR_RING = ITEMS.register(References.UnlocalizedName.WARRIOR_RING, () -> new MagicRingItem("Empowers melee attacks", 4.0f));
	public static final RegistryObject<MagicRingItem> FORTUNE_RING = ITEMS.register(References.UnlocalizedName.FORTUNE_RING, () -> new MagicRingItem("Find more emerald shards"));
	public static final RegistryObject<MagicRingItem> ARROWFIND_RING = ITEMS.register(References.UnlocalizedName.ARROWFIND_RING, () -> new MagicRingItem("Sometimes find arrows"));
	public static final RegistryObject<MagicRingItem> AZUREFIND_RING = ITEMS.register(References.UnlocalizedName.AZUREFIND_RING, () -> new MagicRingItem("Occasionally find azurite"));
	public static final RegistryObject<MagicRingItem> WISH_RING = ITEMS.register(References.UnlocalizedName.WISH_RING, WishRingItem::new);
	public static final RegistryObject<MagicRingItem> RESONANCE_RING = ITEMS.register(References.UnlocalizedName.RESONANCE_RING, () -> new MagicRingItem("Increase efficiency of other rings"));
	public static final RegistryObject<RelicsItem> PLAIN_RING = ITEMS.register(References.UnlocalizedName.PLAIN_RING, () -> new RelicsItem(64, "Has potential"));

	public static final RegistryObject<PhoenixRingItem> PHOENIX_RING = ITEMS.register(References.UnlocalizedName.PHOENIX_RING, PhoenixRingItem::new);

	public static void addTabItems(ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
		RelicsItems.ITEMS.getEntries().forEach(itemRegistryObject -> output.accept(itemRegistryObject.get().getDefaultInstance()));
	}
}