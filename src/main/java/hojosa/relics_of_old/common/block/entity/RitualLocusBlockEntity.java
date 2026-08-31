package hojosa.relics_of_old.common.block.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import hojosa.relics_of_old.common.block.StarwellBlock;
import hojosa.relics_of_old.common.init.RelicsBlockEntities;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.common.recipes.RitualRecipe;
import hojosa.relics_of_old.common.recipes.RitualRecipeBase;
import hojosa.relics_of_old.common.recipes.RitualRecipeComponent;
import hojosa.relics_of_old.common.ritual.Edge;
import hojosa.relics_of_old.common.ritual.RitualGrid;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import slimeknights.mantle.block.entity.MantleBlockEntity;

public class RitualLocusBlockEntity extends MantleBlockEntity {
	public boolean active;
	public boolean stable;
	public RitualGrid grid;
	public int[] ticksSinceEmpty = new int[8];
	public int awakeTicks;
	public long[] pulseStartTime = new long[8];
	public boolean dirty;
	public static final int PULSE_SEPARATION = 3;

	public int successEffectTimer = 0;
	public static final int SUCCESS_EFFECT_DURATION = 30;
	public boolean successGoing = false;
	private RitualRecipeBase lastMatchedRecipe;

	public RitualLocusBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public RitualLocusBlockEntity(BlockPos pos, BlockState state) {
		super(RelicsBlockEntities.RITUAL_LOCUS_BLOCK_ENTITY.get(), pos, state);
	}

	// Items sitting above the ritual focus
	public List<ItemEntity> itemsInRitual() {
		AABB bounds = new AABB(worldPosition).move(0, 1, 0).inflate(0.5, 0.5, 0.5);
		return level.getEntitiesOfClass(ItemEntity.class, bounds);
	}

	// Entities of a given type above the focus
	public <T extends Entity> List<T> targetsInRitual(Class<T> type) {
		AABB bounds = new AABB(worldPosition).move(0, 1, 0).inflate(0.5, 0.5, 0.5);
		return level.getEntitiesOfClass(type, bounds);
	}

	public Block focusBlock() {
		return level.getBlockState(worldPosition.above()).getBlock();
	}

	public void clearFocusBlock() {
		level.removeBlock(worldPosition.above(), false);
	}

	public boolean tryInvoke(Player player) {
		if (!active)
			return false;
		stable = grid.isGridStable(level);
		if (dirty || !stable)
			return false;

		RitualRecipe ingredients = getIngredients();
		boolean success = false;
		
		if (lastMatchedRecipe != null && lastMatchedRecipe.matchesRitual(ingredients)) {
	          success = lastMatchedRecipe.invoke(ingredients, this, player);
	      } else {
	          var recipes = level.getRecipeManager().getAllRecipesFor(RitualRecipeBase.Type.INSTANCE);
	          for (RitualRecipeBase recipe : recipes) {
	              if (recipe.matchesRitual(ingredients)) {
	                  lastMatchedRecipe = recipe;
	                  success = recipe.invoke(ingredients, this, player);
	                  break;
	              }
	          }
	      }


		if (success) {
			level.playSound(null, worldPosition, RelicsSounds.RITUAL_SUCCESS.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
			level.playSound(null, worldPosition, RelicsSounds.RITUAL_LASER.get(), SoundSource.BLOCKS, 0.15f, 1.0f);
			successGoing = true;
		} else {
			level.playSound(null, worldPosition, RelicsSounds.RITUAL_FAIL.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
		}

		dirty = true;
		grid.clearEdges();
		stable = false;
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		return success;
	}

	public void tick() {
		if (level == null)
			return;

		if (grid == null) {
			grid = new RitualGrid(worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ());
		}

		// Wakeup sync
		if (awakeTicks == 0 && !level.isClientSide) {
			if (!grid.inhabitedPoints(level).isEmpty()) {
				dirty = true;
			}
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		}

		// Success effect timer
		if (successGoing) {
			successEffectTimer++;
			if (successEffectTimer >= SUCCESS_EFFECT_DURATION) {
				successGoing = false;
				successEffectTimer = 0;
			}
		}
		// Check for active starwell below
		BlockPos below = worldPosition.below();
		BlockState belowState = level.getBlockState(below);
		active = belowState.is(RelicsBlocks.STARWELL_CORE.get()) && belowState.getValue(StarwellBlock.ACTIVE);

		if (!active) {
			awakeTicks = 0;
			return;
		}

		// Monitor grid point changes
		for (int i = 0; i < 8; i++) {
			if (level.getBlockState(grid.places[i]).isAir()) {
				if (!level.isClientSide && ticksSinceEmpty[i] != 0) {
					onGridChange(i, false);
				}
				ticksSinceEmpty[i] = 0;
			} else {
				if (!level.isClientSide && ticksSinceEmpty[i] == 0) {
					onGridChange(i, true);
				}
				if (ticksSinceEmpty[i] < 10) {
					ticksSinceEmpty[i]++;
				}
			}
		}
		awakeTicks++;
	}

	private void onGridChange(int pointChanged, boolean placed) {
		stable = grid.isGridStable(level);

		if (placed && awakeTicks > 1) {
			doPlacement(pointChanged);
		}

		if (stable && !dirty) {
			level.playSound(null, worldPosition, RelicsSounds.RITUAL_READY.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
		}

		if (!placed) {
			if (grid.inhabitedPoints(level).isEmpty()) {
				dirty = false;
			} else {
				if (!dirty) {
					level.playSound(null, worldPosition, RelicsSounds.RING_SAD.get(), SoundSource.BLOCKS, 0.7f, 1.0f);
				}
				dirty = true;
			}
			grid.clearEdges();
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		}
	}

	private void doPlacement(int point) {
		BlockPos pos = grid.places[point];
		if (!dirty) {
			pulseStartTime[point] = level.getGameTime();
			int edgeNumber = grid.inhabitedPoints(level).size() - 1;

			// Play ring sound for this point
			playChimeForIndex(edgeNumber, pos);

			int destination = grid.makeEdgeFrom(point, edgeNumber);
			pulseStartTime[destination] = level.getGameTime() + PULSE_SEPARATION;
		}
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
	}

	// Plays the appropriate chime pitch for the given grid index
	private void playChimeForIndex(int index, BlockPos pos) {
		SoundEvent ring = switch (index) {
		case 0 -> RelicsSounds.RING_0.get();
		case 1 -> RelicsSounds.RING_1.get();
		case 2 -> RelicsSounds.RING_2.get();
		case 3 -> RelicsSounds.RING_3.get();
		// no RING.4 in original
		case 5 -> RelicsSounds.RING_5.get();
		case 6 -> RelicsSounds.RING_6.get();
		case 7 -> RelicsSounds.RING_7.get();
		default -> RelicsSounds.RING_0.get();
		};
		level.playSound(null, pos, ring, SoundSource.BLOCKS, 0.5f, 1.0f);
	}

	// Builds the recipe from the current grid state
	public RitualRecipe getIngredients() {
		RitualRecipe recipe = new RitualRecipe();
		if (!stable)
			return recipe;

		Set<Integer> counted = new HashSet<>();
		for (Edge e : grid.edges) {
			Block b1 = grid.blockOnPoint(e.first, level);
			Block b2 = grid.blockOnPoint(e.second, level);
			if (b1 != Blocks.AIR && b2 != Blocks.AIR) {
				recipe.add(new RitualRecipeComponent(b1, b2));
			}
			counted.add(e.first);
			counted.add(e.second);
		}
		// Singletons not covered by edges
		for (int i = 0; i < 8; i++) {
			if (counted.contains(i))
				continue;
			Block block = grid.blockOnPoint(i, level);
			if (block != Blocks.AIR) {
				recipe.add(new RitualRecipeComponent(block));
			}
		}
		return recipe;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		ticksSinceEmpty = tag.getIntArray("ticksSinceEmpty");
		if (ticksSinceEmpty.length == 0)
			ticksSinceEmpty = new int[8];
		awakeTicks = tag.getInt("awakeTicks");
		dirty = tag.getBoolean("dirty");
		long[] pulse = tag.getLongArray("pulseStartTime");
		pulseStartTime = pulse.length == 8 ? pulse : new long[8];
		readEdgesFromTag(tag);
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		tag.putIntArray("ticksSinceEmpty", ticksSinceEmpty);
		tag.putInt("awakeTicks", awakeTicks);
		tag.putBoolean("dirty", dirty);
		tag.putLongArray("pulseStartTime", pulseStartTime);
		writeEdgesToTag(tag);
		super.saveAdditional(tag);
	}

	private void writeEdgesToTag(CompoundTag tag) {
		if (grid == null)
			return;
		List<Integer> edgeList = new ArrayList<>();
		for (Edge edge : grid.edges) {
			edgeList.add(edge.first);
			edgeList.add(edge.second);
		}
		tag.putIntArray("edges", edgeList.stream().mapToInt(i -> i).toArray());
	}

	private void readEdgesFromTag(CompoundTag tag) {
		int[] edgeInts = tag.getIntArray("edges");
		if (grid == null) {
			grid = new RitualGrid(worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ());
		}
		grid.clearEdges();
		for (int i = 0; i < edgeInts.length; i += 2) {
			grid.edges.add(new Edge(edgeInts[i], edgeInts[i + 1]));
		}
	}

	@Override
	protected boolean shouldSyncOnUpdate() {
		return true;
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		CompoundTag tag = pkt.getTag();
		dirty = tag.getBoolean("dirty");
		successGoing = tag.getBoolean("successGoing");
		long[] pulse = tag.getLongArray("pulseStartTime");
		if (pulse.length == 8)
			System.arraycopy(pulse, 0, pulseStartTime, 0, 8);
		readEdgesFromTag(tag);
	}

	@Override
	public @NotNull CompoundTag getUpdateTag() {
		CompoundTag tag = serializeNBT();
		tag.putBoolean("successGoing", successGoing);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		dirty = tag.getBoolean("dirty");
		successGoing = tag.getBoolean("successGoing");
		awakeTicks = tag.getInt("awakeTicks");
		readEdgesFromTag(tag);
		ticksSinceEmpty = new int[8];
	}

	@Override
	public AABB getRenderBoundingBox() {
		return new AABB(worldPosition.offset(-5, -5, -5), worldPosition.offset(5, 5, 5));
	}
}