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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualEnchantingRecipe extends RitualRecipeBase {

	private final ResourceLocation enchantmentId;
	private final int enchantLevel;
	private final int xpCost;

	public RitualEnchantingRecipe(ResourceLocation id, List<RitualRecipeComponent> components, ResourceLocation enchantmentId, int enchantLevel, int xpCost) {
		super(id, components, null);
		this.enchantmentId = enchantmentId;
		this.enchantLevel = enchantLevel;
		this.xpCost = xpCost;
	}

	@Override
	public boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
		if (caster == null || caster.level().isClientSide)
			return false;
		if (!matchesRitual(ingredients))
			return false;

		// Find single-stackable item above the locus
		List<ItemEntity> items = location.itemsInRitual();
		if (items.size() != 1)
			return false;
		ItemStack stack = items.get(0).getItem();
		if (stack.getMaxStackSize() != 1)
			return false;

		// Resolve enchantment from registry
		Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(enchantmentId);
		if (enchantment == null)
			return false;

		// Spend XP levels or deal damage for shortfall
		if (!spendRitualLevels(caster, xpCost))
			return false;

		stack.enchant(enchantment, enchantLevel);
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	public static class Serializer implements RecipeSerializer<RitualEnchantingRecipe> {
		public static final Serializer INSTANCE = new Serializer();
		public static final ResourceLocation ID = RelicsUtil.modLoc("ritual_enchanting");

		private Serializer() {
		}

		@Override
		public RitualEnchantingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			List<RitualRecipeComponent> components = parseComponents(GsonHelper.getAsJsonArray(json, "components"));
			ResourceLocation enchantmentId = ResourceLocation.tryParse(GsonHelper.getAsString(json, "enchantment"));
			int enchantLevel = GsonHelper.getAsInt(json, "enchant_level");
			int xpCost = GsonHelper.getAsInt(json, "xp_cost");
			return new RitualEnchantingRecipe(recipeId, components, enchantmentId, enchantLevel, xpCost);
		}

		@Override
		public RitualEnchantingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
			List<RitualRecipeComponent> components = readComponents(buf);
			ResourceLocation enchantmentId = buf.readResourceLocation();
			int enchantLevel = buf.readInt();
			int xpCost = buf.readInt();
			return new RitualEnchantingRecipe(recipeId, components, enchantmentId, enchantLevel, xpCost);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, RitualEnchantingRecipe recipe) {
			writeComponents(buf, recipe.components);
			buf.writeResourceLocation(recipe.enchantmentId);
			buf.writeInt(recipe.enchantLevel);
			buf.writeInt(recipe.xpCost);
		}
	}
}