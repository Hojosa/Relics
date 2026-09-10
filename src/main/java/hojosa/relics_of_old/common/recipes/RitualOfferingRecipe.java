package hojosa.relics_of_old.common.recipes;

import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import hojosa.relics_of_old.common.block.entity.RitualLocusBlockEntity;
import hojosa.relics_of_old.common.entity.FallingStarEntity;
import hojosa.relics_of_old.common.player.SpiritFavor;
import hojosa.relics_of_old.common.player.SpiritFavorProvider;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

//Ritual offering recipe — consumes an offered item via the spirit favor system.
public class RitualOfferingRecipe extends RitualRecipeBase {

	private final String spirit;

	public RitualOfferingRecipe(ResourceLocation id, List<RitualRecipeComponent> components, @Nullable FocusFilter focus, String spirit) {
		super(id, components, focus);
		this.spirit = spirit;
	}

	@Override
	public boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
		if (caster == null || caster.level().isClientSide)
			return false;
		if (!matchesRitual(ingredients))
			return false;

		Level level = location.getLevel();
		BlockPos pos = location.getBlockPos();
		boolean hasAltar = false;

		// Validate focus block if required
		if (focus != null && focus.type() == FocusType.BLOCK) {
			Block focusBlock = location.focusBlock();
			ResourceLocation focusId = ForgeRegistries.BLOCKS.getKey(focusBlock);
			if (!focus.target().equals(focusId))
				return false;
			hasAltar = true;
		}

		// Determine offering: focus block (non-altar) or dropped item
		ItemStack offering = ItemStack.EMPTY;
		ItemEntity offeredEntity = null;
		boolean offeringIsBlock = false;

		if (!hasAltar) {
			// Basic offering: check for block on top of locus first, then dropped items
			Block blockAbove = location.focusBlock();
			if (blockAbove != Blocks.AIR) {
				offering = new ItemStack(blockAbove);
				offeringIsBlock = true;
			}
		}

		if (offering.isEmpty()) {
			// Check for dropped items above the locus
			List<ItemEntity> items = location.itemsInRitual();
			if (items.isEmpty())
				return false;
			offeredEntity = items.get(0);
			offering = offeredEntity.getItem();
		}

		// Altar boosts favor gain by 1.5x
		float altarBoostFactor = hasAltar ? 1.5f : 1.0f;

		// Process through spirit favor system
		SpiritFavor[] favorHolder = new SpiritFavor[1];
		caster.getCapability(SpiritFavorProvider.SPIRIT_FAVOR).ifPresent(f -> favorHolder[0] = f);
		if (favorHolder[0] == null)
			return false;
		SpiritFavor favor = favorHolder[0];

		boolean isRequest = favor.hasRequestMatch(spirit, offering);
		boolean granted = false;

		// Try boon request first (deterministic exchange)
		List<ItemStack> result = favor.handleItemRequest(spirit, caster, offering, offering.getCount());
		if (result != null) {
			FallingStarEntity star = spawnRewardStar(level, pos);
			for (ItemStack stack : result) {
				star.addItem(stack);
			}
			granted = true;
		}

		// If request was recognized but not granted (insufficient favor), return the item
		if (isRequest && !granted) {
			FallingStarEntity star = spawnRewardStar(level, pos);
			star.addItem(offering.copy());
		}

		// Add favor from the offering
		int offeringValue = favor.handleOffering(spirit, caster, offering, altarBoostFactor);
		if (offeringValue == -1 && !isRequest)
			return false;

		if (!isRequest) {
			// Mood message
			favor.sendMoodMessage(spirit, caster, offeringValue);

			// Gratitude roll (chance for a bonus reward)
			ItemStack thanks = favor.gratitude(spirit, caster, offeringValue);
			if (thanks != null) {
				FallingStarEntity star = spawnRewardStar(level, pos);
				star.addItem(thanks);
			}
		}

		// Consume the offering
		if (offeredEntity != null) {
			offeredEntity.discard();
		}
		if (offeringIsBlock) {
			// Replace the focus block with fire (LG2 behavior for basic offering)
			level.setBlockAndUpdate(pos.above(), Blocks.FIRE.defaultBlockState());
		}

		return true;
	}

	// Spawn a reward star above the ritual location
	private FallingStarEntity spawnRewardStar(Level level, BlockPos pos) {
		FallingStarEntity star = new FallingStarEntity(level, pos.getX(), pos.getZ(), pos.getY());
		level.addFreshEntity(star);
		return star;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	// --- Serializer ---
	public static class Serializer implements RecipeSerializer<RitualOfferingRecipe> {
		public static final Serializer INSTANCE = new Serializer();
		public static final ResourceLocation ID = RelicsUtil.modLoc("ritual_offering");

		private Serializer() {
		}

		@Override
		public RitualOfferingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			List<RitualRecipeComponent> components = parseComponents(GsonHelper.getAsJsonArray(json, "components"));
			FocusFilter focus = parseFocus(json);
			String spirit = GsonHelper.getAsString(json, "spirit");
			return new RitualOfferingRecipe(recipeId, components, focus, spirit);
		}

		@Override
		public RitualOfferingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
			List<RitualRecipeComponent> components = readComponents(buf);
			FocusFilter focus = readFocus(buf);
			String spirit = buf.readUtf();
			return new RitualOfferingRecipe(recipeId, components, focus, spirit);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, RitualOfferingRecipe recipe) {
			writeComponents(buf, recipe.components);
			writeFocus(buf, recipe.focus);
			buf.writeUtf(recipe.spirit);
		}
	}
}