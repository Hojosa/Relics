package hojosa.relics_of_old.common.datagen.providers;


import java.util.function.Consumer;

import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsTags;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.RelicsUtil;
import hojosa.relics_of_old.lib.recipe.StonecutterRetexturedRecipeBuilder;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
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
		.define('s', Items.MILK_BUCKET)
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
		.pattern("s  ")
		.pattern("s  ")
		.pattern("w  ")
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
		.pattern("i  ")
		.pattern("e  ")
		.pattern("s  ")
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
	}
}