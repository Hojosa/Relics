package hojosa.relics_of_old.common.recipes;

import java.util.List;

import com.google.gson.JsonObject;

import hojosa.relics_of_old.common.block.entity.RitualLocusBlockEntity;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RitualCrucibleRecipe extends RitualRecipeBase {

	public RitualCrucibleRecipe(ResourceLocation id, List<RitualRecipeComponent> components) {
		super(id, components, null); // no focus
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
		Level level = location.getLevel();

		// Look up vanilla smelting recipe
		var optional = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new net.minecraft.world.SimpleContainer(stack), level);
		if (optional.isEmpty())
			return false;

		ItemStack output = optional.get().getResultItem(level.registryAccess());
		int total = output.getCount() * stack.getCount();

		// Spawn smelted output stacks
		while (total > 0) {
			ItemStack oneOutput = output.copy();
			int count = Math.min(oneOutput.getMaxStackSize(), total);
			oneOutput.setCount(count);
			total -= count;
			ItemEntity outEntity = new ItemEntity(level, location.getBlockPos().getX() + 0.5, location.getBlockPos().getY() + 1.5, location.getBlockPos().getZ() + 0.5, oneOutput);
			outEntity.setDeltaMovement(0, 0, 0);
			level.addFreshEntity(outEntity);
		}
		ei.discard();
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	public static class Serializer implements RecipeSerializer<RitualCrucibleRecipe> {
		public static final Serializer INSTANCE = new Serializer();
		public static final ResourceLocation ID = RelicsUtil.modLoc("ritual_crucible");

		private Serializer() {
		}

		@Override
		public RitualCrucibleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			List<RitualRecipeComponent> components = parseComponents(GsonHelper.getAsJsonArray(json, "components"));
			return new RitualCrucibleRecipe(recipeId, components);
		}

		@Override
		public RitualCrucibleRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
			List<RitualRecipeComponent> components = readComponents(buf);
			return new RitualCrucibleRecipe(recipeId, components);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, RitualCrucibleRecipe recipe) {
			writeComponents(buf, recipe.components);
		}
	}
}