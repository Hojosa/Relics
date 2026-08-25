package hojosa.relics_of_old.common.recipes;

import net.minecraft.world.level.block.Block;

public class RitualRecipeElement {
	public final Block block;

	public RitualRecipeElement(Block block) {
        this.block = block;
    }

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RitualRecipeElement other) {
			return other.block == this.block;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return block.hashCode();
	}

	@Override
	public String toString() {
		return block.toString();
	}
}