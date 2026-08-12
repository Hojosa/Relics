package hojosa.relics_of_old.common.recipes;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import hojosa.relics_of_old.common.item.Medallion;
import hojosa.relics_of_old.lib.RelicsUtil;
import hojosa.relics_of_old.lib.RelicsUtil.ElementType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;

public class MedallionSmithingRecipe implements SmithingRecipe {

	private final ResourceLocation id;

	public MedallionSmithingRecipe(ResourceLocation id) {
		this.id = id;
	}

	@Override
	public boolean matches(Container pContainer, Level pLevel) {
		// slot 0 = template (unused, must be empty)
		// slot 1 = base (any sword without existing augment)
		// slot 2 = addition (any charged medallion)
		if (!pContainer.getItem(0).isEmpty())
			return false;

		ItemStack base = pContainer.getItem(1);
		ItemStack addition = pContainer.getItem(2);

		if (!(base.getItem() instanceof SwordItem))
			return false;
		if (base.hasTag() && base.getTag().contains("medallion"))
			return false;
		if (!(addition.getItem() instanceof Medallion))
			return false;

		return true;
	}

	@Override
	public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
		ItemStack base = pContainer.getItem(1);
		ItemStack addition = pContainer.getItem(2);

		ItemStack result = base.copy();
		if (!(addition.getItem() instanceof Medallion medallion))
			return ItemStack.EMPTY;

		// store the element type from the medallion
		ElementType type = medallion.getType();
		if (type == null)
			return ItemStack.EMPTY;

		result.getOrCreateTag().putString("medallion", type.getSerializedName());
		return result;

	}

	@Override
	public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
		return ItemStack.EMPTY;
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
	public boolean isTemplateIngredient(ItemStack pStack) {
		return pStack.isEmpty();
	}

	@Override
	public boolean isBaseIngredient(ItemStack pStack) {
		if (!(pStack.getItem() instanceof SwordItem))
			return false;
		return !(pStack.hasTag() && pStack.getTag().contains("medallion"));
	}

	@Override
	public boolean isAdditionIngredient(ItemStack pStack) {
		return pStack.getItem() instanceof Medallion;
	}

	public static class Serializer implements RecipeSerializer<MedallionSmithingRecipe> {
		public static final Serializer INSTANCE = new Serializer();
		public static final ResourceLocation ID = RelicsUtil.modLoc("medallion_smithing");

		private Serializer() {
		}

		@Override
		public MedallionSmithingRecipe fromJson(ResourceLocation id, JsonObject json) {
			return new MedallionSmithingRecipe(id);
		}

		@Override
		public @Nullable MedallionSmithingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			return new MedallionSmithingRecipe(id);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, MedallionSmithingRecipe recipe) {
			// no additional data needed
		}
	}
}
