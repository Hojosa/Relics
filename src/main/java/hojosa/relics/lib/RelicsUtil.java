package hojosa.relics.lib;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import hojosa.relics.Relics;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsUtil {
	static Random random = new Random();
	private static final Map<Block, List<Block>> BLOCK_CYCLES = new HashMap<>();

	public static ResourceLocation mcLoc(String path) {
		return new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, path);
	}

	public static ResourceLocation forgeLoc(String path) {
		return new ResourceLocation("forge", path);
	}

	public static ResourceLocation modLoc(String path) {
		return new ResourceLocation(References.MOD_ID, path);
	}

	public static float r(float phase) {
		phase = (float) (phase * (Math.PI * 2));
		double r = (Math.sin((phase + 0.0d)) + 1.0d) * 0.5d;
		double g = (Math.sin((phase + 2.0943952f)) + 1.0f) * 0.5f;
		double b = (Math.sin((phase + 4.1887903f)) + 1.0f) * 0.5f;
		double resat = Math.min(r, Math.min(g, b));
		double scaler = 1.0f / Math.max(r -= resat, Math.max(g -= resat, b -= resat));
		r = Math.min(scaler * r, 1.0f);
		return (float) r;
	}

	public static float g(float phase) {
		phase = (float) (phase * (Math.PI * 2));
		double r = (Math.sin((phase + 0.0f)) + 1.0f) * 0.5f;
		double g = (Math.sin((phase + 2.0943952f)) + 1.0f) * 0.5f;
		double b = (Math.sin((phase + 4.1887903f)) + 1.0f) * 0.5f;
		double resat = Math.min(r, Math.min(g, b));
		double scaler = 1.0f / Math.max(r -= resat, Math.max(g -= resat, b -= resat));
		g = Math.min(scaler * g * 0.5f + 0.5f, 1.0f);
		return (float) g;
	}

	public static float b(float phase) {
		phase = (float) (phase * (Math.PI * 2));
		double r = (Math.sin((phase + 0.0f)) + 1.0f) * 0.5f;
		double g = (Math.sin((phase + 2.0943952f)) + 1.0f) * 0.5f;
		double b = (Math.sin((phase + 4.1887903f)) + 1.0f) * 0.5f;
		double resat = Math.min(r, Math.min(g, b));
		double scaler = 1.0f / Math.max(r -= resat, Math.max(g -= resat, b -= resat));
		b = Math.min(scaler * b * 0.5f + 0.5f, 1.0f);
		return (float) b;
	}

	public static void setupBlockCycleMap() {

		addCycle(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);
		addCycle(Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.SMOOTH_SANDSTONE);
		addCycle(Blocks.RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE);
		addCycle(Blocks.PUMPKIN, Blocks.MELON);
		addCycle(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE);
		addCycle(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM);
		addCycle(Blocks.OXEYE_DAISY, Blocks.ROSE_BUSH);
		addCycle(Blocks.OAK_SAPLING, Blocks.BIRCH_SAPLING, Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.CHERRY_SAPLING, Blocks.SPRUCE_SAPLING);
		addCycle(Blocks.ANDESITE, Blocks.POLISHED_ANDESITE);
		addCycle(Blocks.GRANITE, Blocks.POLISHED_GRANITE);
		addCycle(Blocks.DIORITE, Blocks.POLISHED_DIORITE);
	}

	// Helper method to add cycles
	private static void addCycle(Block... blocks) {
		List<Block> cycle = Arrays.asList(blocks);
		for (Block block : cycle) {
			BLOCK_CYCLES.put(block, cycle);
		}
	}

	// get the next block in our block list, return blocks.air if not found
	public static Block getNextBlock(Block block) {
		List<Block> cycle = BLOCK_CYCLES.get(block);
		return cycle.get((cycle.indexOf(block) + 1) % cycle.size());
	}

	public static boolean hasBlockToCycle(Block block) {
		return BLOCK_CYCLES.containsKey(block);
	}

	public static <T extends Animal> void cycleMobVariant(T animal) {
		if (animal.getClass().equals(Cat.class)) {
			((Cat) animal).setVariant(BuiltInRegistries.CAT_VARIANT.byId(random.nextInt(BuiltInRegistries.CAT_VARIANT.size())));
			return;
		}
		if (animal.getClass().equals(Sheep.class)) {
			((Sheep) animal).setColor(DyeColor.byId(random.nextInt(DyeColor.values().length)));
		}

		try {
			// Get the `getVariant` method from the subclass
//			Method getVariantMethod = animal.getClass().getMethod("getVariant");
			Method getVariantMethod = ObfuscationReflectionHelper.findMethod(animal.getClass(), "m_28554_");

			// Invoke the method to get the current variant
			Object variant = getVariantMethod.invoke(animal);

			// Get the enum values of the Variant class
//			Method valuesMethod = variant.getClass().getMethod("values");
			Method valuesMethod = ObfuscationReflectionHelper.findMethod(variant.getClass(), "values");
			Object[] variants = (Object[]) valuesMethod.invoke(null);

			// Pick a random variant
			Object randomVariant = variants[random.nextInt(variants.length)];

			// Get the `setVariant` method and set the new variant
//			Method setVariantMethod = animal.getClass().getMethod("setVariant", variant.getClass());
			Method setVariantMethod = ObfuscationReflectionHelper.findMethod(animal.getClass(), "m_28464_", variant.getClass());
			setVariantMethod.invoke(animal, randomVariant);
		} catch (Exception e) {
			Relics.LOGGER.error("error or mob without variant when trying to get method for " + animal + ": " + e);
		}
	}
}
