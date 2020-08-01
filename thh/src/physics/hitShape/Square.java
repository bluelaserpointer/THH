package physics.hitShape;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Stroke;

import core.GHQ;
import physics.HasPoint;

public class Square extends HitShape {
	private static final long serialVersionUID = 8168254451812660305L;
	public static final Square SQUARE_10 = new Square(10);
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
	public boolean boundingBoxIntersectsDot(int x, int y) {
		return point().intAbsDX(x) < side/2 && point().intAbsDY(y) < side/2;
	}
	@Override
	public boolean intersectsDot(int x, int y) {
		return super.boundingBoxIntersectsDot(x, y);
	}
	@Override
	public boolean intersectsLine(int x1, int y1, int x2, int y2) {
		return super.boundingBoxIntersectsLine(x1, y1, x2, y2);
	}
	@Override
	public void fill(Color color) {
		GHQ.getG2D(color).fillRect(point().intX() - side/2, point().intY() - side/2, side, side);
	}
	@Override
	public void draw(Color color, Stroke stroke) {
		GHQ.getG2D(color).drawRect(point().intX() - side/2, point().intY() - side/2, side, side);
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
