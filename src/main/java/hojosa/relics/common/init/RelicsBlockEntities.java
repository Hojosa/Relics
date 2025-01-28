package hojosa.relics.common.init;

import hojosa.relics.common.block.entity.SwordPedestalBlockEntity;
import hojosa.relics.lib.References;
import hojosa.relics.lib.block.entity.RetexturedSwordPedestalEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.BlockEntityTypeDeferredRegister;

public class RelicsBlockEntities {

	public static final BlockEntityTypeDeferredRegister BLOCK_ENTITIES = new BlockEntityTypeDeferredRegister(References.MOD_ID);

	public static final RegistryObject<BlockEntityType<RetexturedSwordPedestalEntity>> REXTURED_SWORD_PEDESTAL_BLOCK_ENTITY = BLOCK_ENTITIES.register(References.UnlocalizedName.SWORD_PEDESTAL,
			RetexturedSwordPedestalEntity::new, builder -> builder.add(RelicsBlocks.SWORD_PEDESTAL_NORMAL.get(), RelicsBlocks.SWORD_PEDESTAL_RELIC_VARIANTS.get()));

	public static final RegistryObject<BlockEntityType<SwordPedestalBlockEntity>> SWORD_PEDESTAL_BLOCK_ENTITY = BLOCK_ENTITIES.register(References.UnlocalizedName.SWORD_PEDESTAL_FANCY,
			SwordPedestalBlockEntity::new, builder -> builder.add(RelicsBlocks.SWORD_PEDESTAL_RELIC.get(), RelicsBlocks.SWORD_PEDESTAL_TIME.get(), RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get()));
}
