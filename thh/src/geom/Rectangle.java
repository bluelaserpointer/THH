package geom;

import java.awt.geom.Line2D;

import physicis.Coordinate;

public class Rectangle extends HitShape{
	public final int WIDTH,HEIGHT;
	public Rectangle(Coordinate coordinate, int w,int h) {
		super(coordinate);
		WIDTH = w;
		HEIGHT = h;
	}
	@Override
	public boolean intersects(HitShape shape) {
		if(shape instanceof Rectangle) {
			return Math.abs(super.COORDINATE.getDX(shape.COORDINATE)) < (WIDTH + ((Rectangle)shape).WIDTH)/2 && Math.abs(super.COORDINATE.getDY(shape.COORDINATE)) < (HEIGHT + ((Rectangle)shape).HEIGHT)/2;
		}else if(shape instanceof Square) {
			final int SIDE = ((Square)shape).SIDE;
			return Math.abs(super.COORDINATE.getDX(shape.COORDINATE)) < (WIDTH + SIDE)/2 && Math.abs(super.COORDINATE.getDY(shape.COORDINATE)) < (HEIGHT + SIDE)/2;
		}
		return false;
	}
	@Override
	public boolean intersects(Line2D line) {
		// TODO Auto-generated method stub
		return false;
	}
}
