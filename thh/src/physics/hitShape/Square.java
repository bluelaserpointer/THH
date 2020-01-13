package physics.hitShape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;

import core.GHQ;
import physics.HasPoint;

public class Square extends HitShape{
	private static final long serialVersionUID = 8168254451812660305L;
	protected int side;
	public Square(HasPoint owner, int side) {
		super(owner);
		this.side = side;
	}
	public Square(HasPoint owner) {
		super(owner);
	}
	public Square(int side) {
		this.side = side;
	}
	public Square() {}
	//init
	public Square setSide(int side) {
		this.side = side;
		return this;
	}
	@Override
	public boolean intersects(HitShape shape) {
		if(shape instanceof Square) {
			return point().inRangeXY(shape.point(), (side + ((Square)shape).side)/2);
		}else if(shape instanceof RectShape) {
			return point().inRangeXY(shape.point(), (side + ((RectShape)shape).width)/2, (side + ((RectShape)shape).height)/2);
		}else if(shape instanceof Circle) {
			// TODO lacking strictness
			return point().inRangeXY(shape.point(), (side + ((Circle)shape).radius)/2);
		}else if(shape instanceof MyPolygon) {
			final Polygon POLY = ((MyPolygon)shape).polygon;
			return POLY.intersects(point().intX() - side/2, point().intY() - side/2, side, side);
		}else {
			System.out.println("unhandled type: " + this.getClass().getName() + " against " + shape.getClass().getName());
		}
		return false;
	}
	@Override
	public boolean intersectsDot(int x, int y) {
		return point().intAbsDX(x) < side/2 && point().intAbsDY(y) < side/2;
	}
	@Override
	public boolean intersectsLine(int x1, int y1, int x2, int y2) {
		return point().getRectangle2D(side, side).intersectsLine(x1, y1, x2, y2);
	}
	@Override
	public void fill(Color color) {
		final Graphics2D G2 = GHQ.getG2D();
		final int TX = point().intX(), TY = point().intY();
		G2.translate(TX, TY);
		G2.setColor(color);
		G2.fillRect(-side/2, -side/2, side, side);
		G2.translate(-TX, -TY);
	}
	@Override
	public void draw(Color color, Stroke stroke) {
		final Graphics2D G2 = GHQ.getG2D();
		final int TX = point().intX(), TY = point().intY();
		G2.translate(TX, TY);
		G2.setColor(color);
		G2.setStroke(stroke);
		G2.drawRect(-side/2, -side/2, side, side);
		G2.translate(-TX, -TY);
	}
	
	//tool
	@Override
	public HitShape clone(HasPoint newOwner) {
		return new Square(newOwner, side);
	}
	
	//information
	public int side() {
		return side;
	}
	@Override
	public int width() {
		return side;
	}
	@Override
	public int height() {
		return side;
	}
}
