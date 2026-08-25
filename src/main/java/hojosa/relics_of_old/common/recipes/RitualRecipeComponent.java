package hojosa.relics_of_old.common.recipes;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.level.block.Block;

public class RitualRecipeComponent {
	public Set<RitualRecipeElement> blocks;
	public ComponentType type;

	public enum ComponentType {
		SINGLETON, MATCH, DUO
	}

	private RitualRecipeComponent() {
		this.blocks = new HashSet<>();
	}

	// Single block ingredient
	public RitualRecipeComponent(Block single) {
		this();
		blocks.add(new RitualRecipeElement(single));
		type = ComponentType.SINGLETON;
	}

	// Pair of blocks (same = MATCH, different = DUO)
	public RitualRecipeComponent(Block first, Block second) {
		this();
		RitualRecipeElement e1 = new RitualRecipeElement(first);
		RitualRecipeElement e2 = new RitualRecipeElement(second);
		if (e1.equals(e2)) {
			blocks.add(e1);
			type = ComponentType.MATCH;
		} else {
			blocks.add(e1);
			blocks.add(e2);
			type = ComponentType.DUO;
		}
	}

	public boolean isMetBy(RitualRecipeComponent other) {
		if (other.type != this.type)
			return false;
		for (RitualRecipeElement ingredient : other.blocks) {
			if (!this.blocks.contains(ingredient))
				return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RitualRecipeComponent other) {
			return other.type == this.type && other.blocks.equals(this.blocks);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return blocks.hashCode() + type.hashCode();
	}
}