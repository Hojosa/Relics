package hojosa.relics_of_old.common.block.entity;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import hojosa.relics_of_old.client.particle.InfusionItemParticle;
import hojosa.relics_of_old.common.block.StarwellBlock;
import hojosa.relics_of_old.common.init.RelicsBlockEntities;
import hojosa.relics_of_old.common.init.RelicsBlocks;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.common.recipes.InfusionRitualRecipe;
import hojosa.relics_of_old.common.ritual.Edge;
import hojosa.relics_of_old.common.ritual.RitualGrid;
import hojosa.relics_of_old.lib.block.entity.RelicsBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import slimeknights.mantle.block.entity.MantleBlockEntity;

public class InfusionLocusBlockEntity extends RelicsBlockEntity {
	public boolean active;
	public boolean crafting;
	public int progress;
	public static final int CRAFT_DURATION = 100;
	public static final int FLOAT_DURATION = 20;
	public int[] ticksSinceOccupied = new int[8];

	// Target item placed on the locus (the item being infused)
//	private ItemStack targetItem = ItemStack.EMPTY;
	// Cached recipe result for renderer
	private ItemStack resultPreview = ItemStack.EMPTY;

	public RitualGrid grid;
	private boolean helpersPlaced;

	// Edge connection data (synced to client for rendering)
	public int awakeTicks;
	public long[] pulseStartTime = new long[8];
	public boolean dirty;
	public float itemRotation;

	public InfusionLocusBlockEntity(BlockPos pos, BlockState state) {
		super(RelicsBlockEntities.INFUSION_LOCUS_BLOCK_ENTITY.get(), 1, pos, state);
	}

	public ItemStack getTargetItem() {
		return getItem(0);
	}

	public void setTargetItem(ItemStack stack) {
		setItem(0, stack);
	}

	public ItemStack getResultPreview() {
		return resultPreview;
	}

	public int getProgress() {
		return progress;
	}

	public boolean isCrafting() {
		return crafting;
	}

	public void tick() {
		if (level == null)
			return;

		if (grid == null) {
			grid = new RitualGrid(worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ());
		}
		spawnCraftingParticles();
		// Check for active starwell below
		BlockPos below = worldPosition.below();
		BlockState belowState = level.getBlockState(below);
		boolean wasActive = active;
		active = belowState.is(RelicsBlocks.STARWELL_CORE.get()) && belowState.getValue(StarwellBlock.ACTIVE);

		if (!active) {
			if (wasActive) {
				// Starwell deactivated — cancel crafting, remove helpers
				cancelCrafting();
				removeHelpers(true);
				helpersPlaced = false;
			}
			awakeTicks = 0;
			return;
		}

		// Place helpers when becoming active
		if (!helpersPlaced) {
			placeHelpers();
			helpersPlaced = true;
			if (!level.isClientSide) {
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
			}
		}

		// Run crafting logic
		if (crafting && !level.isClientSide) {
			tickCrafting();
		}

		awakeTicks++;
		for (int i = 0; i < 8; i++) {
			if (level.getBlockEntity(grid.places[i]) instanceof InfusionItemHolderBlockEntity helper) {
				if (!helper.getItem(0).isEmpty()) {
					if (ticksSinceOccupied[i] < 10)
						ticksSinceOccupied[i]++;
				} else {
					ticksSinceOccupied[i] = 0;
				}
			} else {
				ticksSinceOccupied[i] = 0;
			}
		}
	}

	// Place invisible helper blocks at all 8 grid positions
	public void placeHelpers() {
		if (level == null || level.isClientSide || grid == null)
			return;

		for (int i = 0; i < 8; i++) {
			BlockPos pos = grid.places[i];
			if (level.getBlockState(pos).isAir()) {
				level.setBlock(pos, RelicsBlocks.INFUSION_ITEM_HOLDER.get().defaultBlockState(), 3);
				if (level.getBlockEntity(pos) instanceof InfusionItemHolderBlockEntity helper) {
					helper.setLocusPos(worldPosition);
				}
			}
		}
	}

	// Remove all helper blocks, optionally dropping their items
	public void removeHelpers(boolean dropItems) {
		if (level == null || level.isClientSide || grid == null)
			return;

		for (int i = 0; i < 8; i++) {
			BlockPos pos = grid.places[i];
			if (level.getBlockState(pos).is(RelicsBlocks.INFUSION_ITEM_HOLDER.get())) {
				if (dropItems && level.getBlockEntity(pos) instanceof InfusionItemHolderBlockEntity helper) {
					ItemStack item = helper.getItem(0);
					if (!item.isEmpty()) {
						Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, item);
					}
				}
				level.removeBlock(pos, false);
			}
		}
		if (grid != null) {
			grid.clearEdges();
		}
	}

	// Called by stardust spell activation
	public boolean tryInvoke(Player player) {
		if (!active || crafting)
			return false;
		if (level == null || level.isClientSide)
			return false;

		// Must have a target item
		if (getTargetItem().isEmpty() || !isGridValid()) {
			level.playSound(null, worldPosition, RelicsSounds.RITUAL_FAIL.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
			return false;
		}

		// Gather ingredient items from helpers
		List<ItemStack> ingredients = gatherItems();
		if (ingredients.size() < 2) {
			level.playSound(null, worldPosition, RelicsSounds.RITUAL_FAIL.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
			return false;
		}

		// Build a container for recipe matching: target in slot 0, then ingredients
		SimpleContainer container = new SimpleContainer(1 + ingredients.size());
		container.setItem(0, getTargetItem().copy());
		for (int i = 0; i < ingredients.size(); i++) {
			container.setItem(i + 1, ingredients.get(i).copy());
		}

		// Find matching recipe
		InfusionRitualRecipe recipe = level.getRecipeManager().getRecipeFor(InfusionRitualRecipe.Type.INSTANCE, container, level).orElse(null);

		if (recipe == null) {
			level.playSound(null, worldPosition, RelicsSounds.RITUAL_FAIL.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
			return false;
		}

		// Start crafting
		crafting = true;
		progress = 0;
		resultPreview = recipe.getResultItem(null);
		level.playSound(null, worldPosition, RelicsSounds.RITUAL_SUCCESS.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
		level.playSound(null, worldPosition, RelicsSounds.MAGIC_CRAFTING.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		return true;
	}

	// Gather non-empty items from all 8 helper positions
	private List<ItemStack> gatherItems() {
		List<ItemStack> items = new ArrayList<>();
		if (grid == null)
			return items;

		for (int i = 0; i < 8; i++) {
			BlockPos pos = grid.places[i];
			if (level.getBlockEntity(pos) instanceof InfusionItemHolderBlockEntity helper) {
				ItemStack stack = helper.getItem(0);
				if (!stack.isEmpty()) {
					items.add(stack);
				}
			}
		}
		return items;
	}

	private void tickCrafting() {
		progress++;
		spawnCraftingParticles();

		// Play crafting sound at intervals
		if (progress % 25 == 0 && progress < CRAFT_DURATION) {
			level.playSound(null, worldPosition, RelicsSounds.MAGIC_CRAFTING.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
		}

		if (progress >= CRAFT_DURATION) {
			onCraftComplete();
		}

		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
	}

	// Spawn item particles trailing from each helper toward the center
	private void spawnCraftingParticles() {
		if (!level.isClientSide || !crafting || grid == null || progress > CRAFT_DURATION - 15)
			return;

		// Target is the floating item position
		double targetX = worldPosition.getX() + 0.5;
		float futureProgress = Math.min((float) (progress + 10) / FLOAT_DURATION, 1.0f);
		double targetY = worldPosition.getY() + 1.05 + futureProgress * 1.0;
		double targetZ = worldPosition.getZ() + 0.5;

		for (int i = 0; i < 8; i++) {
			BlockPos pos = grid.places[i];
			if (level.getBlockEntity(pos) instanceof InfusionItemHolderBlockEntity helper) {
				ItemStack stack = helper.getItem(0);
				if (!stack.isEmpty()) {
					double x = pos.getX() + 0.5;
					double y = pos.getY() + 0.1;
					double z = pos.getZ() + 0.5;

					InfusionItemParticle.spawn(stack, x, y, z, targetX, targetY, targetZ);
				}
			}
		}
	}

	// Crafting complete — consume items, spawn result
	private void onCraftComplete() {
		// Consume target item
		setItem(0, ItemStack.EMPTY);

		// Consume ingredient items from helpers
		for (int i = 0; i < 8; i++) {
			BlockPos pos = grid.places[i];
			if (level.getBlockEntity(pos) instanceof InfusionItemHolderBlockEntity helper) {
				if (!helper.getItem(0).isEmpty()) {
					helper.removeItem(0, 1);
				}
			}
		}

		// Spawn result item
		if (!resultPreview.isEmpty()) {
			ItemEntity result = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 2.0, worldPosition.getZ() + 0.5, resultPreview.copy());
			result.setPickUpDelay(15);
			result.setDeltaMovement(0, 0, 0);
			level.addFreshEntity(result);
		}

		level.playSound(null, worldPosition, RelicsSounds.ITEM_GET.get(), SoundSource.BLOCKS, 1.0f, 1.0f);

		// Reset state
		crafting = false;
		progress = 0;
		resultPreview = ItemStack.EMPTY;
		grid.clearEdges();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
	}

	private void cancelCrafting() {
		if (crafting) {
			crafting = false;
			progress = 0;
			resultPreview = ItemStack.EMPTY;
			if (level != null && !level.isClientSide) {
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
			}
		}
	}

	// Called by helper blocks when an item is placed/removed to rebuild edges
	public void onHelperItemChanged(int pointIndex, boolean placed) {
		if (level == null || level.isClientSide || grid == null)
			return;

		if (placed) {
			doPlacement(pointIndex);
		} else {
			// Item removed — clear edges, mark dirty unless all items gone
			if (occupiedPoints().isEmpty()) {
				dirty = false;
			} else {
				if (!dirty) {
					level.playSound(null, worldPosition, RelicsSounds.RING_SAD.get(), SoundSource.BLOCKS, 0.7f, 1.0f);
				}
				dirty = true;
			}
			grid.clearEdges();
		}

		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
	}

	private void doPlacement(int pointIndex) {
		if (dirty)
			return;

		BlockPos pos = grid.places[pointIndex];
		int edgeNumber = occupiedPoints().size() - 1;

		pulseStartTime[pointIndex] = level.getGameTime();
		playChimeForIndex(edgeNumber, pos, 0.5f, 1.0f);

		int destination = grid.makeEdgeFrom(pointIndex, edgeNumber);
		pulseStartTime[destination] = level.getGameTime() + RitualLocusBlockEntity.PULSE_SEPARATION;
	}

	// Which grid points currently hold an item
	public List<Integer> occupiedPoints() {
		List<Integer> points = new ArrayList<>();
		if (grid == null)
			return points;
		for (int i = 0; i < 8; i++) {
			if (level.getBlockEntity(grid.places[i]) instanceof InfusionItemHolderBlockEntity helper) {
				if (!helper.getItem(0).isEmpty()) {
					points.add(i);
				}
			}
		}
		return points;
	}

	// Check that the grid is valid for invocation: no empty nodes
	public boolean isGridValid() {
		if (dirty || grid.edges.isEmpty())
			return false;
		List<Integer> occupied = occupiedPoints();
		for (Edge edge : grid.edges) {
			if (!occupied.contains(edge.first) || !occupied.contains(edge.second))
				return false;
		}
		return true;
	}

	private void playChimeForIndex(int index, BlockPos pos, float volume, float pitch) {
		SoundEvent ring = switch (index) {
		case 0 -> RelicsSounds.RING_0.get();
		case 1 -> RelicsSounds.RING_1.get();
		case 2 -> RelicsSounds.RING_2.get();
		case 3 -> RelicsSounds.RING_3.get();
		case 5 -> RelicsSounds.RING_5.get();
		case 6 -> RelicsSounds.RING_6.get();
		case 7 -> RelicsSounds.RING_7.get();
		default -> RelicsSounds.RING_0.get();
		};
		level.playSound(null, pos, ring, SoundSource.BLOCKS, volume, pitch);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
//		targetItem = ItemStack.of(tag.getCompound("TargetItem"));
		resultPreview = ItemStack.of(tag.getCompound("ResultPreview"));
		crafting = tag.getBoolean("Crafting");
		progress = tag.getInt("Progress");
		helpersPlaced = tag.getBoolean("HelpersPlaced");
		awakeTicks = tag.getInt("AwakeTicks");
//		long[] pulse = tag.getLongArray("PulseStartTime");
//		pulseStartTime = pulse.length == 8 ? pulse : new long[8];
		dirty = tag.getBoolean("Dirty");
//		ticksSinceOccupied = tag.getIntArray("TicksSinceOccupied");
//		if (ticksSinceOccupied.length == 0)
//			ticksSinceOccupied = new int[8];
		long[] pulse = tag.getLongArray("PulseStartTime");
		if (pulse.length == 8)
			System.arraycopy(pulse, 0, pulseStartTime, 0, 8);
		int[] ticks = tag.getIntArray("TicksSinceOccupied");
		if (ticks.length == 8)
			System.arraycopy(ticks, 0, ticksSinceOccupied, 0, 8);
		readEdgesFromTag(tag);
		itemRotation = tag.getFloat("ItemRotation");
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
//		tag.put("TargetItem", targetItem.save(new CompoundTag()));
		tag.put("ResultPreview", resultPreview.save(new CompoundTag()));
		tag.putBoolean("Crafting", crafting);
		tag.putInt("Progress", progress);
		tag.putBoolean("HelpersPlaced", helpersPlaced);
		tag.putInt("AwakeTicks", awakeTicks);
		tag.putLongArray("PulseStartTime", pulseStartTime);
		tag.putBoolean("Dirty", dirty);
		tag.putIntArray("TicksSinceOccupied", ticksSinceOccupied);
		writeEdgesToTag(tag);
		tag.putFloat("ItemRotation", itemRotation);
	}

	private void writeEdgesToTag(CompoundTag tag) {
		if (grid == null)
			return;
		List<Integer> edgeList = new ArrayList<>();
		for (Edge edge : grid.edges) {
			edgeList.add(edge.first);
			edgeList.add(edge.second);
		}
		tag.putIntArray("Edges", edgeList.stream().mapToInt(i -> i).toArray());
	}

	private void readEdgesFromTag(CompoundTag tag) {
		int[] edgeInts = tag.getIntArray("Edges");
		if (grid == null) {
			grid = new RitualGrid(worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ());
		}
		grid.clearEdges();
		for (int i = 0; i < edgeInts.length; i += 2) {
			grid.edges.add(new Edge(edgeInts[i], edgeInts[i + 1]));
		}
	}

//	@Override
//	protected boolean shouldSyncOnUpdate() {
//		return true;
//	}

//	@Override
//	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
//		CompoundTag tag = pkt.getTag();
//		targetItem = ItemStack.of(tag.getCompound("TargetItem"));
//		resultPreview = ItemStack.of(tag.getCompound("ResultPreview"));
//		crafting = tag.getBoolean("Crafting");
//		progress = tag.getInt("Progress");
//		long[] pulse = tag.getLongArray("PulseStartTime");
//		if (pulse.length == 8)
//			System.arraycopy(pulse, 0, pulseStartTime, 0, 8);
//		readEdgesFromTag(tag);
//		itemRotation = tag.getFloat("ItemRotation");
//	}

//	@Override
//	public @NotNull CompoundTag getUpdateTag() {
//		return serializeNBT();
//	}
//
//	@Override
//	public void handleUpdateTag(CompoundTag tag) {
//		targetItem = ItemStack.of(tag.getCompound("TargetItem"));
//		resultPreview = ItemStack.of(tag.getCompound("ResultPreview"));
//		crafting = tag.getBoolean("Crafting");
//		progress = tag.getInt("Progress");
//		awakeTicks = tag.getInt("AwakeTicks");
//		ticksSinceOccupied = new int[8];
//		readEdgesFromTag(tag);
//	}

	@Override
	public AABB getRenderBoundingBox() {
		return new AABB(worldPosition.offset(-5, -5, -5), worldPosition.offset(5, 5, 5));
	}
}