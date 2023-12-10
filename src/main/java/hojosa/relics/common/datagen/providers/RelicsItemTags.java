package hojosa.relics.common.datagen.providers;


import org.jetbrains.annotations.Nullable;

import hojosa.relics.Relics;
import hojosa.relics.integration.curios.RelicsCuriosIntegration;
import hojosa.relics.lib.References;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RelicsItemTags extends ItemTagsProvider {

	public RelicsItemTags(DataGenerator pGenerator, BlockTagsProvider pBlockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(pGenerator, pBlockTagsProvider, References.MODID, existingFileHelper);
	}

	@Override 
	protected void addTags() {
		if(Relics.curiosPresent){
			RelicsCuriosIntegration.generateItemTags(this::tag);
		}
	}

    @Override
    public String getName() {
        return "Relics Item Tags";
    }
}
