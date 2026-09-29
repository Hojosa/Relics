package hojosa.relics_of_old.common.worldgen.structures;

import hojosa.relics_of_old.common.worldgen.RelicsStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class PedestalPiece extends TemplateStructurePiece {

	public PedestalPiece(StructureTemplateManager manager, ResourceLocation template, BlockPos pos) {
		super(RelicsStructures.PEDESTAL_PIECE.get(), 0, manager, template, template.toString(), makeSettings(), pos);
	}

	// Deserialization constructor
	public PedestalPiece(StructurePieceSerializationContext context, CompoundTag tag) {
		super(RelicsStructures.PEDESTAL_PIECE.get(), tag, context.structureTemplateManager(), loc -> makeSettings());
	}

	private static StructurePlaceSettings makeSettings() {
		return new StructurePlaceSettings().addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
		// Place at surface level
		int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, this.templatePosition.getX(), this.templatePosition.getZ());
		this.templatePosition = this.templatePosition.offset(0, surfaceY - this.templatePosition.getY(), 0);
		this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
		super.postProcess(level, structureManager, generator, random, box, chunkPos, pos);
	}

	@Override
	protected void handleDataMarker(String marker, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {
	}
}