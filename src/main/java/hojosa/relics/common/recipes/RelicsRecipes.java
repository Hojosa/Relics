package hojosa.relics.common.recipes;

import java.util.function.Supplier;

import hojosa.relics.lib.References;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS  = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, References.MOD_ID);
    
    public static final RegistryObject<RecipeSerializer<MagicInfusionRecipce>> MAGIC_INFUSION_SERIALIZER = registerSerializer("magic_infusion", () -> MagicInfusionRecipce.Serializer.INSTANCE);
    
    private static <T extends Recipe<Container>> RegistryObject<RecipeSerializer<T>> registerSerializer(String name, Supplier<RecipeSerializer<T>> type) {
        return SERIALIZERS.register(name, type);
    } 
}
