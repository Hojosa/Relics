package hojosa.relics_of_old.common.datagen.providers;

import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
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
		withExistingParent(RelicsBlocks.MYSTIC_SHRUB);
		withExistingParent(RelicsBlocks.CLAY_JAR);
		withExistingParent(RelicsBlocks.BOOST_PLATE);
		withExistingParent(RelicsBlocks.SUGAR_CUBE);
		withExistingParent(RelicsBlocks.BOMB_FLOWER);
		withExistingParent(RelicsBlocks.STARRY_SAND);
		withExistingParent(RelicsBlocks.STRUCK_DIRT);
		withExistingParent(RelicsBlocks.STRUCK_SAND);
		withExistingParent(RelicsBlocks.STARWELL_FRAME);
		withExistingParent(RelicsBlocks.STARWELL_CORE);
		withExistingParent(RelicsBlocks.SKY_LENS);
		withExistingParent(RelicsBlocks.RITUAL_LOCUS);

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
		itemWithOverride(RelicsItems.MYSTIC_SEED, "thundering", "charged");
		basicItem(RelicsBlocks.CALTROPS.asItem());
		basicItem(RelicsItems.WOODEN_BOOMERANG);
		basicItem(RelicsItems.MAGIC_BOOMERANG);
		basicItem(RelicsItems.STARSTEEL_BOOMERANG);
		basicItem(RelicsItems.MILK_CHOCOLATE);
		basicItem(RelicsItems.EARTH_MEDALLION);
		basicItemWithOtherTexture(RelicsItems.EMPTY_EARTH_MEDALLION, RelicsItems.EARTH_MEDALLION.getId());
		basicItem(RelicsItems.FIRE_MEDALLION);
		basicItemWithOtherTexture(RelicsItems.EMPTY_FIRE_MEDALLION, RelicsItems.FIRE_MEDALLION.getId());
		basicItem(RelicsItems.WIND_MEDALLION);
		basicItemWithOtherTexture(RelicsItems.EMPTY_WIND_MEDALLION, RelicsItems.WIND_MEDALLION.getId());
		basicItem(RelicsItems.ENDER_MEDALLION);
		basicItemWithOtherTexture(RelicsItems.EMPTY_ENDER_MEDALLION, RelicsItems.ENDER_MEDALLION.getId());
		basicItem(RelicsItems.FOREST_MEDALLION);
		basicItemWithOtherTexture(RelicsItems.EMPTY_FOREST_MEDALLION, RelicsItems.FOREST_MEDALLION.getId());
		basicItem(RelicsItems.MAGIC_MEDALLION);
		basicItemWithOtherTexture(RelicsItems.EMPTY_MAGIC_MEDALLION, RelicsItems.MAGIC_MEDALLION.getId());
		basicItem(RelicsItems.ICE_MEDALLION);
		basicItemWithOtherTexture(RelicsItems.EMPTY_ICE_MEDALLION, RelicsItems.ICE_MEDALLION.getId());
		basicItem(RelicsItems.WATER_MEDALLION);
		basicItemWithOtherTexture(RelicsItems.EMPTY_WATER_MEDALLION, RelicsItems.WATER_MEDALLION.getId());
		basicItem(RelicsItems.SHADOW_MEDALLION);
		basicItemWithOtherTexture(RelicsItems.EMPTY_SHADOW_MEDALLION, RelicsItems.SHADOW_MEDALLION.getId());
		basicItem(RelicsItems.LIGHTNING_MEDALLION);
		basicItemWithOtherTexture(RelicsItems.EMPTY_LIGHTNING_MEDALLION, RelicsItems.LIGHTNING_MEDALLION.getId());
		basicItem(RelicsItems.STAR_MEDALLION);
		basicItemWithOtherTexture(RelicsItems.EMPTY_STAR_MEDALLION, RelicsItems.STAR_MEDALLION.getId());
		basicItem(RelicsItems.PYRO_AMULET);
		basicItem(RelicsItems.GEO_AMULET);
		basicItem(RelicsItems.AERO_AMULET);
		basicItem(RelicsItems.END_AMULET);
		basicItem(RelicsItems.WHIRLWIND_BOOTS);
		basicItem(RelicsItems.TITAN_BAND);
		basicItem(RelicsItems.HEADBAND_OF_VALOR);
		basicItem(RelicsItems.REED_PIPES);
		basicItem(RelicsItems.SLIME_SWORD);
		basicItem(RelicsItems.CAPTURE_EGG);
		basicItem(RelicsItems.ENDER_SWORD);
		basicItem(RelicsItems.ROCK_CANDY_REDSTONE);
		basicItem(RelicsItems.ROCK_CANDY_LAPIS);
		basicItem(RelicsItems.ROCK_CANDY_EMERALD);
		basicItem(RelicsItems.ROCK_CANDY_DIAMOND);
		basicItem(RelicsItems.BOMB);
		basicItem(RelicsItems.BOMB_ARROW);
		basicItem(RelicsItems.BOMB_BAG);
		basicItem(RelicsItems.FULGURITE);
		basicItem(RelicsItems.TUNING_FORK);
		basicItem(RelicsItems.STARGLASS_LUMP);
		basicItem(RelicsItems.STARSTEEL_INGOT);
		basicItem(RelicsItems.STARSTEEL_DUST);
		basicItem(RelicsItems.AZURITE_DUST);
		basicItem(RelicsItems.AZURITE_DOT);
		basicItem(RelicsItems.ABSTRACTION_GEL);
		basicItem(RelicsItems.DIMENSIONAL_CATALYST);
		// Nucleus models (ame layered textures, tinted at runtime)
		nucleusItem(RelicsItems.NUCLEUS_FIRE);
		nucleusItem(RelicsItems.NUCLEUS_ICE);
		nucleusItem(RelicsItems.NUCLEUS_LIGHTNING);
		nucleusItem(RelicsItems.NUCLEUS_CUT);
		nucleusItem(RelicsItems.NUCLEUS_SKY);
		nucleusItem(RelicsItems.NUCLEUS_SUN);
		nucleusItem(RelicsItems.NUCLEUS_NAVIGATE);
		nucleusItem(RelicsItems.NUCLEUS_DARK);
		nucleusItem(RelicsItems.NUCLEUS_STAR);
		nucleusItem(RelicsItems.NUCLEUS_HEALTH);
		nucleusItem(RelicsItems.NUCLEUS_WEAPON);
		nucleusItem(RelicsItems.NUCLEUS_WEALTH);
		// Gem models (same layered textures, tinted at runtime)
		gemItem(RelicsItems.GEM_FIRE);
		gemItem(RelicsItems.GEM_ICE);
		gemItem(RelicsItems.GEM_LIGHTNING);
		gemItem(RelicsItems.GEM_CUT);
		gemItem(RelicsItems.GEM_SKY);
		gemItem(RelicsItems.GEM_SUN);
		gemItem(RelicsItems.GEM_NAVIGATE);
		gemItem(RelicsItems.GEM_DARK);
		gemItem(RelicsItems.GEM_STAR);
		gemItem(RelicsItems.GEM_HEALTH);
		gemItem(RelicsItems.GEM_WEAPON);
		gemItem(RelicsItems.GEM_WEALTH);
		basicItem(RelicsItems.MUSIC_DISC_DRAGONDOT);
		basicItem(RelicsItems.AZURITE_SPHERE);
		basicItem(RelicsItems.STARGLASS_SHELL);
		basicItem(RelicsItems.WATER_SHELL);
		basicItem(RelicsItems.LAVA_SHELL);
		basicItem(RelicsItems.BLAST_SHELL);
		sparkleOrb(RelicsItems.GLITTERING_ORB);
		sparkleOrb(RelicsItems.BURNING_ORB);
		sparkleOrb(RelicsItems.FREEZING_ORB);
		sparkleOrb(RelicsItems.SHOCKING_ORB);
		// Rings — gold base
		gemRingItem(RelicsItems.SPEED_RING, "gold_ring");
		gemRingItem(RelicsItems.CONVECTION_RING, "gold_ring");
		gemRingItem(RelicsItems.SOFT_FALL_RING, "gold_ring");
		gemRingItem(RelicsItems.COLD_FEET_RING, "gold_ring");
		gemRingItem(RelicsItems.THIEF_RING, "gold_ring");
		// Rings — iron base
		gemRingItem(RelicsItems.MAGE_RING, "iron_ring");
		gemRingItem(RelicsItems.WARRIOR_RING, "iron_ring");
		// Rings — wood base
		gemRingItem(RelicsItems.FORTUNE_RING, "wooden_ring");
		gemRingItem(RelicsItems.ARROWFIND_RING, "wooden_ring");
		gemRingItem(RelicsItems.AZUREFIND_RING, "wooden_ring");
		// Rings — starglass base (tinted via color handler)
		gemRingItem(RelicsItems.WISH_RING, "iron_ring");
		gemRingItem(RelicsItems.RESONANCE_RING, "iron_ring");
		// Rings — no gem
		plainRingItem(RelicsItems.PLAIN_RING, "gold_ring");
		plainRingItem(RelicsItems.PHOENIX_RING, "gold_ring");
		basicItem(RelicsItems.PHOENIX_CHARM);
		basicItem(RelicsItems.BLAST_CHARM);
		basicItem(RelicsItems.FEATHER_CHARM);

		// magic mirror model
		// base model that contains the base transform settings for the model
		ModelFile magic_mirror_base = getBuilder("magic_mirror_base").parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", modLoc("item/magic_mirror")).transforms()
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, 90, 55).translation(0, 5, 2).scale(0.85f, 0.85f, 1f).end().transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0, 90, -55)
				.translation(0, 5, 2).scale(0.85f, 0.85f, 1f).end().transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, -90, 100).translation(1.13f, 3.2f, 1.13f).scale(0.85f, 0.85f, 1f).end()
				.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, -90, 25).translation(1.13f, 3.2f, 1.13f).scale(0.85f, 0.85f, 1f).end().end();

		// models for the inUse animation
		for (int i = 0; i < 4; i++) {
			getBuilder("magic_mirror_use_" + i).parent(magic_mirror_base).texture("layer0", modLoc("item/magic_mirror_use_" + i)).transforms().transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, 0, 65)
					.translation(-3, 5, 2).scale(0.85f, 0.85f, 1f).end().transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0, 0, -28).translation(-3, 5, 2).scale(0.85f, 0.85f, 1f).end()
					.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(-12f, -12f, 103f).translation(-4f, 2f, -2.7f).scale(0.85f, 0.85f, 1f).end().transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
					.rotation(-11f, -12f, 11f).translation(-2f, 2f, -2.75f).scale(0.85f, 0.85f, 1f).end().end();
		}

		// lit model
		getBuilder("magic_mirror_lit").parent(magic_mirror_base).texture("layer0", modLoc("item/magic_mirror_lit"));

		// normal model with all overrides
		getBuilder(RelicsItems.MAGIC_MIRROR.getId().toString()).parent(magic_mirror_base)
//			.texture("layer0", modLoc("item/magic_mirror"))
				.override().predicate(modLoc("active"), 1.0f).model(new ModelFile.UncheckedModelFile(modLoc("item/magic_mirror_lit"))).end().override().predicate(modLoc("using"), 0.25f)
				.model(new ModelFile.UncheckedModelFile(modLoc("item/magic_mirror_use_0"))).end().override().predicate(modLoc("using"), 0.5f).model(new ModelFile.UncheckedModelFile(modLoc("item/magic_mirror_use_1")))
				.end().override().predicate(modLoc("using"), 0.75f).model(new ModelFile.UncheckedModelFile(modLoc("item/magic_mirror_use_2"))).end().override().predicate(modLoc("using"), 1.0f)
				.model(new ModelFile.UncheckedModelFile(modLoc("item/magic_mirror_use_3"))).end();

		// flawless mirror
		getBuilder(RelicsItems.FLAWLESS_MAGIC_MIRROR.getId().toString()).parent(magic_mirror_base)
//	      .texture("layer0", modLoc("item/magic_mirror"))
				.override().predicate(modLoc("active"), 1.0f).model(new ModelFile.UncheckedModelFile(modLoc("item/magic_mirror_lit"))).end().override().predicate(modLoc("using"), 0.25f)
				.model(new ModelFile.UncheckedModelFile(modLoc("item/magic_mirror_use_0"))).end().override().predicate(modLoc("using"), 0.5f).model(new ModelFile.UncheckedModelFile(modLoc("item/magic_mirror_use_1")))
				.end().override().predicate(modLoc("using"), 0.75f).model(new ModelFile.UncheckedModelFile(modLoc("item/magic_mirror_use_2"))).end().override().predicate(modLoc("using"), 1.0f)
				.model(new ModelFile.UncheckedModelFile(modLoc("item/magic_mirror_use_3"))).end();
	}

	private ItemModelBuilder infusedItem(RegistryObject<RelicsItem> item, RegistryObject<RelicsItem> parent) {
		return getBuilder(item.getId().toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0",
				ResourceLocation.fromNamespaceAndPath(item.getId().getNamespace(), "item/" + parent.getId().getPath()));
	}

	private void nucleusItem(RegistryObject<? extends Item> item) {
		getBuilder(item.getId().toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", modLoc("item/nucleus_base")).texture("layer1", modLoc("item/nucleus_core"));
	}

	private void sparkleOrb(RegistryObject<? extends Item> item) {
		getBuilder(item.getId().toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", modLoc("item/" + item.getId().getPath())).texture("layer1", modLoc("item/orb_sparkle"));
	}

	private void gemRingItem(RegistryObject<? extends Item> item, String ringBase) {
		getBuilder(item.getId().toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", modLoc("item/" + ringBase)).texture("layer1", modLoc("item/ring_orb_base"))
				.texture("layer2", modLoc("item/ring_orb_fill")).texture("layer3", modLoc("item/ring_orb_overlay"));
	}

	private void plainRingItem(RegistryObject<? extends Item> item, String ringBase) {
		getBuilder(item.getId().toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", modLoc("item/" + ringBase));
	}

	private void gemItem(RegistryObject<? extends Item> item) {
		getBuilder(item.getId().toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", modLoc("item/gem_base")).texture("layer1", modLoc("item/gem_fill")).texture("layer2",
				modLoc("item/gem_overlay"));
	}

	private void withExistingParent(ItemObject<Block> itemRef) {
		withExistingParent(itemRef.getId().toString(), modLoc("block/" + itemRef.getId().getPath()));
	}

	private void basicItem(RegistryObject<? extends Item> itemRef) {
		basicItem(itemRef.getId());
	}

	private ItemModelBuilder basicItemWithOtherTexture(RegistryObject<? extends Item> item, ResourceLocation textureName) {
		return getBuilder(item.getId().toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0",
				ResourceLocation.fromNamespaceAndPath(item.getId().getNamespace(), "item/" + textureName.getPath()));
	}

	private void itemWithOverride(RegistryObject<? extends Item> itemRef, String predicate, String variantSuffix) {
		// variant model
		getBuilder(itemRef.getId().getPath() + "_" + variantSuffix).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", modLoc("item/" + itemRef.getId().getPath() + "_" + variantSuffix));

		// base model with override
		getBuilder(itemRef.getId().toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", modLoc("item/" + itemRef.getId().getPath())).override().predicate(modLoc(predicate), 1.0f)
				.model(new ModelFile.UncheckedModelFile(modLoc("item/" + itemRef.getId().getPath() + "_" + variantSuffix))).end();
	}
}