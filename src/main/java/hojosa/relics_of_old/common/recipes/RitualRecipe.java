package hojosa.relics_of_old.common.recipes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RitualRecipe {
	public Map<RitualRecipeComponent, Integer> ingredients = new HashMap<>();

	public RitualRecipe add(RitualRecipeComponent toAdd) {
		ingredients.merge(toAdd, 1, Integer::sum);
		return this;
	}

	public boolean accepts(RitualRecipe other) {
		Set<RitualRecipeComponent> requirements = new HashSet<>(ingredients.keySet());
		Set<RitualRecipeComponent> supply = new HashSet<>(other.ingredients.keySet());

		if (requirements.size() != supply.size())
			return false;

		for (RitualRecipeComponent check : ingredients.keySet()) {
			boolean fulfilled = false;
			for (RitualRecipeComponent input : other.ingredients.keySet()) {
				if (check.isMetBy(input)) {
					requirements.remove(check);
					supply.remove(input);
					fulfilled = true;
					break;
				}
			}
			if (!fulfilled)
				return false;
		}
		return requirements.isEmpty() && supply.isEmpty();
	}
}