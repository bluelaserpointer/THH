package hitShape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import core.GHQ;
import physics.Point;

public class Rectangle extends HitShape{
	private static final long serialVersionUID = 3869237032416439346L;
	public final int WIDTH,HEIGHT;
	public Rectangle(int w,int h) {
		WIDTH = w;
		HEIGHT = h;
	}
	@Override
	public boolean intersects(Point coordinate1, HitShape shape, Point coordinate2) {
		if(shape instanceof Rectangle) {
			return coordinate1.inRangeXY(coordinate2, (WIDTH + ((Rectangle)shape).WIDTH)/2, (HEIGHT + ((Rectangle)shape).HEIGHT)/2);
		}else if(shape instanceof Square) {
			final int SIDE = ((Square)shape).SIDE;
			return coordinate1.inRangeXY(coordinate2, (WIDTH + SIDE)/2, (HEIGHT + SIDE)/2);
		}
		return false;
	}
	@Override
	public boolean intersectsDot(int myX, int myY, int x, int y) {
		return Math.abs(myX - x) < WIDTH/2 && Math.abs(myY - y) < HEIGHT/2;
	}
	@Override
	public boolean intersectsLine(int myX, int myY, int x1, int y1, int x2, int y2) {
		return new Rectangle2D.Double(myX - WIDTH/2, myY - HEIGHT/2, myX + WIDTH/2, myY + HEIGHT/2).intersectsLine(x1, y1, x2, y2);
	}
	@Override
	public void fill(Point point, Color color) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		final int TX = point.intX(), TY = point.intY();
		G2.translate(TX, TY);
		G2.setColor(color);
		G2.fillRect(-WIDTH/2, -HEIGHT/2, WIDTH, HEIGHT);
		G2.translate(-TX, -TY);
	}
	@Override
	public void draw(Point point, Color color, Stroke stroke) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		final int TX = point.intX(), TY = point.intY();
		G2.translate(TX, TY);
		G2.setColor(color);
		G2.setStroke(stroke);
		G2.drawRect(-WIDTH/2, -HEIGHT/2, WIDTH, HEIGHT);
		G2.translate(-TX, -TY);
	}
	
	//tool
	@Override
	public HitShape clone() {
		return new Rectangle(WIDTH, HEIGHT);
	}
	
	//information
	@Override
	public int width() {
		return WIDTH;
	}
	@Override
	public int height() {
		return HEIGHT;
	}
}
