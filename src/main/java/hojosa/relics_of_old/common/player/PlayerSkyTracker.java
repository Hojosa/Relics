package hojosa.relics_of_old.common.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlayerSkyTracker {
	private double lastSkyX, lastSkyY, lastSkyZ;
	private ResourceKey<Level> lastSkyDimension;
	private boolean hasPosition = false;

	public static PlayerSkyTracker get(Player player) {
		return player.getCapability(PlayerSkyTrackerProvider.PLAYER_SKY_TRACKER).orElse(null);
	}

	// Called from inventoryTick of items that need sky tracking
	public void updateIfUnderSky(Player player) {
		if (player.level().canSeeSky(player.blockPosition().above(2))) {
			lastSkyX = player.getX();
			lastSkyY = player.getY();
			lastSkyZ = player.getZ();
			lastSkyDimension = player.level().dimension();
			hasPosition = true;
		}
	}

	public boolean hasPosition() {
		return hasPosition;
	}

	public double getLastSkyX() {
		return lastSkyX;
	}

	public double getLastSkyY() {
		return lastSkyY;
	}

	public double getLastSkyZ() {
		return lastSkyZ;
	}

	public ResourceKey<Level> getLastSkyDimension() {
		return lastSkyDimension;
	}

	public boolean isInSameDimension(Player player) {
		return hasPosition && player.level().dimension().equals(lastSkyDimension);
	}

	public void copyFrom(PlayerSkyTracker source) {
		this.lastSkyX = source.lastSkyX;
		this.lastSkyY = source.lastSkyY;
		this.lastSkyZ = source.lastSkyZ;
		this.lastSkyDimension = source.lastSkyDimension;
		this.hasPosition = source.hasPosition;
	}

	public void saveNBTData(CompoundTag nbt) {
		nbt.putBoolean("hasPosition", hasPosition);
		if (hasPosition) {
			nbt.putDouble("lastSkyX", lastSkyX);
			nbt.putDouble("lastSkyY", lastSkyY);
			nbt.putDouble("lastSkyZ", lastSkyZ);
			nbt.putString("lastSkyDimension", lastSkyDimension.location().toString());
		}
	}

	public void loadNBTData(CompoundTag nbt) {
		hasPosition = nbt.getBoolean("hasPosition");
		if (hasPosition) {
			lastSkyX = nbt.getDouble("lastSkyX");
			lastSkyY = nbt.getDouble("lastSkyY");
			lastSkyZ = nbt.getDouble("lastSkyZ");
			lastSkyDimension = ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, ResourceLocation.tryParse(nbt.getString("lastSkyDimension")));
		}
	}
}