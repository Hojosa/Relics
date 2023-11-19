package hojosa.relics.common.init;

import hojosa.relics.common.block.entity.SwordPedestalBlockEntity;
import hojosa.relics.lib.References;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RelicsBlockEntities {
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
			ForgeRegistries.BLOCK_ENTITIES, References.MODID);

	public static final RegistryObject<BlockEntityType<SwordPedestalBlockEntity>> SWORD_PEDESTAL_BLOCK_ENTITY = BLOCK_ENTITIES.register(
			References.UnlocalizedName.SWORD_PEDESTAL, () -> BlockEntityType.Builder.of(SwordPedestalBlockEntity::new, RelicsBlocks.SWORD_PEDESTAL.get(), RelicsBlocks.SWORD_PEDESTAL_OOT.get()
					).build(null));
	
//	public static final RegistryObject<BlockEntityType<SwordPedestalBlockEntity>> SWORD_PEDESTAL_OOT_BLOCK_ENTITY = BLOCK_ENTITIES.register(
//			References.UnlocalizedName.SWORD_PEDESTAL_OOT, () -> BlockEntityType.Builder.of(SwordPedestalBlockEntity::new, RelicsBlocks.SWORD_PEDESTAL_OOT.get()
//					).build(null));
}
