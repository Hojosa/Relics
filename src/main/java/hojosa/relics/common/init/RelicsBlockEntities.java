package hojosa.relics.common.init;

import hojosa.relics.common.block.entity.SwordPedestalBlockEntityNew;
import hojosa.relics.lib.References;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RelicsBlockEntities {
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
			ForgeRegistries.BLOCK_ENTITY_TYPES, References.MODID);

//	public static final RegistryObject<BlockEntityType<SwordPedestalBlockEntity>> SWORD_PEDESTAL_BLOCK_ENTITY = BLOCK_ENTITIES.register(
//			References.UnlocalizedName.SWORD_PEDESTAL, () -> BlockEntityType.Builder.of(SwordPedestalBlockEntity::new, RelicsBlocks.SWORD_PEDESTAL_TIME.get(), RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get()
//					).build(null));
	
	public static final RegistryObject<BlockEntityType<SwordPedestalBlockEntityNew>> SWORD_PEDESTAL_BLOCK_ENTITY = BLOCK_ENTITIES.register(
			References.UnlocalizedName.SWORD_PEDESTAL, () -> BlockEntityType.Builder.of(SwordPedestalBlockEntityNew::new, RelicsBlocks.SWORD_PEDESTAL.get(), RelicsBlocks.SWORD_PEDESTAL_TIME.get(), RelicsBlocks.SWORD_PEDESTAL_TWILIGHT.get()
					).build(null));
}
