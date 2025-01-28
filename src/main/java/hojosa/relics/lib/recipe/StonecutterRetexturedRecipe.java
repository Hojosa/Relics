package hojosa.relics.lib.recipe;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import lombok.Getter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.CraftingHelper;
import slimeknights.mantle.recipe.helper.LoggingRecipeSerializer;
import slimeknights.mantle.util.JsonHelper;
import slimeknights.mantle.util.RetexturedHelper;

public class StonecutterRetexturedRecipe extends StonecutterRecipe {
	@Getter
	private final Ingredient texture;
	private final boolean matchAll;

	public StonecutterRetexturedRecipe(StonecutterRecipe orig, Ingredient texture, boolean matchAll) {
		super(orig.getId(), orig.getGroup(), orig.getIngredients().get(0), orig.result);
		this.texture = texture;
		this.matchAll = matchAll;
	}

	@Override
	public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
		ItemStack result = super.assemble(pContainer, pRegistryAccess);
		ItemStack stack = pContainer.getItem(0);
		return RetexturedHelper.setTexture(result, Block.byItem(stack.getItem()));
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return RelicsRecipeSerializers.STONECUTTER_RETEXTURED;
	}

	public static class Serializer implements LoggingRecipeSerializer<StonecutterRetexturedRecipe> {

		@Override
		public StonecutterRetexturedRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			StonecutterRecipe recipe = STONECUTTER.fromJson(recipeId, json);
			Ingredient texture = CraftingHelper.getIngredient(JsonHelper.getElement(json, "texture"), false);
			boolean matchAll = false;
			if (json.has("match_all")) {
				matchAll = json.get("match_all").getAsBoolean();
			}
			return new StonecutterRetexturedRecipe(recipe, texture, matchAll);
		}

		@Nullable
		@Override
		public StonecutterRetexturedRecipe fromNetworkSafe(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			StonecutterRecipe recipe = STONECUTTER.fromNetwork(recipeId, buffer);
			return recipe == null ? null
					: new StonecutterRetexturedRecipe(recipe, Ingredient.fromNetwork(buffer), buffer.readBoolean());
		}

		@Override
		public void toNetworkSafe(FriendlyByteBuf buffer, StonecutterRetexturedRecipe recipe) {
			STONECUTTER.toNetwork(buffer, recipe);
			recipe.texture.toNetwork(buffer);
			buffer.writeBoolean(recipe.matchAll);
		}
	}
}
