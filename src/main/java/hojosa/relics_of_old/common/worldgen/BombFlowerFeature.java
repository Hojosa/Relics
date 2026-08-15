package hojosa.relics_of_old.common.worldgen;

import java.util.Optional;

import com.mojang.serialization.Codec;

import hojosa.relics_of_old.common.init.RelicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.Heightmap;

public class BombFlowerFeature extends Feature<NoneFeatureConfiguration> {

	public BombFlowerFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos origin = context.origin();
		RandomSource random = context.random();
		BlockState flower = RelicsBlocks.BOMB_FLOWER.get().defaultBlockState();
		int maxFlowers = 2 + random.nextInt(4); // 2-5
		int placed = 0;

		for (int attempt = 0; attempt < 6 && placed < maxFlowers; attempt++) {
			int x = origin.getX() + random.nextInt(16) - 8;
			int z = origin.getZ() + random.nextInt(16) - 8;
			int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
			int y = level.getMinBuildHeight() + random.nextInt(Math.max(1, surfaceY - level.getMinBuildHeight()));

			BlockPos probe = new BlockPos(x, y, z);

			Optional<BlockPos> lavaOpt = BlockPos.findClosestMatch(probe, 8, 8, pos -> level.getBlockState(pos).getFluidState().is(FluidTags.LAVA));

			if (lavaOpt.isEmpty())
				continue;

			// found lava — scan 4x4x4 around it for valid flower spots
			BlockPos lavaPos = lavaOpt.get();
			for (int dx = -2; dx <= 2 && placed < maxFlowers; dx++) {
				for (int dy = -2; dy <= 2 && placed < maxFlowers; dy++) {
					for (int dz = -2; dz <= 2 && placed < maxFlowers; dz++) {
						BlockPos flowerPos = lavaPos.offset(dx, dy, dz);
						if (level.getBlockState(flowerPos).isAir() && flower.canSurvive(level, flowerPos)) {
							level.setBlock(flowerPos, flower, 2);
							placed++;
						}
					}
				}
			}
		}
		return placed > 0;
	}
}