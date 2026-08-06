package hojosa.relics_of_old.common.datagen.providers;

import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.References;
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
		add(RelicsSounds.SWORD_DRAW_SOUND, definition().subtitle(getSubtitle(RelicsSounds.SWORD_DRAW_SOUND)).with(sound(RelicsSounds.SWORD_DRAW_SOUND.getId())));
		add(RelicsSounds.SWORD_PLACE_SOUND, definition().subtitle(getSubtitle(RelicsSounds.SWORD_PLACE_SOUND)).with(sound(RelicsSounds.SWORD_PLACE_SOUND.getId())));
		add(RelicsSounds.FANCY_SWORD_PLACE_SOUND, definition().subtitle(getSubtitle(RelicsSounds.FANCY_SWORD_PLACE_SOUND)).with(sound(RelicsSounds.FANCY_SWORD_PLACE_SOUND.getId())));
		add(RelicsSounds.STAR_FALL_SOUND, definition().subtitle(getSubtitle(RelicsSounds.STAR_FALL_SOUND)).with(sound(RelicsSounds.STAR_FALL_SOUND.getId())));
		add(RelicsSounds.STAR_TWINKLE_SOUND, definition().subtitle(getSubtitle(RelicsSounds.STAR_TWINKLE_SOUND)).with(sound(RelicsSounds.STAR_TWINKLE_SOUND.getId())));
		add(RelicsSounds.STAR_CAUGHT_SOUND, definition().subtitle(getSubtitle(RelicsSounds.STAR_CAUGHT_SOUND)).with(sound(RelicsSounds.STAR_CAUGHT_SOUND.getId())));
		add(RelicsSounds.HEART, definition().subtitle(getSubtitle(RelicsSounds.HEART)).with(sound(RelicsSounds.HEART.getId())));
		add(RelicsSounds.REVIVE, definition().subtitle(getSubtitle(RelicsSounds.REVIVE)).with(sound(RelicsSounds.REVIVE.getId())));
		add(RelicsSounds.EMERALD_PICKUP, definition().subtitle(getSubtitle(RelicsSounds.EMERALD_PICKUP)).with(sound(RelicsSounds.EMERALD_PICKUP.getId())));
		add(RelicsSounds.EMERALD_PIECE_PICKUP, definition().subtitle(getSubtitle(RelicsSounds.EMERALD_PIECE_PICKUP)).with(sound(RelicsSounds.EMERALD_PIECE_PICKUP.getId())));
		add(RelicsSounds.EMERALD_SHARD_PICKUP, definition().subtitle(getSubtitle(RelicsSounds.EMERALD_SHARD_PICKUP)).with(sound(RelicsSounds.EMERALD_SHARD_PICKUP.getId())));
		add(RelicsSounds.INFUSE_CHARGE, definition().subtitle(getSubtitle(RelicsSounds.INFUSE_CHARGE)).with(sound(RelicsSounds.INFUSE_CHARGE.getId())));
		add(RelicsSounds.INFUSE_SUCCESS, definition().subtitle(getSubtitle(RelicsSounds.INFUSE_SUCCESS)).with(sound(RelicsSounds.INFUSE_SUCCESS.getId())));
		add(RelicsSounds.OOT_SWORD_DRAW, definition().subtitle(getSubtitle(RelicsSounds.OOT_SWORD_DRAW)).with(sound(RelicsSounds.OOT_SWORD_DRAW.getId())));
		add(RelicsSounds.TP_SWORD_DRAW, definition().subtitle(getSubtitle(RelicsSounds.TP_SWORD_DRAW)).with(sound(RelicsSounds.TP_SWORD_DRAW.getId())));
		add(RelicsSounds.MAGIC_CRAFTING, definition().subtitle(getSubtitle(RelicsSounds.MAGIC_CRAFTING)).with(sound(RelicsSounds.MAGIC_CRAFTING.getId())));
		add(RelicsSounds.ITEM_GET, definition().subtitle(getSubtitle(RelicsSounds.ITEM_GET)).with(sound(RelicsSounds.ITEM_GET.getId())));
		add(RelicsSounds.CALTROPS_LAND, definition().subtitle(getSubtitle(RelicsSounds.CALTROPS_LAND)).with(sound(RelicsSounds.CALTROPS_LAND.getId())).with(sound(RelicsSounds.CALTROPS_LAND.getId()+"2")).with(sound(RelicsSounds.CALTROPS_LAND.getId()+"3")));
		add(RelicsSounds.CALTROPS_TAP, definition().subtitle(getSubtitle(RelicsSounds.CALTROPS_TAP)).with(sound(RelicsSounds.CALTROPS_TAP.getId())).with(sound(RelicsSounds.CALTROPS_TAP.getId()+"2")));
		add(RelicsSounds.TRANSFORM, definition().subtitle(getSubtitle(RelicsSounds.TRANSFORM)).with(sound(RelicsSounds.TRANSFORM.getId())));
		add(RelicsSounds.SPRINKLE, definition().subtitle(getSubtitle(RelicsSounds.SPRINKLE)).with(sound(RelicsSounds.SPRINKLE.getId())).with(sound(RelicsSounds.SPRINKLE.getId()+"2")));
		add(RelicsSounds.SINE, definition().subtitle(getSubtitle(RelicsSounds.SINE)).with(sound(RelicsSounds.SINE.getId())));
		add(RelicsSounds.MAGIC_BOOMERANG, definition().subtitle(getSubtitle(RelicsSounds.MAGIC_BOOMERANG)).with(sound(RelicsSounds.MAGIC_BOOMERANG.getId())));
		add(RelicsSounds.REPEL, definition().subtitle(getSubtitle(RelicsSounds.REPEL)).with(sound(RelicsSounds.REPEL.getId())));
		add(RelicsSounds.DASH, definition().subtitle(getSubtitle(RelicsSounds.DASH)).with(sound(RelicsSounds.DASH.getId())));
		add(RelicsSounds.THROW, definition().subtitle(getSubtitle(RelicsSounds.THROW)).with(sound(RelicsSounds.THROW.getId())));
		add(RelicsSounds.LIFT, definition().subtitle(getSubtitle(RelicsSounds.LIFT)).with(sound(RelicsSounds.LIFT.getId())));
		add(RelicsSounds.ESCALATE, definition().subtitle(getSubtitle(RelicsSounds.ESCALATE)).with(sound(RelicsSounds.ESCALATE.getId())));
		add(RelicsSounds.FLUTE_PLAY, definition().subtitle(getSubtitle(RelicsSounds.FLUTE_PLAY)).with(sound(RelicsSounds.FLUTE_PLAY.getId())));
		add(RelicsSounds.FLUTE_SUSTAIN, definition().subtitle(getSubtitle(RelicsSounds.FLUTE_SUSTAIN)).with(sound(RelicsSounds.FLUTE_SUSTAIN.getId())));
		add(RelicsSounds.PARTIAL_CHARGE, definition().subtitle(getSubtitle(RelicsSounds.PARTIAL_CHARGE)).with(sound(RelicsSounds.PARTIAL_CHARGE.getId())));
		add(RelicsSounds.FULL_CHARGE, definition().subtitle(getSubtitle(RelicsSounds.FULL_CHARGE)).with(sound(RelicsSounds.FULL_CHARGE.getId())));
	}

	private String getSubtitle(RegistryObject<SoundEvent> sound) {
		return References.MOD_ID + ".subtitle." + sound.getId().getPath();
	}

	@Override
	public String getName() {
		return "Relics Sounds";
	}
}