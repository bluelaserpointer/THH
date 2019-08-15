package hitShape;

import java.awt.Color;
import java.awt.Stroke;
import java.io.Serializable;

import physics.HasBoundingBox;
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
	public static final HitShape NULL_HITSHAPE = new HitShape(Point.NULL_POINT) {
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
		public HitShape clone(Point newPoint) {
			return null;
		}
		@Override
		public void fill(Point point, Color color) {}
		@Override
		public void draw(Point point, Color color, Stroke stroke) {}
	};

	protected Point myPoint;
	public HitShape() {
		myPoint = Point.NULL_POINT;
	}
	public HitShape(Point myPoint) {
		this.myPoint = myPoint == null ? new Point.IntPoint() : myPoint;
	}
	public void setPoint(Point point) {
		myPoint = point;
	}
	public abstract boolean intersects(HitShape shape);
	public final boolean intersects(HasHitShape target) {
		final HitShape SHAPE = target.hitShape();
		return SHAPE == null ? false : intersects(SHAPE);
	}
	public final boolean intersects_atNewPoint(double dx, double dy, HitShape shape) {
		myPoint.addXY(dx, dy);
		final boolean JUDGE = intersects(shape);
		myPoint.addXY(-dx, -dy);
		return JUDGE;
	}
	public final boolean intersects_atNewPoint(double dx, double dy, HasHitShape target) {
		final HitShape SHAPE = target.hitShape();
		return SHAPE == null ? false : intersects_atNewPoint(dx, dy, SHAPE);
	}
	public abstract boolean intersectsDot(int x, int y);
	public abstract boolean intersectsLine(int x1, int y1, int x2, int y2);
	
	//tool
	public abstract HitShape clone(Point newPoint);
	public abstract void fill(Point point, Color color);
	public abstract void draw(Point point, Color color, Stroke stroke);
	
	//information
	@Override
	public Point point() {
		return myPoint;
	}
}
