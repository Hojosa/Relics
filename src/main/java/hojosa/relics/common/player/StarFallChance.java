package hojosa.relics.common.player;

import java.util.Random;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;

public class StarFallChance {
	@Getter @Setter
	private int starChance = 0;
	@Getter @Setter
    private int starsCollected = 0;
	private Random random = new Random();

    public void copyFrom(StarFallChance source) {
        this.starChance = source.starChance;
        this.starsCollected = source.starsCollected;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("starChance", starChance);
        nbt.putInt("starsCollected", starsCollected);
    }

    public void loadNBTData(CompoundTag nbt) {
    	starChance = nbt.getInt("starChance");
    	starsCollected = nbt.getInt("starsCollected");
    }
    
    public void rollNewChance() {
    	this.starChance = random.nextInt(0, 100);
    }
}
