package geom;

import java.awt.geom.Line2D;

import physicis.Coordinate;

public class Circle extends HitShape{
	public final int RADIUS;
	
	public Circle(Coordinate coordinate, int radius) {
		super(coordinate);
		RADIUS = radius;
	}
	@Override
	public boolean intersects(HitShape shape) {
		if(shape instanceof Circle) {
			final double DR = RADIUS + ((Circle)shape).RADIUS;
			return super.COORDINATE.getDistance(shape.COORDINATE) < DR*DR;
		}else if(shape instanceof Square) {
			// TODO lacking strictness
			final int S2 = (RADIUS + ((Square)shape).SIDE)/2;
			return Math.abs(super.COORDINATE.getDX(shape.COORDINATE)) < S2 && Math.abs(super.COORDINATE.getDY(shape.COORDINATE)) < S2;
		}
		return false;
	}

	@Override
	public boolean intersects(Line2D line) {
		return line.ptLineDistSq(super.COORDINATE.getX() ,super.COORDINATE.getY()) <= RADIUS;
	}

}
