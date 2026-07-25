package hojosa.relics_of_old.common.worldgen;

import hojosa.relics_of_old.common.init.RelicsFeatures;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class RelicsConfiguredFeatures {
	public static final ResourceKey<ConfiguredFeature<?, ?>> SHRUB_CLUSTER =
	          registerKey("shrub_cluster");

	      public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
	          register(context, SHRUB_CLUSTER, RelicsFeatures.SHRUB_CLUSTER.get(),
	              NoneFeatureConfiguration.INSTANCE);
	      }

	      private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
	          return ResourceKey.create(Registries.CONFIGURED_FEATURE, RelicsUtil.modLoc(name));
	      }

	      private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
	              BootstapContext<ConfiguredFeature<?, ?>> context,
	              ResourceKey<ConfiguredFeature<?, ?>> key,
	              F feature, FC config) {
	          context.register(key, new ConfiguredFeature<>(feature, config));
	      }
}