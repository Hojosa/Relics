package hojosa.relics_of_old.common.recipes;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import hojosa.relics_of_old.common.block.entity.RitualLocusBlockEntity;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class RitualRecipeBase implements Recipe<Container> {

	protected final ResourceLocation id;
	protected final List<RitualRecipeComponent> components;
	protected final FocusFilter focus; // nullable

	public RitualRecipeBase(ResourceLocation id, List<RitualRecipeComponent> components, FocusFilter focus) {
		this.id = id;
		this.components = components;
		this.focus = focus;
	}

	// Focus type enum
	public enum FocusType {
		ITEM, BLOCK, ENTITY
	}

	// Focus specification parsed from JSON
	public record FocusFilter(FocusType type, ResourceLocation target) {
	}

	// Build a RitualRecipe from the component list for matching
	public RitualRecipe buildRecipeTemplate() {
		RitualRecipe recipe = new RitualRecipe();
		for (RitualRecipeComponent comp : components) {
			recipe.add(comp);
		}
		return recipe;
	}

	// Check if the grid's ingredient pattern matches this recipe
	public boolean matchesRitual(RitualRecipe ingredients) {
		return buildRecipeTemplate().accepts(ingredients);
	}

	// Subclass-specific invocation logic
	public abstract boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster);

	// --- Recipe<Container> boilerplate (not used for matching, but required) ---

	@Override
	public boolean matches(Container pContainer, Level pLevel) {
		return false; // matching done via matchesRitual
	}

	@Override
	public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return false;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}

	// Shared RecipeType
	public static class Type implements RecipeType<RitualRecipeBase> {
		private Type() {
		}

		public static final Type INSTANCE = new Type();

		@Override
		public String toString() {
			return "relics_of_old:ritual";
		}
	}

	// --- Shared parsing helpers for all serializers ---

	public static List<RitualRecipeComponent> parseComponents(JsonArray componentArray) {
		List<RitualRecipeComponent> components = new ArrayList<>();
		for (int i = 0; i < componentArray.size(); i++) {
			JsonObject comp = componentArray.get(i).getAsJsonObject();
			if (comp.has("keystone_block")) {
				Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse(comp.get("keystone_block").getAsString()));
				components.add(new RitualRecipeComponent(block));
			} else {
				Block a = ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse(comp.get("block_a").getAsString()));
				Block b = ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse(comp.get("block_b").getAsString()));
				components.add(new RitualRecipeComponent(a, b));
			}
		}
		return components;
	}

	public static FocusFilter parseFocus(JsonObject json) {
		if (!json.has("focus"))
			return null;
		JsonObject focusObj = json.getAsJsonObject("focus");
		if (focusObj.has("item")) {
			return new FocusFilter(FocusType.ITEM, ResourceLocation.parse(focusObj.get("item").getAsString()));
		} else if (focusObj.has("block")) {
			return new FocusFilter(FocusType.BLOCK, ResourceLocation.parse(focusObj.get("block").getAsString()));
		} else if (focusObj.has("entity")) {
			return new FocusFilter(FocusType.ENTITY, ResourceLocation.parse(focusObj.get("entity").getAsString()));
		}
		return null;
	}

	// Network serialization helpers for components
	public static void writeComponents(FriendlyByteBuf buf, List<RitualRecipeComponent> components) {
		buf.writeInt(components.size());
		for (RitualRecipeComponent comp : components) {
			buf.writeEnum(comp.type);
			for (RitualRecipeElement elem : comp.blocks) {
				buf.writeResourceLocation(ForgeRegistries.BLOCKS.getKey(elem.block));
			}
		}
	}

	public static List<RitualRecipeComponent> readComponents(FriendlyByteBuf buf) {
		int count = buf.readInt();
		List<RitualRecipeComponent> components = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			RitualRecipeComponent.ComponentType type = buf.readEnum(RitualRecipeComponent.ComponentType.class);
			if (type == RitualRecipeComponent.ComponentType.SINGLETON) {
				Block block = ForgeRegistries.BLOCKS.getValue(buf.readResourceLocation());
				components.add(new RitualRecipeComponent(block));
			} else {
				// DUO or MATCH — 2 blocks
				Block a = ForgeRegistries.BLOCKS.getValue(buf.readResourceLocation());
				Block b = ForgeRegistries.BLOCKS.getValue(buf.readResourceLocation());
				components.add(new RitualRecipeComponent(a, b));
			}
		}
		return components;
	}

	public static void writeFocus(FriendlyByteBuf buf, FocusFilter focus) {
		buf.writeBoolean(focus != null);
		if (focus != null) {
			buf.writeEnum(focus.type);
			buf.writeResourceLocation(focus.target);
		}
	}

	public static FocusFilter readFocus(FriendlyByteBuf buf) {
		if (!buf.readBoolean())
			return null;
		FocusType type = buf.readEnum(FocusType.class);
		ResourceLocation target = buf.readResourceLocation();
		return new FocusFilter(type, target);
	}
}