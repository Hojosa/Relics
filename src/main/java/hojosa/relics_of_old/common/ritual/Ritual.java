package hojosa.relics_of_old.common.ritual;

import java.util.List;

import hojosa.relics_of_old.common.block.entity.RitualLocusBlockEntity;
import hojosa.relics_of_old.common.recipes.RitualRecipe;
import hojosa.relics_of_old.common.recipes.RitualRecipeComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class Ritual {
	public RitualRecipe components;
	public String name;

	public Ritual(String name, RitualRecipe parts) {
		this.name = name;
		this.components = parts;
	}

	public boolean accepts(RitualRecipe ingredients) {
		return components.accepts(ingredients);
	}

	public abstract boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster);

	// Filters items/blocks at the focus position based on a filter object
	public List<Entity> filterFocus(Object filter, RitualLocusBlockEntity location) {
		List<ItemEntity> items = location.itemsInRitual();
		java.util.ArrayList<Entity> result = new java.util.ArrayList<>();

		// Entity class filter
		if (filter instanceof Class<?> c && Entity.class.isAssignableFrom(c)) {
			@SuppressWarnings("unchecked")
			Class<Entity> entityClass = (Class<Entity>) c;
			return location.targetsInRitual(entityClass);
		}

		// Item/Block filter against dropped items
		for (ItemEntity itemEntity : items) {
			ItemStack stack = itemEntity.getItem();
			if (filter instanceof ItemStack filterStack) {
				if (filterStack.getItem() == stack.getItem()) {
					result.add(itemEntity);
				}
			} else if (filter instanceof net.minecraft.world.item.Item filterItem) {
				if (filterItem == stack.getItem()) {
					result.add(itemEntity);
				}
			} else if (filter instanceof Block filterBlock) {
				if (stack.getItem() instanceof BlockItem bi && Block.byItem(bi) == filterBlock) {
					result.add(itemEntity);
				}
			}
		}

		// Block filter: also check focus block above
		if (filter instanceof Block filterBlock) {
			Block focusBlock = location.focusBlock();
			if (result.size() > 0 && focusBlock == Blocks.AIR)
				return result;
			if (filterBlock == focusBlock)
				return result;
			return null;
		}
		return result;
	}

	// Summoning ritual subtype
	public static class Summoning extends Ritual {
		public EntityType<?> entityType;
		public Object focusFilter;

		public Summoning(String name, EntityType<?> creature, RitualRecipeComponent identifiers, Object focusFilter) {
			this(name, creature, new RitualRecipe().add(identifiers).add(new RitualRecipeComponent(Blocks.EMERALD_BLOCK)), focusFilter);
		}

		public Summoning(String name, EntityType<?> creature, RitualRecipe recipe, Object focusFilter) {
			super(name, recipe);
			this.entityType = creature;
			this.focusFilter = focusFilter;
		}

		@Override
		public boolean invoke(RitualRecipe ingredients, RitualLocusBlockEntity location, Player caster) {
			List<Entity> targets = filterFocus(focusFilter, location);
			if (targets == null)
				return false;
			if (!components.accepts(ingredients))
				return false;

			if (targets.size() > 0) {
				targets.get(0).discard();
			} else if (focusFilter instanceof Block) {
				location.clearFocusBlock();
			} else {
				return false;
			}
			
			Level level = location.getLevel();
			Entity mob = entityType.create(level);
			BlockPos pos = location.getBlockPos();
			mob.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			if (mob instanceof Mob living) {
				ForgeEventFactory.onFinalizeSpawn(living, (ServerLevel) level, level.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
			}
			level.addFreshEntity(mob);
//			try {
//				Mob summoned = creatureClass.getConstructor(net.minecraft.world.entity.EntityType.class, net.minecraft.world.level.Level.class)
//						.newInstance(net.minecraft.world.entity.EntityType.byString(
//								net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(net.minecraft.world.entity.EntityType.byString(creatureClass.getSimpleName().toLowerCase()).orElse(null)).toString())
//								.orElse(null), location.getLevel());
//				if (summoned == null)
//					return false;
//				BlockPos pos = location.getBlockPos();
//				summoned.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
//				summoned.finalizeSpawn(((net.minecraft.server.level.ServerLevel) location.getLevel()), location.getLevel().getCurrentDifficultyAt(pos), net.minecraft.world.entity.MobSpawnType.MOB_SUMMONED, null, null);
//				location.getLevel().addFreshEntity(summoned);
//				// Fallback: use EntityType registry
//			} catch (Exception e) {
//				// Summoning will be set up per-ritual with proper EntityType references
//				return false;
//			}
			return true;
		}
	}
}