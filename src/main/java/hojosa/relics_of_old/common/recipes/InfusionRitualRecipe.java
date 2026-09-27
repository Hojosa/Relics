package hojosa.relics_of_old.common.recipes;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import hojosa.relics_of_old.Relics;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

//Infusion ritual recipe: target item + 2-8 unordered ingredients -> result
//Container layout: slot 0 = target item, slots 1+ = ingredient items
public class InfusionRitualRecipe implements Recipe<Container> {
	private final ResourceLocation id;
	private final Ingredient target;
	private final NonNullList<Ingredient> ingredients;
	private final ItemStack result;

	public InfusionRitualRecipe(ResourceLocation id, Ingredient target, NonNullList<Ingredient> ingredients, ItemStack result) {
		this.id = id;
		this.target = target;
		this.ingredients = ingredients;
		this.result = result;
	}

	public Ingredient getTarget() {
		return target;
	}

	@Override
	public boolean matches(Container container, Level level) {
		if (level.isClientSide())
			return false;

		// Slot 0 is the target item
		if (!target.test(container.getItem(0)))
			return false;

		// Remaining slots are ingredients (unordered matching)
		int ingredientCount = container.getContainerSize() - 1;
		if (ingredientCount != ingredients.size())
			return false;

		var inventory = NonNullList.<ItemStack>create();
		for (int i = 1; i < container.getContainerSize(); i++) {
			ItemStack item = container.getItem(i);
			if (!item.isEmpty()) {
				inventory.add(item);
			}
		}
		return RecipeMatcher.findMatches(inventory, this.ingredients) != null;
	}

	@Override
	public @NotNull NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public ItemStack assemble(Container container, RegistryAccess registryAccess) {
		return result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return result.copy();
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}

	public static class Type implements RecipeType<InfusionRitualRecipe> {
		private Type() {
		}

		public static final Type INSTANCE = new Type();
		public static final String ID = "infusion_ritual";
	}

	public static class Serializer implements RecipeSerializer<InfusionRitualRecipe> {
		private Serializer() {
		}

		public static final Serializer INSTANCE = new Serializer();
		public static final ResourceLocation ID = RelicsUtil.modLoc("infusion_ritual");

		@Override
		public InfusionRitualRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			Ingredient target = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "target"));
			ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

			JsonArray ingredientsJson = GsonHelper.getAsJsonArray(json, "ingredients");
			NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientsJson.size(), Ingredient.EMPTY);
			for (int i = 0; i < ingredients.size(); i++) {
				ingredients.set(i, Ingredient.fromJson(ingredientsJson.get(i)));
			}
			if (ingredients.size() < 4 || ingredients.size() > 8) {
				throw new com.google.gson.JsonSyntaxException("Infusion ritual recipe must have 4-8 ingredients, got " + ingredients.size());
			}
			return new InfusionRitualRecipe(recipeId, target, ingredients, result);
		}

		@Override
		public @Nullable InfusionRitualRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
			try {
				Ingredient target = Ingredient.fromNetwork(buf);
				int size = buf.readInt();
				NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
				for (int i = 0; i < size; i++) {
					ingredients.set(i, Ingredient.fromNetwork(buf));
				}
				ItemStack result = buf.readItem();
				return new InfusionRitualRecipe(recipeId, target, ingredients, result);
			} catch (Exception ex) {
				Relics.LOGGER.error("Unable to read infusion ritual recipe ({}) from network buffer.", recipeId);
				throw ex;
			}
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, InfusionRitualRecipe recipe) {
			recipe.target.toNetwork(buf);
			buf.writeInt(recipe.ingredients.size());
			for (Ingredient ingredient : recipe.ingredients) {
				ingredient.toNetwork(buf);
			}
			buf.writeItemStack(recipe.getResultItem(null), false);
		}
	}
}