package geom;

import java.awt.geom.Line2D;

public class Square implements HitShape{
	public final int SIDE;
	public Square(int side) {
		SIDE = side;
	}
	@Override
	public boolean intersects(int x1, int y1, HitShape shape, int x2, int y2) {
		if(shape instanceof Square) {
			final int S2 = (SIDE + ((Square)shape).SIDE)/2;
			return Math.abs(x1 - x2) < S2 && Math.abs(y1 - y2) < S2;
		}else if(shape instanceof Rectangle) {
			return Math.abs(x1 - x2) < (SIDE + ((Rectangle)shape).WIDTH)/2 && Math.abs(y1 - y2) < (SIDE + ((Rectangle)shape).HEIGHT)/2;
		}else if(shape instanceof Circle) {
			// TODO lacking strictness
			final int S2 = (SIDE + ((Circle)shape).RADIUS)/2;
			return Math.abs(x1 - x2) < S2 && Math.abs(y1 - y2) < S2;
		}
		return false;
	}

	@Override
	public boolean intersects(int x1, int y1, Line2D line) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int getSide() {
		return SIDE;
	}
	
}
