package physics.hitShape;

import java.awt.Color;
import java.awt.Stroke;
import java.io.Serializable;

import core.GHQ;
import physics.HasBoundingBox;
import physics.HasPoint;
import physics.HitInteractable;
import physics.Point;

/**
 * A primal class for managing object hit area.Need {@link Point} to create this instance.<br>
 * The {@link HitShape#intersects(DoublePoint, HasHitShape, DoublePoint)} method provide geometry hit judge,
 * while {@link HitInteractable#intersects(HitInteractable)} can perform grouped hit judge.
 * 
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class HitShape implements HasBoundingBox, Serializable{
	private static final long serialVersionUID = -4544439590281786292L;
	public static final HitShape NULL_HITSHAPE = new HitShape() {
		private static final long serialVersionUID = -4252823940204998087L;
		@Override
		public int width() {
			return 0;
		}
		@Override
		public int height() {
			return 0;
		}
		@Override
		public boolean intersects(HitShape shape) {
			return false;
		}
		@Override
		public boolean intersectsDot(int x, int y) {
			return false;
		}
		@Override
		public boolean intersectsLine(int x1, int y1, int x2, int y2) {
			return false;
		}
		@Override
		public HitShape clone(HasPoint newOwner) {
			return this;
		}
		@Override
		public void fill(Color color) {}
		@Override
		public void draw(Color color, Stroke stroke) {}
	};

	protected HasPoint owner;
	public HitShape() {
		owner = HasPoint.NULL_COORDINATE_SOURCE;
	}
	public HitShape(HasPoint owner) {
		this.owner = owner;
	}
	public boolean intersects(HitShape shape) {
		return point().inRangeXY(shape.point(), (width() + shape.width())/2, (height() + shape.height())/2);
	}
	public final boolean intersects(HasHitShape target) {
		final HitShape SHAPE = target.hitShape();
		return SHAPE == null ? false : intersects(SHAPE);
	}
	public final boolean intersects_atNewPoint(double dx, double dy, HitShape shape) {
		point().addXY(dx, dy);
		final boolean JUDGE = intersects(shape);
		point().addXY(-dx, -dy);
		return JUDGE;
	}
	public final boolean intersects_atNewPoint(double dx, double dy, HasHitShape target) {
		final HitShape SHAPE = target.hitShape();
		return SHAPE == null ? false : intersects_atNewPoint(dx, dy, SHAPE);
	}
	public boolean intersectsDot(int x, int y) {
		return point().intAbsDX(x) < width()/2 && point().intAbsDX(y) < height()/2;
	}
	public boolean intersectsLine(int x1, int y1, int x2, int y2) {
		return point().getRectangle2D(width(), height()).intersectsLine(x1, y1, x2, y2);
	}
	
	//tool
	public abstract HitShape clone(HasPoint newOwner);
	public void fill(Color color) {
		GHQ.getG2D(color).fillRect(point().intX() - width()/2, point().intY() - height()/2, width(), height());
	}
	public void draw(Color color, Stroke stroke) {
		GHQ.getG2D(color).drawRect(point().intX() - width()/2, point().intY() - height()/2, width(), height());
	}
	
	//information
	@Override
	public Point point() {
		return owner.point();
	}
}
