package physics;

public interface HasGridPoint extends HasPoint {
	public abstract GridPoint gridPoint();
	@Override
	public default Point point() {
		return gridPoint();
	}
}
