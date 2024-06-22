package hojosa.relics.common.init;

import hojosa.relics.lib.References;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RelicsSounds {
	
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, References.MODID);

	public static final RegistryObject<SoundEvent>SWORD_PLACE_SOUND = registerSound(
			References.UnlocalizedName.SWORD_PLACE_SOUND
	);
	
    private static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(References.MODID, name)));
    }
	
}
