package hitShape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.Collection;

import core.ErrorCounter;
import core.GHQ;
import physics.Point;

public class MyPolygon extends HitShape{
	private static final long serialVersionUID = 421774100779183845L;

	protected Polygon polygon;
	private Point points[];
	
	private static final Polygon setPolygon(Point[] points) {
		final int[] px = new int[points.length], py = new int[points.length];
		for(int i = 0;i < points.length;++i) {
			px[i] = points[i].intX();
			py[i] = points[i].intY();
		}
		return new Polygon(px, py, points.length);
	}
	public MyPolygon(Point basePoint, Point[] points) {
		super(basePoint);
		polygon = setPolygon(this.points = points);
	}
	public MyPolygon(Point basePoint, Collection<Point> points) {
		super(basePoint);
		polygon = setPolygon(this.points = points.toArray(new Point.IntPoint[0]));
	}
	public MyPolygon(Point basePoint, int[] pointX, int[] pointY) {
		super(basePoint);
		if(pointX.length != pointY.length)
			ErrorCounter.put("Terrain's constructer called Illegally: pointX.length = " + pointX.length + ", pointY.length = " + pointY.length);
		polygon = new Polygon(pointX, pointY, Math.min(pointX.length, pointY.length));
		points = new Point[pointX.length];
		for(int i = 0;i < pointX.length;++i)
			points[i] = new Point.IntPoint(pointX[i], pointY[i]);
	}
	@Override
	public boolean intersects(HitShape shape) {
		final Point P2 = shape.point();
		if(shape instanceof Rectangle) {
			final Rectangle RECT = (Rectangle)shape;
			return polygon.intersects(P2.intX(), P2.intY(), RECT.WIDTH, RECT.HEIGHT);
		}else if(shape instanceof Square) {
			final int SIDE = ((Square)shape).SIDE;
			return polygon.intersects(P2.intX(), P2.intY(), SIDE, SIDE);
		}else if(shape instanceof Circle) {
			final int SIDE = ((Circle)shape).RADIUS*2;
			return polygon.intersects(P2.intX(), P2.intY(), SIDE, SIDE);
		}else {
			System.out.println("unhandled type: " + this.getClass().getName() + " against " + shape.getClass().getName());
		}
		return false;
	}

	@Override
	public boolean intersectsDot(int x, int y) {
		return polygon.contains(x - myPoint.intX(), y - myPoint.intY());
	}

	@Override
	public boolean intersectsLine(int x1, int y1, int x2, int y2) {
		final Line2D line = new Line2D.Double(x1 - myPoint.intX(), y1 - myPoint.intY(), x2 - myPoint.intX(), y2 - myPoint.intY());
		if(line.intersectsLine(points[points.length - 1].intX(), points[points.length - 1].intY(), points[0].intX(), points[0].intY()))
			return true;
		for(int i = 0;i < points.length - 1;i++) {
			if(line.intersectsLine(points[i].intX(), points[i].intY(), points[i + 1].intX(), points[i + 1].intY()))
				return true;
		}
		return false;
	}
	@Override
	public void fill(Point point, Color color) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		final int TX = point.intX(), TY = point.intY();
		G2.translate(TX, TY);
		G2.setColor(color);
		G2.fill(polygon);
		G2.translate(-TX, -TY);
	}
	@Override
	public void draw(Point point, Color color, Stroke stroke) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		final int TX = point.intX(), TY = point.intY();
		G2.translate(TX, TY);
		G2.setColor(color);
		G2.setStroke(stroke);
		G2.draw(polygon);
		G2.translate(-TX, -TY);
	}
	//information
	@Override
	public HitShape clone(Point newPoint) {
		return new MyPolygon(newPoint, points);
	}
	public Point[] getPoints() {
		return points;
	}
	@Override
	public int width() {
		return polygon.getBounds().width;
	}
	@Override
	public int height() {
		return polygon.getBounds().height;
	}
}
