package hojosa.relics.common.datagen.providers;

import java.util.function.Consumer;

import hojosa.relics.common.init.RelicsBlocks;
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
		String hasItem = "hasItem";
		
		Advancement root = Advancement.Builder.advancement()
				.display(new DisplayInfo(PatchouliAPI.get().getBookStack(RelicsUtil.modLoc("tome")), 
						Component.literal("Relics"), Component.literal("Find an Ancient Tome of Relics"), 
						RelicsUtil.modLoc("textures/block/starstone_block.png"), 
						FrameType.TASK, true, false, false))
				.addCriterion(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(PatchouliAPI.get().getBookStack(RelicsUtil.modLoc("tome")).getItem()))
				.save(saver, RelicsUtil.modLoc("root"), existingFileHelper);
		
		//lost pages
		Advancement lostPages = Advancement.Builder.advancement()
				.display(new ItemStack(RelicsItems.LOST_PAGE_1.get()), 
						Component.literal("Lost Pages"), Component.literal("Find a Lost Page"), null, 
						FrameType.TASK, true, false, false)
				.parent(root)
				.addCriterion("found_any_page", InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.LOST_PAGE_1.get(), RelicsItems.LOST_PAGE_2.get(), RelicsItems.LOST_PAGE_3.get(), RelicsItems.LOST_PAGE_4.get(), RelicsItems.LOST_PAGE_5.get()))
				.save(saver, RelicsUtil.modLoc("lost_pages"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.LOST_PAGE_1.get()), 
				Component.literal("Lost Pages 1"), Component.literal("Find Lost Page 1 and aquire its knowledge"), null, 
				FrameType.TASK, true, false, false))
		.parent(lostPages)
		.addCriterion("page", ConsumeItemTrigger.TriggerInstance.usedItem(RelicsItems.LOST_PAGE_1.get()))
		.save(saver, RelicsUtil.modLoc("lost_page_1"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.LOST_PAGE_2.get()), 
				Component.literal("Lost Pages 2"), Component.literal("Find Lost Page 2 and aquire its knowledge"), null, 
				FrameType.TASK, true, false, false))
		.parent(lostPages)
		.addCriterion("page", ConsumeItemTrigger.TriggerInstance.usedItem(RelicsItems.LOST_PAGE_2.get()))
		.save(saver, RelicsUtil.modLoc("lost_page_2"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.LOST_PAGE_3.get()), 
				Component.literal("Lost Pages 3"), Component.literal("Find Lost Page 3 and aquire its knowledge"), null, 
				FrameType.TASK, true, false, false))
		.parent(lostPages)
		.addCriterion("page", ConsumeItemTrigger.TriggerInstance.usedItem(RelicsItems.LOST_PAGE_3.get()))
		.save(saver, RelicsUtil.modLoc("lost_page_3"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.LOST_PAGE_4.get()), 
				Component.literal("Lost Pages 4"), Component.literal("Find Lost Page 4 and aquire its knowledge"), null, 
				FrameType.TASK, true, false, false))
		.parent(lostPages)
		.addCriterion("page", ConsumeItemTrigger.TriggerInstance.usedItem(RelicsItems.LOST_PAGE_4.get()))
		.save(saver, RelicsUtil.modLoc("lost_page_4"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.LOST_PAGE_5.get()), 
				Component.literal("Lost Pages 5"), Component.literal("Find Lost Page 5 and aquire its knowledge"), null, 
				FrameType.TASK, true, false, false))
		.parent(lostPages)
		.addCriterion("page6", ConsumeItemTrigger.TriggerInstance.usedItem(RelicsItems.LOST_PAGE_5.get()))
		.save(saver, RelicsUtil.modLoc("lost_page_5"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.LOST_PAGE_6.get()), 
				Component.literal("Lost Pages 6"), Component.literal("Find Lost Page 6 and aquire its knowledge"), null, 
				FrameType.TASK, true, false, false))
		.parent(lostPages)
		.addCriterion("page", ConsumeItemTrigger.TriggerInstance.usedItem(RelicsItems.LOST_PAGE_6.get()))
		.save(saver, RelicsUtil.modLoc("lost_page_6"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.LOST_PAGE_7.get()), 
				Component.literal("Lost Pages 6"), Component.literal("Find Lost Page 7 and aquire its knowledge"), null, 
				FrameType.TASK, true, false, false))
		.parent(lostPages)
		.addCriterion("page", ConsumeItemTrigger.TriggerInstance.usedItem(RelicsItems.LOST_PAGE_7.get()))
		.save(saver, RelicsUtil.modLoc("lost_page_7"), existingFileHelper);
		
		
		//pedestals
		Advancement pedestals = Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsBlocks.SWORD_PEDESTAL_RELIC.get()), 
				Component.literal("Sword Pedestals"), Component.literal("Make your first sword pedestal"), null, 
				FrameType.TASK, true, false, false))
		.parent(root)
		.addCriterion(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsBlocks.SWORD_PEDESTAL_NORMAL.get(), RelicsBlocks.SWORD_PEDESTAL_RELIC.get(), RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS.get(), RelicsBlocks.SWORD_PEDESTAL_STONE.get(), RelicsBlocks.SWORD_PEDESTAL_TIME.get(), RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get()))
		.save(saver, RelicsUtil.modLoc("sword_pedestals"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsBlocks.SWORD_PEDESTAL_TIME.get()), 
				Component.literal("Sword Pedestal of Time"), Component.literal("Hero of Time"), null, 
				FrameType.TASK, true, false, false))
		.parent(pedestals)
		.addCriterion(hasItem,  InventoryChangeTrigger.TriggerInstance.hasItems(RelicsBlocks.SWORD_PEDESTAL_TIME.get()))
		.save(saver, RelicsUtil.modLoc("sword_pedestal_time"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get()), 
				Component.literal("Sword Pedestal of Twilight"), Component.literal("Hero of Twilight"), null, 
				FrameType.TASK, true, false, false))
		.parent(pedestals)
		.addCriterion(hasItem,  InventoryChangeTrigger.TriggerInstance.hasItems(RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get()))
		.save(saver, RelicsUtil.modLoc("sword_pedestal_twilight"), existingFileHelper);
		
		//shooting stars
		Advancement shootingStar = Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.STAR_PIECE.get()), 
				Component.literal("Shooting Star"), Component.literal("Find a shooting star and collect a star piece"), null, 
				FrameType.TASK, true, false, false))
		.parent(root)
		.addCriterion(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.STAR_PIECE.get()))
		.save(saver, RelicsUtil.modLoc("shooting_stars"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsItems.INFUSED_STAR_PIECE.get()), 
				Component.literal("Infused Star Piece"), Component.literal("successfully infuse a star piece"), null, 
				FrameType.TASK, true, false, false))
		.parent(shootingStar)
		.addCriterion(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.INFUSED_STAR_PIECE.get()))
		.save(saver, RelicsUtil.modLoc("star_piece"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsBlocks.INFUSED_STARSTONE_BLOCK.get()), 
				Component.literal("Infused Star Stone Block"), Component.literal("This seems to be more than just a fancy block"), null, 
				FrameType.TASK, true, false, false))
		.parent(shootingStar)
		.addCriterion(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsBlocks.INFUSED_STARSTONE_BLOCK.get()))
		.save(saver, RelicsUtil.modLoc("infused_star_stone_block"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsBlocks.SKYBEAM_BLOCK.get()), 
				Component.literal("Skybeam Block"), Component.literal("Skybeams, yey"), null, 
				FrameType.TASK, true, false, false))
		.parent(shootingStar)
		.addCriterion(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsBlocks.SKYBEAM_BLOCK.get()))
		.save(saver, RelicsUtil.modLoc("skybeam"), existingFileHelper);
		
		//relics
		Advancement relics = Advancement.Builder.advancement()
				.display(new DisplayInfo(PatchouliAPI.get().getBookStack(RelicsUtil.modLoc("tome")), 
						Component.literal("Obtain a Relic"), Component.literal("Obtain your first relic"), null, 
						FrameType.TASK, true, false, false))
				.parent(root)
				.addCriterion(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsItems.PHOENIX_FEATHER.get(), RelicsItems.FIRE_TABLET.get(), RelicsItems.WATER_TABLET.get(), RelicsItems.FIRE_SWORD.get(), RelicsItems.MASTER_SWORD.get(), RelicsBlocks.ODDISH_POT.get()))
				.save(saver, RelicsUtil.modLoc("infused_star_piece"), existingFileHelper);
		
		Advancement.Builder.advancement()
		.display(new DisplayInfo(new ItemStack(RelicsBlocks.ODDISH_POT.get()), 
				Component.literal("Is that a relic?"), Component.literal("Odd, it seems to be a plant???"), null, 
				FrameType.TASK, true, false, false))
		.parent(relics)
		.addCriterion(hasItem, InventoryChangeTrigger.TriggerInstance.hasItems(RelicsBlocks.ODDISH_POT.get()))
		.save(saver, RelicsUtil.modLoc("oddish"), existingFileHelper);
	}
}
