package hojosa.relics.lib.recipe;

import static slimeknights.mantle.registration.RegistrationHelper.injected;

import hojosa.relics.lib.References;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ObjectHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsRecipeSerializers {

	@ObjectHolder(registryName = "minecraft:recipe_serializer", value = References.MOD_ID
			+ ":crafting_stonecutter_retextured")
	public static final RecipeSerializer<?> STONECUTTER_RETEXTURED = injected();
}
