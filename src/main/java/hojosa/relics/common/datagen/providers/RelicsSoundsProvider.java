package hojosa.relics.common.datagen.providers;

import hojosa.relics.common.init.RelicsSounds;
import hojosa.relics.lib.References;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;

public class RelicsSoundsProvider extends SoundDefinitionsProvider {

	public RelicsSoundsProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, References.MOD_ID, helper);
	}

	@Override
	public void registerSounds() {
		add(RelicsSounds.SWORD_DRAW_SOUND, definition()
				.subtitle(getSubtitle(RelicsSounds.SWORD_DRAW_SOUND))
				.with(sound(RelicsSounds.SWORD_DRAW_SOUND.getId())));
		add(RelicsSounds.SWORD_PLACE_SOUND, definition()
				.subtitle(getSubtitle(RelicsSounds.SWORD_PLACE_SOUND))
				.with(sound(RelicsSounds.SWORD_PLACE_SOUND.getId())));
		add(RelicsSounds.FANCY_SWORD_PLACE_SOUND, definition()
				.subtitle(getSubtitle(RelicsSounds.FANCY_SWORD_PLACE_SOUND))
				.with(sound(RelicsSounds.FANCY_SWORD_PLACE_SOUND.getId())));
		add(RelicsSounds.STAR_FALL_SOUND, definition()
				.subtitle(getSubtitle(RelicsSounds.STAR_FALL_SOUND))
				.with(sound(RelicsSounds.STAR_FALL_SOUND.getId())));
		add(RelicsSounds.STAR_TWINKLE_SOUND, definition()
				.subtitle(getSubtitle(RelicsSounds.STAR_TWINKLE_SOUND))
				.with(sound(RelicsSounds.STAR_TWINKLE_SOUND.getId())));
		add(RelicsSounds.STAR_CAUGHT_SOUND, definition()
				.subtitle(getSubtitle(RelicsSounds.STAR_CAUGHT_SOUND))
				.with(sound(RelicsSounds.STAR_CAUGHT_SOUND.getId())));
		add(RelicsSounds.HEART, definition()
				.subtitle(getSubtitle(RelicsSounds.HEART))
				.with(sound(RelicsSounds.HEART.getId())));
		add(RelicsSounds.REVIVE, definition()
				.subtitle(getSubtitle(RelicsSounds.REVIVE))
				.with(sound(RelicsSounds.REVIVE.getId())));
		add(RelicsSounds.EMERALD_PICKUP, definition()
				.subtitle(getSubtitle(RelicsSounds.EMERALD_PICKUP))
				.with(sound(RelicsSounds.EMERALD_PICKUP.getId())));
	}
	
	private String getSubtitle(RegistryObject<SoundEvent> sound) {
		return References.MOD_ID + ".subtitle." + sound.getId().getPath();
	}
	
	@Override
	public String getName() {
		return "Relics Sounds";
	}
}
