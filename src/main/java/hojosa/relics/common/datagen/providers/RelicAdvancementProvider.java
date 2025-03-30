package hojosa.relics.common.datagen.providers;

import java.util.function.Consumer;

import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.lib.RelicsUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider.AdvancementGenerator;
import vazkii.patchouli.api.PatchouliAPI;

public class RelicAdvancementProvider implements AdvancementGenerator{


	@Override
	public void generate(Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
		
		Advancement root = Advancement.Builder.advancement()
				.display(new DisplayInfo(PatchouliAPI.get().getBookStack(RelicsUtil.modLoc("tome")), 
						Component.literal("Relics"), Component.literal("Find an Ancient Tome of Relics"), 
						RelicsUtil.modLoc("textures/block/starstone_block.png"), 
						FrameType.TASK, true, false, false))
				.addCriterion("has_book", InventoryChangeTrigger.TriggerInstance.hasItems(PatchouliAPI.get().getBookStack(RelicsUtil.modLoc("tome")).getItem()))
				.save(saver, RelicsUtil.modLoc("root"), existingFileHelper);
		
		Advancement lostPages = Advancement.Builder.advancement()
				.display(new ItemStack(RelicsItems.LOST_PAGE_1.get()), 
						Component.literal("Lost Pages"), Component.literal("Find a Lost Page"), null, 
						FrameType.TASK, true, false, false)
				.parent(root)
				.addCriterion("has_book", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.LOST_PAGE_1.get()))
				.save(saver, RelicsUtil.modLoc("lost_pages"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.LOST_PAGE_1.get()), 
				Component.literal("Lost Pages 1"), Component.literal("Find Lost Page 1 and aquire its knowledge"), null, 
				FrameType.TASK, true, false, false))
		.parent(lostPages)
		.addCriterion("has_book", ConsumeItemTrigger.TriggerInstance.usedItem(RelicsItems.LOST_PAGE_1.get()))
		.save(saver, RelicsUtil.modLoc("lost_page_1"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.LOST_PAGE_2.get()), 
				Component.literal("Lost Pages 2"), Component.literal("Find Lost Page 2 and aquire its knowledge"), null, 
				FrameType.TASK, true, false, false))
		.parent(lostPages)
		.addCriterion("has_book", ConsumeItemTrigger.TriggerInstance.usedItem(RelicsItems.LOST_PAGE_2.get()))
		.save(saver, RelicsUtil.modLoc("lost_page_2"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.LOST_PAGE_3.get()), 
				Component.literal("Lost Pages 3"), Component.literal("Find Lost Page 3 and aquire its knowledge"), null, 
				FrameType.TASK, true, false, false))
		.parent(lostPages)
		.addCriterion("has_book", ConsumeItemTrigger.TriggerInstance.usedItem(RelicsItems.LOST_PAGE_3.get()))
		.save(saver, RelicsUtil.modLoc("lost_page_3"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.LOST_PAGE_4.get()), 
				Component.literal("Lost Pages 4"), Component.literal("Find Lost Page 4 and aquire its knowledge"), null, 
				FrameType.TASK, true, false, false))
		.parent(lostPages)
		.addCriterion("has_book", ConsumeItemTrigger.TriggerInstance.usedItem(RelicsItems.LOST_PAGE_4.get()))
		.save(saver, RelicsUtil.modLoc("lost_page_4"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.LOST_PAGE_5.get()), 
				Component.literal("Lost Pages 5"), Component.literal("Find Lost Page 1 and aquire its knowledge"), null, 
				FrameType.TASK, true, false, false))
		.parent(lostPages)
		.addCriterion("has_book", ConsumeItemTrigger.TriggerInstance.usedItem(RelicsItems.LOST_PAGE_5.get()))
		.save(saver, RelicsUtil.modLoc("lost_page_5"), existingFileHelper);
		
	}
}
