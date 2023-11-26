package hojosa.relics.common.datagen.providers;

import org.jetbrains.annotations.Nullable;

import hojosa.relics.common.block.RelicsBlock;
import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.lib.References;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RelicsBlockTags extends BlockTagsProvider {

	public RelicsBlockTags(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
		super(pGenerator, References.MODID, existingFileHelper);
	}

	@Override 
	protected void addTags() {
		tag(BlockTags.MINEABLE_WITH_PICKAXE)
		.add(RelicsBlocks.LAPIS_BRICK.get())
		.add(RelicsBlocks.SWORD_PEDESTAL.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_OOT.get());
		tag(BlockTags.NEEDS_STONE_TOOL)
		.add(RelicsBlocks.LAPIS_BRICK.get())
		.add(RelicsBlocks.SWORD_PEDESTAL.get())
		.add(RelicsBlocks.SWORD_PEDESTAL_OOT.get());
	}

    @Override
    public String getName() {
        return "Relics Block Tags";
    }
}
