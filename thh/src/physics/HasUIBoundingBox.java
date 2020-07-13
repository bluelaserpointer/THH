package physics;

public interface HasUIBoundingBox extends HasBoundingBox {
	@Override
	public default int centerX() {
		return point().intX() + width()/2;
	}
	@Override
	public default int centerY() {
		return point().intY() + height()/2;
	}
	@Override
	public default int leftX() {
		return point().intX();
	}
	@Override
	public default int topY() {
		return point().intY();
	}
}
