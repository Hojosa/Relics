package hojosa.relics_of_old.common.player;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import hojosa.relics_of_old.common.ritual.SpiritDefinition;
import hojosa.relics_of_old.common.ritual.SpiritDefinitionLoader;
import hojosa.relics_of_old.common.ritual.SpiritDefinition.BoonEntry;
import hojosa.relics_of_old.common.ritual.SpiritDefinition.RewardType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class SpiritFavor {
	private CompoundTag favorData = new CompoundTag();

	// --- Favor accessors (per spirit, keyed by spirit name) ---

	private CompoundTag getSpiritTag(String spirit) {
		if (!favorData.contains(spirit)) {
			favorData.put(spirit, new CompoundTag());
		}
		return favorData.getCompound(spirit);
	}

	public int getFavor(String spirit) {
		return getSpiritTag(spirit).getInt("favor");
	}

	public void adjustFavor(String spirit, int amount) {
		CompoundTag tag = getSpiritTag(spirit);
		int current = tag.getInt("favor");
		tag.putInt("favor", current + amount);
		// Positive offerings reduce intervention cooldown (LG2: amount * refreshRatio)
		if (amount > 0) {
			SpiritDefinition def = SpiritDefinitionLoader.getSpirit(spirit);
			float ratio = def != null ? def.favorInterventionRefreshRatio : 3.0f;
			int interventionTime = tag.getInt("interventionTime");
			tag.putInt("interventionTime", interventionTime - (int) (amount * ratio));
		}
	}

	// --- Donation cooldown tracking (prevents spamming the same offering) ---

	private int getRecentDonationCharge(String spirit, Player player) {
		CompoundTag tag = getSpiritTag(spirit);
		int donationTime = tag.getInt("lastDonationWorldSeconds");
		int nowTime = (int) (player.level().getGameTime() / 20L);
		int charge = tag.getInt("recentDonationCharge");
		SpiritDefinition def = SpiritDefinitionLoader.getSpirit(spirit);
		int cooldown = def != null ? def.donationCooldown : 300;
		int donationAgo = nowTime - donationTime;
		// Charge decays linearly over cooldown period
		return donationAgo >= cooldown ? 0 : (int) (charge * (1.0 - (double) donationAgo / cooldown));
	}

	// --- Opinion lookup (offering value for a given item) ---

	public int getOpinion(String spirit, ItemStack offering) {
		SpiritDefinition def = SpiritDefinitionLoader.getSpirit(spirit);
		if (def == null)
			return -1;
		int count = offering.getCount();

		// Check item registry name
		ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(offering.getItem());
		if (itemId != null && def.opinions.containsKey(itemId)) {
			return def.opinions.get(itemId) * count;
		}

		// Check block form (for block items offered as blocks on the grid)
		if (offering.getItem() instanceof net.minecraft.world.item.BlockItem bi) {
			ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(bi.getBlock());
			if (blockId != null && def.opinions.containsKey(blockId)) {
				return def.opinions.get(blockId) * count;
			}
		}

		return -1;
	}

	// Net offering value after staleness/donation cooldown
	private int netOfferingValue(String spirit, Player player, ItemStack offering) {
		int charge = getRecentDonationCharge(spirit, player);
		int value = getOpinion(spirit, offering);
		if (value == -1)
			return -1;
		SpiritDefinition def = SpiritDefinitionLoader.getSpirit(spirit);
		float staleness = def != null ? def.stalenessScaling : 0.0f;
		// LG2: if charge covers the full value, scale by staleness; otherwise subtract charge
		if (charge >= value) {
			value = (int) (value * staleness);
		} else {
			value = (int) ((value - charge) + charge * staleness);
		}
		return value;
	}

	// --- Offering handling (add favor from a donated item) ---

	public int handleOffering(String spirit, Player player, ItemStack offering, float multiplier) {
		CompoundTag tag = getSpiritTag(spirit);
		int charge = getRecentDonationCharge(spirit, player);
		int offeringValue = netOfferingValue(spirit, player, offering);
		offeringValue = (int) (offeringValue * multiplier);
		if (offeringValue == -1)
			return -1;

		int current = tag.getInt("favor");
		tag.putInt("recentDonationCharge", charge + offeringValue);
		tag.putInt("lastDonationWorldSeconds", (int) (player.level().getGameTime() / 20L));
		tag.putInt("favor", current + offeringValue);
		return offeringValue;
	}
	// --- Boon request (deterministic, player explicitly offers a matching item) ---

	@Nullable
	public List<ItemStack> handleItemRequest(String spirit, Player player, ItemStack offering, int count) {
		SpiritDefinition def = SpiritDefinitionLoader.getSpirit(spirit);
		if (def == null)
			return null;

		BoonEntry boon = matchRequest(def, offering);
		if (boon == null)
			return null;

		// Enchant boon: apply enchantment to the offered item
		if (boon.rewardType == RewardType.ENCHANT) {
			Enchantment ench = boon.getEnchantment();
			if (ench == null)
				return null;
			if (!ench.canEnchant(offering))
				return null;
			// Check for incompatible enchantments
			var existing = EnchantmentHelper.getEnchantments(offering);
			for (Enchantment e : existing.keySet()) {
				if (!ench.isCompatibleWith(e))
					return null;
			}
			// Deterministic: floor ratio = 1.0
			int cost = count * boon.cost;
			if (!rollFavor(spirit, player, cost, 1.0f, 1.0f))
				return null;
			adjustFavor(spirit, -cost);

			ItemStack result = offering.copy();
			result.enchant(ench, boon.enchantLevel);
			return List.of(result);
		}

		// Item boon: swap offered item for reward item(s)
		ItemStack rewardStack = boon.buildRewardStack(1);
		if (rewardStack == null)
			return null;

		int cost = count * boon.cost;
		if (!rollFavor(spirit, player, cost, 1.0f, 1.0f))
			return null;
		adjustFavor(spirit, -cost);

		// Build reward stacks respecting max stack sizes
		int total = count * rewardStack.getCount();
		int limit = rewardStack.getMaxStackSize();
		List<ItemStack> items = new ArrayList<>();
		while (total > 0) {
			ItemStack prize = rewardStack.copy();
			int c = Math.min(limit, total);
			prize.setCount(c);
			total -= c;
			items.add(prize);
		}
		return items;
	}
	// --- Gratitude (random reward proportional to offering value) ---

	@Nullable
	public ItemStack gratitude(String spirit, Player player, int offeredValue) {
		SpiritDefinition def = SpiritDefinitionLoader.getSpirit(spirit);
		if (def == null)
			return null;

		int budget = offeredValue;
		// Bonus from existing favor
		int bonus = Math.min(budget, getFavor(spirit) / 10);
		budget += bonus;

		int spending = 0;
		ItemStack prize = null;
		for (BoonEntry boon : def.boons) {
			if (!boon.allowGratitude())
				continue;
			if (boon.rewardType != RewardType.ITEM)
				continue;
			int roll = player.getRandom().nextInt(budget + 1);
			int price = boon.cost;
			// Higher roll than the cost = eligible, pick highest-cost eligible boon
			if (roll > price && price > spending) {
				spending = price;
				prize = boon.buildRewardStack(1);
			}
		}
		return prize;
	}

	// --- Favor roll (used for boon requests) ---

	public boolean rollFavor(String spirit, Player player, int target, float cap, float floorRatio) {
		int totalFavor = getFavor(spirit);
		int floor = (int) (totalFavor * floorRatio);
		int budget = (int) (totalFavor * cap);
		if (budget < target)
			return false;
		int roll = player.getRandom().nextInt(totalFavor - floor + 1) + floor;
		return roll >= target;
	}

	// --- Intervention (used by phoenix emblem active ability) ---

	public boolean attemptIntervention(String spirit, Player player, int cost, float scale) {
		SpiritDefinition def = SpiritDefinitionLoader.getSpirit(spirit);
		int cooldown = def != null ? def.interventionCooldown : 900;
		float cap = def != null ? def.interventionCap : 0.25f;

		CompoundTag tag = getSpiritTag(spirit);
		int interventionTime = tag.getInt("interventionTime");
		int nowTime = (int) (player.level().getGameTime() / 20L);
		int interventionAgo = nowTime - interventionTime;

		// Cooldown ratio with slight curve (LG2: ratio * 1.03 - 0.03)
		float ratio = interventionAgo >= cooldown ? 1.0f : (float) interventionAgo / cooldown;
		ratio = ratio * 1.03f - 0.03f;
		if (ratio < 0.0f)
			ratio = 0.0f;

		int favor = getFavor(spirit);
		int spendable = (int) (favor * ratio * scale);
		if (player.getRandom().nextInt(spendable + 1) >= cost && cost <= favor * cap) {
			adjustFavor(spirit, -cost);
			tag.putInt("interventionTime", nowTime);
			return true;
		}
		return false;
	}
	// --- Boon matching (find which boon matches the offered item) ---

	@Nullable
	public BoonEntry matchRequest(SpiritDefinition def, ItemStack offering) {
		ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(offering.getItem());
		for (BoonEntry boon : def.boons) {
			// Match by exact item ID
			if (boon.requestItem != null && boon.requestItem.equals(itemId)) {
				return boon;
			}
			// Match by item tag
			if (boon.requestTag != null) {
				TagKey<net.minecraft.world.item.Item> tag = TagKey.create(Registries.ITEM, boon.requestTag);
				if (offering.is(tag)) {
					return boon;
				}
			}
		}
		return null;
	}

	// --- Mood message (LG2 chat feedback on offering) ---

	public void sendMoodMessage(String spirit, Player player, int offeringValue) {
		String mood = "indifferent to";
		if (offeringValue > 0)
			mood = "appreciative of";
		if (offeringValue >= 100)
			mood = "grateful for";
		if (offeringValue >= 500)
			mood = "enthusiastic about";
		if (offeringValue >= 2000)
			mood = "ecstatic about";

		// Spirit name for display
		String name = spirit.equals("phoenix") ? "the Phoenix" : spirit;
		ChatFormatting color = spirit.equals("phoenix") ? ChatFormatting.GRAY : ChatFormatting.GRAY;

		player.sendSystemMessage(Component.literal("(You sense " + name + " is " + mood + " your offering.)").withStyle(color, ChatFormatting.ITALIC));
	}

	// --- Has request match (for checking before consuming the item) ---

	public boolean hasRequestMatch(String spirit, ItemStack offering) {
		SpiritDefinition def = SpiritDefinitionLoader.getSpirit(spirit);
		if (def == null)
			return false;
		return matchRequest(def, offering) != null;
	}

	// --- NBT persistence ---

	public void saveNBTData(CompoundTag nbt) {
		nbt.put("spiritFavor", favorData.copy());
	}

	public void loadNBTData(CompoundTag nbt) {
		if (nbt.contains("spiritFavor")) {
			favorData = nbt.getCompound("spiritFavor").copy();
		}
	}

	public void copyFrom(SpiritFavor source) {
		this.favorData = source.favorData.copy();
	}
}