package hojosa.relics_of_old.common.worldgen;

import com.mojang.serialization.Codec;

import hojosa.relics_of_old.common.block.StarwellBlock;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class StarwellFeature extends Feature<NoneFeatureConfiguration> {
	public StarwellFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel level = ctx.level();
		BlockPos origin = ctx.origin();

		// Find surface at this xz
		int x = origin.getX();
		int z = origin.getZ();
		int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);

		if (!suitableSurface(level, x, surfaceY, z))
			return false;

		buildWell(level, x, surfaceY, z);
		return true;
	}

	private boolean isSuitableGround(BlockState state) {
		return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.SAND) || state.is(Blocks.GRAVEL) || state.is(Blocks.MYCELIUM) || state.is(Blocks.STONE);
	}

	// Checks a 3x3 area: must be flat surface, replaceable top, suitable ground
	// below
	private boolean suitableSurface(WorldGenLevel level, int x, int y, int z) {
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x + dx, z + dz);
				if (surfaceY != y)
					return false;

				BlockState top = level.getBlockState(new BlockPos(x + dx, y, z + dz));
				if (!top.canBeReplaced())
					return false;

				BlockState ground = level.getBlockState(new BlockPos(x + dx, y - 1, z + dz));
				if (!isSuitableGround(ground))
					return false;
			}
		}
		return true;
	}

	// Builds the starwell structure
	private void buildWell(WorldGenLevel level, int x, int y, int z) {
		BlockState frame = RelicsBlocks.STARWELL_FRAME.get().defaultBlockState();
		BlockState activeCore = RelicsBlocks.STARWELL_CORE.get().defaultBlockState().setValue(StarwellBlock.ACTIVE, true);

		// Top ring (y-1): 8 frame blocks in 3x3 minus center
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (dx == 0 && dz == 0) {
					// Center of ring is air (the well opening)
					level.setBlock(new BlockPos(x, y - 1, z), Blocks.AIR.defaultBlockState(), 2);
				} else {
					level.setBlock(new BlockPos(x + dx, y - 1, z + dz), frame, 2);
				}
				// Clear surface blocks
				level.setBlock(new BlockPos(x + dx, y, z + dz), Blocks.AIR.defaultBlockState(), 2);
			}
		}

		// Bottom plus (y-2): 4 cardinal frame + core center
		level.setBlock(new BlockPos(x - 1, y - 2, z), frame, 2);
		level.setBlock(new BlockPos(x + 1, y - 2, z), frame, 2);
		level.setBlock(new BlockPos(x, y - 2, z - 1), frame, 2);
		level.setBlock(new BlockPos(x, y - 2, z + 1), frame, 2);
		level.setBlock(new BlockPos(x, y - 2, z), activeCore, 2);
	}
}