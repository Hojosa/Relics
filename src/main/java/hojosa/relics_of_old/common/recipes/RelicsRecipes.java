package hojosa.relics_of_old.common.recipes;

import java.util.function.Supplier;

import hojosa.relics_of_old.lib.References;
import lombok.AccessLevel;
import lombok.NoArgsConstructor; 
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsRecipes {
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, References.MOD_ID);
	public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, References.MOD_ID);

	// serializers
	public static final RegistryObject<RecipeSerializer<MagicInfusionRecipce>> MAGIC_INFUSION_SERIALIZER = registerSerializer("magic_infusion", () -> MagicInfusionRecipce.Serializer.INSTANCE);
	public static final RegistryObject<RecipeSerializer<MedallionSmithingRecipe>> MEDALLION_SMITHING_SERIALIZER = registerSerializer("medallion_smithing", () -> MedallionSmithingRecipe.Serializer.INSTANCE);
	public static final RegistryObject<RecipeSerializer<RitualSummoningRecipe>> RITUAL_SUMMONING_SERIALIZER = registerSerializer("ritual_summoning", () -> RitualSummoningRecipe.Serializer.INSTANCE);
	public static final RegistryObject<RecipeSerializer<RitualConvertRecipe>> RITUAL_CONVERT_SERIALIZER = registerSerializer("ritual_convert", () -> RitualConvertRecipe.Serializer.INSTANCE);
	public static final RegistryObject<RecipeSerializer<RitualCrucibleRecipe>> RITUAL_CRUCIBLE_SERIALIZER = registerSerializer("ritual_crucible", () -> RitualCrucibleRecipe.Serializer.INSTANCE);
	public static final RegistryObject<RecipeSerializer<RitualBlessingRecipe>> RITUAL_BLESSING_SERIALIZER = registerSerializer("ritual_blessing", () -> RitualBlessingRecipe.Serializer.INSTANCE);
	public static final RegistryObject<RecipeSerializer<RitualEnchantingRecipe>> RITUAL_ENCHANTING_SERIALIZER = registerSerializer("ritual_enchanting", () -> RitualEnchantingRecipe.Serializer.INSTANCE);

	// types
	public static final RegistryObject<RecipeType<RitualRecipeBase>> RITUAL_TYPE = TYPES.register("ritual", () -> RitualRecipeBase.Type.INSTANCE);

	private static <T extends Recipe<Container>> RegistryObject<RecipeSerializer<T>> registerSerializer(String name, Supplier<RecipeSerializer<T>> type) {
		return SERIALIZERS.register(name, type);
	}
}