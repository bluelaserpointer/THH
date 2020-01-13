package physics;

public interface HasPoint {
	public static final HasPoint NULL_COORDINATE_SOURCE = new HasPoint() {
		private final Point nullCoordinate = new Point.IntPoint();
		@Override
		public Point point() {
			return nullCoordinate;
		}
	};
	public abstract Point point();
	public default int intX() {
		return point().intX();
	}
	public default int intY() {
		return point().intY();
	}
	public default double doubleX() {
		return point().doubleX();
	}
	public default double doubleY() {
		return point().doubleY();
	}
}
