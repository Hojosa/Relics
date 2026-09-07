package hojosa.relics_of_old.common.player;

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

public class PlayerGlideDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
	public static Capability<PlayerGlideData> PLAYER_GLIDE_DATA = CapabilityManager.get(new CapabilityToken<PlayerGlideData>() {
	});

	private PlayerGlideData data = null;
	private final LazyOptional<PlayerGlideData> optional = LazyOptional.of(this::createData);

	private PlayerGlideData createData() {
		if (this.data == null) {
			this.data = new PlayerGlideData();
		}
		return this.data;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == PLAYER_GLIDE_DATA) {
			return optional.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		createData().saveNBTData(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		createData().loadNBTData(nbt);
	}
}