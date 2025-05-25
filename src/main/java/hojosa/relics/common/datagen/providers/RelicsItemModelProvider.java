package hojosa.relics.common.datagen.providers;

import hojosa.relics.common.init.RelicsBlocks;
import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.common.item.RelicsItem;
import hojosa.relics.lib.References;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.object.ItemObject;

public class RelicsItemModelProvider extends ItemModelProvider {

	public RelicsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, References.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		withExistingParent(RelicsBlocks.SKYBEAM_BLOCK);
		withExistingParent(RelicsBlocks.STARSTONE_BLOCK);
		withExistingParent(RelicsBlocks.INFUSED_STARSTONE_BLOCK);
		withExistingParent(RelicsBlocks.STARBEAM_TORCH);
		basicItem(RelicsItems.STAR_PIECE);
		basicItem(RelicsItems.STAR_DUST);
		basicItem(RelicsItems.STAR_STONE);
		basicItem(RelicsItems.EMERALD_PIECE);
		basicItem(RelicsItems.EMERALD_SHARD);
		basicItem(RelicsItems.FIRE_SWORD);
		basicItem(RelicsItems.MASTER_SWORD);
		basicItem(RelicsItems.FIRE_TABLET);
		basicItem(RelicsItems.WATER_TABLET);
		basicItem(RelicsItems.PHOENIX_FEATHER);
		basicItem(RelicsItems.HEART);
		basicItem(RelicsItems.TOTEM_DUST);
		infusedItem(RelicsItems.INFUSED_STAR_PIECE, RelicsItems.STAR_PIECE);
		infusedItem(RelicsItems.INFUSED_STAR_DUST, RelicsItems.STAR_DUST);
		infusedItem(RelicsItems.INFUSED_STAR_STONE, RelicsItems.STAR_STONE);
		basicItem(RelicsItems.LOST_PAGE_1);
		basicItem(RelicsItems.LOST_PAGE_2);
		basicItem(RelicsItems.LOST_PAGE_3);
		basicItem(RelicsItems.LOST_PAGE_4);
		basicItem(RelicsItems.LOST_PAGE_5);
		basicItem(RelicsItems.LOST_PAGE_6);
		basicItem(RelicsItems.LOST_PAGE_7);
		basicItem(RelicsItems.MAGIC_POWDER);
		basicItem(RelicsItems.BLANK_TABLET);
	}

	private ItemModelBuilder infusedItem(RegistryObject<RelicsItem> item, RegistryObject<RelicsItem> parent) {
		return getBuilder(item.getId().toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0",
				new ResourceLocation(item.getId().getNamespace(), "item/" + parent.getId().getPath()));
	}

	private void withExistingParent(ItemObject<Block> itemRef) {
		withExistingParent(itemRef.getId().toString(), modLoc("block/" + itemRef.getId().getPath()));
	}

	private void basicItem(RegistryObject<? extends Item> itemRef) {
		basicItem(itemRef.getId());
	}
}
