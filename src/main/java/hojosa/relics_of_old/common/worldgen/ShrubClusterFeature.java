package hojosa.relics_of_old.common.worldgen;

import com.mojang.serialization.Codec;

import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ShrubClusterFeature extends Feature<NoneFeatureConfiguration> {
	
	public ShrubClusterFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    	WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        if (random.nextDouble() < 0.3) {
            RelicsUtil.buildShrubStar(level, origin, false);
        } else {
        	RelicsUtil.buildClump(level, origin, false);
        }
        return true;
    }
}