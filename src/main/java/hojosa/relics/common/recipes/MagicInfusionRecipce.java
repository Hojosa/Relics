package hojosa.relics.common.recipes;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import hojosa.relics.lib.RelicsUtil;
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

public class MagicInfusionRecipce implements Recipe<Container> {
	private final NonNullList<Ingredient> inputItems;
	private final ItemStack output;
	private final ResourceLocation id;

	public MagicInfusionRecipce(NonNullList<Ingredient> inputItems, ItemStack output, ResourceLocation id) {
		this.inputItems = inputItems;
		this.output = output;
		this.id = id;
	}

	@Override
	public boolean matches(Container pContainer, Level pLevel) {
		if (pLevel.isClientSide()) {
			return false;
		}

        if (this.inputItems.size() != pContainer.getContainerSize())
            return false;
		
		var inventory = NonNullList.<ItemStack>create();
        for (var i = 0; i < pContainer.getContainerSize(); i++) {
            var item = pContainer.getItem(i);
            if (!item.isEmpty()) {
            	inventory.add(item);
            }
        }
		return RecipeMatcher.findMatches(inventory, this.inputItems) != null;
	}

	@Override
	public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
		return output.copy();
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
		return output.copy();
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
	
	public static class Type implements RecipeType<MagicInfusionRecipce> {
		public static final Type INSTANCE = new Type();
		public static final String ID = "magic_infusion";
	}
	
	public static class Serializer implements RecipeSerializer<MagicInfusionRecipce> {
		public static final Serializer INSTANCE = new Serializer();
		public static final ResourceLocation ID = RelicsUtil.modLoc("magic_infusion");
		
		@Override
		public MagicInfusionRecipce fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(4, Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new MagicInfusionRecipce(inputs, output, pRecipeId);
		}
		
		@Override
		public @Nullable MagicInfusionRecipce fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack output = pBuffer.readItem();
            return new MagicInfusionRecipce(inputs, output, pRecipeId);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, MagicInfusionRecipce pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
		}
	}
}
