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
}
