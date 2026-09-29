package hojosa.relics_of_old.common.worldgen;

import java.util.List;
import java.util.Map;

import hojosa.relics_of_old.common.init.RelicsTags;
import hojosa.relics_of_old.common.worldgen.structures.PedestalPiece;
import hojosa.relics_of_old.common.worldgen.structures.PedestalStructure;
import hojosa.relics_of_old.common.worldgen.structures.StarwellPiece;
import hojosa.relics_of_old.common.worldgen.structures.StarwellStructure;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RelicsStructures {
	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, References.MOD_ID);
	public static final DeferredRegister<StructurePieceType> PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, References.MOD_ID);

	public static final RegistryObject<StructurePieceType> STARWELL_PIECE = PIECE_TYPES.register("starwell", () -> StarwellPiece::new);
	public static final RegistryObject<StructurePieceType> PEDESTAL_PIECE = PIECE_TYPES.register("pedestal", () -> PedestalPiece::new);

	public static final RegistryObject<StructureType<StarwellStructure>> STARWELL = STRUCTURE_TYPES.register("starwell", () -> () -> StarwellStructure.CODEC);
	public static final RegistryObject<StructureType<PedestalStructure>> PEDESTAL = STRUCTURE_TYPES.register("pedestal", () -> () -> PedestalStructure.CODEC);

	public static void bootstrapStructures(BootstapContext<Structure> context) {
		context.register(ResourceKey.create(Registries.STRUCTURE, RelicsUtil.modLoc("starwell")), new StarwellStructure(
				new Structure.StructureSettings(context.lookup(Registries.BIOME).getOrThrow(RelicsTags.Biomes.HasStructure.STARWELL), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));

		context.register(ResourceKey.create(Registries.STRUCTURE, RelicsUtil.modLoc("pedestal_of_time")),
				new PedestalStructure(new Structure.StructureSettings(context.lookup(Registries.BIOME).getOrThrow(RelicsTags.Biomes.HasStructure.PEDESTAL_TIME), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES,
						TerrainAdjustment.BEARD_THIN), RelicsUtil.modLoc("pedestal_of_time"), 19, 19));

		context.register(ResourceKey.create(Registries.STRUCTURE, RelicsUtil.modLoc("pedestal_of_twilight")),
				new PedestalStructure(new Structure.StructureSettings(context.lookup(Registries.BIOME).getOrThrow(RelicsTags.Biomes.HasStructure.PEDESTAL_TWILIGHT), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES,
						TerrainAdjustment.BEARD_THIN), RelicsUtil.modLoc("pedestal_of_twilight"), 19, 19));
	}

	public static void bootstrapStructureSets(BootstapContext<StructureSet> context) {
		context.register(ResourceKey.create(Registries.STRUCTURE_SET, RelicsUtil.modLoc("starwells")), new StructureSet(
				context.lookup(Registries.STRUCTURE).getOrThrow(ResourceKey.create(Registries.STRUCTURE, RelicsUtil.modLoc("starwell"))), new RandomSpreadStructurePlacement(40, 30, RandomSpreadType.LINEAR, 1847592063)));
		context.register(ResourceKey.create(Registries.STRUCTURE_SET, RelicsUtil.modLoc("pedestals")),
				new StructureSet(
						List.of(new StructureSet.StructureSelectionEntry(context.lookup(Registries.STRUCTURE).getOrThrow(ResourceKey.create(Registries.STRUCTURE, RelicsUtil.modLoc("pedestal_of_time"))), 1),
								new StructureSet.StructureSelectionEntry(context.lookup(Registries.STRUCTURE).getOrThrow(ResourceKey.create(Registries.STRUCTURE, RelicsUtil.modLoc("pedestal_of_twilight"))), 1)),
						new RandomSpreadStructurePlacement(256, 200, RandomSpreadType.LINEAR, 1694767090)));
	}
}