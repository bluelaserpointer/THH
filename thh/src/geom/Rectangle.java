package geom;

import java.awt.geom.Line2D;

public class Rectangle extends HitShape{
	public final int WIDTH,HEIGHT;
	public Rectangle(int w,int h) {
		WIDTH = w;
		HEIGHT = h;
	}
	@Override
	public boolean intersects(int x1, int y1, HitShape shape, int x2, int y2) {
		if(shape instanceof Rectangle) {
			return Math.abs(x1 - x2) < (WIDTH + ((Rectangle)shape).WIDTH)/2 && Math.abs(y1 - y2) < (HEIGHT + ((Rectangle)shape).HEIGHT)/2;
		}else if(shape instanceof Square) {
			final int SIDE = ((Square)shape).SIDE;
			return Math.abs(x1 - x2) < (WIDTH + SIDE)/2 && Math.abs(y1 - y2) < (HEIGHT + SIDE)/2;
		}
		return false;
	}
	@Override
	public boolean intersects(int x1, int y1, Line2D line) {
		// TODO Auto-generated method stub
		return false;
	}
}
