package hojosa.relics_of_old.common.player;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class PlayerGlideData {
	// Y ceiling for energy budget — 0 means not gliding
	@Getter
	@Setter
	private float glideCharge = 0.0f;
	// previous tick's glideCharge, used for boost particle threshold
	@Getter
	@Setter
	private float lastGlideCharge = 0.0f;
	// glide ratio: 4.0 for azure, 7.0 for phoenix
	@Getter
	@Setter
	private float glideRatio = 4.0f;
	@Getter
	@Setter
	private int skylensTagCharge = 0;

	public static PlayerGlideData get(Player player) {
		return player.getCapability(PlayerGlideDataProvider.PLAYER_GLIDE_DATA).orElse(null);
	}

	public boolean isGliding() {
		return glideCharge > 0.0f;
	}

	public void stopGliding() {
		this.glideCharge = 0.0f;
		this.lastGlideCharge = 0.0f;
	}

	public void tickSkylensCharge() {
		if (skylensTagCharge > 0) {
			skylensTagCharge--;
		}
	}

	public void copyFrom(PlayerGlideData source) {
		this.glideCharge = source.glideCharge;
		this.lastGlideCharge = source.lastGlideCharge;
		this.glideRatio = source.glideRatio;
		this.skylensTagCharge = source.skylensTagCharge;
	}

	public void saveNBTData(CompoundTag nbt) {
		nbt.putFloat("glideCharge", glideCharge);
		nbt.putFloat("lastGlideCharge", lastGlideCharge);
		nbt.putFloat("glideRatio", glideRatio);
		nbt.putInt("skylensTagCharge", skylensTagCharge);
	}

	public void loadNBTData(CompoundTag nbt) {
		glideCharge = nbt.getFloat("glideCharge");
		lastGlideCharge = nbt.getFloat("lastGlideCharge");
		glideRatio = nbt.getFloat("glideRatio");
		skylensTagCharge = nbt.getInt("skylensTagCharge");
	}
}