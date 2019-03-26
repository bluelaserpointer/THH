package geom;

import physicis.Coordinate;

public class Rectangle extends HitShape{
	private static final long serialVersionUID = 3869237032416439346L;
	public final int WIDTH,HEIGHT;
	public Rectangle(int w,int h) {
		WIDTH = w;
		HEIGHT = h;
	}
	@Override
	public boolean intersects(Coordinate coordinate1, HitShape shape, Coordinate coordinate2) {
		if(shape instanceof Rectangle) {
			return coordinate1.isCloser_DXDY(coordinate2, (WIDTH + ((Rectangle)shape).WIDTH)/2, (HEIGHT + ((Rectangle)shape).HEIGHT)/2);
		}else if(shape instanceof Square) {
			final int SIDE = ((Square)shape).SIDE;
			return coordinate1.isCloser_DXDY(coordinate2, (WIDTH + SIDE)/2, (HEIGHT + SIDE)/2);
		}
		return false;
	}
	
	//tool
	@Override
	public HitShape clone() {
		return new Rectangle(WIDTH, HEIGHT);
	}
	
	//information
	@Override
	public int getWidth() {
		return WIDTH;
	}
	@Override
	public int getHeight() {
		return HEIGHT;
	}
}
