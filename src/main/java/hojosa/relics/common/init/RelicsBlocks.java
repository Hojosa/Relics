package hojosa.relics.common.init;

import java.util.function.Function;
import java.util.function.Predicate;

import hojosa.relics.common.block.FancySwordPedestal;
import hojosa.relics.common.block.NormalSwordPedestal;
import hojosa.relics.common.block.RelicSwordPedestal;
import hojosa.relics.common.block.StoneSwordPedestal;
import hojosa.relics.lib.References;
import hojosa.relics.lib.block.RelicsFacingBlock;
import hojosa.relics.lib.block.RelicsNormalBlock;
import hojosa.relics.lib.block.SwordPedestalBaseBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import slimeknights.mantle.registration.deferred.BlockDeferredRegister;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.mantle.util.RetexturedHelper;

public class RelicsBlocks {
	public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(References.MOD_ID);

	protected static final Item.Properties ITEM_PROPS = new Item.Properties();
	protected static final Function<Block, ? extends BlockItem> BLOCK_ITEM = (b) -> new BlockItem(b, ITEM_PROPS);

	public static final ItemObject<Block> LAPIS_BRICK = BLOCKS.register(References.UnlocalizedName.LAPIS_BRICK,
			() -> new RelicsNormalBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)), BLOCK_ITEM);
	
	public static final ItemObject<Block> ODDISH_POT = BLOCKS.register(References.UnlocalizedName.ODDISH_POT,
			() -> new RelicsFacingBlock(BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)), BLOCK_ITEM);

	public static final ItemObject<SwordPedestalBaseBlock> SWORD_PEDESTAL_NORMAL, SWORD_PEDESTAL_RELIC, SWORD_PEDESTAL_RELIC_VARIANTS, SWORD_PEDESTAL_TIME, SWORD_PEDESTAL_TWILIGHT, SWORD_PEDESTAL_STONE;
	static {
		Block.Properties STONE_TABLE = builder(MapColor.COLOR_GRAY, SoundType.METAL)
				.instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 9.0F)
				.noOcclusion();
		SWORD_PEDESTAL_NORMAL = BLOCKS.register(References.UnlocalizedName.SWORD_PEDESTAL_NORMAL, () -> new NormalSwordPedestal(STONE_TABLE), BLOCK_ITEM);
		SWORD_PEDESTAL_RELIC = BLOCKS.register(References.UnlocalizedName.SWORD_PEDESTAL, () -> new FancySwordPedestal(STONE_TABLE, 0, References.PedestalShapes.BASIC_SHAPE, References.PedestalShapes.BASIC_SWORD_SHAPE, false), BLOCK_ITEM);
		SWORD_PEDESTAL_RELIC_VARIANTS = BLOCKS.register(References.UnlocalizedName.SWORD_PEDESTAL_RELIC, () -> new RelicSwordPedestal(STONE_TABLE), BLOCK_ITEM);
		SWORD_PEDESTAL_TIME = BLOCKS.register(References.UnlocalizedName.SWORD_PEDESTAL_TIME, () -> new FancySwordPedestal(STONE_TABLE, 0.1, References.PedestalShapes.TIME_SHAPE, References.PedestalShapes.TIME_SWORD_SHAPE, true), BLOCK_ITEM);
		SWORD_PEDESTAL_TWILIGHT = BLOCKS.register(References.UnlocalizedName.SWORD_PEDESTAL_TWILIGHT, () -> new FancySwordPedestal(STONE_TABLE, 0.13, References.PedestalShapes.TWILIGHT_SHAPE, References.PedestalShapes.TWILIGHT_SWORD_SHAPE, true), BLOCK_ITEM);
		SWORD_PEDESTAL_STONE = BLOCKS.register(References.UnlocalizedName.SWORD_PEDESTAL_STONE, () -> new StoneSwordPedestal(STONE_TABLE), BLOCK_ITEM);

	}

	/**
	 * We use this builder to ensure that our blocks all have the most important
	 * properties set. This way it'll stick out if a block doesn't have a sound set.
	 * It may be a bit less clear at first, since the actual builder methods tell
	 * you what each value means, but as long as we don't statically import the
	 * enums it should be just as readable.
	 */
	protected static BlockBehaviour.Properties builder(SoundType soundType) {
		return Block.Properties.of().sound(soundType);
	}

	/** Same as above, but with a color */
	protected static BlockBehaviour.Properties builder(MapColor color, SoundType soundType) {
		return builder(soundType).mapColor(color);
	}

	public static void addTabItems(ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
		output.accept(LAPIS_BRICK.get().asItem());
		output.accept(SWORD_PEDESTAL_RELIC.get().asItem());
		output.accept(SWORD_PEDESTAL_TIME.get().asItem());
		output.accept(SWORD_PEDESTAL_TWILIGHT.get().asItem());
		output.accept(SWORD_PEDESTAL_STONE.get().asItem());
		output.accept(ODDISH_POT.get().asItem());
		
		Predicate<ItemStack> variants = stack -> {
			output.accept(stack);
			return false;
		};

		RetexturedHelper.addTagVariants(variants, SWORD_PEDESTAL_NORMAL, RelicsTags.Items.SWORD_PEDESTAL_VARIANTS);
		RetexturedHelper.addTagVariants(variants, SWORD_PEDESTAL_RELIC_VARIANTS, RelicsTags.Items.SWORD_PEDESTAL_VARIANTS);

	}

	public RelicsBlocks() {
		/* Disable automatic default public constructor */
	}
}
