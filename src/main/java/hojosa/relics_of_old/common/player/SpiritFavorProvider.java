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

public class SpiritFavorProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

	public static Capability<SpiritFavor> SPIRIT_FAVOR = CapabilityManager.get(new CapabilityToken<SpiritFavor>() {
	});

	private SpiritFavor favor = null;
	private final LazyOptional<SpiritFavor> optional = LazyOptional.of(this::createFavor);

	private SpiritFavor createFavor() {
		if (this.favor == null) {
			this.favor = new SpiritFavor();
		}
		return this.favor;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == SPIRIT_FAVOR) {
			return optional.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		createFavor().saveNBTData(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		createFavor().loadNBTData(nbt);
	}
}