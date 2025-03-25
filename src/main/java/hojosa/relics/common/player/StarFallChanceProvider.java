package hojosa.relics.common.player;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class StarFallChanceProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

	public static Capability<StarFallChance> PLAYER_STAR_FALL = CapabilityManager.get(new CapabilityToken<StarFallChance>() {});

	private StarFallChance starFallCap = null;
	private final LazyOptional<StarFallChance> optional = LazyOptional.of(this::createStarFallCap);

	private StarFallChance createStarFallCap() {
		if (this.starFallCap == null) {
			this.starFallCap = new StarFallChance();
		}
		return this.starFallCap;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == PLAYER_STAR_FALL) {
			return optional.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		createStarFallCap().saveNBTData(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		createStarFallCap().loadNBTData(nbt);
	}
}