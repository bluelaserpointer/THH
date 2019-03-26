package physicis;

public interface HasCoordinate {
	public static final HasCoordinate NULL_COORDINATE_SOURCE = new HasCoordinate() {
		private final Coordinate nullCoordinate = new Coordinate();
		@Override
		public Coordinate getCoordinate() {
			return nullCoordinate;
		}
	};
	public abstract Coordinate getCoordinate();
}
