package physics.hitShape;

import java.awt.Polygon;

import core.GHQ;
import physics.HasPoint;

public class Square extends AbstractRectShape {
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
	
	//tool
	@Override
	public HitShape clone(HasPoint newOwner) {
		return new Square(newOwner, side);
	}
	
	//information
	public int side() {
		return side != AbstractRectShape.MATCH_SCREEN_SIZE ? side : Math.min(GHQ.screenW(), GHQ.screenH());
	}
	@Override
	public int width() {
		return side();
	}
	@Override
	public int height() {
		return side();
	}
	public Square setSide(int side) {
		this.side = side;
		return this;
	}
	@Override
	public Square setWidth(int w) {
		return setSide(w);
	}
	@Override
	public Square setHeight(int h) {
		return setSide(h);
	}
}
