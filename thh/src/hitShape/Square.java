package hitShape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;

import core.GHQ;
import physics.Point;

public class Square extends HitShape{
	private static final long serialVersionUID = 8168254451812660305L;
	public final int SIDE;
	public Square(Point point, int side) {
		super(point);
		SIDE = side;
	}
	@Override
	public boolean intersects(HitShape shape) {
		if(shape instanceof Square) {
			return point().inRangeXY(shape.point(), (SIDE + ((Square)shape).SIDE)/2);
		}else if(shape instanceof Rectangle) {
			return point().inRangeXY(shape.point(), (SIDE + ((Rectangle)shape).WIDTH)/2, (SIDE + ((Rectangle)shape).HEIGHT)/2);
		}else if(shape instanceof Circle) {
			// TODO lacking strictness
			return point().inRangeXY(shape.point(), (SIDE + ((Circle)shape).RADIUS)/2);
		}else if(shape instanceof MyPolygon) {
			final Polygon POLY = ((MyPolygon)shape).polygon;
			return POLY.intersects(point().intX() - SIDE/2, point().intY() - SIDE/2, SIDE, SIDE);
		}else {
			System.out.println("unhandled type: " + this.getClass().getName() + " against " + shape.getClass().getName());
		}
		return false;
	}
	@Override
	public boolean intersectsDot(int x, int y) {
		return myPoint.intAbsDX(x) < SIDE/2 && myPoint.intAbsDY(y) < SIDE/2;
	}
	@Override
	public boolean intersectsLine(int x1, int y1, int x2, int y2) {
		return myPoint.getRectangle2D(SIDE, SIDE).intersectsLine(x1, y1, x2, y2);
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
	public HitShape clone(Point newPoint) {
		return new Square(newPoint, SIDE);
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
