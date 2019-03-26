package geom;

import physicis.Coordinate;

public class Circle extends HitShape{
	private static final long serialVersionUID = -1809578801160258098L;
	public final int RADIUS;
	
	public Circle(int radius) {
		RADIUS = radius;
	}
	@Override
	public boolean intersects(Coordinate coordinate1, HitShape shape, Coordinate coordinate2) {
		if(shape instanceof Circle) {
			final double DR = RADIUS + ((Circle)shape).RADIUS;
			return coordinate1.getDistance(coordinate2) < DR*DR;
		}else if(shape instanceof Square) {
			// TODO lacking strictness
			return coordinate1.isCloser_DXDY(coordinate2,(RADIUS + ((Square)shape).SIDE)/2);
		}
		return false;
	}
	
	//tool
	@Override
	public HitShape clone() {
		return new Circle(RADIUS);
	}
	
	//information
	@Override
	public int getWidth() {
		return RADIUS;
	}
	@Override
	public int getHeight() {
		return RADIUS;
	}
}
