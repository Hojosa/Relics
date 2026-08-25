package hojosa.relics_of_old.common.init;

import hojosa.relics_of_old.common.worldgen.BombFlowerFeature;
import hojosa.relics_of_old.common.worldgen.ShrubClusterFeature;
import hojosa.relics_of_old.common.worldgen.StarwellFeature;
import hojosa.relics_of_old.lib.References;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsFeatures {

	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, References.MOD_ID);

	public static final RegistryObject<Feature<NoneFeatureConfiguration>> SHRUB_CLUSTER = FEATURES.register("shrub_cluster", () -> new ShrubClusterFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> BOMB_FLOWER_CLUSTER = FEATURES.register("bomb_flower_cluster", () -> new BombFlowerFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<StarwellFeature> STARWELL = FEATURES.register("starwell", () -> new StarwellFeature(NoneFeatureConfiguration.CODEC));
}