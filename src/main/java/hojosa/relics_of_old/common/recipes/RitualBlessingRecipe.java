package hojosa.relics_of_old.common.recipes;

import java.util.List;

import com.google.gson.JsonObject;

import hojosa.relics_of_old.common.block.entity.RitualLocusBlockEntity;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualBlessingRecipe extends RitualRecipeBase {

	private final MobEffect effect;
	private final int amplifier;
	private final int baseDuration;
	private final int bonusPerExtra;

	public RitualBlessingRecipe(ResourceLocation id, List<RitualRecipeComponent> components,
                                 FocusFilter focus, MobEffect effect, int amplifier,
                                 int baseDuration, int bonusPerExtra) {
        super(id, components, focus);
        this.effect = effect;
        this.amplifier = amplifier;
        this.baseDuration = baseDuration;
        this.bonusPerExtra = bonusPerExtra;
    }

	@Override
	public boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
		if (caster == null || caster.level().isClientSide)
			return false;
		if (!matchesRitual(ingredients))
			return false;
		if (focus == null)
			return false;

		// Find matching focus item
		List<ItemEntity> items = location.itemsInRitual();
		ItemEntity focusEntity = null;
		for (ItemEntity ie : items) {
			ItemStack stack = ie.getItem();
			if (ForgeRegistries.ITEMS.getKey(stack.getItem()).equals(focus.target())) {
				focusEntity = ie;
				break;
			}
		}
		if (focusEntity == null)
			return false;

		int count = focusEntity.getItem().getCount();
		int duration = baseDuration + (count - 1) * bonusPerExtra;
		caster.addEffect(new MobEffectInstance(effect, duration, amplifier, true, true));
		focusEntity.discard();
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	public static class Serializer implements RecipeSerializer<RitualBlessingRecipe> {
		public static final Serializer INSTANCE = new Serializer();
		public static final ResourceLocation ID = RelicsUtil.modLoc("ritual_blessing");

		private Serializer() {
		}

		@Override
		public RitualBlessingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			List<RitualRecipeComponent> components = parseComponents(GsonHelper.getAsJsonArray(json, "components"));
			FocusFilter focus = parseFocus(json);
			MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.parse(GsonHelper.getAsString(json, "effect")));
			int amplifier = GsonHelper.getAsInt(json, "amplifier", 0);
			int baseDuration = GsonHelper.getAsInt(json, "base_duration");
			int bonusPerExtra = GsonHelper.getAsInt(json, "bonus_per_extra", 0);
			return new RitualBlessingRecipe(recipeId, components, focus, effect, amplifier, baseDuration, bonusPerExtra);
		}

		@Override
		public RitualBlessingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
			List<RitualRecipeComponent> components = readComponents(buf);
			FocusFilter focus = readFocus(buf);
			MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(buf.readResourceLocation());
			int amplifier = buf.readInt();
			int baseDuration = buf.readInt();
			int bonusPerExtra = buf.readInt();
			return new RitualBlessingRecipe(recipeId, components, focus, effect, amplifier, baseDuration, bonusPerExtra);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, RitualBlessingRecipe recipe) {
			writeComponents(buf, recipe.components);
			writeFocus(buf, recipe.focus);
			buf.writeResourceLocation(ForgeRegistries.MOB_EFFECTS.getKey(recipe.effect));
			buf.writeInt(recipe.amplifier);
			buf.writeInt(recipe.baseDuration);
			buf.writeInt(recipe.bonusPerExtra);
		}
	}
}