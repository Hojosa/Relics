package hojosa.relics_of_old.common.datagen.providers;


import java.util.function.Consumer;

import hojosa.relics_of_old.common.datagen.builders.RitualRecipeBuilder;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsTags;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.RelicsUtil;
import hojosa.relics_of_old.lib.recipe.StonecutterRetexturedRecipeBuilder;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import slimeknights.mantle.recipe.crafting.ShapedRetexturedRecipeBuilder;


public class RelicsRecipesProvider extends RecipeProvider {
	
	private String hasItem = "hasItem";

	public RelicsRecipesProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		StonecutterRetexturedRecipeBuilder.fromStonecutter(
				SingleItemRecipeBuilder.stonecutting(Ingredient.of(RelicsTags.Items.SWORD_PEDESTAL_VARIANTS), RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL_NORMAL)
				.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE)))
		.setSource(RelicsTags.Items.SWORD_PEDESTAL_VARIANTS)
		.setMatchAll()
		.build(consumer);
		
		StonecutterRetexturedRecipeBuilder.fromStonecutter(
				SingleItemRecipeBuilder.stonecutting(Ingredient.of(RelicsTags.Items.SWORD_PEDESTAL_VARIANTS), RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS)
				.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE)))
		.setSource(RelicsTags.Items.SWORD_PEDESTAL_VARIANTS)
		.setMatchAll()
		.build(consumer);
		
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.CHISELED_STONE_BRICKS), RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL_TIME)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CHISELED_STONE_BRICKS))
		.save(consumer);
		
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.CHISELED_STONE_BRICKS), RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL_TWILIGHT)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CHISELED_STONE_BRICKS))
		.save(consumer);
		
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.STONE_BRICKS), RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL_RELIC)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CHISELED_STONE_BRICKS))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsBlocks.ODDISH_POT.get())
		.pattern("g")
		.pattern("p")
		.define('g', Items.GRASS)
		.define('p', Items.FLOWER_POT)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.FLOWER_POT))
		.save(consumer);
		
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.STONE), RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL_STONE)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.STAR_DUST.get(), 3)
		.requires(RelicsItems.STAR_PIECE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STAR_PIECE.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.STAR_PIECE.get())
		.pattern("dd")
		.pattern("dd")
		.define('d', RelicsItems.STAR_DUST.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STAR_DUST.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.STAR_STONE.get())
		.pattern("ppp")
		.pattern("ppp")
		.pattern("ppp")
		.define('p', RelicsItems.STAR_PIECE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STAR_PIECE.get()))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.INFUSED_STAR_DUST.get(), 3)
		.requires(RelicsItems.INFUSED_STAR_PIECE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.INFUSED_STAR_PIECE.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.INFUSED_STAR_PIECE.get())
		.pattern("dd")
		.pattern("dd")
		.define('d', RelicsItems.INFUSED_STAR_DUST.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.INFUSED_STAR_DUST.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.INFUSED_STAR_STONE.get())
		.pattern("ppp")
		.pattern("ppp")
		.pattern("ppp")
		.define('p', RelicsItems.INFUSED_STAR_PIECE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.INFUSED_STAR_PIECE.get()))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.EMERALD_PIECE.get(), 8)
		.requires(Items.EMERALD)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.EMERALD))
		.save(consumer, RelicsUtil.modLoc("emerald_piece_reverse"));
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.EMERALD_SHARD.get(), 8)
		.requires(RelicsItems.EMERALD_PIECE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.EMERALD_PIECE.get()))
		.save(consumer);
		
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(Items.TOTEM_OF_UNDYING), RecipeCategory.MISC, RelicsItems.TOTEM_DUST.get().asItem(), 0.2f, 60)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.TOTEM_OF_UNDYING))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsBlocks.STARSTONE_BLOCK.get())
		.pattern("sss")
		.pattern("sss")
		.pattern("sss")
		.define('s', RelicsItems.STAR_STONE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STAR_STONE.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsBlocks.INFUSED_STARSTONE_BLOCK.get())
		.pattern("sss")
		.pattern("sss")
		.pattern("sss")
		.define('s', RelicsItems.INFUSED_STAR_STONE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STAR_STONE.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsBlocks.SKYBEAM_BLOCK.get())
		.pattern("ooo")
		.pattern("oso")
		.pattern("ooo")
		.define('s', RelicsItems.STAR_STONE.get())
		.define('o', Blocks.OBSIDIAN)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STAR_STONE.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsBlocks.STARBEAM_TORCH.get(), 8)
		.pattern(" s ")
		.pattern("sds")
		.pattern(" g ")
		.define('s', RelicsItems.STAR_DUST.get())
		.define('d', Items.DIAMOND)
		.define('g', Items.GOLD_INGOT)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STAR_DUST.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsBlocks.CALTROPS.get(), 4)
		.pattern(" i ")
		.pattern(" i ")
		.pattern("i i")
		.define('i', Items.IRON_INGOT)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.MAGIC_MIRROR.get(), 1)
		.pattern("ege")
		.pattern("gdg")
		.pattern("ege")
		.define('e', Items.ENDER_PEARL)
		.define('g', Items.GOLD_INGOT)
		.define('d', Items.DIAMOND)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsBlocks.CLAY_JAR.get(), 16)
		.pattern("b b")
		.pattern("b b")
		.pattern("bbb")
		.define('b', Items.BRICK)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.BRICK))
		.save(consumer);

		ShapedRetexturedRecipeBuilder.fromShaped(
			ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsBlocks.CLAY_JAR.get(), 16)
			.pattern("t t")
			.pattern("t t")
			.pattern("ttt")
			.define('t', RelicsTags.Items.CLAY_JAR_VARIANTS)
			.group(References.CREATIVE_TAB)
			.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsBlocks.CLAY_JAR.get())))
			.setSource(RelicsTags.Items.CLAY_JAR_VARIANTS)
			.setMatchAll()
			.build(consumer, RelicsUtil.modLoc("clay_jar_retextured"));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.MILK_CHOCOLATE.get())
		.pattern("mc ")
		.pattern("s  ")
		.define('m', Items.MILK_BUCKET)
		.define('c', Items.COCOA_BEANS)
		.define('s', Items.SUGAR)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.MILK_BUCKET))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.EMPTY_EARTH_MEDALLION.get())
		.pattern("gbg")
		.pattern("bab")
		.pattern("gbg")
		.define('g', Items.GOLD_NUGGET)
		.define('b', Items.BRICK)
		.define('a', Items.DIRT)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.EMPTY_FIRE_MEDALLION.get())
		.pattern("gbg")
		.pattern("bab")
		.pattern("gbg")
		.define('g', Items.GOLD_NUGGET)
		.define('b', Items.BRICK)
		.define('a', Items.FIRE_CHARGE)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.EMPTY_WIND_MEDALLION.get())
		.pattern("gbg")
		.pattern("bab")
		.pattern("gbg")
		.define('g', Items.GOLD_NUGGET)
		.define('b', Items.BRICK)
		.define('a', Items.ARROW)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.EMPTY_ENDER_MEDALLION.get())
		.pattern("gbg")
		.pattern("bab")
		.pattern("gbg")
		.define('g', Items.GOLD_NUGGET)
		.define('b', Items.BRICK)
		.define('a', Items.ENDER_PEARL)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.WOODEN_BOOMERANG.get())
		.pattern("www")
		.pattern("w  ")
		.pattern("w  ")
		.define('w', ItemTags.PLANKS)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.OAK_PLANKS))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.MAGIC_BOOMERANG.get())
		.pattern("dgg")
		.pattern("g  ")
		.pattern("g  ")
		.define('d', Items.DIAMOND)
		.define('g', Items.GOLD_INGOT)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.MAGIC_BOOMERANG.get())
		.pattern("wg ")
		.pattern("gd ")
		.define('w', RelicsItems.WOODEN_BOOMERANG.get())
		.define('d', Items.DIAMOND)
		.define('g', Items.GOLD_INGOT)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND))
		.save(consumer, RelicsUtil.modLoc("magic_boomerang_upgrade"));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.STARSTEEL_BOOMERANG.get())
		.pattern("gss")
		.pattern("s  ")
		.pattern("s  ")
		.define('g', RelicsItems.STARGLASS_LUMP.get())
		.define('s', RelicsItems.STARSTEEL_INGOT.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STARGLASS_LUMP.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.STARSTEEL_BOOMERANG.get())
		.pattern("ms ")
		.pattern("sg ")
		.define('m', RelicsItems.MAGIC_BOOMERANG.get())
		.define('g', RelicsItems.STARGLASS_LUMP.get())
		.define('s', RelicsItems.STARSTEEL_INGOT.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STARGLASS_LUMP.get()))
		.save(consumer, RelicsUtil.modLoc("starsteel_boomerang_upgrade"));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.PYRO_AMULET.get())
		.pattern(" g ")
		.pattern("g g")
		.pattern(" a ")
		.define('g', Items.GOLD_INGOT)
		.define('a', RelicsItems.FIRE_MEDALLION.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.FIRE_MEDALLION.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.GEO_AMULET.get())
		.pattern(" g ")
		.pattern("g g")
		.pattern(" a ")
		.define('g', Items.GOLD_INGOT)
		.define('a', RelicsItems.EARTH_MEDALLION.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.EARTH_MEDALLION.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.AERO_AMULET.get())
		.pattern(" g ")
		.pattern("g g")
		.pattern(" a ")
		.define('g', Items.GOLD_INGOT)
		.define('a', RelicsItems.WIND_MEDALLION.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.WIND_MEDALLION.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.END_AMULET.get())
		.pattern(" g ")
		.pattern("g g")
		.pattern(" a ")
		.define('g', Items.GOLD_INGOT)
		.define('a', RelicsItems.ENDER_MEDALLION.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ENDER_MEDALLION.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.WHIRLWIND_BOOTS.get())
		.pattern("g g")
		.pattern("waw")
		.define('g', Items.GOLD_INGOT)
		.define('w', ItemTags.WOOL)
		.define('a', RelicsItems.WIND_MEDALLION.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.WIND_MEDALLION.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.TITAN_BAND.get())
		.pattern("glg")
		.pattern("lal")
		.pattern("glg")
		.define('g', Items.GOLD_INGOT)
		.define('l', Items.LEATHER)
		.define('a', RelicsItems.EARTH_MEDALLION.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.EARTH_MEDALLION.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.HEADBAND_OF_VALOR.get())
		.pattern("www")
		.pattern("waw")
		.define('w', ItemTags.WOOL)
		.define('a', RelicsItems.FIRE_MEDALLION.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.FIRE_MEDALLION.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.REED_PIPES.get())
		.pattern("rrr")
		.pattern("rr ")
		.pattern("r  ")
		.define('r', Items.SUGAR_CANE)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR_CANE))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.SLIME_SWORD.get())
		.pattern("s")
		.pattern("s")
		.pattern("w")
		.define('s', Items.SLIME_BALL)
		.define('w', Items.STICK)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.SLIME_BALL))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsBlocks.BOOST_PLATE.get())
		.pattern("odo")
		.pattern("asa")
		.pattern("odo")
		.define('o', Items.OBSIDIAN)
		.define('d', Items.DIAMOND)
		.define('a', Items.AMETHYST_SHARD)
		.define('s', RelicsItems.STAR_DUST.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STAR_DUST.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.ENDER_SWORD.get())
		.pattern("i")
		.pattern("e")
		.pattern("s")
		.define('i', Items.IRON_INGOT)
		.define('e', RelicsItems.ENDER_MEDALLION.get())
		.define('s', Items.STICK)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ENDER_MEDALLION.get()))
		.save(consumer);
		
		// Sugar Cube: 9 sugar -> 1 sugar cube
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, RelicsBlocks.SUGAR_CUBE.get())
		.pattern("sss")
		.pattern("sss")
		.pattern("sss")
		.define('s', Items.SUGAR)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR))
		.save(consumer);
		
		// Sugar Cube -> 9 sugar (shapeless reverse)
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SUGAR, 9)
		.requires(RelicsBlocks.SUGAR_CUBE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsBlocks.SUGAR_CUBE.get()))
		.save(consumer, RelicsUtil.modLoc("sugar_from_sugar_cube"));
		
		// Rock Candy: 6x sugar cube tag + stick + gem + water bucket
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, RelicsItems.ROCK_CANDY_REDSTONE.get())
		.requires(Ingredient.of(RelicsTags.Items.SUGAR_CUBES), 6)
		.requires(Items.STICK)
		.requires(Items.REDSTONE)
		.requires(Items.WATER_BUCKET)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, RelicsItems.ROCK_CANDY_LAPIS.get())
		.requires(Ingredient.of(RelicsTags.Items.SUGAR_CUBES), 6)
		.requires(Items.STICK)
		.requires(Items.LAPIS_LAZULI)
		.requires(Items.WATER_BUCKET)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, RelicsItems.ROCK_CANDY_EMERALD.get())
		.requires(Ingredient.of(RelicsTags.Items.SUGAR_CUBES), 6)
		.requires(Items.STICK)
		.requires(Items.EMERALD)
		.requires(Items.WATER_BUCKET)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR))
		.save(consumer);
	
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, RelicsItems.ROCK_CANDY_DIAMOND.get())
		.requires(Ingredient.of(RelicsTags.Items.SUGAR_CUBES), 6)
		.requires(Items.STICK)
		.requires(Items.DIAMOND)
		.requires(Items.WATER_BUCKET)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, RelicsItems.BOMB_ARROW.get())
		.requires(Items.ARROW)
		.requires(RelicsItems.BOMB.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.BOMB.get()))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, RelicsBlocks.STARRY_SAND.get())
		.requires(Items.SAND)
		.requires(RelicsItems.STAR_DUST.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STAR_DUST.get()))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, RelicsBlocks.STARRY_SAND.get())
		.requires(Items.SAND)
		.requires(RelicsItems.INFUSED_STAR_DUST.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.INFUSED_STAR_DUST.get()))
		.save(consumer, RelicsUtil.modLoc("starry_sand_from_infused_dust"));
		
		// ---- Ritual recipes ----
		RitualRecipeBuilder.summoning()
		.pair(Blocks.RED_MUSHROOM, Blocks.MYCELIUM)
		.keystone(Blocks.EMERALD_BLOCK)
		.focusItem(Items.MUSHROOM_STEW)
		.result(EntityType.MOOSHROOM)
		.save(consumer, "summon_mooshroom");
		
		RitualRecipeBuilder.summoning()
		.pair(Blocks.WHITE_WOOL, Blocks.GRASS_BLOCK)
		.keystone(Blocks.EMERALD_BLOCK)
		.focusBlock(Blocks.GRASS_BLOCK)
		.result(EntityType.SHEEP)
		.save(consumer, "summon_sheep");
		
		RitualRecipeBuilder.summoning()
		.pair(Blocks.HAY_BLOCK, Blocks.GRASS_BLOCK)
		.keystone(Blocks.EMERALD_BLOCK)
		.focusItem(Items.SUGAR)
		.result(EntityType.HORSE, 4)
		.result(EntityType.DONKEY, 1)
		.save(consumer, "summon_horse");
		
		RitualRecipeBuilder.summoning()
		.pair(Blocks.BROWN_MUSHROOM, Blocks.GRASS_BLOCK)
		.keystone(Blocks.EMERALD_BLOCK)
		.focusItem(Items.CARROT)
		.result(EntityType.PIG)
		.save(consumer, "summon_pig");
		
		RitualRecipeBuilder.convert()
		.pair(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM)
		.source(EntityType.COW)
		.result(EntityType.MOOSHROOM)
		.save(consumer, "convert_mooshroom");
		
		RitualRecipeBuilder.crucible()
		.pair(Blocks.LAVA, Blocks.LAVA)
		.save(consumer, "crucible");
		
		RitualRecipeBuilder.blessing()
		.pair(Blocks.STONE, Blocks.STONE)
		.focusItem(Items.LEATHER)
		.effect(MobEffects.DAMAGE_RESISTANCE, 1)
		.duration(3600, 600)
		.save(consumer, "stoneskin");

//		// Soul tether enchanting
//		RitualRecipeBuilder.enchanting()
//		.pair(Blocks.SOUL_SAND, Blocks.TRIPWIRE)
//		.keystone(Blocks.IRON_BLOCK)
//		.nbt("soulTether", true)
//		.xpCost(10)
//		.save(consumer, "soul_tether");
		
		// Starglass lump: smelt starry sand
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(RelicsBlocks.STARRY_SAND.get()), RecipeCategory.MISC, RelicsItems.STARGLASS_LUMP.get(), 0.0f, 200)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsBlocks.STARRY_SAND.get()))
		.save(consumer);

		// Starsteel dust: infused star dust + iron dust (requires another mod to provide forge:dusts/iron)
		ConditionalRecipe.builder()
	    .addCondition(new NotCondition(new TagEmptyCondition(ResourceLocation.fromNamespaceAndPath("forge", "dusts/iron"))))
	    .addRecipe(
	    		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.STARSTEEL_DUST.get())
	    		.requires(RelicsItems.INFUSED_STAR_DUST.get())
	            .requires(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("forge", "dusts/iron")))
	            .unlockedBy("has_infused_star_dust", has(RelicsItems.INFUSED_STAR_DUST.get()))
	            ::save)
	    .build(consumer, ResourceLocation.fromNamespaceAndPath(References.MOD_ID, "starsteel_dust_from_iron_dust"));

		// Starsteel ingot: smelt starsteel dust
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(RelicsItems.STARSTEEL_DUST.get()), RecipeCategory.MISC, RelicsItems.STARSTEEL_INGOT.get(), 0.0f, 200)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STARSTEEL_DUST.get()))
		.save(consumer);

		// Starsteel ingot (alt): dimensional catalyst + iron ingot + infused star dust
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.STARSTEEL_INGOT.get())
		.requires(RelicsItems.DIMENSIONAL_CATALYST.get())
		.requires(Items.IRON_INGOT)
		.requires(RelicsItems.INFUSED_STAR_DUST.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.DIMENSIONAL_CATALYST.get()))
		.save(consumer, RelicsUtil.modLoc("starsteel_ingot_from_catalyst"));

		// Abstraction gel: slime ball + infused star dust -> 4 gel
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.ABSTRACTION_GEL.get(), 4)
		.requires(Items.SLIME_BALL)
		.requires(RelicsItems.INFUSED_STAR_DUST.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.INFUSED_STAR_DUST.get()))
		.save(consumer);

		// Dimensional catalyst: infused star dust + ender pearl -> 16 catalyst
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.DIMENSIONAL_CATALYST.get(), 16)
		.requires(RelicsItems.INFUSED_STAR_DUST.get())
		.requires(Items.ENDER_PEARL)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.INFUSED_STAR_DUST.get()))
		.save(consumer);

		// Azurite dot: smelt azurite dust
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(RelicsItems.AZURITE_DUST.get()), RecipeCategory.MISC, RelicsItems.AZURITE_DOT.get(), 0.0f, 200)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.AZURITE_DUST.get()))
		.save(consumer);
		
		// Azurite dust: reverse from azurite dot
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.AZURITE_DUST.get())
		.requires(RelicsItems.AZURITE_DOT.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.AZURITE_DOT.get()))
		.save(consumer, RelicsUtil.modLoc("azurite_dust_from_dot"));

		// ---- Nucleus recipes: abstraction gel + ingredient ----

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.NUCLEUS_FIRE.get())
		.requires(RelicsItems.ABSTRACTION_GEL.get())
		.requires(Items.LAVA_BUCKET)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ABSTRACTION_GEL.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.NUCLEUS_ICE.get())
		.requires(RelicsItems.ABSTRACTION_GEL.get())
		.requires(Blocks.ICE)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ABSTRACTION_GEL.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.NUCLEUS_LIGHTNING.get())
		.requires(RelicsItems.ABSTRACTION_GEL.get())
		.requires(RelicsItems.FULGURITE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ABSTRACTION_GEL.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.NUCLEUS_CUT.get())
		.requires(RelicsItems.ABSTRACTION_GEL.get())
		.requires(Items.SHEARS)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ABSTRACTION_GEL.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.NUCLEUS_SKY.get())
		.requires(RelicsItems.ABSTRACTION_GEL.get())
		.requires(RelicsItems.AZURITE_DOT.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ABSTRACTION_GEL.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.NUCLEUS_SUN.get())
		.requires(RelicsItems.ABSTRACTION_GEL.get())
		.requires(Items.SUNFLOWER)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ABSTRACTION_GEL.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.NUCLEUS_NAVIGATE.get())
		.requires(RelicsItems.ABSTRACTION_GEL.get())
		.requires(Items.COMPASS)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ABSTRACTION_GEL.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.NUCLEUS_DARK.get())
		.requires(RelicsItems.ABSTRACTION_GEL.get())
		.requires(Blocks.OBSIDIAN)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ABSTRACTION_GEL.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.NUCLEUS_STAR.get())
		.requires(RelicsItems.ABSTRACTION_GEL.get())
		.requires(RelicsItems.STAR_PIECE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ABSTRACTION_GEL.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.NUCLEUS_HEALTH.get())
		.requires(RelicsItems.ABSTRACTION_GEL.get())
		.requires(Items.APPLE)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ABSTRACTION_GEL.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.NUCLEUS_WEAPON.get())
		.requires(RelicsItems.ABSTRACTION_GEL.get())
		.requires(Items.IRON_SWORD)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ABSTRACTION_GEL.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.NUCLEUS_WEALTH.get())
		.requires(RelicsItems.ABSTRACTION_GEL.get())
		.requires(Items.EMERALD)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.ABSTRACTION_GEL.get()))
		.save(consumer);

		// ---- Gem recipes: diamond + nucleus ----

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.GEM_FIRE.get())
		.requires(Items.DIAMOND)
		.requires(RelicsItems.NUCLEUS_FIRE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.NUCLEUS_FIRE.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.GEM_ICE.get())
		.requires(Items.DIAMOND)
		.requires(RelicsItems.NUCLEUS_ICE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.NUCLEUS_ICE.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.GEM_LIGHTNING.get())
		.requires(Items.DIAMOND)
		.requires(RelicsItems.NUCLEUS_LIGHTNING.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.NUCLEUS_LIGHTNING.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.GEM_CUT.get())
		.requires(Items.DIAMOND)
		.requires(RelicsItems.NUCLEUS_CUT.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.NUCLEUS_CUT.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.GEM_SKY.get())
		.requires(Items.DIAMOND)
		.requires(RelicsItems.NUCLEUS_SKY.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.NUCLEUS_SKY.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.GEM_SUN.get())
		.requires(Items.DIAMOND)
		.requires(RelicsItems.NUCLEUS_SUN.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.NUCLEUS_SUN.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.GEM_NAVIGATE.get())
		.requires(Items.DIAMOND)
		.requires(RelicsItems.NUCLEUS_NAVIGATE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.NUCLEUS_NAVIGATE.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.GEM_DARK.get())
		.requires(Items.DIAMOND)
		.requires(RelicsItems.NUCLEUS_DARK.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.NUCLEUS_DARK.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.GEM_STAR.get())
		.requires(Items.DIAMOND)
		.requires(RelicsItems.NUCLEUS_STAR.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.NUCLEUS_STAR.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.GEM_HEALTH.get())
		.requires(Items.DIAMOND)
		.requires(RelicsItems.NUCLEUS_HEALTH.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.NUCLEUS_HEALTH.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.GEM_WEAPON.get())
		.requires(Items.DIAMOND)
		.requires(RelicsItems.NUCLEUS_WEAPON.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.NUCLEUS_WEAPON.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.GEM_WEALTH.get())
		.requires(Items.DIAMOND)
		.requires(RelicsItems.NUCLEUS_WEALTH.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.NUCLEUS_WEALTH.get()))
		.save(consumer);

		// Azurite sphere: 9 azurite dots → 1 sphere
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.AZURITE_SPHERE.get())
		.define('D', RelicsItems.AZURITE_DOT.get())
		.pattern("DDD").pattern("DDD").pattern("DDD")
		.unlockedBy(hasItem, has(RelicsItems.AZURITE_DOT.get()))
		.group(References.CREATIVE_TAB).save(consumer);

		// Empty shell: 4 starglass lumps → 8 shells
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.STARGLASS_SHELL.get(), 8)
		.define('X', RelicsItems.STARGLASS_LUMP.get())
		.pattern(" X ").pattern("X X").pattern(" X ")
		.unlockedBy(hasItem, has(RelicsItems.STARGLASS_LUMP.get()))
		.group(References.CREATIVE_TAB).save(consumer);

		// Water shell
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.WATER_SHELL.get())
		.requires(RelicsItems.STARGLASS_SHELL.get())
		.requires(RelicsItems.DIMENSIONAL_CATALYST.get())
		.requires(Items.WATER_BUCKET)
		.unlockedBy(hasItem, has(RelicsItems.STARGLASS_SHELL.get()))
		.group(References.CREATIVE_TAB).save(consumer);

		// Lava shell
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.LAVA_SHELL.get())
		.requires(RelicsItems.STARGLASS_SHELL.get())
		.requires(RelicsItems.DIMENSIONAL_CATALYST.get())
		.requires(Items.LAVA_BUCKET)
		.unlockedBy(hasItem, has(RelicsItems.STARGLASS_SHELL.get()))
		.group(References.CREATIVE_TAB).save(consumer);

		// Blast shell
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.BLAST_SHELL.get())
		.requires(RelicsItems.STARGLASS_SHELL.get())
		.requires(RelicsItems.DIMENSIONAL_CATALYST.get())
		.requires(Items.GUNPOWDER)
		.requires(Items.FLINT)
		.unlockedBy(hasItem, has(RelicsItems.STARGLASS_SHELL.get()))
		.group(References.CREATIVE_TAB).save(consumer);

		// Glittering orb
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.GLITTERING_ORB.get())
		.requires(RelicsItems.STARGLASS_SHELL.get())
		.requires(RelicsItems.DIMENSIONAL_CATALYST.get())
		.requires(RelicsItems.INFUSED_STAR_PIECE.get())
		.unlockedBy(hasItem, has(RelicsItems.STARGLASS_SHELL.get()))
		.group(References.CREATIVE_TAB).save(consumer);

		// Burning orb
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.BURNING_ORB.get())
		.requires(RelicsItems.STARGLASS_SHELL.get())
		.requires(RelicsItems.DIMENSIONAL_CATALYST.get())
		.requires(RelicsItems.DIMENSIONAL_CATALYST.get())
		.requires(RelicsItems.INFUSED_STAR_PIECE.get())
		.requires(RelicsItems.NUCLEUS_FIRE.get())
		.unlockedBy(hasItem, has(RelicsItems.STARGLASS_SHELL.get()))
		.group(References.CREATIVE_TAB).save(consumer);
		
		// Freezing orb
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.FREEZING_ORB.get())
		.requires(RelicsItems.STARGLASS_SHELL.get())
		.requires(RelicsItems.DIMENSIONAL_CATALYST.get())
		.requires(RelicsItems.DIMENSIONAL_CATALYST.get())
		.requires(RelicsItems.INFUSED_STAR_PIECE.get())
		.requires(RelicsItems.NUCLEUS_ICE.get())
		.unlockedBy(hasItem, has(RelicsItems.STARGLASS_SHELL.get()))
		.group(References.CREATIVE_TAB).save(consumer);

		// Shocking orb
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.SHOCKING_ORB.get())
		.requires(RelicsItems.STARGLASS_SHELL.get())
		.requires(RelicsItems.DIMENSIONAL_CATALYST.get())
		.requires(RelicsItems.DIMENSIONAL_CATALYST.get())
		.requires(RelicsItems.INFUSED_STAR_PIECE.get())
		.requires(RelicsItems.NUCLEUS_LIGHTNING.get())
		.unlockedBy(hasItem, has(RelicsItems.STARGLASS_SHELL.get()))
		.group(References.CREATIVE_TAB).save(consumer);
		
		// Gold band rings
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.SPEED_RING.get())
        .pattern(" MG").pattern("MSM").pattern(" M ")
        .define('M', Items.GOLD_INGOT)
        .define('G', RelicsItems.GEM_LIGHTNING.get())
        .define('S', RelicsItems.INFUSED_STAR_PIECE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.GEM_LIGHTNING.get()))
        .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.SOFT_FALL_RING.get())
        .pattern(" MG").pattern("MSM").pattern(" M ")
        .define('M', Items.GOLD_INGOT)
        .define('G', RelicsItems.GEM_SKY.get())
        .define('S', RelicsItems.INFUSED_STAR_PIECE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.GEM_SKY.get()))
        .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.CONVECTION_RING.get())
        .pattern(" MG").pattern("MSM").pattern(" M ")
        .define('M', Items.GOLD_INGOT)
        .define('G', RelicsItems.GEM_FIRE.get())
        .define('S', RelicsItems.INFUSED_STAR_PIECE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.GEM_FIRE.get()))
        .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.COLD_FEET_RING.get())
        .pattern(" MG").pattern("MSM").pattern(" M ")
        .define('M', Items.GOLD_INGOT)
        .define('G', RelicsItems.GEM_ICE.get())
        .define('S', RelicsItems.INFUSED_STAR_PIECE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.GEM_ICE.get()))
        .save(consumer);

        // Plain ring: no gem
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.PLAIN_RING.get())
        .pattern(" M ").pattern("MSM").pattern(" M ")
        .define('M', Items.GOLD_INGOT)
        .define('S', RelicsItems.INFUSED_STAR_PIECE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.INFUSED_STAR_PIECE.get()))
        .save(consumer);

        // Starglass band rings
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.RESONANCE_RING.get())
        .pattern(" MG").pattern("MSM").pattern(" M ")
        .define('M', RelicsItems.STARGLASS_LUMP.get())
        .define('G', RelicsItems.GEM_SUN.get())
        .define('S', RelicsItems.INFUSED_STAR_PIECE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.GEM_SUN.get()))
        .save(consumer);

        // Wish ring uses infused star stone instead of piece
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.WISH_RING.get())
        .pattern(" MG").pattern("MSM").pattern(" M ")
        .define('M', RelicsItems.STARGLASS_LUMP.get())
        .define('G', RelicsItems.GEM_STAR.get())
        .define('S', RelicsItems.INFUSED_STAR_STONE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.GEM_STAR.get()))
        .save(consumer);

        // Iron band rings
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.MAGE_RING.get())
        .pattern(" MG").pattern("MSM").pattern(" M ")
        .define('M', Items.IRON_INGOT)
        .define('G', RelicsItems.GEM_STAR.get())
        .define('S', RelicsItems.INFUSED_STAR_PIECE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.GEM_STAR.get()))
        .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.WARRIOR_RING.get())
        .pattern(" MG").pattern("MSM").pattern(" M ")
        .define('M', Items.IRON_INGOT)
        .define('G', RelicsItems.GEM_WEAPON.get())
        .define('S', RelicsItems.INFUSED_STAR_PIECE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.GEM_WEAPON.get()))
        .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.THIEF_RING.get())
        .pattern(" MG").pattern("MSM").pattern(" M ")
        .define('M', Items.IRON_INGOT)
        .define('G', RelicsItems.GEM_DARK.get())
        .define('S', RelicsItems.INFUSED_STAR_PIECE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.GEM_DARK.get()))
        .save(consumer);

        // Wood band rings (planks tag)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.ARROWFIND_RING.get())
        .pattern(" MG").pattern("MSM").pattern(" M ")
        .define('M', ItemTags.PLANKS)
        .define('G', RelicsItems.GEM_WEAPON.get())
        .define('S', RelicsItems.INFUSED_STAR_PIECE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.GEM_WEAPON.get()))
        .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.AZUREFIND_RING.get())
        .pattern(" MG").pattern("MSM").pattern(" M ")
        .define('M', ItemTags.PLANKS)
        .define('G', RelicsItems.GEM_SKY.get())
        .define('S', RelicsItems.INFUSED_STAR_PIECE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.GEM_SKY.get()))
        .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.FORTUNE_RING.get())
        .pattern(" MG").pattern("MSM")
        .pattern(" M ")
        .define('M', ItemTags.PLANKS)
        .define('G', RelicsItems.GEM_WEALTH.get())
        .define('S', RelicsItems.INFUSED_STAR_PIECE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.GEM_WEALTH.get()))
        .save(consumer);
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.PHOENIX_CHARM.get())
        .requires(RelicsItems.PHOENIX_FEATHER.get())
        .requires(RelicsItems.INFUSED_STAR_PIECE.get())
        .requires(Items.STRING)
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.PHOENIX_FEATHER.get()))
        .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.BLAST_CHARM.get())
        .requires(Items.GUNPOWDER)
        .requires(RelicsItems.INFUSED_STAR_PIECE.get())
        .requires(Items.STRING)
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.INFUSED_STAR_PIECE.get()))
        .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.FEATHER_CHARM.get())
        .requires(Items.FEATHER)
        .requires(RelicsItems.INFUSED_STAR_PIECE.get())
        .requires(Items.STRING)
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.INFUSED_STAR_PIECE.get()))
        .save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsBlocks.SKY_LENS.get())
        .pattern("SSS")
        .pattern("SAS")
        .pattern("SSS")
        .define('S', RelicsItems.STARGLASS_LUMP.get())
        .define('A', RelicsItems.AZURITE_SPHERE.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.STARGLASS_LUMP.get()))
        .save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.TUNING_FORK.get())
        .pattern("S S")
        .pattern("SSS")
        .pattern(" S ")
        .define('S', RelicsItems.STARSTEEL_INGOT.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.STARSTEEL_INGOT.get()))
        .save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsBlocks.RITUAL_LOCUS.get())
        .pattern("SNS")
        .pattern("NSN")
        .pattern("SNS")
        .define('S', RelicsItems.STARGLASS_LUMP.get())
        .define('N', Items.GOLD_NUGGET)
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.STARGLASS_LUMP.get()))
        .save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.BOMB_BAG.get())
        .pattern("L L")
        .pattern("LBL")
        .pattern("LLL")
        .define('L', Items.LEATHER)
        .define('B', RelicsItems.BOMB.get())
        .group(References.CREATIVE_TAB)
        .unlockedBy(hasItem, has(RelicsItems.BOMB.get()))
        .save(consumer);
        
        // fire staff
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RelicsItems.FIRE_STAFF.get())
        .pattern("  O")
        .pattern(" S ")
        .pattern("S  ")
		.define('O', RelicsItems.BURNING_ORB.get())
		.define('S', Items.STICK)
		.unlockedBy("has_burning_orb", has(RelicsItems.BURNING_ORB.get()))
		.save(consumer);
		
		// ice staff
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RelicsItems.ICE_STAFF.get())
		.pattern("  O")
		.pattern(" S ")
		.pattern("S  ")
		.define('O', RelicsItems.FREEZING_ORB.get())
		.define('S', Items.STICK)
		.unlockedBy("has_freezing_orb", has(RelicsItems.FREEZING_ORB.get()))
		.save(consumer);
		
		// lightning staff
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RelicsItems.LIGHTNING_STAFF.get())
		.pattern("  O")
		.pattern(" S ")
		.pattern("S  ")
		.define('O', RelicsItems.SHOCKING_ORB.get())
		.define('S', Items.STICK)
		.unlockedBy("has_shocking_orb", has(RelicsItems.SHOCKING_ORB.get()))
		.save(consumer);
		
		// twinkle staff
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RelicsItems.TWINKLE_STAFF.get())
		.pattern("  O")
		.pattern(" S ")
		.pattern("S  ")
		.define('O', RelicsItems.GLITTERING_ORB.get())
		.define('S', Items.STICK)
		.unlockedBy("has_glittering_orb", has(RelicsItems.GLITTERING_ORB.get()))
		.save(consumer);
		
		// blank spellbook (shapeless: book + ink sac + infused star dust + phoenix feather)
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, RelicsItems.BLANK_SPELLBOOK.get())
		.requires(Items.BOOK)
		.requires(Items.INK_SAC)
		.requires(RelicsItems.INFUSED_STAR_DUST.get())
		.requires(RelicsItems.PHOENIX_FEATHER.get())
		.unlockedBy("has_infused_star_dust", has(RelicsItems.INFUSED_STAR_DUST.get()))
		.save(consumer);

		// tome of scythewind
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, RelicsItems.TOME_SCYTHEWIND.get())
		.requires(RelicsItems.BLANK_SPELLBOOK.get())
		.requires(RelicsItems.INFUSED_STAR_PIECE.get())
		.requires(RelicsItems.NUCLEUS_SKY.get())
		.requires(RelicsItems.NUCLEUS_CUT.get())
		.unlockedBy("has_blank_spellbook", has(RelicsItems.BLANK_SPELLBOOK.get()))
		.save(consumer);

		// tome of rayfire
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, RelicsItems.TOME_RAYFIRE.get())
		.requires(RelicsItems.BLANK_SPELLBOOK.get())
		.requires(RelicsItems.INFUSED_STAR_PIECE.get())
		.requires(RelicsItems.NUCLEUS_FIRE.get())
		.requires(RelicsItems.NUCLEUS_SUN.get())
		.unlockedBy("has_blank_spellbook", has(RelicsItems.BLANK_SPELLBOOK.get()))
		.save(consumer);

		// tome of exeunt
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, RelicsItems.TOME_EXEUNT.get())
		.requires(RelicsItems.BLANK_SPELLBOOK.get())
		.requires(RelicsItems.INFUSED_STAR_PIECE.get())
		.requires(RelicsItems.NUCLEUS_SKY.get())
		.requires(RelicsItems.NUCLEUS_NAVIGATE.get())
		.unlockedBy("has_blank_spellbook", has(RelicsItems.BLANK_SPELLBOOK.get()))
		.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.AZURE_FEATHER.get())
		.requires(Items.FEATHER)
		.requires(RelicsItems.AZURITE_DUST.get())
		.requires(RelicsItems.INFUSED_STAR_DUST.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, has(RelicsItems.AZURITE_DUST.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.AZURE_MANTLE.get())
		.pattern("FGF")
		.pattern("FFF")
		.pattern("FFF")
		.define('F', RelicsItems.AZURE_FEATHER.get())
		.define('G', Items.GOLD_INGOT)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, has(RelicsItems.AZURE_FEATHER.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.PHOENIX_MANTLE.get())
		.pattern("FGF")
		.pattern("FFF")
		.pattern("FFF")
		.define('F', RelicsItems.PHOENIX_FEATHER.get())
		.define('G', RelicsItems.SUNFIRE_DIAMOND.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, has(RelicsItems.PHOENIX_FEATHER.get()))
		.save(consumer);
		
		// Phoenix offering: fire+gold_block edge + starstone singleton
		RitualRecipeBuilder.offering()
		.pair(Blocks.FIRE, Blocks.GOLD_BLOCK)
		.keystone(RelicsBlocks.STARSTONE_BLOCK.get())
		.spirit("phoenix")
		.save(consumer, "phoenix_offering");

		// Phoenix altar offering: fire+fire edge, requires altar as focus
		RitualRecipeBuilder.offering()
		.pair(Blocks.FIRE, Blocks.FIRE)
		.focusBlock(RelicsBlocks.PHOENIX_ALTAR.get())
		.spirit("phoenix")
		.save(consumer, "phoenix_altar_offering");

		// Black emblem: 5 gold ingots in cross (LG2 recipe)
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsItems.BLACK_EMBLEM.get())
		.pattern(" G ")
		.pattern("GGG")
		.pattern(" G ")
		.define('G', Items.GOLD_INGOT)
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT))
		.save(consumer);

		// Phoenix altar: gold_block frame + phoenix emblem + sunfire diamond
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RelicsBlocks.PHOENIX_ALTAR.get())
		.pattern("GEG")
		.pattern("GDG")
		.define('G', Blocks.GOLD_BLOCK)
		.define('E', RelicsItems.PHOENIX_EMBLEM.get())
		.define('D', RelicsItems.SUNFIRE_DIAMOND.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.SUNFIRE_DIAMOND.get()))
		.save(consumer);
		}
}