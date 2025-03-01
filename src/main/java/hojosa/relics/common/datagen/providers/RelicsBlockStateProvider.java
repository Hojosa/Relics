package hojosa.relics.common.datagen.providers;

import org.jetbrains.annotations.NotNull;

import hojosa.relics.common.block.NormalSwordPedestal;
import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.lib.References;
import hojosa.relics.lib.block.SwordPedestalBaseBlock;
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
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(RelicsBlocks.LAPIS_BRICK.get());
		simpleBlockInfused(RelicsBlocks.INFUSED_STARSTONE_BLOCK.get(), RelicsBlocks.STARSTONE_BLOCK.get());
		simpleBlock(RelicsBlocks.STARSTONE_BLOCK.get());
		simpleBlock(RelicsBlocks.SKYBEAM_BLOCK.get());

//		registerPedestal(RelicsBlocks.SWORD_PEDESTAL_BASIC.get());
//		registerPedestal();
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
	
	@Override
	public @NotNull String getName() {
		return "Relics BlockState and Models";
	}
}
