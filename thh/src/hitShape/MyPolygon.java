package hitShape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.Arrays;

import core.ErrorCounter;
import core.GHQ;
import physics.Point;

public class MyPolygon extends HitShape{
	private static final long serialVersionUID = 421774100779183845L;

	protected Polygon polygon;
	public final int WIDTH, HEIGHT;
	private int px[], py[];

	public MyPolygon() {
		polygon = new Polygon();
		final java.awt.Rectangle RECT = polygon.getBounds();
		WIDTH = RECT.width;
		HEIGHT = RECT.height;
	}
	public MyPolygon(Polygon polygon) {
		this.polygon = polygon;
		final java.awt.Rectangle RECT = polygon.getBounds();
		WIDTH = RECT.width;
		HEIGHT = RECT.height;
	}
	public MyPolygon(int[] pointX,int[] pointY) {
		if(pointX.length < pointY.length) {
			ErrorCounter.put("Terrain's constructer called Illegally: pointX.length = " + pointX.length + ", pointY.length = " + pointY.length);
			polygon = new Polygon(pointX,pointY,pointX.length);
			px = pointX;py = Arrays.copyOf(pointY, pointX.length);
		}else if(pointX.length > pointY.length){
			ErrorCounter.put("Terrain's constructer called Illegally: pointX.length = " + pointX.length + ", pointY.length = " + pointY.length);
			polygon = new Polygon(pointX,pointY,pointY.length);
			px = Arrays.copyOf(pointX, pointY.length);py = pointY;
		}else {
			polygon = new Polygon(pointX,pointY,pointX.length);
			px = pointX;py = pointY;
		}
		final java.awt.Rectangle RECT = polygon.getBounds();
		WIDTH = RECT.width;
		HEIGHT = RECT.height;
	}
	@Override
	public boolean intersects(Point p1, HitShape shape, Point p2) {
		if(shape instanceof Rectangle) {
			final Rectangle RECT = (Rectangle)shape;
			return polygon.intersects(p2.intX(), p2.intY(), RECT.WIDTH, RECT.HEIGHT);
		}else if(shape instanceof Square) {
			final int SIDE = ((Square)shape).SIDE;
			return polygon.intersects(p2.intX(), p2.intY(), SIDE, SIDE);
		}else if(shape instanceof Circle) {
			final int SIDE = ((Circle)shape).RADIUS*2;
			return polygon.intersects(p2.intX(), p2.intY(), SIDE, SIDE);
		}
		//System.out.println("out:" + x + ", " + y);
		return false;
	}

	@Override
	public boolean intersectsDot(int myX, int myY, int x, int y) {
		return polygon.contains(x, y);
	}

	@Override
	public boolean intersectsLine(int myX, int myY, int x1, int y1, int x2, int y2) {
		final Line2D line = new Line2D.Double(x1, y1, x2, y2);
		for(int i = 0;i < px.length - 1;i++) {
			if(line.intersectsLine(px[i], py[i], px[i + 1], py[i + 1]))
				return true;
		}
		if(line.intersectsLine(px[px.length - 1], py[px.length - 1], px[0], py[0]))
			return true;
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

	@Override
	public HitShape clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int width() {
		return WIDTH;
	}

	@Override
	public int height() {
		return HEIGHT;
	}

}
