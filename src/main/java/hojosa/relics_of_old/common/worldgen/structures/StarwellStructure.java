package hojosa.relics_of_old.common.worldgen.structures;

import java.util.Optional;

import com.mojang.serialization.Codec;

import hojosa.relics_of_old.common.worldgen.RelicsStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class StarwellStructure extends Structure {

	public static final Codec<StarwellStructure> CODEC = simpleCodec(StarwellStructure::new);

	public StarwellStructure(StructureSettings settings) {
		super(settings);
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos chunkpos = context.chunkPos();
		int x = chunkpos.getMiddleBlockX();
		int z = chunkpos.getMiddleBlockZ();
		ChunkGenerator gen = context.chunkGenerator();

		// Sample all 4 corners of the 3x3 footprint
		int h1 = gen.getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
		int h2 = gen.getFirstOccupiedHeight(x + 2, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
		int h3 = gen.getFirstOccupiedHeight(x, z + 2, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
		int h4 = gen.getFirstOccupiedHeight(x + 2, z + 2, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());

		int lowest = Math.min(Math.min(h1, h2), Math.min(h3, h4));
		int highest = Math.max(Math.max(h1, h2), Math.max(h3, h4));

		// Reject if terrain is below sea level (water)
		if (lowest < gen.getSeaLevel()) {
			return Optional.empty();
		}

		// Reject uneven terrain (slopes, cliff edges)
		if (highest - lowest > 2) {
			return Optional.empty();
		}

		return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> {
			generatePieces(builder, context);
		});
	}

	private void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
		ChunkPos chunkpos = context.chunkPos();
		BlockPos pos = new BlockPos(chunkpos.getMiddleBlockX(), 0, chunkpos.getMiddleBlockZ());
		builder.addPiece(new StarwellPiece(context.structureTemplateManager(), pos));
	}

	@Override
	public StructureType<?> type() {
		return RelicsStructures.STARWELL.get();
	}
}