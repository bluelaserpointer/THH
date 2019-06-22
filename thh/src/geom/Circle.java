package geom;

import physics.Point;

public class Circle extends HitShape{
	private static final long serialVersionUID = -1809578801160258098L;
	public final int RADIUS;
	
	public Circle(int radius) {
		RADIUS = radius;
	}
	@Override
	public boolean intersects(Point coordinate1, HitShape shape, Point coordinate2) {
		if(shape instanceof Circle) {
			final double DR = RADIUS + ((Circle)shape).RADIUS;
			return coordinate1.distanceSq(coordinate2) < DR*DR;
		}else if(shape instanceof Square) {
			// TODO lacking strictness
			return coordinate1.inRangeXY(coordinate2,(RADIUS + ((Square)shape).SIDE)/2);
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
