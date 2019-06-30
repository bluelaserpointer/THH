package physics;

import java.util.LinkedList;

public class Route {
	final LinkedList<Point> points;
	public Route(LinkedList<Point> points) {
		this.points = points;
	}
	public Point removeNext() {
		return points.isEmpty() ? null : points.removeLast();
	}
	public Point peek() {
		return points.isEmpty() ? null : points.peekLast();
	}
	public boolean isEmpty() {
		return points.isEmpty();
	}
	public void clear() {
		points.clear();
	}
}
