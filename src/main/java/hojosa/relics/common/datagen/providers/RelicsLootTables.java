package hojosa.relics.common.datagen.providers;

import hojosa.relics.common.init.RelicsBlockEntities;
import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.lib.References;
import net.minecraft.data.DataGenerator;

public class RelicsLootTables extends BaseLootTableProvider{

	public RelicsLootTables(DataGenerator dataGeneratorIn) {
		super(dataGeneratorIn);
	}
	
	@Override
	protected void addTables() {
		lootTables.put(RelicsBlocks.LAPIS_BRICK.get(), createSimpleTable("lapis_brick", RelicsBlocks.LAPIS_BRICK.get()));
		lootTables.put(RelicsBlocks.SWORD_PEDESTAL.get(), createStandardTable(References.UnlocalizedName.SWORD_PEDESTAL, RelicsBlocks.SWORD_PEDESTAL.get(), RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get()));
		lootTables.put(RelicsBlocks.SWORD_PEDESTAL_OOT.get(), createStandardTable(References.UnlocalizedName.SWORD_PEDESTAL_OOT, RelicsBlocks.SWORD_PEDESTAL_OOT.get(), RelicsBlockEntities.SWORD_PEDESTAL_BLOCK_ENTITY.get()));
	}

}
