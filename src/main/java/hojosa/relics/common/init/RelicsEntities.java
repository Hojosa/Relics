package hojosa.relics.common.init;

import hojosa.relics.common.entity.FallingStarEntity;
import hojosa.relics.lib.References;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RelicsEntities {
	private RelicsEntities() {
		// Private constructor to hide the implicit public one.
	}

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, References.MOD_ID);

	public static final RegistryObject<EntityType<FallingStarEntity>> FALLING_STAR = ENTITY_TYPES.register(References.UnlocalizedName.FALLING_STAR,
			() -> EntityType.Builder.<FallingStarEntity>of(FallingStarEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(References.UnlocalizedName.FALLING_STAR));

}