package hojosa.relics_of_old.common.recipes;

import java.util.List;

import com.google.gson.JsonObject;

import hojosa.relics_of_old.common.block.entity.RitualLocusBlockEntity;
import hojosa.relics_of_old.common.init.RelicsTags;
import hojosa.relics_of_old.common.item.MagicRingItem;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualDismantleRecipe extends RitualRecipeBase {
	public RitualDismantleRecipe(ResourceLocation id, List<RitualRecipeComponent> components) {
		super(id, components, null);
	}

	@Override
	public boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
		if (caster == null || caster.level().isClientSide)
			return false;
		if (!matchesRitual(ingredients))
			return false;

		List<ItemEntity> items = location.itemsInRitual();
		if (items.size() != 1)
			return false;

		ItemEntity ei = items.get(0);
		ItemStack stack = ei.getItem();

		ItemStack part = extractRepairMaterial(stack, location.getLevel());
		if (part == null || part.isEmpty())
			return false;

		// Destroy input, spawn output
		ei.discard();
		ItemEntity output = new ItemEntity(location.getLevel(), location.getBlockPos().getX() + 0.5, location.getBlockPos().getY() + 1.5, location.getBlockPos().getZ() + 0.5, part);
		output.setDeltaMovement(0, 0, 0);
		location.getLevel().addFreshEntity(output);
		return true;
	}

	// Extract the repair ingredient from a tool, armor, sword, or magic ring
	private static ItemStack extractRepairMaterial(ItemStack stack, Level level) {
		// Tiered items (tools, swords) — get repair ingredient from tier
		if (stack.getItem() instanceof TieredItem tiered) {
			Ingredient repairIngredient = tiered.getTier().getRepairIngredient();
			return firstItemFrom(repairIngredient);
		}

		// Armor — get repair ingredient from armor material
		if (stack.getItem() instanceof ArmorItem armor) {
			Ingredient repairIngredient = armor.getMaterial().getRepairIngredient();
			return firstItemFrom(repairIngredient);
		}

		// Magic rings — look up crafting recipe and return the gem ingredient
        if (stack.getItem() instanceof MagicRingItem) {
            ResourceLocation recipeId = ForgeRegistries.ITEMS.getKey(stack.getItem());
            if (recipeId == null) return null;
            return level.getRecipeManager().byKey(recipeId)
                .map(recipe -> {
                    for (Ingredient ing : recipe.getIngredients()) {
                        for (ItemStack match : ing.getItems()) {
                            if (match.is(RelicsTags.Items.GEMS)) {
                                return match.copy();
                            }
                        }
                    }
                    return ItemStack.EMPTY;
                })
                .orElse(ItemStack.EMPTY);
        }

		return null;
	}

	// Get the first concrete item from an Ingredient
	private static ItemStack firstItemFrom(Ingredient ingredient) {
		ItemStack[] items = ingredient.getItems();
		if (items.length > 0) {
			return items[0].copy();
		}
		return null;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	public static class Serializer implements RecipeSerializer<RitualDismantleRecipe> {
		public static final Serializer INSTANCE = new Serializer();
		public static final ResourceLocation ID = RelicsUtil.modLoc("ritual_dismantle");

		private Serializer() {
		}

		@Override
		public RitualDismantleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			List<RitualRecipeComponent> components = parseComponents(GsonHelper.getAsJsonArray(json, "components"));
			return new RitualDismantleRecipe(recipeId, components);
		}

		@Override
		public RitualDismantleRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
			List<RitualRecipeComponent> components = readComponents(buf);
			return new RitualDismantleRecipe(recipeId, components);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, RitualDismantleRecipe recipe) {
			writeComponents(buf, recipe.components);
		}
	}
}