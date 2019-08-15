package physics;

import java.awt.geom.Rectangle2D;

import hitShape.HasHitShape;
import hitShape.HitShape;

public interface HitInteractable extends HasHitShape, HasStandpoint{
	public static final HitInteractable NULL_HIT_INTERACTABLE = new HitInteractable() {
		@Override
		public Point point() {
			return Point.NULL_POINT;
		}
		@Override
		public HitShape hitShape() {
			return null;
		}
		@Override
		public Standpoint standpoint() {
			return null;
		}
		@Override
		public int width() {
			return 0;
		}
		@Override
		public int height() {
			return 0;
		}
	};
	public default boolean intersects(HitInteractable target) {
		return !isFriend(target) && hitShape().intersects(target);
	}
	public default boolean intersects_atNewPoint(double dx, double dy, HitInteractable target) {
		return !isFriend(target) && hitShape().intersects_atNewPoint(dx, dy, target);
	}
	public default boolean intersectsDot(int x, int y) {
		return hitShape().intersectsDot(x, y);
	}
	public default boolean intersectsLine(int x1, int y1, int x2, int y2) {
		return hitShape().intersectsLine(x1, y1, x2, y2);
	}
	public default boolean intersectsLine(Point p1, Point p2) {
		return intersectsLine(p1.intX(), p1.intY(), p2.intX(), p2.intY());
	}
	public default boolean intersectsLine(HasDynam object1, HasDynam object2) {
		return object1 == null || object2 == null ? false : intersectsLine(object1.dynam(), object2.dynam());
	}
	@Override
	public default Rectangle2D boundingBox() {
		final Point cod = point();
		final HitShape hitshape = hitShape();
		return new Rectangle2D.Double(cod.doubleX(), cod.doubleY(), hitshape.width(), hitshape.height());
	}
}
