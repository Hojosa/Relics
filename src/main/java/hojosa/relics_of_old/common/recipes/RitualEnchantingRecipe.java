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

public class RitualEnchantingRecipe extends RitualRecipeBase {

	private final String nbtTag;
    private final boolean nbtValue;
    private final int xpCost;

    public RitualEnchantingRecipe(ResourceLocation id, List<RitualRecipeComponent> components,
                                   String nbtTag, boolean nbtValue, int xpCost) {
        super(id, components, null); // no item focus — target IS the focus
        this.nbtTag = nbtTag;
        this.nbtValue = nbtValue;
        this.xpCost = xpCost;
    }

    @Override
    public boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
        if (caster == null || caster.level().isClientSide) return false;
        if (!matchesRitual(ingredients)) return false;

        // Find single-stackable item above the locus
        List<ItemEntity> items = location.itemsInRitual();
        if (items.size() != 1) return false;
        ItemStack stack = items.get(0).getItem();
        if (stack.getMaxStackSize() != 1) return false;

        // Spend XP levels or deal damage for shortfall
        if (!spendRitualLevels(caster, xpCost)) return false;

        stack.getOrCreateTag().putBoolean(nbtTag, nbtValue);
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<RitualEnchantingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = RelicsUtil.modLoc("ritual_enchanting");
        private Serializer() {}

        @Override
        public RitualEnchantingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            List<RitualRecipeComponent> components = parseComponents(
                GsonHelper.getAsJsonArray(json, "components"));
            String nbtTag = GsonHelper.getAsString(json, "nbt_tag");
            boolean nbtValue = GsonHelper.getAsBoolean(json, "nbt_value");
            int xpCost = GsonHelper.getAsInt(json, "xp_cost");
            return new RitualEnchantingRecipe(recipeId, components, nbtTag, nbtValue, xpCost);
        }

        @Override
        public RitualEnchantingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
            List<RitualRecipeComponent> components = readComponents(buf);
            String nbtTag = buf.readUtf();
            boolean nbtValue = buf.readBoolean();
            int xpCost = buf.readInt();
            return new RitualEnchantingRecipe(recipeId, components, nbtTag, nbtValue, xpCost);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RitualEnchantingRecipe recipe) {
            writeComponents(buf, recipe.components);
            buf.writeUtf(recipe.nbtTag);
            buf.writeBoolean(recipe.nbtValue);
            buf.writeInt(recipe.xpCost);
        }
    }
}