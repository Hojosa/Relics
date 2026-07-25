package hojosa.relics_of_old.common.datagen.providers;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import hojosa.relics_of_old.common.worldgen.RelicsBiomeModifiers;
import hojosa.relics_of_old.common.worldgen.RelicsConfiguredFeatures;
import hojosa.relics_of_old.common.worldgen.RelicsPlacedFeatures;
import hojosa.relics_of_old.lib.References;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class RelicsWorldGenProvider extends DatapackBuiltinEntriesProvider {

	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.CONFIGURED_FEATURE, RelicsConfiguredFeatures::bootstrap).add(Registries.PLACED_FEATURE, RelicsPlacedFeatures::bootstrap)
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, RelicsBiomeModifiers::bootstrap);

	public RelicsWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BUILDER, Set.of(References.MOD_ID));
	}

	@Override
	public String getName() {
		return "Relics WorldGen";
	}
}