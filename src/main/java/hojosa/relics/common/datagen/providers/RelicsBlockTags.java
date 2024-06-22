package hojosa.relics.common.datagen.providers;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.lib.References;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RelicsBlockTags extends BlockTagsProvider {

	public RelicsBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, References.MODID, existingFileHelper);
	}

	@Override 
	protected void addTags(HolderLookup.Provider provider) {
		tag(BlockTags.MINEABLE_WITH_PICKAXE)
		.add(RelicsBlocks.LAPIS_BRICK.get())
		.add(RelicsBlocks.SWORD_PEDESTAL.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_OOT.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get());
		tag(BlockTags.NEEDS_STONE_TOOL)
		.add(RelicsBlocks.LAPIS_BRICK.get())
		.add(RelicsBlocks.SWORD_PEDESTAL.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_OOT.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get());
	}

    @Override
    public String getName() {
        return "Relics Block Tags";
    }
}
