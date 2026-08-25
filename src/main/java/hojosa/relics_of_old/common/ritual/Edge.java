package hojosa.relics_of_old.common.ritual;

//An edge connecting two numbered points on the ritual grid
public class Edge {
	public final int first;
	public final int second;

	public Edge(int pointA, int pointB) {
		if (pointA == pointB) {
			throw new IllegalArgumentException("Can't connect point " + pointA + " to itself");
		}
		// Always store smaller index first for consistent equality
		if (pointB < pointA) {
			this.first = pointB;
			this.second = pointA;
		} else {
			this.first = pointA;
			this.second = pointB;
		}
	}

	public boolean hasPoint(int point) {
		return first == point || second == point;
	}

	public int otherPoint(int point) {
		if (first == point)
			return second;
		if (second == point)
			return first;
		throw new IllegalArgumentException("Point " + point + " not on edge " + this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Edge other) {
			return other.first == this.first && other.second == this.second;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (first + "," + second).hashCode();
	}

	@Override
	public String toString() {
		return "[" + first + ", " + second + "]";
	}
}