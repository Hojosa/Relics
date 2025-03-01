package hojosa.relics.common.init;

import hojosa.relics.lib.References;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RelicsSounds {
	
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
	
    private static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(References.MOD_ID, name)));
    }
}
