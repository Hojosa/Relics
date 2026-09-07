package hojosa.relics_of_old.common.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerSkyTrackerProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

	public static Capability<PlayerSkyTracker> PLAYER_SKY_TRACKER = CapabilityManager.get(new CapabilityToken<PlayerSkyTracker>() {});

    private PlayerSkyTracker tracker = null;
    private final LazyOptional<PlayerSkyTracker> optional = LazyOptional.of(this::createTracker);

    private PlayerSkyTracker createTracker() {
        if (this.tracker == null) {
            this.tracker = new PlayerSkyTracker();
        }
        return this.tracker;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_SKY_TRACKER) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createTracker().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createTracker().loadNBTData(nbt);
    }
}