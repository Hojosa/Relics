package hojosa.relics_of_old.common.datagen.providers;

import java.util.Map;
import java.util.stream.Collectors;

import hojosa.relics_of_old.common.block.BombFlower;
import hojosa.relics_of_old.common.block.MysticShrub;
import hojosa.relics_of_old.common.block.entity.SwordPedestalBlockEntity;
import hojosa.relics_of_old.common.init.RelicsBlockEntities;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.lib.References;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

public class RelicsLootTables extends VanillaBlockLoot {

	@Override
	protected void generate() {
		dropSelf(RelicsBlocks.ODDISH_POT.get());
		dropSelf(RelicsBlocks.STARSTONE_BLOCK.get());
		dropSelf(RelicsBlocks.SKYBEAM_BLOCK.get());
		dropSelf(RelicsBlocks.STARBEAM_TORCH.get());
		dropSelf(RelicsBlocks.CALTROPS.get());
		dropWhenSilkTouch(RelicsBlocks.CLAY_JAR.get());
		dropSelf(RelicsBlocks.BOOST_PLATE.get());
		createStandardTable(RelicsBlocks.SWORD_PEDESTAL_NORMAL.get(), RelicsBlockEntities.REXTURED_SWORD_PEDESTAL_BLOCK_ENTITY.get(), SwordPedestalBlockEntity.COLOR_TAG, SwordPedestalBlockEntity.GLOW_TAG, SwordPedestalBlockEntity.INFUSED_TAG);
		createStandardTable(RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS.get(), RelicsBlockEntities.REXTURED_SWORD_PEDESTAL_BLOCK_ENTITY.get(), SwordPedestalBlockEntity.COLOR_TAG, SwordPedestalBlockEntity.GLOW_TAG, SwordPedestalBlockEntity.INFUSED_TAG);
		createStandardTable(RelicsBlocks.SWORD_PEDESTAL_RELIC.get(), RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), SwordPedestalBlockEntity.COLOR_TAG, SwordPedestalBlockEntity.GLOW_TAG, SwordPedestalBlockEntity.INFUSED_TAG);
		createStandardTable(RelicsBlocks.SWORD_PEDESTAL_TIME.get(), RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), SwordPedestalBlockEntity.COLOR_TAG, SwordPedestalBlockEntity.GLOW_TAG, SwordPedestalBlockEntity.INFUSED_TAG);
		createStandardTable(RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get(), RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), SwordPedestalBlockEntity.COLOR_TAG, SwordPedestalBlockEntity.GLOW_TAG, SwordPedestalBlockEntity.INFUSED_TAG);
		createStandardTable(RelicsBlocks.SWORD_PEDESTAL_STONE.get(), RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), SwordPedestalBlockEntity.COLOR_TAG, SwordPedestalBlockEntity.GLOW_TAG, SwordPedestalBlockEntity.INFUSED_TAG);
		createStandardTable(RelicsBlocks.INFUSED_STARSTONE_BLOCK.get(), RelicsBlockEntities.INFUSED_STARSTONE_BLOCK_ENTITY.get());
		dropSelf(RelicsBlocks.SUGAR_CUBE.get());
		add(RelicsBlocks.BOMB_FLOWER.get(), LootTable.lootTable()
			      .withPool(LootPool.lootPool()
			          .setRolls(ConstantValue.exactly(1))
			          .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(RelicsBlocks.BOMB_FLOWER.get())
			              .setProperties(StatePropertiesPredicate.Builder.properties()
			                  .hasProperty(BombFlower.STATE, BombFlower.FlowerState.NORMAL)))
			          .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)))
			          .add(LootItem.lootTableItem(RelicsBlocks.BOMB_FLOWER.get()))));
		
		add(RelicsBlocks.MYSTIC_SHRUB.get(), LootTable.lootTable()
			      // Normal drops — 3 independent pools, each 20% chance
			      .withPool(LootPool.lootPool()
			          .setRolls(ConstantValue.exactly(1))
			          .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(RelicsBlocks.MYSTIC_SHRUB.get())
			              .setProperties(StatePropertiesPredicate.Builder.properties()
			                  .hasProperty(MysticShrub.STATE, MysticShrub.ShrubState.NORMAL)))
			          .when(LootItemRandomChanceCondition.randomChance(0.2f))
			          .add(LootItem.lootTableItem(RelicsItems.HEART.get())))
			      .withPool(LootPool.lootPool()
			          .setRolls(ConstantValue.exactly(1))
			          .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(RelicsBlocks.MYSTIC_SHRUB.get())
			              .setProperties(StatePropertiesPredicate.Builder.properties()
			                  .hasProperty(MysticShrub.STATE, MysticShrub.ShrubState.NORMAL)))
			          .when(LootItemRandomChanceCondition.randomChance(0.2f))
			          .add(LootItem.lootTableItem(RelicsItems.EMERALD_SHARD.get())))
			      .withPool(LootPool.lootPool()
			          .setRolls(ConstantValue.exactly(1))
			          .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(RelicsBlocks.MYSTIC_SHRUB.get())
			              .setProperties(StatePropertiesPredicate.Builder.properties()
			                  .hasProperty(MysticShrub.STATE, MysticShrub.ShrubState.NORMAL)))
			          .when(LootItemRandomChanceCondition.randomChance(0.2f))
			          .add(LootItem.lootTableItem(Items.ARROW)))
			      // Charged drops — 1 pool, 1 roll, equal weight alternatives
			      .withPool(LootPool.lootPool()
			          .setRolls(ConstantValue.exactly(1))
			          .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(RelicsBlocks.MYSTIC_SHRUB.get())
			              .setProperties(StatePropertiesPredicate.Builder.properties()
			                  .hasProperty(MysticShrub.STATE, MysticShrub.ShrubState.CHARGED)))
			          .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(1))
			          .add(LootItem.lootTableItem(RelicsItems.HEART.get()).setWeight(1)
			              .apply(SetItemCountFunction.setCount(ConstantValue.exactly(3))))
			          .add(LootItem.lootTableItem(Items.ARROW).setWeight(1)
			              .apply(SetItemCountFunction.setCount(ConstantValue.exactly(5))))
			          .add(LootItem.lootTableItem(RelicsItems.EMERALD_SHARD.get()).setWeight(1)))
			          // TODO: uncomment when bomb item is added
			          // .add(LootItem.lootTableItem(RelicsItems.BOMB.get()).setWeight(1)
			          //     .apply(SetItemCountFunction.setCount(ConstantValue.exactly(3)))))
			  );
		add(RelicsBlocks.STARRY_SAND.get(), LootTable.lootTable()
			      // silk touch: drop the block itself
			      .withPool(LootPool.lootPool()
			          .setRolls(ConstantValue.exactly(1))
			          .when(HAS_SILK_TOUCH)
			          .add(LootItem.lootTableItem(RelicsBlocks.STARRY_SAND.get())))
			      // no silk touch: always drop sand
			      .withPool(LootPool.lootPool()
			          .setRolls(ConstantValue.exactly(1))
			          .when(HAS_NO_SILK_TOUCH)
			          .add(LootItem.lootTableItem(Items.SAND)))
			      // no silk touch: 50% chance to drop star dust (fortune increases chance)
			      .withPool(LootPool.lootPool()
			          .setRolls(ConstantValue.exactly(1))
			          .when(HAS_NO_SILK_TOUCH)
			          .when(BonusLevelTableCondition.bonusLevelFlatChance(
			              Enchantments.BLOCK_FORTUNE, 0.5f, 0.667f, 0.833f, 1.0f))
			          .add(LootItem.lootTableItem(RelicsItems.STAR_DUST.get()))));
		// struck sand: drops sand, + fulgurite when mined with shovel (no silk touch)
		  add(RelicsBlocks.STRUCK_SAND.get(), LootTable.lootTable()
		      .withPool(LootPool.lootPool()
		          .setRolls(ConstantValue.exactly(1))
		          .when(HAS_SILK_TOUCH)
		          .add(LootItem.lootTableItem(RelicsBlocks.STRUCK_SAND.get())))
		      .withPool(LootPool.lootPool()
		          .setRolls(ConstantValue.exactly(1))
		          .when(HAS_NO_SILK_TOUCH)
		          .add(LootItem.lootTableItem(Items.SAND)))
		      .withPool(LootPool.lootPool()
		          .setRolls(ConstantValue.exactly(1))
		          .when(HAS_NO_SILK_TOUCH)
		          .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(net.minecraft.tags.ItemTags.SHOVELS)))
		          .add(LootItem.lootTableItem(RelicsItems.FULGURITE.get()))));

		  // struck dirt: same pattern but drops dirt
		  add(RelicsBlocks.STRUCK_DIRT.get(), LootTable.lootTable()
		      .withPool(LootPool.lootPool()
		          .setRolls(ConstantValue.exactly(1))
		          .when(HAS_SILK_TOUCH)
		          .add(LootItem.lootTableItem(RelicsBlocks.STRUCK_DIRT.get())))
		      .withPool(LootPool.lootPool()
		          .setRolls(ConstantValue.exactly(1))
		          .when(HAS_NO_SILK_TOUCH)
		          .add(LootItem.lootTableItem(Items.DIRT)))
		      .withPool(LootPool.lootPool()
		          .setRolls(ConstantValue.exactly(1))
		          .when(HAS_NO_SILK_TOUCH)
		          .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(net.minecraft.tags.ItemTags.SHOVELS)))
		          .add(LootItem.lootTableItem(RelicsItems.FULGURITE.get()))));

	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return ForgeRegistries.BLOCKS.getEntries().stream().filter(e -> e.getKey().location().getNamespace().equals(References.MOD_ID)).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	private void createStandardTable(Block block, BlockEntityType<?> type, String... tags) {
		LootPoolSingletonContainer.Builder<?> lti = LootItem.lootTableItem(block);
		lti.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY));
		for (String tag : tags) {
			lti.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy(tag, "BlockEntityTag." + tag, CopyNbtFunction.MergeStrategy.REPLACE));
		}
		lti.apply(SetContainerContents.setContents(type).withEntry(DynamicLoot.dynamicEntry(ResourceLocation.withDefaultNamespace("contents"))));

		LootPool.Builder builder = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(lti);
		add(block, LootTable.lootTable().withPool(builder));
	}
}