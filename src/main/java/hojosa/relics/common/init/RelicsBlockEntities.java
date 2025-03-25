package hojosa.relics.common.init;

import hojosa.relics.common.block.entity.InfusedStarstoneBlockEntity;
import hojosa.relics.common.block.entity.RetexturedSwordPedestalEntity;
import hojosa.relics.common.block.entity.SkybeamBlockEntity;
import hojosa.relics.common.block.entity.SwordPedestalBlockEntity;
import hojosa.relics.common.block.entity.SwordPedestalStoneBlockEntity;
import hojosa.relics.lib.References;
import hojosa.relics.lib.block.entity.GlintBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.BlockEntityTypeDeferredRegister;

public class RelicsBlockEntities {
	private RelicsBlockEntities() {
		// Private constructor to hide the implicit public one.
	}

	public static final BlockEntityTypeDeferredRegister BLOCK_ENTITIES = new BlockEntityTypeDeferredRegister(References.MOD_ID);

	public static final RegistryObject<BlockEntityType<RetexturedSwordPedestalEntity>> REXTURED_SWORD_PEDESTAL_BLOCK_ENTITY = BLOCK_ENTITIES.register(References.UnlocalizedName.SWORD_PEDESTAL,
			RetexturedSwordPedestalEntity::new, builder -> builder.add(RelicsBlocks.SWORD_PEDESTAL_NORMAL.get(), RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS.get()));

	public static final RegistryObject<BlockEntityType<SwordPedestalBlockEntity>> SWORD_PEDESTAL_BLOCK_ENTITY = BLOCK_ENTITIES.register(References.UnlocalizedName.SWORD_PEDESTAL_FANCY,
			SwordPedestalBlockEntity::new, builder -> builder.add(RelicsBlocks.SWORD_PEDESTAL_RELIC.get(), RelicsBlocks.SWORD_PEDESTAL_TIME.get(), RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get()));
	
	public static final RegistryObject<BlockEntityType<SwordPedestalStoneBlockEntity>> SWORD_PEDESTAL_STONE_BLOCK_ENTITY = BLOCK_ENTITIES.register(References.UnlocalizedName.SWORD_PEDESTAL_STONE,
			SwordPedestalStoneBlockEntity::new, builder -> builder.add(RelicsBlocks.SWORD_PEDESTAL_STONE.get()));
	
	public static final RegistryObject<BlockEntityType<GlintBlockEntity>> GLINT_BLOCK_ENTITY = BLOCK_ENTITIES.register(References.UnlocalizedName.GLINT_BLOCK,
			GlintBlockEntity::new, builder -> builder.add());
	
	public static final RegistryObject<BlockEntityType<InfusedStarstoneBlockEntity>> INFUSED_STARSTONE_BLOCK_ENTITY = BLOCK_ENTITIES.register(References.UnlocalizedName.INFUSED_STARSTONE_BLOCK,
			InfusedStarstoneBlockEntity::new, builder -> builder.add(RelicsBlocks.INFUSED_STARSTONE_BLOCK.get()));
	
	public static final RegistryObject<BlockEntityType<SkybeamBlockEntity>> SKYBEAM_BLOCK_ENTITY = BLOCK_ENTITIES.register(References.UnlocalizedName.SKYBEAM_BLOCK,
			SkybeamBlockEntity::new, builder -> builder.add(RelicsBlocks.SKYBEAM_BLOCK.get()));
}
