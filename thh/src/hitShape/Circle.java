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
	
	public Circle(Point point, int radius) {
		super(point);
		RADIUS = radius;
	}
	@Override
	public boolean intersects(HitShape shape) {
		if(shape instanceof Circle) {
			final double DR = RADIUS + ((Circle)shape).RADIUS;
			return myPoint.distanceSq(shape.point()) < DR*DR;
		}else if(shape instanceof Square) {
			// TODO lacking strictness
			return myPoint.inRangeXY(shape.point(), RADIUS + ((Square)shape).SIDE/2);
		}else if(shape instanceof Rectangle) {
			// TODO lacking strictness
			return myPoint.inRangeXY(shape.point(), RADIUS + ((Rectangle)shape).WIDTH/2, RADIUS + ((Rectangle)shape).HEIGHT/2);
		}else {
			System.out.println("unhandled type: " + this.getClass().getName() + " against " + shape.getClass().getName());
		}
		return false;
	}
	@Override
	public boolean intersectsDot(int x, int y) {
		return super.myPoint.inRange(x, y, RADIUS);
	}
	@Override
	public boolean intersectsLine(int x1, int y1, int x2, int y2) {
		return Line2D.ptLineDistSq(x1, y1, x2, y2, myPoint.intX(), myPoint.intY()) < RADIUS*RADIUS;
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
	public HitShape clone(Point newPoint) {
		return new Circle(newPoint, RADIUS);
	}
	
	//information
	@Override
	public int width() {
		return RADIUS*2;
	}
	@Override
	public int height() {
		return RADIUS*2;
	}
}
