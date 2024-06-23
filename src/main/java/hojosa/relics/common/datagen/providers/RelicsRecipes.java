package hojosa.relics.common.datagen.providers;


import java.util.function.Consumer;

import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.lib.References;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;


public class RelicsRecipes extends RecipeProvider {

	public RelicsRecipes(PackOutput packOutput) {
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
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL.get())
		.pattern(" x ")
		.pattern("x#x")
		.define('x', Blocks.STONE_SLAB)
		.define('#', Blocks.STONE)
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL_OOT.get())
		.pattern(" x ")
		.pattern("x#x")
		.define('x', Blocks.STONE_SLAB)
		.define('#', Items.GOLD_INGOT)
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get())
		.pattern(" x ")
		.pattern("x#x")
		.define('x', Blocks.STONE_SLAB)
		.define('#', Items.IRON_INGOT)
		.group(References.CREATIVE_TAB)
		.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
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
	}
	
}
