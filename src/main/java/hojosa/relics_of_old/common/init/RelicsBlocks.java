package hojosa.relics_of_old.common.init;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import hojosa.relics_of_old.common.block.BoostPlate;
import hojosa.relics_of_old.common.block.Caltrops;
import hojosa.relics_of_old.common.block.ClayJar;
import hojosa.relics_of_old.common.block.InfusedStarstoneBlock;
import hojosa.relics_of_old.common.block.MysticShrub;
import hojosa.relics_of_old.common.block.NormalSwordPedestal;
import hojosa.relics_of_old.common.block.RelicRetexturedSwordPedestal;
import hojosa.relics_of_old.common.block.RelicSwordPedestal;
import hojosa.relics_of_old.common.block.SkybeamBlock;
import hojosa.relics_of_old.common.block.StarBeamTorch;
import hojosa.relics_of_old.common.block.StoneSwordPedestal;
import hojosa.relics_of_old.common.block.SugarCubeBlock;
import hojosa.relics_of_old.common.block.TimeSwordPedestal;
import hojosa.relics_of_old.common.block.TwilightSwordPedestal;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.block.RelicsFacingBlock;
import hojosa.relics_of_old.lib.block.RelicsNormalBlock;
import hojosa.relics_of_old.lib.block.SelfPlacingBlockItem;
import hojosa.relics_of_old.lib.block.SwordPedestalBaseBlock;
import hojosa.relics_of_old.lib.item.ItemBlockGlint;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import slimeknights.mantle.registration.deferred.BlockDeferredRegister;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.mantle.util.RetexturedHelper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsBlocks {
	public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(References.MOD_ID);

	protected static final Item.Properties ITEM_PROPS = new Item.Properties();
	protected static final Function<Block, ? extends BlockItem> BLOCK_ITEM = b -> new BlockItem(b, ITEM_PROPS);
	protected static final Function<Block, ? extends BlockItem> BLOCK_ITEM_SELFPLACING = b -> new SelfPlacingBlockItem(b, new Item.Properties());
	protected static final Function<Block, ? extends BlockItem> BLOCK_ITEM_UNCOMMON = b -> new BlockItem(b, new Item.Properties().rarity(Rarity.UNCOMMON));
	protected static final Function<Block, ? extends BlockItem> BLOCK_ITEM_EPIC = b -> new BlockItem(b, new Item.Properties().rarity(Rarity.EPIC));
	protected static final Function<Block, ? extends BlockItem> BLOCK_ITEM_GLINT = b -> new ItemBlockGlint(b, new Item.Properties().rarity(Rarity.EPIC));

//	public static final ItemObject<Block> LAPIS_BRICK = BLOCKS.register(References.UnlocalizedName.LAPIS_BRICK,
//			() -> new RelicsNormalBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)), BLOCK_ITEM);

	public static final ItemObject<Block> ODDISH_POT = BLOCKS.register(References.UnlocalizedName.ODDISH_POT, () -> new RelicsFacingBlock(BlockBehaviour.Properties.copy(Blocks.FLOWER_POT).noOcclusion()), BLOCK_ITEM);

	public static final ItemObject<SwordPedestalBaseBlock> SWORD_PEDESTAL_NORMAL, SWORD_PEDESTAL_RELIC, SWORD_PEDESTAL_RELIC_VARIANTS, SWORD_PEDESTAL_TIME, SWORD_PEDESTAL_TWILIGHT, SWORD_PEDESTAL_STONE;
	static {
		BlockBehaviour.Properties stone = builder(MapColor.COLOR_GRAY, SoundType.METAL).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 9.0F).noOcclusion()
				.lightLevel(litBlockEmission(8));
		SWORD_PEDESTAL_NORMAL = BLOCKS.register(References.UnlocalizedName.SWORD_PEDESTAL_NORMAL, () -> new NormalSwordPedestal(stone), BLOCK_ITEM);
		SWORD_PEDESTAL_STONE = BLOCKS.register(References.UnlocalizedName.SWORD_PEDESTAL_STONE, () -> new StoneSwordPedestal(stone), BLOCK_ITEM);
		SWORD_PEDESTAL_RELIC = BLOCKS.register(References.UnlocalizedName.SWORD_PEDESTAL, () -> new RelicSwordPedestal(stone), BLOCK_ITEM);
		SWORD_PEDESTAL_RELIC_VARIANTS = BLOCKS.register(References.UnlocalizedName.SWORD_PEDESTAL_RELIC, () -> new RelicRetexturedSwordPedestal(stone), BLOCK_ITEM);
		SWORD_PEDESTAL_TIME = BLOCKS.register(References.UnlocalizedName.SWORD_PEDESTAL_TIME, () -> new TimeSwordPedestal(stone), BLOCK_ITEM);
		SWORD_PEDESTAL_TWILIGHT = BLOCKS.register(References.UnlocalizedName.SWORD_PEDESTAL_TWILIGHT, () -> new TwilightSwordPedestal(stone), BLOCK_ITEM);
	}

	public static final ItemObject<Block> STARSTONE_BLOCK = BLOCKS.register(References.UnlocalizedName.STARSTONE_BLOCK,
			() -> new RelicsNormalBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).lightLevel(value -> 15)), BLOCK_ITEM_UNCOMMON);

	public static final ItemObject<Block> INFUSED_STARSTONE_BLOCK = BLOCKS.register(References.UnlocalizedName.INFUSED_STARSTONE_BLOCK,
			() -> new InfusedStarstoneBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).lightLevel(value -> 15), true), BLOCK_ITEM_GLINT);

	public static final ItemObject<Block> SKYBEAM_BLOCK = BLOCKS.register(References.UnlocalizedName.SKYBEAM_BLOCK,
			() -> new SkybeamBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).lightLevel(litBlockEmission(10))), BLOCK_ITEM);

	public static final ItemObject<Block> STARBEAM_TORCH = BLOCKS.register(References.UnlocalizedName.STARBEAM_TORCH, () -> new StarBeamTorch(BlockBehaviour.Properties.copy(Blocks.TORCH).lightLevel(value -> 15)),
			BLOCK_ITEM);
	public static final ItemObject<Block> MYSTIC_SHRUB = BLOCKS.register(References.UnlocalizedName.MYSTIC_SHRUB, () -> new MysticShrub(), BLOCK_ITEM);
	public static final ItemObject<Block> CALTROPS = BLOCKS.register(References.UnlocalizedName.CALTROPS, () -> new Caltrops(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)), BLOCK_ITEM_SELFPLACING);
	public static final ItemObject<Block> CLAY_JAR = BLOCKS.register(References.UnlocalizedName.CLAY_JAR,
			() -> new ClayJar(BlockBehaviour.Properties.copy(Blocks.DECORATED_POT).strength(0).noOcclusion().sound(SoundType.DECORATED_POT)), BLOCK_ITEM);
	public static final ItemObject<Block> BOOST_PLATE = BLOCKS.register(References.UnlocalizedName.BOOST_PLATE, () -> new BoostPlate(BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE)), BLOCK_ITEM);
	public static final ItemObject<Block> SUGAR_CUBE = BLOCKS.register(References.UnlocalizedName.SUGAR_CUBE, SugarCubeBlock::new, BLOCK_ITEM);

	/**
	 * We use this builder to ensure that our blocks all have the most important
	 * properties set. This way it'll stick out if a block doesn't have a sound set.
	 * It may be a bit less clear at first, since the actual builder methods tell
	 * you what each value means, but as long as we don't statically import the
	 * enums it should be just as readable.
	 */
	protected static BlockBehaviour.Properties builder(SoundType soundType) {
		return BlockBehaviour.Properties.of().sound(soundType);
	}

	/** Same as above, but with a color */
	protected static BlockBehaviour.Properties builder(MapColor color, SoundType soundType) {
		return builder(soundType).mapColor(color);
	}

	public static void addTabItems(ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
//		output.accept(LAPIS_BRICK.get().asItem());
		output.accept(ODDISH_POT.get().asItem());
		output.accept(SWORD_PEDESTAL_RELIC.get().asItem());
		output.accept(SWORD_PEDESTAL_TIME.get().asItem());
		output.accept(SWORD_PEDESTAL_TWILIGHT.get().asItem());
		output.accept(SWORD_PEDESTAL_STONE.get().asItem());
		output.accept(SKYBEAM_BLOCK.get().asItem());
		output.accept(STARSTONE_BLOCK.get().asItem());
		output.accept(INFUSED_STARSTONE_BLOCK.get().asItem());
		output.accept(STARBEAM_TORCH.get().asItem());
		output.accept(MYSTIC_SHRUB.get().asItem());
		output.accept(CALTROPS.get().asItem());
		output.accept(CLAY_JAR.get().asItem());
		output.accept(BOOST_PLATE.get().asItem());
		output.accept(SUGAR_CUBE.get().asItem());

		Predicate<ItemStack> variants = stack -> {
			output.accept(stack);
			return false;
		};

		RetexturedHelper.addTagVariants(variants, SWORD_PEDESTAL_NORMAL, RelicsTags.Items.SWORD_PEDESTAL_VARIANTS);
		RetexturedHelper.addTagVariants(variants, SWORD_PEDESTAL_RELIC_VARIANTS, RelicsTags.Items.SWORD_PEDESTAL_VARIANTS);
		RetexturedHelper.addTagVariants(variants, CLAY_JAR, RelicsTags.Items.CLAY_JAR_VARIANTS);
	}

	private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
		return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
	}
}