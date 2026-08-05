package hojosa.relics_of_old.common.init;

import hojosa.relics_of_old.common.entity.CaptureEggEntity;
import hojosa.relics_of_old.common.entity.FallingStarEntity;
import hojosa.relics_of_old.common.entity.MagicBoomerangEntity;
import hojosa.relics_of_old.common.entity.MedallionEntity;
import hojosa.relics_of_old.common.entity.StarBeamEntity;
import hojosa.relics_of_old.common.entity.attacks.ArrowStormEntity;
import hojosa.relics_of_old.common.entity.attacks.EnderBombEntity;
import hojosa.relics_of_old.common.entity.attacks.FireblastEntity;
import hojosa.relics_of_old.common.entity.attacks.QuakeEntity;
import hojosa.relics_of_old.lib.References;
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

	public static final RegistryObject<EntityType<MagicBoomerangEntity>> MAGIC_BOOMERANG = ENTITY_TYPES.register(References.UnlocalizedName.MAGIC_BOOMERANG,
			() -> EntityType.Builder.<MagicBoomerangEntity>of(MagicBoomerangEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(16).build(References.UnlocalizedName.MAGIC_BOOMERANG));

	public static final RegistryObject<EntityType<MedallionEntity>> MEDALLION = ENTITY_TYPES.register(References.UnlocalizedName.MEDALLION,
			() -> EntityType.Builder.<MedallionEntity>of(MedallionEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(16).build(References.UnlocalizedName.MEDALLION));
	
	public static final RegistryObject<EntityType<FireblastEntity>> FIREBLAST = ENTITY_TYPES.register(References.UnlocalizedName.FIRE_BLAST,
			() -> EntityType.Builder.<FireblastEntity>of(FireblastEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(16).build(References.UnlocalizedName.FIRE_BLAST));

	public static final RegistryObject<EntityType<ArrowStormEntity>> ARROW_STORM = ENTITY_TYPES.register(References.UnlocalizedName.ARROW_STORM,
			() -> EntityType.Builder.<ArrowStormEntity>of(ArrowStormEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(16).build(References.UnlocalizedName.ARROW_STORM));
	
	public static final RegistryObject<EntityType<QuakeEntity>> QUAKE = ENTITY_TYPES.register(References.UnlocalizedName.QUAKE,
			() -> EntityType.Builder.<QuakeEntity>of(QuakeEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(16).build(References.UnlocalizedName.ARROW_STORM));
	
	public static final RegistryObject<EntityType<EnderBombEntity>> ENDER_BOMB = ENTITY_TYPES.register(References.UnlocalizedName.ENDER_BOMB,
			() -> EntityType.Builder.<EnderBombEntity>of(EnderBombEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(16).build(References.UnlocalizedName.ARROW_STORM));

	public static final RegistryObject<EntityType<CaptureEggEntity>> CAPTURE_EGG = ENTITY_TYPES.register(References.UnlocalizedName.CAPTURE_EGG,
			() -> EntityType.Builder.<CaptureEggEntity>of(CaptureEggEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(16).build(References.UnlocalizedName.CAPTURE_EGG));
}