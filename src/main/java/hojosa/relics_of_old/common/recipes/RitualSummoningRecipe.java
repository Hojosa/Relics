package hojosa.relics_of_old.common.recipes;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import hojosa.relics_of_old.common.block.entity.RitualLocusBlockEntity;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualSummoningRecipe extends RitualRecipeBase {

	private final List<WeightedEntity> results;

	public record WeightedEntity(EntityType<?> entityType, int weight) {
	}

	public RitualSummoningRecipe(ResourceLocation id, List<RitualRecipeComponent> components, FocusFilter focus, List<WeightedEntity> results) {
		super(id, components, focus);
		this.results = results;
	}

	@Override
	public boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
		if (caster == null || caster.level().isClientSide)
			return false;
		if (!matchesRitual(ingredients))
			return false;
		if (focus == null)
			return false;

		// Validate and consume focus
		if (!consumeFocus(location))
			return false;

		// Roll weighted entity
		EntityType<?> chosen = rollEntity();
		Level level = location.getLevel();
		Entity mob = chosen.create(level);
		if (mob == null)
			return false;

		BlockPos pos = location.getBlockPos();
		mob.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
		if (mob instanceof Mob living) {
			ForgeEventFactory.onFinalizeSpawn(living, (ServerLevel) level, level.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
		}
		level.addFreshEntity(mob);
		return true;
	}

	// Weighted random selection
	private EntityType<?> rollEntity() {
	      if (results.size() == 1) return results.get(0).entityType;
	      int totalWeight = results.stream().mapToInt(WeightedEntity::weight).sum();
	      int roll = java.util.concurrent.ThreadLocalRandom.current().nextInt(totalWeight);
	      for (WeightedEntity entry : results) {
	          roll -= entry.weight;
	          if (roll < 0) return entry.entityType;
	      }
	      return results.get(0).entityType;
	  }


	// Focus consumption — item, block, or entity depending on focus type
	private boolean consumeFocus(RitualLocusBlockEntity location) {
		switch (focus.type()) {
		case ITEM -> {
			List<ItemEntity> items = location.itemsInRitual();
			for (ItemEntity ie : items) {
				ItemStack stack = ie.getItem();
				if (ForgeRegistries.ITEMS.getKey(stack.getItem()).equals(focus.target())) {
					ie.discard();
					return true;
				}
			}
			return false;
		}
		case BLOCK -> {
			Block focusBlock = location.focusBlock();
			if (ForgeRegistries.BLOCKS.getKey(focusBlock).equals(focus.target())) {
				location.clearFocusBlock();
				return true;
			}
			// Also check dropped block items
			List<ItemEntity> items = location.itemsInRitual();
			for (ItemEntity ie : items) {
				ItemStack stack = ie.getItem();
				if (stack.getItem() instanceof BlockItem bi && ForgeRegistries.BLOCKS.getKey(Block.byItem(bi)).equals(focus.target())) {
					ie.discard();
					return true;
				}
			}
			return false;
		}
		case ENTITY -> {
			// Entity focus not used by summoning, but support it for completeness
			return false;
		}
		}
		return false;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	// --- Serializer ---
	public static class Serializer implements RecipeSerializer<RitualSummoningRecipe> {
		public static final Serializer INSTANCE = new Serializer();
		public static final ResourceLocation ID = RelicsUtil.modLoc("ritual_summoning");

		private Serializer() {
		}

		@Override
		public RitualSummoningRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			List<RitualRecipeComponent> components = parseComponents(GsonHelper.getAsJsonArray(json, "components"));
			FocusFilter focus = parseFocus(json);

			JsonArray resultsArray = GsonHelper.getAsJsonArray(json, "results");
			List<WeightedEntity> results = new ArrayList<>();
			for (int i = 0; i < resultsArray.size(); i++) {
				JsonObject entry = resultsArray.get(i).getAsJsonObject();
				EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.parse(entry.get("entity").getAsString()));
				int weight = entry.has("weight") ? entry.get("weight").getAsInt() : 1;
				results.add(new WeightedEntity(type, weight));
			}
			return new RitualSummoningRecipe(recipeId, components, focus, results);
		}

		@Override
		public RitualSummoningRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
			List<RitualRecipeComponent> components = readComponents(buf);
			FocusFilter focus = readFocus(buf);
			int count = buf.readInt();
			List<WeightedEntity> results = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(buf.readResourceLocation());
				int weight = buf.readInt();
				results.add(new WeightedEntity(type, weight));
			}
			return new RitualSummoningRecipe(recipeId, components, focus, results);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, RitualSummoningRecipe recipe) {
			writeComponents(buf, recipe.components);
			writeFocus(buf, recipe.focus);
			buf.writeInt(recipe.results.size());
			for (WeightedEntity entry : recipe.results) {
				buf.writeResourceLocation(ForgeRegistries.ENTITY_TYPES.getKey(entry.entityType));
				buf.writeInt(entry.weight);
			}
		}
	}
}