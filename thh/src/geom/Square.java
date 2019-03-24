package geom;

import java.awt.geom.Line2D;

import physicis.Coordinate;

public class Square extends HitShape{
	public final int SIDE;
	public Square(Coordinate coordinate, int side) {
		super(coordinate);
		SIDE = side;
	}
	@Override
	public boolean intersects(HitShape shape) {
		final int DX = Math.abs((int)super.COORDINATE.getDX(shape.COORDINATE)),DY = Math.abs((int)super.COORDINATE.getDY(shape.COORDINATE));
		if(shape instanceof Square) {
			final int S2 = (SIDE + ((Square)shape).SIDE)/2;
			return DX < S2 && DY < S2;
		}else if(shape instanceof Rectangle) {
			return DX < (SIDE + ((Rectangle)shape).WIDTH)/2 && DY < (SIDE + ((Rectangle)shape).HEIGHT)/2;
		}else if(shape instanceof Circle) {
			// TODO lacking strictness
			final int S2 = (SIDE + ((Circle)shape).RADIUS)/2;
			return DX < S2 && DY < S2;
		}
		return false;
	}

	@Override
	public boolean intersects(Line2D line) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int getSide() {
		return SIDE;
	}
	
}
