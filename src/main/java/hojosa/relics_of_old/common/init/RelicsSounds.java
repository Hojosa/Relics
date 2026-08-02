package hojosa.relics_of_old.common.init;

import hojosa.relics_of_old.lib.References;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsSounds {

	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, References.MOD_ID);

	public static final RegistryObject<SoundEvent> SWORD_PLACE_SOUND = registerSound(References.SoundName.SWORD_PLACE_SOUND);
	public static final RegistryObject<SoundEvent> SWORD_DRAW_SOUND = registerSound(References.SoundName.SWORD_DRAW_SOUND);
	public static final RegistryObject<SoundEvent> FANCY_SWORD_PLACE_SOUND = registerSound(References.SoundName.FANCY_SWORD_PLACE_SOUND);
	public static final RegistryObject<SoundEvent> STAR_FALL_SOUND = registerSound(References.SoundName.STAR_FALL_SOUND);
	public static final RegistryObject<SoundEvent> STAR_TWINKLE_SOUND = registerSound(References.SoundName.STAR_TWINKLE_SOUND);
	public static final RegistryObject<SoundEvent> STAR_CAUGHT_SOUND = registerSound(References.SoundName.STAR_CAUGHT_SOUND);
	public static final RegistryObject<SoundEvent> HEART = registerSound(References.UnlocalizedName.HEART);
	public static final RegistryObject<SoundEvent> REVIVE = registerSound(References.SoundName.REVIVE);
	public static final RegistryObject<SoundEvent> EMERALD_PICKUP = registerSound(References.SoundName.EMERALD_PICKUP);
	public static final RegistryObject<SoundEvent> EMERALD_PIECE_PICKUP = registerSound(References.SoundName.EMERALD_PIECE_PICKUP);
	public static final RegistryObject<SoundEvent> EMERALD_SHARD_PICKUP = registerSound(References.SoundName.EMERALD_SHARD_PICKUP);
	public static final RegistryObject<SoundEvent> INFUSE_CHARGE = registerSound(References.SoundName.INFUSE_CHARGE);
	public static final RegistryObject<SoundEvent> INFUSE_SUCCESS = registerSound(References.SoundName.INFUSE_SUCCESS);
	public static final RegistryObject<SoundEvent> OOT_SWORD_DRAW = registerSound(References.SoundName.OOT_SWORD_DRAW);
	public static final RegistryObject<SoundEvent> TP_SWORD_DRAW = registerSound(References.SoundName.TP_SWORD_DRAW);
	public static final RegistryObject<SoundEvent> MAGIC_CRAFTING = registerSound(References.SoundName.MAGIC_CRAFTING);
	public static final RegistryObject<SoundEvent> ITEM_GET = registerSound(References.SoundName.ITEM_GET);
	public static final RegistryObject<SoundEvent> CALTROPS_LAND = registerSound(References.SoundName.CALTROPS_LAND);
	public static final RegistryObject<SoundEvent> CALTROPS_TAP = registerSound(References.SoundName.CALTROPS_TAP);
	public static final RegistryObject<SoundEvent> TRANSFORM = registerSound(References.SoundName.TRANSFORM);;
	public static final RegistryObject<SoundEvent> SPRINKLE = registerSound(References.SoundName.SPRINKLE);
	public static final RegistryObject<SoundEvent> SINE = registerSound(References.SoundName.SINE);
	public static final RegistryObject<SoundEvent> MAGIC_BOOMERANG = registerSound(References.SoundName.MAGIC_BOOMERANG);
	public static final RegistryObject<SoundEvent> REPEL = registerSound(References.SoundName.REPEL);

	private static RegistryObject<SoundEvent> registerSound(String name) {
		return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(References.MOD_ID, name)));
	}
}