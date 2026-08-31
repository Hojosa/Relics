package hojosa.relics_of_old.common.init;

import hojosa.relics_of_old.common.block.entity.RitualLocusBlockEntity;
import hojosa.relics_of_old.common.recipes.RitualRecipe;
import hojosa.relics_of_old.common.recipes.RitualRecipeBase;
import lombok.NoArgsConstructor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
@Deprecated
@NoArgsConstructor
public class RitualManager {
	public static final RitualManager INSTANCE = new RitualManager();
//	public Set<Ritual> rituals = new HashSet<>();

//	private void registerRituals() {
//		// Summon mooshroom: red mushroom + mycelium edge, mushroom stew focus
//		rituals.add(new Ritual.Summoning("summonMooshroom", EntityType.MOOSHROOM, new RitualRecipeComponent(Blocks.RED_MUSHROOM, Blocks.MYCELIUM), Items.MUSHROOM_STEW));
//
//		// Summon sheep: wool + grass edge, grass focus
//		rituals.add(new Ritual.Summoning("summonSheep", EntityType.SHEEP, new RitualRecipeComponent(Blocks.WHITE_WOOL, Blocks.GRASS_BLOCK), Blocks.GRASS_BLOCK));
//
//		// Summon horse: hay + grass edge, sugar focus
//		rituals.add(new Ritual.Summoning("summonHorse", EntityType.HORSE, new RitualRecipeComponent(Blocks.HAY_BLOCK, Blocks.GRASS_BLOCK), Items.SUGAR));
//
//		// Summon pig: brown mushroom + grass edge, carrot focus
//		rituals.add(new Ritual.Summoning("summonPig", EntityType.PIG, new RitualRecipeComponent(Blocks.BROWN_MUSHROOM, Blocks.GRASS_BLOCK), Items.CARROT));
//
//		// Convert cow to mooshroom
//		rituals.add(new Ritual("convertMooshroom", new RitualRecipe().add(new RitualRecipeComponent(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM))) {
//			@Override
//			public boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
//				if (caster == null || caster.level().isClientSide)
//					return false;
//				List<Cow> cows = location.targetsInRitual(Cow.class);
//				if (cows.isEmpty())
//					return false;
//				Cow cow = (Cow) cows.get(0);
//				MushroomCow moosh = new MushroomCow(net.minecraft.world.entity.EntityType.MOOSHROOM, caster.level());
//				moosh.moveTo(cow.getX(), cow.getY(), cow.getZ(), cow.getYRot(), cow.getXRot());
//				moosh.setHealth(cow.getHealth());
//				cow.discard();
//				caster.level().addFreshEntity(moosh);
//				return true;
//			}
//		});
//		// Crucible: lava + lava edge, smelts focus item
//		rituals.add(new Ritual("crucible", new RitualRecipe().add(new RitualRecipeComponent(Blocks.LAVA, Blocks.LAVA))) {
//			@Override
//			public boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
//				List<ItemEntity> items = location.itemsInRitual();
//				if (items.size() != 1)
//					return false;
//				ItemEntity ei = items.get(0);
//				ItemStack stack = ei.getItem();
//				Level level = location.getLevel();
//				var optional = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new net.minecraft.world.SimpleContainer(stack), level);
//				if (optional.isEmpty())
//					return false;
//				ItemStack output = optional.get().getResultItem(level.registryAccess());
//				int total = output.getCount() * stack.getCount();
//				while (total > 0) {
//					ItemStack oneOutput = output.copy();
//					int count = Math.min(oneOutput.getMaxStackSize(), total);
//					oneOutput.setCount(count);
//					total -= count;
//					ItemEntity outEntity = new ItemEntity(level, location.getBlockPos().getX() + 0.5, location.getBlockPos().getY() + 1.5, location.getBlockPos().getZ() + 0.5, oneOutput);
//					outEntity.setDeltaMovement(0, 0, 0);
//					level.addFreshEntity(outEntity);
//				}
//				ei.discard();
//				return true;
//			}
//		});
//
//		// Stoneskin: stone + stone edge, leather focus -> resistance potion
//		rituals.add(new Ritual("stoneskin", new RitualRecipe().add(new RitualRecipeComponent(Blocks.STONE, Blocks.STONE))) {
//			@Override
//			public boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
//				List<ItemEntity> items = location.itemsInRitual();
//				if (items.size() != 1)
//					return false;
//				ItemStack stack = items.get(0).getItem();
//				if (stack.getItem() != Items.LEATHER)
//					return false;
//				int count = stack.getCount();
//				int baseDuration = 3600;
//				int bonusDuration = (count - 1) * 20 * 30;
//				caster.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.DAMAGE_RESISTANCE, baseDuration + bonusDuration, 1, true, true));
//				items.get(0).discard();
//				return true;
//			}
//		});
//		// Soul tether: soul sand + tripwire edge + iron block, enchants held item
//		rituals.add(new Ritual("soulTether", new RitualRecipe().add(new RitualRecipeComponent(Blocks.SOUL_SAND, Blocks.TRIPWIRE)).add(new RitualRecipeComponent(Blocks.IRON_BLOCK))) {
//			@Override
//			public boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
//				List<ItemEntity> items = location.itemsInRitual();
//				if (items.size() != 1)
//					return false;
//				ItemStack stack = items.get(0).getItem();
//				if (stack.getMaxStackSize() != 1)
//					return false;
//				// Spend 10 levels or take damage
//				if (!spendRitualLevels(caster, 10))
//					return false;
//				stack.getOrCreateTag().putBoolean("soulTether", true);
//				return true;
//			}
//		});
//	}

	// Spend XP levels, dealing damage for the shortfall
	@Deprecated
	public static boolean spendRitualLevels(Player caster, int levels) {
		int spend = Math.min(caster.experienceLevel, levels);
		caster.giveExperienceLevels(-spend);
		if (spend < levels) {
			caster.hurt(caster.damageSources().magic(), 4.0f * (levels - spend));
		}
		return caster.getHealth() > 0;
	}
	
	@Deprecated
	public boolean attemptInvocation(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
        Level level = location.getLevel();
        if (level == null) return false;

        // Query all data-driven ritual recipes
        var recipes = level.getRecipeManager().getAllRecipesFor(RitualRecipeBase.Type.INSTANCE);
        for (RitualRecipeBase recipe : recipes) {
            if (recipe.matchesRitual(ingredients)) {
                return recipe.invoke(ingredients, location, caster);
            }
        }
        return false;
    }


//	public boolean attemptInvocation(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
//		for (Ritual r : rituals) {
//			if (r.accepts(ingredients)) {
//				return r.invoke(ingredients, location, caster);
//			}
//		}
//		return false;
//	}
}