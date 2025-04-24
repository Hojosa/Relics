package hojosa.relics.common.init;

import hojosa.relics.common.entity.FallingStarEntity;
import hojosa.relics.common.entity.StarBeamEntity;
import hojosa.relics.lib.References;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsEntities {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, References.MOD_ID);

	public static final RegistryObject<EntityType<FallingStarEntity>> FALLING_STAR = ENTITY_TYPES.register(References.UnlocalizedName.FALLING_STAR,
			() -> EntityType.Builder.<FallingStarEntity>of(FallingStarEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(References.UnlocalizedName.FALLING_STAR));

	
	public static final RegistryObject<EntityType<StarBeamEntity>> STARBEAM = ENTITY_TYPES.register(References.UnlocalizedName.STARBEAM,
			() -> EntityType.Builder.<StarBeamEntity>of(StarBeamEntity::new, MobCategory.MISC).sized(0.1f, 0.1f).clientTrackingRange(16).build(References.UnlocalizedName.STARBEAM));

}