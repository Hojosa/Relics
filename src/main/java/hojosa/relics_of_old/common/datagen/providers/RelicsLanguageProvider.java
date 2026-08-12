package hojosa.relics_of_old.common.datagen.providers;

import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsEnchantments;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.lib.References;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class RelicsLanguageProvider extends LanguageProvider {
	private final String locale;

	public RelicsLanguageProvider(PackOutput output, String locale) {
		super(output, References.MOD_ID, locale);
		this.locale = locale;
	}

	@Override
	protected void addTranslations() {
		// Creative tab
		add("item_group.relics_of_old.tab", "Relics of Old");

		// Blocks
//	      addBlock(RelicsBlocks.LAPIS_BRICK, "Lapis Brick");
//	      add("block.relics_of_old.lapisBrickSlab", "Lapis Slab");
		addBlock(RelicsBlocks.SWORD_PEDESTAL_RELIC, "Sword Pedestal");
		addBlock(RelicsBlocks.SWORD_PEDESTAL_NORMAL, "Normal Sword Pedestal");
		addBlock(RelicsBlocks.SWORD_PEDESTAL_STONE, "Stone Sword Pedestal");
		addBlock(RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS, "Fancy Sword Pedestal");
		addBlock(RelicsBlocks.SWORD_PEDESTAL_TIME, "Sword Pedestal of Time");
		addBlock(RelicsBlocks.SWORD_PEDESTAL_TWILIGHT, "Sword Pedestal of Twilight");
		addBlock(RelicsBlocks.STARSTONE_BLOCK, "Starstone Block");
		addBlock(RelicsBlocks.INFUSED_STARSTONE_BLOCK, "Infused Starstone Block");
		addBlock(RelicsBlocks.SKYBEAM_BLOCK, "Skybeam Block");
		addBlock(RelicsBlocks.ODDISH_POT, "Planted Weed?");
		addBlock(RelicsBlocks.STARBEAM_TORCH, "Star Beam Torch");
		addBlock(RelicsBlocks.MYSTIC_SHRUB, "Mystic Shrub");
		addBlock(RelicsBlocks.CALTROPS, "Caltrops");
		addBlock(RelicsBlocks.CLAY_JAR, "Clay Jar");
		addBlock(RelicsBlocks.BOOST_PLATE, "Boost Plate");
		addBlock(RelicsBlocks.SUGAR_CUBE, "Sugar Cube");

		// Items
		addItem(RelicsItems.FIRE_SWORD, "Flame Sword");
		addItem(RelicsItems.MASTER_SWORD, "Master Sword");
//	      addItem(RelicsItems.CLAY_BLUE, "Blue Clay");
//	      addItem(RelicsItems.BRICK_BLUE, "Blue Brick");
		addItem(RelicsItems.FIRE_TABLET, "Fire Tablet");
		addItem(RelicsItems.WATER_TABLET, "Water Tablet");
		addItem(RelicsItems.BLANK_TABLET, "Blank Tablet");
		addItem(RelicsItems.STAR_STONE, "Star Stone");
		addItem(RelicsItems.STAR_DUST, "Star Dust");
		addItem(RelicsItems.STAR_PIECE, "Star Piece");
		addItem(RelicsItems.INFUSED_STAR_STONE, "Infused Star Stone");
		addItem(RelicsItems.INFUSED_STAR_DUST, "Infused Star Dust");
		addItem(RelicsItems.INFUSED_STAR_PIECE, "Infused Star Piece");
		addItem(RelicsItems.MAGIC_POWDER, "Magic Powder");
		addItem(RelicsItems.PHOENIX_FEATHER, "Phoenix Feather");
		addItem(RelicsItems.EMERALD_PIECE, "Emerald Piece");
		addItem(RelicsItems.EMERALD_SHARD, "Emerald Shard");
		addItem(RelicsItems.HEART, "Heart");
		addItem(RelicsItems.TOTEM_DUST, "Totem Dust");
		addItem(RelicsItems.LOST_PAGE_1, "Lost Page 1");
		addItem(RelicsItems.LOST_PAGE_2, "Lost Page 2");
		addItem(RelicsItems.LOST_PAGE_3, "Lost Page 3");
		addItem(RelicsItems.LOST_PAGE_4, "Lost Page 4");
		addItem(RelicsItems.LOST_PAGE_5, "Lost Page 5");
		addItem(RelicsItems.LOST_PAGE_6, "Lost Page 6");
		addItem(RelicsItems.LOST_PAGE_7, "Lost Page 7");
		addItem(RelicsItems.MILK_CHOCOLATE, "Milk Chocolate");
		addItem(RelicsItems.MAGIC_MIRROR, "Magic Mirror");
		addItem(RelicsItems.FLAWLESS_MAGIC_MIRROR, "Flawless Magic Mirror");
		addItem(RelicsItems.MYSTIC_SEED, "Mystic Seed");
		addItem(RelicsItems.WOODEN_BOOMERANG, "Wooden Bommerang");
		addItem(RelicsItems.MAGIC_BOOMERANG, "Magic Bommerang");
		addItem(RelicsItems.EMPTY_EARTH_MEDALLION, "Empty Earth Medallion");
		addItem(RelicsItems.EARTH_MEDALLION, "Earth Medallion");
		addItem(RelicsItems.EMPTY_FIRE_MEDALLION, "Empty Fire Medallion");
		addItem(RelicsItems.FIRE_MEDALLION, "Fire Medallion");
		addItem(RelicsItems.EMPTY_WIND_MEDALLION, "Empty Wind Medallion");
		addItem(RelicsItems.WIND_MEDALLION, "Wind Medallion");
		addItem(RelicsItems.EMPTY_ENDER_MEDALLION, "Empty Ender Medallion");
		addItem(RelicsItems.ENDER_MEDALLION, "Ender Medallion");
		addItem(RelicsItems.EMPTY_FOREST_MEDALLION, "Empty Forest Medallion");
		addItem(RelicsItems.FOREST_MEDALLION, "Forest Medallion");
		addItem(RelicsItems.EMPTY_MAGIC_MEDALLION, "Empty Magic Medallion");
		addItem(RelicsItems.MAGIC_MEDALLION, "Magic Medallion");
		addItem(RelicsItems.EMPTY_ICE_MEDALLION, "Empty Ice Medallion");
		addItem(RelicsItems.ICE_MEDALLION, "Ice Medallion");
		addItem(RelicsItems.EMPTY_WATER_MEDALLION, "Empty Water Medallion");
		addItem(RelicsItems.WATER_MEDALLION, "Water Medallion");
		addItem(RelicsItems.EMPTY_SHADOW_MEDALLION, "Empty Shadow Medallion");
		addItem(RelicsItems.SHADOW_MEDALLION, "Shadow Medallion");
		addItem(RelicsItems.EMPTY_LIGHTNING_MEDALLION, "Empty Lightning Medallion");
		addItem(RelicsItems.LIGHTNING_MEDALLION, "Lightning Medallion");
		addItem(RelicsItems.EMPTY_STAR_MEDALLION, "Empty Star Medallion");
		addItem(RelicsItems.STAR_MEDALLION, "Star Medallion");
		addItem(RelicsItems.PYRO_AMULET, "Pyro Amulet");
		addItem(RelicsItems.GEO_AMULET, "Geo Amulet");
		addItem(RelicsItems.AERO_AMULET, "Aero Amulet");
		addItem(RelicsItems.END_AMULET, "End Amulet");
		addItem(RelicsItems.WHIRLWIND_BOOTS, "Whirlwind Boots");
		addItem(RelicsItems.TITAN_BAND, "Titan Band");
		addItem(RelicsItems.HEADBAND_OF_VALOR, "Headband of Valor");
		addItem(RelicsItems.REED_PIPES, "Reed Pipes");
		addItem(RelicsItems.SLIME_SWORD, "Slime Sword");
		addItem(RelicsItems.CAPTURE_EGG, "Capture Egg");
		addItem(RelicsItems.ENDER_SWORD, "Ender Sword");
		addItem(RelicsItems.ROCK_CANDY_REDSTONE, "Redstone Rock Candy");
		addItem(RelicsItems.ROCK_CANDY_LAPIS, "Lapis Rock Candy");
		addItem(RelicsItems.ROCK_CANDY_EMERALD, "Emerald Rock Candy");
		addItem(RelicsItems.ROCK_CANDY_DIAMOND, "Diamond Rock Candy");

		// Sword augment tooltips
		add("augment.relics_of_old.fire", "Augment: Fire");
		add("augment.relics_of_old.earth", "Augment: Earth");
		add("augment.relics_of_old.wind", "Augment: Wind");
		add("augment.relics_of_old.ender", "Augment: Ender");
		add("augment.relics_of_old.ice", "Augment: Ice");
		add("augment.relics_of_old.water", "Augment: Water");
		add("augment.relics_of_old.lightning", "Augment: Lightning");
		add("augment.relics_of_old.magic", "Augment: Magic");
		add("augment.relics_of_old.shadow", "Augment: Shadow");
		add("augment.relics_of_old.forest", "Augment: Forest");
		add("augment.relics_of_old.star", "Augment: Star");

		// Other stuff
		addEnchantment(RelicsEnchantments.FOCUS, "Focus");

	}

	@Override
	public String getName() {
		return "Relics Languages " + locale;
	}
}