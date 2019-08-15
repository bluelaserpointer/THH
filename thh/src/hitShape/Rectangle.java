package hitShape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import core.GHQ;
import physics.Point;

public class Rectangle extends HitShape{
	private static final long serialVersionUID = 3869237032416439346L;
	public final int WIDTH,HEIGHT;
	public Rectangle(Point point, int w, int h) {
		super(point);
		WIDTH = w;
		HEIGHT = h;
	}
	@Override
	public boolean intersects(HitShape shape) {
		if(shape instanceof Rectangle) {
			return point().inRangeXY(shape.point(), (WIDTH + ((Rectangle)shape).WIDTH)/2, (HEIGHT + ((Rectangle)shape).HEIGHT)/2);
		}else if(shape instanceof Square) {
			final int SIDE = ((Square)shape).SIDE;
			return point().inRangeXY(shape.point(), (WIDTH + SIDE)/2, (HEIGHT + SIDE)/2);
		}else if(shape instanceof Circle) {
			final int RADIUS = ((Circle)shape).RADIUS;
			return point().inRangeXY(shape.point(), RADIUS + WIDTH/2, RADIUS + HEIGHT/2);
		}else {
			System.out.println("unhandled type: " + this.getClass().getName() + " against " + shape.getClass().getName());
		}
		return false;
	}
	@Override
	public boolean intersectsDot(int x, int y) {
		return myPoint.intAbsDX(x) < WIDTH/2 && myPoint.intAbsDX(y) < HEIGHT/2;
	}
	@Override
	public boolean intersectsLine(int x1, int y1, int x2, int y2) {
		return myPoint.getRectangle2D(WIDTH, HEIGHT).intersectsLine(x1, y1, x2, y2);
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
	public HitShape clone(Point newPoint) {
		return new Rectangle(newPoint, WIDTH, HEIGHT);
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
