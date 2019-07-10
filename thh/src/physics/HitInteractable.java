package physics;

import java.awt.geom.Rectangle2D;

import hitShape.HasHitShape;
import hitShape.HitShape;

public interface HitInteractable extends HasPoint, HasHitShape, HasStandpoint, HasBoundingBox{
	public static final HitInteractable NULL_HIT_INTERACTABLE = new HitInteractable() {
		@Override
		public Point point() {
			return null;
		}
		@Override
		public HitShape hitShape() {
			return null;
		}
		@Override
		public Standpoint standpoint() {
			return null;
		}
	};
	public default boolean intersects(HitInteractable target) {
		return !isFriend(target) && hitShape().intersects(point(), target, target.point());
	}
	public default boolean intersects(HitInteractable target, Point newPoint) {
		return !isFriend(target) && hitShape().intersects(newPoint, target, target.point());
	}
	public default boolean intersectsDot(int x, int y) {
		return hitShape().intersectsDot(point().intX(), point().intY(), x, y);
	}
	public default boolean intersectsLine(int x1, int y1, int x2, int y2) {
		return hitShape().intersectsLine(point().intX(), point().intY(), x1, y1, x2, y2);
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
