package geom;

import physics.Point;

public class Square extends HitShape{
	private static final long serialVersionUID = 8168254451812660305L;
	public final int SIDE;
	public Square(int side) {
		SIDE = side;
	}
	@Override
	public boolean intersects(Point coordinate1, HitShape shape, Point coordinate2) {
		if(shape instanceof Square) {
			return coordinate1.inRangeXY(coordinate2, (SIDE + ((Square)shape).SIDE)/2);
		}else if(shape instanceof Rectangle) {
			return coordinate1.inRangeXY(coordinate2, (SIDE + ((Rectangle)shape).WIDTH)/2, (SIDE + ((Rectangle)shape).HEIGHT)/2);
		}else if(shape instanceof Circle) {
			// TODO lacking strictness
			return coordinate1.inRangeXY(coordinate2, (SIDE + ((Circle)shape).RADIUS)/2);
		}
		return false;
	}
	
	//tool
	@Override
	public HitShape clone() {
		return new Square(SIDE);
	}
	
	//information
	@Override
	public int getWidth() {
		return SIDE;
	}
	@Override
	public int getHeight() {
		return SIDE;
	}
}
