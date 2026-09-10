package hojosa.relics_of_old.common.ritual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import hojosa.relics_of_old.lib.References;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

public class SpiritDefinitionLoader extends SimpleJsonResourceReloadListener {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Map<ResourceLocation, SpiritDefinition> SPIRITS = new HashMap<>();

	public SpiritDefinitionLoader() {
		super(GSON, "spirit");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
		SPIRITS.clear();
		objects.forEach((id, json) -> {
			try {
				SpiritDefinition def = parse(id, json.getAsJsonObject());
				SPIRITS.put(id, def);
			} catch (Exception e) {
				throw new RuntimeException("Failed to parse spirit definition " + id, e);
			}
		});
	}

	@Nullable
	public static SpiritDefinition getSpirit(ResourceLocation id) {
		return SPIRITS.get(id);
	}

	@Nullable
	public static SpiritDefinition getSpirit(String name) {
		return SPIRITS.get(ResourceLocation.fromNamespaceAndPath(References.MOD_ID, name));
	}

	private static SpiritDefinition parse(ResourceLocation id, JsonObject json) {
		float stalenessScaling = GsonHelper.getAsFloat(json, "staleness_scaling", 0.0f);
		int donationCooldown = GsonHelper.getAsInt(json, "donation_cooldown", 300);
		int interventionCooldown = GsonHelper.getAsInt(json, "intervention_cooldown", 900);
		float favorRollFloorRatio = GsonHelper.getAsFloat(json, "favor_roll_floor_ratio", 0.25f);
		float interventionCap = GsonHelper.getAsFloat(json, "intervention_cap", 0.25f);
		float favorInterventionRefreshRatio = GsonHelper.getAsFloat(json, "favor_intervention_refresh_ratio", 3.0f);

		// Parse opinions map
		Map<ResourceLocation, Integer> opinions = new HashMap<>();
		JsonObject opinionsObj = GsonHelper.getAsJsonObject(json, "opinions");
		for (var entry : opinionsObj.entrySet()) {
			opinions.put(ResourceLocation.parse(entry.getKey()), entry.getValue().getAsInt());
		}

		// Parse boons list
		List<SpiritDefinition.BoonEntry> boons = new ArrayList<>();
		JsonArray boonsArray = GsonHelper.getAsJsonArray(json, "boons");
		for (int i = 0; i < boonsArray.size(); i++) {
			boons.add(parseBoon(boonsArray.get(i).getAsJsonObject()));
		}

		return new SpiritDefinition(id, opinions, boons, stalenessScaling, donationCooldown, interventionCooldown, favorRollFloorRatio, interventionCap, favorInterventionRefreshRatio);
	}

	private static SpiritDefinition.BoonEntry parseBoon(JsonObject obj) {
		int cost = GsonHelper.getAsInt(obj, "cost");
		boolean hidden = GsonHelper.getAsBoolean(obj, "hidden", false);

		// Request matching
		ResourceLocation requestItem = obj.has("request") && !obj.get("request").isJsonNull() ? ResourceLocation.parse(obj.get("request").getAsString()) : null;
		ResourceLocation requestTag = obj.has("request_tag") ? ResourceLocation.parse(obj.get("request_tag").getAsString()) : null;

		// Determine reward type
		SpiritDefinition.RewardType type;
		ResourceLocation rewardItem = null;
		int rewardCount = GsonHelper.getAsInt(obj, "reward_count", 1);
		String rewardNbt = obj.has("reward_nbt") ? obj.get("reward_nbt").getAsString() : null;
		ResourceLocation enchantmentId = null;
		int enchantLevel = 1;

		if (obj.has("reward_enchantment")) {
			type = SpiritDefinition.RewardType.ENCHANT;
			enchantmentId = ResourceLocation.parse(obj.get("reward_enchantment").getAsString());
			enchantLevel = GsonHelper.getAsInt(obj, "enchant_level", 1);
		} else {
			type = SpiritDefinition.RewardType.ITEM;
			if (obj.has("reward")) {
				rewardItem = ResourceLocation.parse(obj.get("reward").getAsString());
			}
		}

		return new SpiritDefinition.BoonEntry(cost, hidden, type, requestItem, requestTag, rewardItem, rewardCount, rewardNbt, enchantmentId, enchantLevel);
	}
}