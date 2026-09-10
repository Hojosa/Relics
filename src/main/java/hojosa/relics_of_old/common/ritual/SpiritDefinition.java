package hojosa.relics_of_old.common.ritual;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

//Parsed spirit definition loaded from JSON (opinions + boons + tuning constants)
public class SpiritDefinition {

	public final ResourceLocation id;
	public final Map<ResourceLocation, Integer> opinions;
	public final List<BoonEntry> boons;
	public final float stalenessScaling;
	public final int donationCooldown;
	public final int interventionCooldown;
	public final float favorRollFloorRatio;
	public final float interventionCap;
	public final float favorInterventionRefreshRatio;

	public SpiritDefinition(ResourceLocation id, Map<ResourceLocation, Integer> opinions, List<BoonEntry> boons, float stalenessScaling, int donationCooldown, int interventionCooldown, float favorRollFloorRatio,
			float interventionCap, float favorInterventionRefreshRatio) {
		this.id = id;
		this.opinions = opinions;
		this.boons = boons;
		this.stalenessScaling = stalenessScaling;
		this.donationCooldown = donationCooldown;
		this.interventionCooldown = interventionCooldown;
		this.favorRollFloorRatio = favorRollFloorRatio;
		this.interventionCap = interventionCap;
		this.favorInterventionRefreshRatio = favorInterventionRefreshRatio;
	}

	// Boon reward types
	public enum RewardType {
		ITEM, ENCHANT
	}

	// Single boon entry parsed from JSON
	public static class BoonEntry implements Comparable<BoonEntry> {
		public final int cost;
		public final boolean hidden; // hidden = request-only, no gratitude
		public final RewardType rewardType;

		// Request matching — item ID or tag
		@Nullable
		public final ResourceLocation requestItem;
		@Nullable
		public final ResourceLocation requestTag;

		// Item reward fields
		@Nullable
		public final ResourceLocation rewardItem;
		public final int rewardCount;
		@Nullable
		public final String rewardNbt;

		// Enchant reward fields
		@Nullable
		public final ResourceLocation enchantmentId;
		public final int enchantLevel;

		public BoonEntry(int cost, boolean hidden, RewardType rewardType, @Nullable ResourceLocation requestItem, @Nullable ResourceLocation requestTag, @Nullable ResourceLocation rewardItem, int rewardCount,
				@Nullable String rewardNbt, @Nullable ResourceLocation enchantmentId, int enchantLevel) {
			this.cost = cost;
			this.hidden = hidden;
			this.rewardType = rewardType;
			this.requestItem = requestItem;
			this.requestTag = requestTag;
			this.rewardItem = rewardItem;
			this.rewardCount = rewardCount;
			this.rewardNbt = rewardNbt;
			this.enchantmentId = enchantmentId;
			this.enchantLevel = enchantLevel;
		}

		// Whether gratitude can give this boon randomly
		public boolean allowGratitude() {
			return !hidden;
		}

		// Build the reward ItemStack (for item-type boons)
		@Nullable
		public ItemStack buildRewardStack(int count) {
			if (rewardType != RewardType.ITEM || rewardItem == null)
				return null;
			var item = ForgeRegistries.ITEMS.getValue(rewardItem);
			if (item == null)
				return null;
			ItemStack stack = new ItemStack(item, rewardCount * count);
			if (rewardNbt != null) {
				try {
					stack.setTag(net.minecraft.nbt.TagParser.parseTag(rewardNbt));
				} catch (Exception ignored) {
				}
			}
			return stack;
		}

		// Get the enchantment for enchant-type boons
		@Nullable
		public Enchantment getEnchantment() {
			if (enchantmentId == null)
				return null;
			return ForgeRegistries.ENCHANTMENTS.getValue(enchantmentId);
		}

		@Override
		public int compareTo(BoonEntry other) {
			return this.cost - other.cost;
		}
	}
}