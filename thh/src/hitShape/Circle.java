package hitShape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import core.GHQ;
import physics.Point;

public class Circle extends HitShape{
	private static final long serialVersionUID = -1809578801160258098L;
	public final int RADIUS;
	
	public Circle(int radius) {
		RADIUS = radius;
	}
	@Override
	public boolean intersects(Point p1, HitShape shape, Point p2) {
		if(shape instanceof Circle) {
			final double DR = RADIUS + ((Circle)shape).RADIUS;
			return p1.distanceSq(p2) < DR*DR;
		}else if(shape instanceof Square) {
			// TODO lacking strictness
			return p1.inRangeXY(p2, (RADIUS + ((Square)shape).SIDE)/2);
		}
		return false;
	}
	@Override
	public boolean intersectsDot(int myX, int myY, int x, int y) {
		final int DX = x - myX, DY = y - myY;
		return DX*DX + DY*DY < RADIUS*RADIUS;
	}
	@Override
	public boolean intersectsLine(int myX, int myY, int x1, int y1, int x2, int y2) {
		return Line2D.ptLineDistSq(x1, y1, x2, y2, myX, myY) < RADIUS*RADIUS;
	}
	@Override
	public void fill(Point point, Color color) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		final int TX = point.intX(), TY = point.intY();
		G2.translate(TX, TY);
		G2.setColor(color);
		G2.fillOval(-RADIUS/2, -RADIUS/2, RADIUS, RADIUS);
		G2.translate(-TX, -TY);
	}
	@Override
	public void draw(Point point, Color color, Stroke stroke) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		final int TX = point.intX(), TY = point.intY();
		G2.translate(TX, TY);
		G2.setColor(color);
		G2.setStroke(stroke);
		G2.drawOval(-RADIUS/2, -RADIUS/2, RADIUS, RADIUS);
		G2.translate(-TX, -TY);
	}
	
	//tool
	@Override
	public HitShape clone() {
		return new Circle(RADIUS);
	}
	
	//information
	@Override
	public int width() {
		return RADIUS;
	}
	@Override
	public int height() {
		return RADIUS;
	}
}
