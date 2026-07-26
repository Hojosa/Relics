package hojosa.relics_of_old.common.datagen.providers;

import org.jetbrains.annotations.NotNull;

import hojosa.relics_of_old.common.block.MysticShrub;
import hojosa.relics_of_old.common.block.NormalSwordPedestal;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.lib.References;
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
import slimeknights.mantle.registration.object.ItemObject;

public class RelicsBlockStateProvider extends BlockStateProvider{
	
	public static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    public static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");

	public RelicsBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, References.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlockInfused(RelicsBlocks.INFUSED_STARSTONE_BLOCK.get(), RelicsBlocks.STARSTONE_BLOCK.get());
		simpleBlock(RelicsBlocks.STARSTONE_BLOCK.get());
		simpleBlock(RelicsBlocks.SKYBEAM_BLOCK.get(), models().cubeBottomTop(RelicsBlocks.SKYBEAM_BLOCK.getId().getPath(), 
				modLoc("block/" + References.UnlocalizedName.SKYBEAM_BLOCK + "_side"), mcLoc("block/obsidian"), modLoc("block/" + References.UnlocalizedName.SKYBEAM_BLOCK + "_top")));
		mysticShrub();
		simpleBlock(RelicsBlocks.CALTROPS.get(), models().crop(RelicsBlocks.CALTROPS.getId().getPath(), modLoc("block/"+ References.UnlocalizedName.CALTROPS)).renderType("cutout"));
	}
	
    private void simpleBlockInfused(Block block, Block parent) {
        simpleBlock(block, models().cubeAll(ForgeRegistries.BLOCKS.getKey(block).getPath(), blockTexture(parent)));
    }
	
	private void registerPedestal() {
		ItemObject<SwordPedestalBaseBlock> block = RelicsBlocks.SWORD_PEDESTAL_NORMAL;
		MultiPartBlockStateBuilder multiPartBuilder = getMultipartBuilder(block.get());
		
		multiPartBuilder.part()
		.modelFile(models().withExistingParent("example_model_file", modLoc("block/sword_pedestal")))
		.addModel()
		.condition(BlockStateProperties.FACING, Direction.NORTH)
//		.condition(BlockStateProperties.FACING, )
//		.condition(BlockStateProperties.FACING, Direction.SOUTH)
//		.condition(BlockStateProperties.FACING, Direction.WEST)
		.end();
		
		multiPartBuilder.part()
		.modelFile(models().getExistingFile(modLoc("block/pedestal_glow")))
		.addModel()
		
//		.nestedGroup()
		.condition(BlockStateProperties.FACING, Direction.NORTH)
		.useOr()
		.condition(NormalSwordPedestal.REPAIR, true)
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

	      getVariantBuilder(block)
	          .partialState().with(MysticShrub.STATE, MysticShrub.ShrubState.NORMAL)
	              .modelForState().modelFile(normal).addModel()
	          .partialState().with(MysticShrub.STATE, MysticShrub.ShrubState.CHARGED)
	              .modelForState().modelFile(charged).addModel()
	          .partialState().with(MysticShrub.STATE, MysticShrub.ShrubState.STUMP)
	              .modelForState().modelFile(stump).addModel();
	  }
	
	@Override
	public @NotNull String getName() {
		return "Relics BlockState and Models";
	}
}