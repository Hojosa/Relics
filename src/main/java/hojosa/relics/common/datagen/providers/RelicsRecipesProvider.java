package hojosa.relics.common.datagen.providers;


import java.util.function.Consumer;

import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.common.init.RelicsTags;
import hojosa.relics.lib.References;
import hojosa.relics.lib.RelicsUtil;
import hojosa.relics.lib.recipe.StonecutterRetexturedRecipeBuilder;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;


public class RelicsRecipesProvider extends RecipeProvider {

	public RelicsRecipesProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RelicsBlocks.LAPIS_BRICK.get())
		.pattern("xx")
		.pattern("xx")
		.define('x', RelicsItems.BRICK_BLUE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.LAPIS_BLOCK))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.CLAY_BLUE.get())
		.requires(Items.CLAY_BALL)
		.requires(Items.LAPIS_LAZULI)
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CLAY_BALL))
		.save(consumer);
		
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(RelicsItems.CLAY_BLUE.get()), RecipeCategory.MISC, RelicsItems.BRICK_BLUE.get(), 1.0f, 100)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.CLAY_BLUE.get()))
		.save(consumer);

		StonecutterRetexturedRecipeBuilder.fromStonecutter(
				SingleItemRecipeBuilder.stonecutting(Ingredient.of(RelicsTags.Items.SWORD_PEDESTAL_VARIANTS), RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL_NORMAL)
				.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.CLAY_BLUE.get())))
		.setSource(RelicsTags.Items.SWORD_PEDESTAL_VARIANTS)
		.setMatchAll()
		.build(consumer);
		
		StonecutterRetexturedRecipeBuilder.fromStonecutter(
				SingleItemRecipeBuilder.stonecutting(Ingredient.of(RelicsTags.Items.SWORD_PEDESTAL_VARIANTS), RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL_RELIC)
				.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.CLAY_BLUE.get())))
		.setSource(RelicsTags.Items.SWORD_PEDESTAL_VARIANTS)
		.setMatchAll()
		.build(consumer);
		
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.CHISELED_STONE_BRICKS), RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL_TIME)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CHISELED_STONE_BRICKS))
		.save(consumer);
		
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.CHISELED_STONE_BRICKS), RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL_TWILIGHT)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CHISELED_STONE_BRICKS))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsBlocks.ODDISH_POT.get())
		.pattern("g")
		.pattern("p")
		.define('g', Items.GRASS)
		.define('p', Items.FLOWER_POT)
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.FLOWER_POT))
		.save(consumer);
		
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.STONE), RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL_STONE)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.STAR_DUST.get(), 3)
		.requires(RelicsItems.STAR_PIECE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STAR_PIECE.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.STAR_PIECE.get())
		.pattern("dd")
		.pattern("dd")
		.define('d', RelicsItems.STAR_DUST.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STAR_DUST.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.STAR_STONE.get())
		.pattern("ppp")
		.pattern("ppp")
		.pattern("ppp")
		.define('p', RelicsItems.STAR_PIECE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STAR_PIECE.get()))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.INFUSED_STAR_DUST.get(), 3)
		.requires(RelicsItems.INFUSED_STAR_PIECE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.INFUSED_STAR_PIECE.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.INFUSED_STAR_PIECE.get())
		.pattern("dd")
		.pattern("dd")
		.define('d', RelicsItems.INFUSED_STAR_DUST.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.INFUSED_STAR_DUST.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.INFUSED_STAR_STONE.get())
		.pattern("ppp")
		.pattern("ppp")
		.pattern("ppp")
		.define('p', RelicsItems.INFUSED_STAR_PIECE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.INFUSED_STAR_PIECE.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsItems.EMERALD_PIECE.get())
		.pattern("eee")
		.pattern("e e")
		.pattern("eee")
		.define('e', RelicsItems.EMERALD_SHARD.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.EMERALD_SHARD.get()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.EMERALD)
		.pattern("eee")
		.pattern("e e")
		.pattern("eee")
		.define('e', RelicsItems.EMERALD_PIECE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.EMERALD_PIECE.get()))
		.save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.EMERALD_PIECE.get(), 8)
		.requires(Items.EMERALD)
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.EMERALD_PIECE.get()))
		.save(consumer, RelicsUtil.modLoc("emerald_piece_reverse"));
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RelicsItems.EMERALD_SHARD.get(), 8)
		.requires(RelicsItems.EMERALD_PIECE.get())
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.EMERALD_PIECE.get()))
		.save(consumer);
		
	}
}
