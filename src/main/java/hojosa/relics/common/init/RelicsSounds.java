package hojosa.relics.common.init;

import hojosa.relics.lib.References;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RelicsSounds {
	private RelicsSounds() {
		// Private constructor to hide the implicit public one.
	}
	
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, References.MOD_ID);

	public static final RegistryObject<SoundEvent>SWORD_PLACE_SOUND = registerSound(
			References.UnlocalizedName.SWORD_PLACE_SOUND
	);
	
	public static final RegistryObject<SoundEvent>SWORD_DRAW_SOUND = registerSound(
			References.UnlocalizedName.SWORD_DRAW_SOUND
	);
	
	public static final RegistryObject<SoundEvent>FANCY_SWORD_PLACE_SOUND = registerSound(
			References.UnlocalizedName.FANCY_SWORD_PLACE_SOUND
	);
	
	public static final RegistryObject<SoundEvent>STAR_FALL_SOUND = registerSound(
			References.UnlocalizedName.STAR_FALL_SOUND
	);
	
	public static final RegistryObject<SoundEvent>STAR_TWINKLE_SOUND = registerSound(
			References.UnlocalizedName.STAR_TWINKLE_SOUND
	);
	
	public static final RegistryObject<SoundEvent>STAR_CAUGHT_SOUND = registerSound(
			References.UnlocalizedName.STAR_CAUGHT_SOUND
	);
	
	public static final RegistryObject<SoundEvent>HEART = registerSound(
			References.UnlocalizedName.HEART
	);
	
	public static final RegistryObject<SoundEvent>REVIVE = registerSound(
			References.UnlocalizedName.REVIVE
	);
	
	public static final RegistryObject<SoundEvent>EMERALD_PICKUP = registerSound(
			References.UnlocalizedName.EMERALD_PICKUP
	);
	
	public static final RegistryObject<SoundEvent>INFUSE_CHARGE = registerSound(
			References.UnlocalizedName.INFUSE_CHARGE
	);
	
	public static final RegistryObject<SoundEvent>INFUSE_SUCCESS = registerSound(
			References.UnlocalizedName.INFUSE_SUCCESS
	);
	
	public static final RegistryObject<SoundEvent>OOT_SWORD_DRAW = registerSound(
			References.UnlocalizedName.OOT_SWORD_DRAW
	);
	
	public static final RegistryObject<SoundEvent>TP_SWORD_DRAW = registerSound(
			References.UnlocalizedName.TP_SWORD_DRAW
	);
	
    private static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(References.MOD_ID, name)));
    }
}
