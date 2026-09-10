package hojosa.relics_of_old.common.datagen.providers;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsTags;
import hojosa.relics_of_old.lib.References;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.mantle.Mantle;

public class RelicsBlockTags extends BlockTagsProvider {

	public RelicsBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, References.MOD_ID, existingFileHelper);
	}

	@Override 
	protected void addTags(HolderLookup.Provider provider) {
		tag(BlockTags.MINEABLE_WITH_PICKAXE)
		.add(RelicsBlocks.SWORD_PEDESTAL_NORMAL.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_STONE.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_RELIC.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_TIME.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get())
		.add(RelicsBlocks.INFUSED_STARSTONE_BLOCK.get())
		.add(RelicsBlocks.SKYBEAM_BLOCK.get())
		.add(RelicsBlocks.STARSTONE_BLOCK.get())
		.add(RelicsBlocks.ODDISH_POT.get())
		.add(RelicsBlocks.BOOST_PLATE.get())
		.add(RelicsBlocks.STARWELL_FRAME.get())
		.add(RelicsBlocks.STARWELL_CORE.get())
		.add(RelicsBlocks.SKY_LENS.get())
		.add(RelicsBlocks.RITUAL_LOCUS.get())
		.add(RelicsBlocks.PHOENIX_ALTAR.get());
		tag(BlockTags.NEEDS_STONE_TOOL)
		.add(RelicsBlocks.SWORD_PEDESTAL_NORMAL.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_STONE.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_RELIC.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_TIME.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get())
		.add(RelicsBlocks.ODDISH_POT.get())
		.add(RelicsBlocks.SKY_LENS.get())
		.add(RelicsBlocks.RITUAL_LOCUS.get());
		tag(BlockTags.NEEDS_IRON_TOOL)
		.add(RelicsBlocks.INFUSED_STARSTONE_BLOCK.get())
		.add(RelicsBlocks.SKYBEAM_BLOCK.get())
		.add(RelicsBlocks.STARSTONE_BLOCK.get());
		tag(BlockTags.NEEDS_DIAMOND_TOOL)
		.add(RelicsBlocks.STARWELL_FRAME.get())
		.add(RelicsBlocks.STARWELL_CORE.get())
		.add(RelicsBlocks.PHOENIX_ALTAR.get());
		tag(RelicsTags.Blocks.SWORD_PEDESTAL_VARIANTS)
		.add(Blocks.STONE, Blocks.POLISHED_GRANITE, Blocks.POLISHED_BLACKSTONE, Blocks.POLISHED_ANDESITE, Blocks.POLISHED_DIORITE, Blocks.CUT_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.NETHER_BRICKS, Blocks.RED_NETHER_BRICKS, Blocks.POLISHED_BASALT, Blocks.OBSIDIAN)
		.addOptionalTag(Mantle.commonResource("storage_blocks"));
		tag(RelicsTags.Blocks.CLAY_JAR_VARIANTS)
		.addTag(BlockTags.TERRACOTTA);
		tag(RelicsTags.Blocks.BOMBABLE)
	    .add(Blocks.COBBLESTONE, Blocks.TNT)
	    .add(RelicsBlocks.CLAY_JAR.get());
		tag(BlockTags.MINEABLE_WITH_SHOVEL)
		.add(RelicsBlocks.STARRY_SAND.get());
	}

    @Override
    public String getName() {
        return "Relics Block Tags";
    }
}