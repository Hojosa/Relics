package hojosa.relics_of_old.common.datagen.providers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.lib.References;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class SpiritDefinitionProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;

	public SpiritDefinitionProvider(PackOutput output) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "spirit");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		List<CompletableFuture<?>> futures = new ArrayList<>();
		futures.add(generatePhoenix(cache));
		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	private CompletableFuture<?> generatePhoenix(CachedOutput cache) {
		JsonObject json = new JsonObject();

		// Tuning constants (LG2 defaults)
		json.addProperty("staleness_scaling", 0.0f);
		json.addProperty("donation_cooldown", 300);
		json.addProperty("intervention_cooldown", 900);
		json.addProperty("favor_roll_floor_ratio", 0.25f);
		json.addProperty("intervention_cap", 0.25f);
		json.addProperty("favor_intervention_refresh_ratio", 3.0f);

		// Opinions — offering values
		JsonObject opinions = new JsonObject();
		addOpinion(opinions, Items.BONE, 3);
		addOpinion(opinions, Items.ROTTEN_FLESH, 3);
		addOpinion(opinions, Items.GOLD_NUGGET, 3);
		addOpinion(opinions, Items.GOLD_INGOT, 30);
		addBlockOpinion(opinions, net.minecraft.world.level.block.Blocks.GOLD_BLOCK, 300);
		addOpinion(opinions, Items.GHAST_TEAR, 100);
		addOpinion(opinions, Items.GRASS, 3);
		addOpinion(opinions, RelicsItems.STAR_DUST.get(), 30);
		addOpinion(opinions, RelicsItems.INFUSED_STAR_DUST.get(), 100);
		addOpinion(opinions, RelicsItems.STAR_STONE.get(), 1000);
		addOpinion(opinions, RelicsItems.INFUSED_STAR_STONE.get(), 1500);
		addOpinion(opinions, RelicsItems.STAR_PIECE.get(), 45);
		addOpinion(opinions, RelicsItems.INFUSED_STAR_PIECE.get(), 150);
		addBlockOpinion(opinions, RelicsBlocks.STARSTONE_BLOCK.get(), 1000);
		addBlockOpinion(opinions, RelicsBlocks.INFUSED_STARSTONE_BLOCK.get(), 1500);
		json.add("opinions", opinions);

		// Boons — rewards
		JsonArray boons = new JsonArray();

		// Feather → phoenix feather (300)
		boons.add(itemBoon(300, Items.FEATHER, RelicsItems.PHOENIX_FEATHER.get(), 1));
		// Gratitude-only: 3x phoenix feather (900)
		boons.add(gratitudeBoon(900, RelicsItems.PHOENIX_FEATHER.get(), 3));
		// Gratitude-only: 5x phoenix feather (1500)
		boons.add(gratitudeBoon(1500, RelicsItems.PHOENIX_FEATHER.get(), 5));
		// Diamond → sunfire diamond (3000)
		boons.add(itemBoon(3000, Items.DIAMOND, RelicsItems.SUNFIRE_DIAMOND.get(), 1));
		// Sword → fire aspect (150, hidden)
		boons.add(enchantBoon(150, "forge:tools/swords", "minecraft:fire_aspect", 1));
		// Bow → flame (150, hidden)
		boons.add(enchantBoonItem(150, Items.BOW, "minecraft:flame", 1));
		// Armor → fire protection (150, hidden)
		boons.add(enchantBoon(150, "relics_of_old:enchantable_armor", "minecraft:fire_protection", 1));
		// Plain ring → phoenix ring (500, hidden)
		boons.add(hiddenItemBoon(500, RelicsItems.PLAIN_RING.get(), RelicsItems.PHOENIX_RING.get(), 1));
		// Water bottle → fire resistance potion (300, hidden)
		JsonObject potionBoon = new JsonObject();
		potionBoon.addProperty("cost", 300);
		potionBoon.addProperty("hidden", true);
		potionBoon.addProperty("request", "minecraft:potion");
		potionBoon.addProperty("request_nbt", "{Potion:\"minecraft:water\"}");
		potionBoon.addProperty("reward", "minecraft:potion");
		potionBoon.addProperty("reward_nbt", "{Potion:\"minecraft:fire_resistance\"}");
		boons.add(potionBoon);
		// Black emblem → phoenix emblem (0, hidden)
		boons.add(hiddenItemBoon(0, RelicsItems.BLACK_EMBLEM.get(), RelicsItems.PHOENIX_EMBLEM.get(), 1));

		json.add("boons", boons);

		Path path = pathProvider.json(ResourceLocation.fromNamespaceAndPath(References.MOD_ID, "phoenix"));
		return DataProvider.saveStable(cache, json, path);
	}

	private static void addOpinion(JsonObject opinions, Item item, int value) {
		opinions.addProperty(ForgeRegistries.ITEMS.getKey(item).toString(), value);
	}

	private static void addBlockOpinion(JsonObject opinions, Block block, int value) {
		opinions.addProperty(ForgeRegistries.BLOCKS.getKey(block).toString(), value);
	}

	private static JsonObject itemBoon(int cost, Item request, Item reward, int count) {
		JsonObject obj = new JsonObject();
		obj.addProperty("cost", cost);
		obj.addProperty("request", ForgeRegistries.ITEMS.getKey(request).toString());
		obj.addProperty("reward", ForgeRegistries.ITEMS.getKey(reward).toString());
		if (count > 1)
			obj.addProperty("reward_count", count);
		return obj;
	}

	private static JsonObject hiddenItemBoon(int cost, Item request, Item reward, int count) {
		JsonObject obj = itemBoon(cost, request, reward, count);
		obj.addProperty("hidden", true);
		return obj;
	}

	private static JsonObject gratitudeBoon(int cost, Item reward, int count) {
		JsonObject obj = new JsonObject();
		obj.addProperty("cost", cost);
		obj.addProperty("reward", ForgeRegistries.ITEMS.getKey(reward).toString());
		if (count > 1)
			obj.addProperty("reward_count", count);
		return obj;
	}

	private static JsonObject enchantBoon(int cost, String requestTag, String enchantment, int level) {
		JsonObject obj = new JsonObject();
		obj.addProperty("cost", cost);
		obj.addProperty("hidden", true);
		obj.addProperty("request_tag", requestTag);
		obj.addProperty("reward_enchantment", enchantment);
		obj.addProperty("enchant_level", level);
		return obj;
	}

	private static JsonObject enchantBoonItem(int cost, Item request, String enchantment, int level) {
		JsonObject obj = new JsonObject();
		obj.addProperty("cost", cost);
		obj.addProperty("hidden", true);
		obj.addProperty("request", ForgeRegistries.ITEMS.getKey(request).toString());
		obj.addProperty("reward_enchantment", enchantment);
		obj.addProperty("enchant_level", level);
		return obj;
	}

	@Override
	public String getName() {
		return "Relics Spirit Definitions";
	}
}