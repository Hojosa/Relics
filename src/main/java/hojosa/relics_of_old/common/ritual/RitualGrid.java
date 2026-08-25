package hojosa.relics_of_old.common.ritual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RitualGrid {

	public Set<Edge> edges;
	public BlockPos center;
	public BlockPos[] places;
	public int[] offsets;

	// All valid edges (excluding directly opposite pairs)
	public static final List<Edge> LEGIT_EDGES = new ArrayList<>();

	static {
		for (int i = 0; i < 8; i++) {
			for (int j = i + 1; j < 8; j++) {
				LEGIT_EDGES.add(new Edge(i, j));
			}
		}
		// Remove opposite pairs
		LEGIT_EDGES.remove(new Edge(0, 4));
		LEGIT_EDGES.remove(new Edge(1, 5));
		LEGIT_EDGES.remove(new Edge(2, 6));
		LEGIT_EDGES.remove(new Edge(3, 7));
	}

	public RitualGrid(int x, int y, int z) {
		this.center = new BlockPos(x, y, z);
		this.edges = new HashSet<>();
		this.places = new BlockPos[8];
		this.offsets = new int[8];

		// 8 points in a circle, roughly radius 3
		places[0] = new BlockPos(x + 3, y, z);
		places[1] = new BlockPos(x + 2, y, z + 2);
		places[2] = new BlockPos(x, y, z + 3);
		places[3] = new BlockPos(x - 2, y, z + 2);
		places[4] = new BlockPos(x - 3, y, z);
		places[5] = new BlockPos(x - 2, y, z - 2);
		places[6] = new BlockPos(x, y, z - 3);
		places[7] = new BlockPos(x + 2, y, z - 2);

		generateOffsets();
	}

	// Deterministic offsets seeded by position
	private void generateOffsets() {
		Random rand = new Random(center.hashCode() + 1);
		for (int i = 0; i < offsets.length; i++) {
			int offset = rand.nextInt(6) + 1;
			if (offset >= 4) {
				++offset;
			}
			offsets[i] = offset;
		}
	}

	public void clearEdges() {
		edges.clear();
	}

	// Creates or toggles an edge from the given point using the offset table
	public int makeEdgeFrom(int position, int offsetIndex) {
		int offset = offsets[offsetIndex];
		int end = (position + offset) % 8;
		if (offset != 0) {
			Edge edge = new Edge(position, end);
			if (edges.contains(edge)) {
				edges.remove(edge);
			} else {
				edges.add(edge);
			}
		}
		return end;
	}

	public List<Integer> pointsConnectedTo(int point) {
		List<Integer> points = new ArrayList<>();
		for (Edge edge : edges) {
			if (edge.hasPoint(point)) {
				points.add(edge.otherPoint(point));
			}
		}
		return points;
	}

	public List<Integer> inhabitedPoints(Level level) {
		List<Integer> points = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			if (!level.getBlockState(places[i]).isAir()) {
				points.add(i);
			}
		}
		return points;
	}

	// Grid is stable when all 8 points have blocks and all edge endpoints are
	// populated
	public boolean isGridStable(Level level) {
		if (edges.isEmpty())
			return false;
		List<Integer> populated = inhabitedPoints(level);
		for (int i : populated) {
			for (int j : pointsConnectedTo(i)) {
				if (!populated.contains(j))
					return false;
			}
		}
		return populated.size() == 8;
	}

	public Block blockOnPoint(int point, Level level) {
		return level.getBlockState(places[point]).getBlock();
	}

	public BlockState stateOnPoint(int point, Level level) {
		return level.getBlockState(places[point]);
	}
}