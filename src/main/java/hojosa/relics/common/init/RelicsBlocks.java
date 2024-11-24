package hojosa.relics.common.init;

import java.util.function.Supplier;

import hojosa.relics.lib.References;
import hojosa.relics.lib.block.RelicsNormalBlock;
import hojosa.relics.lib.block.SwordPedestalBaseBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RelicsBlocks{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MODID);

	public static final RegistryObject<Block>LAPIS_BRICK = registerBlock(
			References.UnlocalizedName.LAPIS_BRICK, () -> new RelicsNormalBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS))
	);
	
	public static final RegistryObject<Block>SWORD_PEDESTAL = registerBlock(
			References.UnlocalizedName.SWORD_PEDESTAL, () -> new SwordPedestalBaseBlock(Blocks.STONECUTTER, References.PedestalShapes.BASIC_SHAPE, References.PedestalShapes.BASIC_SWORD_SHAPE)
	);
	
	public static final RegistryObject<Block>SWORD_PEDESTAL_TIME = registerBlock(
			References.UnlocalizedName.SWORD_PEDESTAL_TIME, () -> new SwordPedestalBaseBlock(Blocks.STONECUTTER, References.PedestalShapes.TIME_SHAPE, References.PedestalShapes.TIME_SWORD_SHAPE)
	);
	
	public static final RegistryObject<Block>SWORD_PEDESTAL_TWILIGHT = registerBlock(
			References.UnlocalizedName.SWORD_PEDESTAL_TWILIGHT, () -> new SwordPedestalBaseBlock(Blocks.STONECUTTER, References.PedestalShapes.TWILIGHT_SHAPE, References.PedestalShapes.TWILIGHT_SWORD_SHAPE)
	);

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block) {
        return registerBlock(name, block, false);
    }

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block, boolean excludeBlockItemRegistry) {
        RegistryObject<Block> registryObject = BLOCKS.register(name, block);
        if (!excludeBlockItemRegistry) {
            registerBlockItem(name, registryObject);
        }
        return registryObject;
    }

    private static void registerBlockItem(String name, RegistryObject<Block> blockRegistryObject) {
        RelicsItems.ITEMS.register(
                name,
                () -> new BlockItem(blockRegistryObject.get(), getDefaultItemProperties())
        );
    }

    private static Item.Properties getDefaultItemProperties() {
        Item.Properties properties = new Item.Properties();//.tab(References.ITEM_GROUP);
        return properties;
    }

//    public static boolean excludeBlockItemRegistry(ResourceLocation registryName) {
//        ArrayList<String> excludeBlocks = new ArrayList<>();
//        return excludeBlocks.contains(registryName.toString());
//    }

    public RelicsBlocks() {
        /* Disable automatic default public constructor */
    }
}
