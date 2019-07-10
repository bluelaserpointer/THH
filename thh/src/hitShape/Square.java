package hitShape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import core.GHQ;
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
		}else if(shape instanceof MyPolygon) {
			final Polygon POLY = ((MyPolygon)shape).polygon;
			return POLY.intersects(coordinate1.intX() - SIDE/2, coordinate1.intY() - SIDE/2, SIDE, SIDE);
		}
		return false;
	}
	@Override
	public boolean intersectsDot(int myX, int myY, int x, int y) {
		return Math.abs(myX - x) < SIDE/2 && Math.abs(myY - y) < SIDE/2;
	}
	@Override
	public boolean intersectsLine(int myX, int myY, int x1, int y1, int x2, int y2) {
		return new Rectangle2D.Double(myX - SIDE/2, myY - SIDE/2, myX + SIDE/2, myY + SIDE/2).intersectsLine(x1, y1, x2, y2);
	}
	@Override
	public void fill(Point point, Color color) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		final int TX = point.intX(), TY = point.intY();
		G2.translate(TX, TY);
		G2.setColor(color);
		G2.fillRect(-SIDE/2, -SIDE/2, SIDE, SIDE);
		G2.translate(-TX, -TY);
	}
	@Override
	public void draw(Point point, Color color, Stroke stroke) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		final int TX = point.intX(), TY = point.intY();
		G2.translate(TX, TY);
		G2.setColor(color);
		G2.setStroke(stroke);
		G2.drawRect(-SIDE/2, -SIDE/2, SIDE, SIDE);
		G2.translate(-TX, -TY);
	}
	
	//tool
	@Override
	public HitShape clone() {
		return new Square(SIDE);
	}
	
	//information
	@Override
	public int width() {
		return SIDE;
	}
	@Override
	public int height() {
		return SIDE;
	}
}
