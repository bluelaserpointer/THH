package physicis;

public interface CoordinateInteractable {
	public static final CoordinateInteractable BlankDI = new CoordinateInteractable() {
		private final Coordinate nullCoordinate = new Coordinate();
		@Override
		public Coordinate getCoordinate() {
			return nullCoordinate;
		}
	};
	public abstract Coordinate getCoordinate();
}
