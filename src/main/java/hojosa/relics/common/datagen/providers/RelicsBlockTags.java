package hojosa.relics.common.datagen.providers;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.common.init.RelicsTags;
import hojosa.relics.lib.References;
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
		.add(RelicsBlocks.LAPIS_BRICK.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_NORMAL.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_RELIC.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_TIME.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get());
		tag(BlockTags.NEEDS_STONE_TOOL)
		.add(RelicsBlocks.LAPIS_BRICK.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_NORMAL.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_RELIC.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_TIME.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get());
		tag(RelicsTags.Blocks.SWORD_PEDESTAL_VARIANTS)
		.add(Blocks.STONE, Blocks.POLISHED_GRANITE, Blocks.POLISHED_BLACKSTONE, Blocks.POLISHED_ANDESITE, Blocks.POLISHED_DIORITE, Blocks.CUT_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.NETHER_BRICKS, Blocks.RED_NETHER_BRICKS, Blocks.POLISHED_BASALT, Blocks.OBSIDIAN)
		.addOptionalTag(Mantle.commonResource("storage_blocks"));
	}

    @Override
    public String getName() {
        return "Relics Block Tags";
    }
}
