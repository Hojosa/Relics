package hojosa.relics.common.datagen.providers;

import java.util.Map;
import java.util.stream.Collectors;

import hojosa.relics.common.block.entity.SwordPedestalBlockEntity;
import hojosa.relics.common.init.RelicsBlockEntities;
import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.lib.References;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

public class RelicsLootTables extends VanillaBlockLoot {

	@Override
	protected void generate() {
		dropSelf(RelicsBlocks.LAPIS_BRICK.get());
		dropSelf(RelicsBlocks.ODDISH_POT.get());
		dropSelf(RelicsBlocks.INFUSED_STARSTONE_BLOCK.get());
		dropSelf(RelicsBlocks.STARSTONE_BLOCK.get());
		dropSelf(RelicsBlocks.SKYBEAM_BLOCK.get());
		createStandardTable(RelicsBlocks.SWORD_PEDESTAL_NORMAL.get(), RelicsBlockEntities.REXTURED_SWORD_PEDESTAL_BLOCK_ENTITY.get(), SwordPedestalBlockEntity.ITEMS_TAG);
		createStandardTable(RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS.get(), RelicsBlockEntities.REXTURED_SWORD_PEDESTAL_BLOCK_ENTITY.get(), SwordPedestalBlockEntity.ITEMS_TAG);
		createStandardTable(RelicsBlocks.SWORD_PEDESTAL_RELIC.get(), RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), SwordPedestalBlockEntity.ITEMS_TAG);
		createStandardTable(RelicsBlocks.SWORD_PEDESTAL_TIME.get(), RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), SwordPedestalBlockEntity.ITEMS_TAG);
		createStandardTable(RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get(), RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), SwordPedestalBlockEntity.ITEMS_TAG);
		createStandardTable(RelicsBlocks.SWORD_PEDESTAL_STONE.get(), RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get(), SwordPedestalBlockEntity.ITEMS_TAG);
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
		lti.apply(SetContainerContents.setContents(type).withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents"))));

		LootPool.Builder builder = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(lti);
		add(block, LootTable.lootTable().withPool(builder));
	}
}
