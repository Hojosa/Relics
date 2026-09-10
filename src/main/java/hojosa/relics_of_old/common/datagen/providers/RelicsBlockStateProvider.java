package hojosa.relics_of_old.common.datagen.providers;

import org.jetbrains.annotations.NotNull;

import hojosa.relics_of_old.common.block.BombFlower;
import hojosa.relics_of_old.common.block.BoostPlate;
import hojosa.relics_of_old.common.block.MysticShrub;
import hojosa.relics_of_old.common.block.NormalSwordPedestal;
import hojosa.relics_of_old.common.block.StarwellBlock;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.block.RelicsFacingBlock;
import hojosa.relics_of_old.lib.block.SwordPedestalBaseBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.client.model.builder.RetexturedModelBuilder;
import slimeknights.mantle.registration.object.ItemObject;

public class RelicsBlockStateProvider extends BlockStateProvider {

	public static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
	public static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");

	public RelicsBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, References.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlockInfused(RelicsBlocks.INFUSED_STARSTONE_BLOCK.get(), RelicsBlocks.STARSTONE_BLOCK.get());
		simpleBlock(RelicsBlocks.STARSTONE_BLOCK.get());
		simpleBlock(RelicsBlocks.SKYBEAM_BLOCK.get(), models().cubeBottomTop(RelicsBlocks.SKYBEAM_BLOCK.getId().getPath(), modLoc("block/" + References.UnlocalizedName.SKYBEAM_BLOCK + "_side"), mcLoc("block/obsidian"),
				modLoc("block/" + References.UnlocalizedName.SKYBEAM_BLOCK + "_top")));
		mysticShrub();
		simpleBlock(RelicsBlocks.CALTROPS.get(), models().crop(RelicsBlocks.CALTROPS.getId().getPath(), modLoc("block/" + References.UnlocalizedName.CALTROPS)).renderType("cutout"));
		boostPlate();
		clayJar();
		simpleBlock(RelicsBlocks.SUGAR_CUBE.get());
		bombFlower();
		simpleBlock(RelicsBlocks.STARRY_SAND.get());
		simpleBlock(RelicsBlocks.STRUCK_SAND.get(), models().cubeBottomTop(RelicsBlocks.STRUCK_SAND.getId().getPath(), mcLoc("block/sand"), mcLoc("block/sand"), modLoc("block/struck_sand")));
		simpleBlock(RelicsBlocks.STRUCK_DIRT.get(), models().cubeBottomTop(RelicsBlocks.STRUCK_DIRT.getId().getPath(), mcLoc("block/dirt"), mcLoc("block/dirt"), modLoc("block/struck_dirt")));
		simpleBlock(RelicsBlocks.STARWELL_FRAME.get());
		starwellCore();
		simpleBlock(RelicsBlocks.SKY_LENS.get());
		simpleBlock(RelicsBlocks.RITUAL_LOCUS.get());
		phoenixAltar();
	}

	private void simpleBlockInfused(Block block, Block parent) {
		simpleBlock(block, models().cubeAll(ForgeRegistries.BLOCKS.getKey(block).getPath(), blockTexture(parent)));
	}

	private void registerPedestal() {
		ItemObject<SwordPedestalBaseBlock> block = RelicsBlocks.SWORD_PEDESTAL_NORMAL;
		MultiPartBlockStateBuilder multiPartBuilder = getMultipartBuilder(block.get());

		multiPartBuilder.part().modelFile(models().withExistingParent("example_model_file", modLoc("block/sword_pedestal"))).addModel().condition(BlockStateProperties.FACING, Direction.NORTH)
//		.condition(BlockStateProperties.FACING, )
//		.condition(BlockStateProperties.FACING, Direction.SOUTH)
//		.condition(BlockStateProperties.FACING, Direction.WEST)
				.end();

		multiPartBuilder.part().modelFile(models().getExistingFile(modLoc("block/pedestal_glow"))).addModel()

//		.nestedGroup()
				.condition(BlockStateProperties.FACING, Direction.NORTH).useOr().condition(NormalSwordPedestal.REPAIR, true)
//		.nestedGroup()
//		.condition(BlockStateProperties.FACING, Direction.SOUTH)
//		.condition(block.REPAIR, Boolean.TRUE)
//		.endNestedGroup()
//		.endNestedGroup()
				.end();
	}

	private void mysticShrub() {
		Block block = RelicsBlocks.MYSTIC_SHRUB.get();

		ModelFile normal = models().cross("mystic_shrub", modLoc("block/mystic_shrub")).renderType("cutout");
		ModelFile charged = models().cross("mystic_shrub_charged", modLoc("block/mystic_shrub_charged")).renderType("cutout");
		ModelFile stump = models().cross("mystic_shrub_stump", modLoc("block/mystic_shrub_stump")).renderType("cutout");

		getVariantBuilder(block).partialState().with(MysticShrub.STATE, MysticShrub.ShrubState.NORMAL).modelForState().modelFile(normal).addModel().partialState().with(MysticShrub.STATE, MysticShrub.ShrubState.CHARGED)
				.modelForState().modelFile(charged).addModel().partialState().with(MysticShrub.STATE, MysticShrub.ShrubState.STUMP).modelForState().modelFile(stump).addModel();
	}

	private void bombFlower() {
		Block block = RelicsBlocks.BOMB_FLOWER.get();

		ModelFile normal = models().cross("bomb_flower", modLoc("block/bomb_flower")).renderType("cutout");
		ModelFile cut = models().cross("bomb_flower_cut", modLoc("block/bomb_flower_cut")).renderType("cutout");

		getVariantBuilder(block).partialState().with(BombFlower.STATE, BombFlower.FlowerState.NORMAL).modelForState().modelFile(normal).addModel().partialState().with(BombFlower.STATE, BombFlower.FlowerState.CUT)
				.modelForState().modelFile(cut).addModel();
	}

	private void boostPlate() {
		Block block = RelicsBlocks.BOOST_PLATE.get();
		String base = References.UnlocalizedName.BOOST_PLATE;

		ModelFile speed = models().withExistingParent(base, mcLoc("block/pressure_plate_up")).texture("texture", modLoc("block/" + base + "_speed"));
		ModelFile jump = models().withExistingParent(base + "_jump", mcLoc("block/pressure_plate_up")).texture("texture", modLoc("block/" + base + "_jump"));
		ModelFile healing = models().withExistingParent(base + "_heal", mcLoc("block/pressure_plate_up")).texture("texture", modLoc("block/" + base + "_heal"));

		for (BoostPlate.BoostType type : BoostPlate.BoostType.values()) {
			ModelFile model = switch (type) {
			case SPEED -> speed;
			case JUMP -> jump;
			case HEALING -> healing;
			};
			getVariantBuilder(block).partialState().with(BoostPlate.TYPE, type).with(RelicsFacingBlock.FACING, Direction.NORTH).modelForState().modelFile(model).rotationY(0).addModel().partialState()
					.with(BoostPlate.TYPE, type).with(RelicsFacingBlock.FACING, Direction.EAST).modelForState().modelFile(model).rotationY(90).addModel().partialState().with(BoostPlate.TYPE, type)
					.with(RelicsFacingBlock.FACING, Direction.SOUTH).modelForState().modelFile(model).rotationY(180).addModel().partialState().with(BoostPlate.TYPE, type).with(RelicsFacingBlock.FACING, Direction.WEST)
					.modelForState().modelFile(model).rotationY(270).addModel();
		}
	}

	private void clayJar() {
		Block block = RelicsBlocks.CLAY_JAR.get();
		ModelFile model = models().withExistingParent("clay_jar", modLoc("block/clay_jar_base")).renderType("cutout").texture("jar_side", modLoc("block/clay_jar_side")).texture("jar_top", modLoc("block/clay_jar_top"))
				.texture("jar_bottom", modLoc("block/clay_jar_bottom")).texture("jar_side_overlay", modLoc("block/clay_jar_side_overlay")).texture("jar_top_overlay", modLoc("block/clay_jar_top_overlay"))
				.texture("particle", modLoc("block/clay_jar_bottom")).customLoader(RetexturedModelBuilder::new).retexture("jar_side").retexture("jar_top").retexture("jar_bottom").retexture("particle").end();
		simpleBlock(block, model);
	}

	private void starwellCore() {
		Block block = RelicsBlocks.STARWELL_CORE.get();
		String base = References.UnlocalizedName.STARWELL_CORE;

		// side + bottom reuse the frame texture, top switches on active state
		ModelFile inert = models().cubeBottomTop(base, modLoc("block/" + References.UnlocalizedName.STARWELL_FRAME), modLoc("block/" + References.UnlocalizedName.STARWELL_FRAME), mcLoc("block/obsidian"));
		ModelFile active = models().cubeBottomTop(base + "_active", modLoc("block/" + References.UnlocalizedName.STARWELL_FRAME), modLoc("block/" + References.UnlocalizedName.STARWELL_FRAME),
				modLoc("block/" + base + "_top_active"));

		getVariantBuilder(block).partialState().with(StarwellBlock.ACTIVE, false).modelForState().modelFile(inert).addModel().partialState().with(StarwellBlock.ACTIVE, true).modelForState().modelFile(active).addModel();
	}

	private void phoenixAltar() {
	      Block block = RelicsBlocks.PHOENIX_ALTAR.get();
	      ModelFile model = models().getBuilder("phoenix_altar")
	          .parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
	          .texture("side", modLoc("block/phoenix_altar_side"))
	          .texture("top", modLoc("block/phoenix_altar_top"))
	          .texture("particle", modLoc("block/phoenix_altar_side"))
	          .element()
	              .from(0, 0, 0).to(16, 12, 16)
	              .face(Direction.NORTH).texture("#side").uvs(0, 4, 16, 16).cullface(Direction.NORTH).end()
	              .face(Direction.SOUTH).texture("#side").uvs(0, 4, 16, 16).cullface(Direction.SOUTH).end()
	              .face(Direction.EAST).texture("#side").uvs(0, 4, 16, 16).cullface(Direction.EAST).end()
	              .face(Direction.WEST).texture("#side").uvs(0, 4, 16, 16).cullface(Direction.WEST).end()
	              .face(Direction.UP).texture("#top").uvs(0, 0, 16, 16).end()
	              .face(Direction.DOWN).texture("#top").uvs(0, 0, 16, 16).cullface(Direction.DOWN).end()
	          .end();
	      simpleBlock(block, model);
	}

	@Override
	public @NotNull String getName() {
		return "Relics BlockState and Models";
	}
}