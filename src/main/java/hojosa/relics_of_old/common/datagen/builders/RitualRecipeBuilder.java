package hojosa.relics_of_old.common.datagen.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import hojosa.relics_of_old.common.recipes.RitualBlessingRecipe;
import hojosa.relics_of_old.common.recipes.RitualConvertRecipe;
import hojosa.relics_of_old.common.recipes.RitualCrucibleRecipe;
import hojosa.relics_of_old.common.recipes.RitualEnchantingRecipe;
import hojosa.relics_of_old.common.recipes.RitualSummoningRecipe;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualRecipeBuilder {

	// --- Component entry (keystone or pair) ---
	private record ComponentEntry(Block blockA, @Nullable Block blockB) {
		// blockB == null means keystone
		JsonObject toJson() {
			JsonObject obj = new JsonObject();
			if (blockB == null) {
				obj.addProperty("keystone_block", ForgeRegistries.BLOCKS.getKey(blockA).toString());
			} else {
				obj.addProperty("block_a", ForgeRegistries.BLOCKS.getKey(blockA).toString());
				obj.addProperty("block_b", ForgeRegistries.BLOCKS.getKey(blockB).toString());
			}
			return obj;
		}
	}

	// --- Weighted entity for summoning ---
	private record WeightedEntityEntry(EntityType<?> entityType, int weight) {
		JsonObject toJson() {
			JsonObject obj = new JsonObject();
			obj.addProperty("entity", ForgeRegistries.ENTITY_TYPES.getKey(entityType).toString());
			if (weight != 1) {
				obj.addProperty("weight", weight);
			}
			return obj;
		}
	}

	// ============ SUMMONING ============

	public static SummoningBuilder summoning() {
		return new SummoningBuilder();
	}

	public static class SummoningBuilder {
		private final List<ComponentEntry> components = new ArrayList<>();
		private String focusType; // "item" or "block"
		private ResourceLocation focusTarget;
		private final List<WeightedEntityEntry> results = new ArrayList<>();

		public SummoningBuilder pair(Block a, Block b) {
			components.add(new ComponentEntry(a, b));
			return this;
		}

		public SummoningBuilder keystone(Block block) {
			components.add(new ComponentEntry(block, null));
			return this;
		}

		public SummoningBuilder focusItem(Item item) {
			this.focusType = "item";
			this.focusTarget = ForgeRegistries.ITEMS.getKey(item);
			return this;
		}

		public SummoningBuilder focusBlock(Block block) {
			this.focusType = "block";
			this.focusTarget = ForgeRegistries.BLOCKS.getKey(block);
			return this;
		}

		public SummoningBuilder result(EntityType<?> entity) {
			results.add(new WeightedEntityEntry(entity, 1));
			return this;
		}

		public SummoningBuilder result(EntityType<?> entity, int weight) {
			results.add(new WeightedEntityEntry(entity, weight));
			return this;
		}

		public void save(Consumer<FinishedRecipe> consumer, String name) {
			consumer.accept(new FinishedRecipe() {
				@Override
				public void serializeRecipeData(JsonObject json) {
					// Components
					JsonArray comps = new JsonArray();
					for (ComponentEntry c : components)
						comps.add(c.toJson());
					json.add("components", comps);
					// Focus
					if (focusType != null) {
						JsonObject focus = new JsonObject();
						focus.addProperty(focusType, focusTarget.toString());
						json.add("focus", focus);
					}
					// Results
					JsonArray res = new JsonArray();
					for (WeightedEntityEntry e : results)
						res.add(e.toJson());
					json.add("results", res);
				}

				@Override
				public ResourceLocation getId() {
					return RelicsUtil.modLoc("ritual/" + name);
				}

				@Override
				public RecipeSerializer<?> getType() {
					return RitualSummoningRecipe.Serializer.INSTANCE;
				}

				@Override
				public @Nullable JsonObject serializeAdvancement() {
					return null; // no advancement
				}

				@Override
				public @Nullable ResourceLocation getAdvancementId() {
					return null;
				}
			});
		}
	}

	// ============ CONVERT ============

	public static ConvertBuilder convert() {
		return new ConvertBuilder();
	}

	public static class ConvertBuilder {
		private final List<ComponentEntry> components = new ArrayList<>();
		private EntityType<?> sourceEntity;
		private EntityType<?> resultEntity;

		public ConvertBuilder pair(Block a, Block b) {
			components.add(new ComponentEntry(a, b));
			return this;
		}

		public ConvertBuilder keystone(Block block) {
			components.add(new ComponentEntry(block, null));
			return this;
		}

		public ConvertBuilder source(EntityType<?> entity) {
			this.sourceEntity = entity;
			return this;
		}

		public ConvertBuilder result(EntityType<?> entity) {
			this.resultEntity = entity;
			return this;
		}

		public void save(Consumer<FinishedRecipe> consumer, String name) {
			consumer.accept(new FinishedRecipe() {
				@Override
				public void serializeRecipeData(JsonObject json) {
					JsonArray comps = new JsonArray();
					for (ComponentEntry c : components)
						comps.add(c.toJson());
					json.add("components", comps);

					JsonObject focus = new JsonObject();
					focus.addProperty("entity", ForgeRegistries.ENTITY_TYPES.getKey(sourceEntity).toString());
					json.add("focus", focus);

					json.addProperty("result", ForgeRegistries.ENTITY_TYPES.getKey(resultEntity).toString());
				}

				@Override
				public ResourceLocation getId() {
					return RelicsUtil.modLoc("ritual/" + name);
				}

				@Override
				public RecipeSerializer<?> getType() {
					return RitualConvertRecipe.Serializer.INSTANCE;
				}

				@Override
				public @Nullable JsonObject serializeAdvancement() {
					return null;
				}

				@Override
				public @Nullable ResourceLocation getAdvancementId() {
					return null;
				}
			});
		}
	}

	// ============ CRUCIBLE ============

	public static CrucibleBuilder crucible() {
		return new CrucibleBuilder();
	}

	public static class CrucibleBuilder {
		private final List<ComponentEntry> components = new ArrayList<>();

		public CrucibleBuilder pair(Block a, Block b) {
			components.add(new ComponentEntry(a, b));
			return this;
		}

		public CrucibleBuilder keystone(Block block) {
			components.add(new ComponentEntry(block, null));
			return this;
		}

		public void save(Consumer<FinishedRecipe> consumer, String name) {
			consumer.accept(new FinishedRecipe() {
				@Override
				public void serializeRecipeData(JsonObject json) {
					JsonArray comps = new JsonArray();
					for (ComponentEntry c : components)
						comps.add(c.toJson());
					json.add("components", comps);
				}

				@Override
				public ResourceLocation getId() {
					return RelicsUtil.modLoc("ritual/" + name);
				}

				@Override
				public RecipeSerializer<?> getType() {
					return RitualCrucibleRecipe.Serializer.INSTANCE;
				}

				@Override
				public @Nullable JsonObject serializeAdvancement() {
					return null;
				}

				@Override
				public @Nullable ResourceLocation getAdvancementId() {
					return null;
				}
			});
		}
	}

	// ============ BLESSING ============

	public static BlessingBuilder blessing() {
		return new BlessingBuilder();
	}

	public static class BlessingBuilder {
		private final List<ComponentEntry> components = new ArrayList<>();
		private Item focusItem;
		private MobEffect effect;
		private int amplifier;
		private int baseDuration;
		private int bonusPerExtra;

		public BlessingBuilder pair(Block a, Block b) {
			components.add(new ComponentEntry(a, b));
			return this;
		}

		public BlessingBuilder keystone(Block block) {
			components.add(new ComponentEntry(block, null));
			return this;
		}

		public BlessingBuilder focusItem(Item item) {
			this.focusItem = item;
			return this;
		}

		public BlessingBuilder effect(MobEffect effect, int amplifier) {
			this.effect = effect;
			this.amplifier = amplifier;
			return this;
		}

		public BlessingBuilder duration(int baseDuration, int bonusPerExtra) {
			this.baseDuration = baseDuration;
			this.bonusPerExtra = bonusPerExtra;
			return this;
		}

		public void save(Consumer<FinishedRecipe> consumer, String name) {
			consumer.accept(new FinishedRecipe() {
				@Override
				public void serializeRecipeData(JsonObject json) {
					JsonArray comps = new JsonArray();
					for (ComponentEntry c : components)
						comps.add(c.toJson());
					json.add("components", comps);

					JsonObject focus = new JsonObject();
					focus.addProperty("item", ForgeRegistries.ITEMS.getKey(focusItem).toString());
					json.add("focus", focus);

					json.addProperty("effect", ForgeRegistries.MOB_EFFECTS.getKey(effect).toString());
					json.addProperty("amplifier", amplifier);
					json.addProperty("base_duration", baseDuration);
					json.addProperty("bonus_per_extra", bonusPerExtra);
				}

				@Override
				public ResourceLocation getId() {
					return RelicsUtil.modLoc("ritual/" + name);
				}

				@Override
				public RecipeSerializer<?> getType() {
					return RitualBlessingRecipe.Serializer.INSTANCE;
				}

				@Override
				public @Nullable JsonObject serializeAdvancement() {
					return null;
				}

				@Override
				public @Nullable ResourceLocation getAdvancementId() {
					return null;
				}
			});
		}
	}

	// ============ ENCHANTING ============

	public static EnchantingBuilder enchanting() {
		return new EnchantingBuilder();
	}

	public static class EnchantingBuilder {
		private final List<ComponentEntry> components = new ArrayList<>();
		private String nbtTag;
		private boolean nbtValue;
		private int xpCost;

		public EnchantingBuilder pair(Block a, Block b) {
			components.add(new ComponentEntry(a, b));
			return this;
		}

		public EnchantingBuilder keystone(Block block) {
			components.add(new ComponentEntry(block, null));
			return this;
		}

		public EnchantingBuilder nbt(String tag, boolean value) {
			this.nbtTag = tag;
			this.nbtValue = value;
			return this;
		}

		public EnchantingBuilder xpCost(int cost) {
			this.xpCost = cost;
			return this;
		}

		public void save(Consumer<FinishedRecipe> consumer, String name) {
			consumer.accept(new FinishedRecipe() {
				@Override
				public void serializeRecipeData(JsonObject json) {
					JsonArray comps = new JsonArray();
					for (ComponentEntry c : components)
						comps.add(c.toJson());
					json.add("components", comps);

					json.addProperty("nbt_tag", nbtTag);
					json.addProperty("nbt_value", nbtValue);
					json.addProperty("xp_cost", xpCost);
				}

				@Override
				public ResourceLocation getId() {
					return RelicsUtil.modLoc("ritual/" + name);
				}

				@Override
				public RecipeSerializer<?> getType() {
					return RitualEnchantingRecipe.Serializer.INSTANCE;
				}

				@Override
				public @Nullable JsonObject serializeAdvancement() {
					return null;
				}

				@Override
				public @Nullable ResourceLocation getAdvancementId() {
					return null;
				}
			});
		}
	}
}