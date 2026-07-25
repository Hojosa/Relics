package hojosa.relics_of_old.common.init;

import hojosa.relics_of_old.common.worldgen.ShrubClusterFeature;
import hojosa.relics_of_old.lib.References;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RelicsFeatures {
	
	public static final DeferredRegister<Feature<?>> FEATURES =
	          DeferredRegister.create(ForgeRegistries.FEATURES, References.MOD_ID);

	      public static final RegistryObject<Feature<NoneFeatureConfiguration>> SHRUB_CLUSTER =
	          FEATURES.register("shrub_cluster", () -> new ShrubClusterFeature(NoneFeatureConfiguration.CODEC));
}