package hojosa.relics_of_old.common.worldgen.structures;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import hojosa.relics_of_old.common.worldgen.RelicsStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class PedestalStructure extends Structure {

	public static final Codec<PedestalStructure> CODEC = RecordCodecBuilder.<PedestalStructure>mapCodec(instance -> instance
			.group(settingsCodec(instance), ResourceLocation.CODEC.fieldOf("template").forGetter(s -> s.template), Codec.INT.fieldOf("width").forGetter(s -> s.width), Codec.INT.fieldOf("depth").forGetter(s -> s.depth))
			.apply(instance, PedestalStructure::new)).codec();

	private final ResourceLocation template;
	private final int width;
	private final int depth;

	public PedestalStructure(StructureSettings settings, ResourceLocation template, int width, int depth) {
		super(settings);
		this.template = template;
		this.width = width;
		this.depth = depth;
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos chunkpos = context.chunkPos();
		int x = chunkpos.getMiddleBlockX();
		int z = chunkpos.getMiddleBlockZ();
		ChunkGenerator gen = context.chunkGenerator();

		// Sample corners for flatness and water check
		int h1 = gen.getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
		int h2 = gen.getFirstOccupiedHeight(x + width - 1, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
		int h3 = gen.getFirstOccupiedHeight(x, z + depth - 1, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
		int h4 = gen.getFirstOccupiedHeight(x + width - 1, z + depth - 1, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());

		int lowest = Math.min(Math.min(h1, h2), Math.min(h3, h4));
		int highest = Math.max(Math.max(h1, h2), Math.max(h3, h4));

		// Reject if below sea level
		if (lowest < gen.getSeaLevel()) {
			return Optional.empty();
		}

		// Reject uneven terrain
		if (highest - lowest > 4) {
			return Optional.empty();
		}

		return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> {
			generatePieces(builder, context);
		});
	}

	private void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
		ChunkPos chunkpos = context.chunkPos();
		BlockPos pos = new BlockPos(chunkpos.getMiddleBlockX(), 0, chunkpos.getMiddleBlockZ());
		builder.addPiece(new PedestalPiece(context.structureTemplateManager(), template, pos));
	}

	@Override
	public StructureType<?> type() {
		return RelicsStructures.PEDESTAL.get();
	}
}