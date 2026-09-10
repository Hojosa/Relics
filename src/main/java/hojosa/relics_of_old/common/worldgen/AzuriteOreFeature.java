package hojosa.relics_of_old.common.worldgen;

import com.mojang.serialization.Codec;

import hojosa.relics_of_old.common.init.RelicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class AzuriteOreFeature extends Feature<NoneFeatureConfiguration> {
	private static final int MIN_ALTITUDE = 100;
	private static final int MAX_ALTITUDE = 110;
	private static final int PATCH_RADIUS = 2;
	private static final int PATCH_HEIGHT = 2;
	private static final float DENSITY = 0.5f;

	public AzuriteOreFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos origin = context.origin();
		RandomSource random = context.random();

		int y = random.nextInt(MAX_ALTITUDE - MIN_ALTITUDE + 1) + MIN_ALTITUDE;
		int chunkX = origin.getX();
		int chunkZ = origin.getZ();
		boolean placed = false;

		// Scan along a random X column for N/S cave walls
		int localX = random.nextInt(16);
		for (int localZ = 0; localZ < 16; localZ++) {
			int x = localX + chunkX;
			int z = localZ + chunkZ;
			BlockState current = level.getBlockState(new BlockPos(x, y, z));
			BlockState south = level.getBlockState(new BlockPos(x, y, z + 1));

			if (isStone(current) && south.isAir()) {
				placed |= generatePatch(level, random, x, y, z, Direction.SOUTH);
			}
			if (current.isAir() && isStone(south)) {
				placed |= generatePatch(level, random, x, y, z + 1, Direction.NORTH);
			}
		}

		// Scan along a random Z column for E/W cave walls
		int localZ = random.nextInt(16);
		for (localX = 0; localX < 16; localX++) {
			int x = localX + chunkX;
			int z = localZ + chunkZ;
			BlockState current = level.getBlockState(new BlockPos(x, y, z));
			BlockState east = level.getBlockState(new BlockPos(x + 1, y, z));

			if (isStone(current) && east.isAir()) {
				placed |= generatePatch(level, random, x, y, z, Direction.EAST);
			}
			if (current.isAir() && isStone(east)) {
				placed |= generatePatch(level, random, x + 1, y, z, Direction.WEST);
			}
		}

		return placed;
	}

	// Generate a flat patch of ore on a cave wall surface
	private boolean generatePatch(WorldGenLevel level, RandomSource random, int coreX, int coreY, int coreZ, Direction airDir) {
		BlockState ore = RelicsBlocks.AZURITE_ORE.get().defaultBlockState();
		boolean placed = false;

		if (airDir == Direction.EAST || airDir == Direction.WEST) {
			for (int dy = -PATCH_HEIGHT; dy <= PATCH_HEIGHT; dy++) {
				for (int dz = -PATCH_RADIUS; dz <= PATCH_RADIUS; dz++) {
					if (random.nextFloat() >= DENSITY)
						continue;
					BlockPos pos = new BlockPos(coreX, coreY + dy, coreZ + dz);
					BlockPos airPos = pos.relative(airDir);
					if (isStone(level.getBlockState(pos)) && level.getBlockState(airPos).isAir()) {
						level.setBlock(pos, ore, 2);
						placed = true;
					}
				}
			}
		} else {
			for (int dy = -PATCH_HEIGHT; dy <= PATCH_HEIGHT; dy++) {
				for (int dx = -PATCH_RADIUS; dx <= PATCH_RADIUS; dx++) {
					if (random.nextFloat() >= DENSITY)
						continue;
					BlockPos pos = new BlockPos(coreX + dx, coreY + dy, coreZ);
					BlockPos airPos = pos.relative(airDir);
					if (isStone(level.getBlockState(pos)) && level.getBlockState(airPos).isAir()) {
						level.setBlock(pos, ore, 2);
						placed = true;
					}
				}
			}
		}
		return placed;
	}
}