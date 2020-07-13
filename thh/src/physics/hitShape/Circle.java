package physics.hitShape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import core.GHQ;
import physics.HasPoint;
import preset.structure.Tile.TileHitShape;

public class Circle extends HitShape {
	private static final long serialVersionUID = -1809578801160258098L;
	public int radius;
	
	public Circle(HasPoint owner, int radius) {
		super(owner);
		this.radius = radius;
	}
	//init
	public Circle setRadius(int radius) {
		this.radius = radius;
		return this;
	}
	@Override
	public boolean intersects(HitShape shape) {
		if(shape instanceof Circle) {
			final double DR = radius + ((Circle)shape).radius;
			return point().distanceSq(shape.point()) < DR*DR;
		}else if(shape instanceof Square) {
			// TODO lacking strictness
			return point().inRangeXY(shape.point(), radius + ((Square)shape).side/2);
		}else if(shape instanceof RectShape) {
			// TODO lacking strictness
			return point().inRangeXY(shape.point(), radius + ((RectShape)shape).width/2, radius + ((RectShape)shape).height/2);
		}else if(shape instanceof TileHitShape) {
			// TODO lacking strictness
			return ((TileHitShape)shape).intersects(this);
		}else {
			System.out.println("unhandled type: " + this.getClass().getName() + " against " + shape.getClass().getName());
		}
		return false;
	}
	@Override
	public boolean intersectsDot(int x, int y) {
		return point().inRange(x, y, radius);
	}
	@Override
	public boolean intersectsLine(int x1, int y1, int x2, int y2) {
		return Line2D.ptLineDistSq(x1, y1, x2, y2, point().intX(), point().intY()) < radius*radius;
	}
	@Override
	public void fill(Color color) {
		final Graphics2D G2 = GHQ.getG2D();
		final int TX = point().intX(), TY = point().intY();
		G2.translate(TX, TY);
		G2.setColor(color);
		G2.fillOval(-radius/2, -radius/2, radius, radius);
		G2.translate(-TX, -TY);
	}
	@Override
	public void draw(Color color, Stroke stroke) {
		final Graphics2D G2 = GHQ.getG2D();
		final int TX = point().intX(), TY = point().intY();
		G2.translate(TX, TY);
		G2.setColor(color);
		G2.setStroke(stroke);
		G2.drawOval(-radius/2, -radius/2, radius, radius);
		G2.translate(-TX, -TY);
	}
	
	//tool
	@Override
	public HitShape clone(HasPoint newOwner) {
		return new Circle(newOwner, radius);
	}
	
	//information
	public int radius() {
		return radius;
	}
	@Override
	public int width() {
		return radius*2;
	}
	@Override
	public int height() {
		return radius*2;
	}
}
