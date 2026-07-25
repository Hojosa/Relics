package hojosa.relics_of_old.common.worldgen;

import hojosa.relics_of_old.common.init.RelicsTags;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class RelicsBiomeModifiers {
	public static final ResourceKey<BiomeModifier> ADD_SHRUB_CLUSTERS = registerKey("add_shrub_clusters");

	public static void bootstrap(BootstapContext<BiomeModifier> context) {
		var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
		var biomes = context.lookup(Registries.BIOME);

		context.register(ADD_SHRUB_CLUSTERS, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes.getOrThrow(RelicsTags.Biomes.HasFeature.MYSTIC_SHRUB),
				HolderSet.direct(placedFeatures.getOrThrow(RelicsPlacedFeatures.SHRUB_CLUSTER_PLACED)), GenerationStep.Decoration.VEGETAL_DECORATION));
	}

	private static ResourceKey<BiomeModifier> registerKey(String name) {
		return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, RelicsUtil.modLoc(name));
	}
}