package physics;

import java.awt.geom.Rectangle2D;

import physics.hitShape.HasHitShape;
import physics.hitShape.HitShape;
import physics.hitShape.RectShape;

public interface HitInteractable extends HasHitShape, HasHitGroup{
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
		public HitRule hitGroup() {
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
		return hitableGroup(target) && hitShape().intersects(target);
	}
	public default boolean intersects_atNewPoint(double dx, double dy, HitInteractable target) {
		return hitableGroup(target) && hitShape().intersects_atNewPoint(dx, dy, target);
	}
	@Override
	public default boolean intersectsDot(int x, int y) {
		return hitShape().intersectsDot(x, y);
	}
	public default boolean intersectsLine(int x1, int y1, int x2, int y2) {
		return hitShape().intersectsLine(x1, y1, x2, y2);
	}
	public default boolean intersectsLine(Point p1, Point p2) {
		return intersectsLine(p1.intX(), p1.intY(), p2.intX(), p2.intY());
	}
	public default boolean intersectsLine(HasPoint object1, HasPoint object2) {
		return object1 == null || object2 == null ? false : intersectsLine(object1.point(), object2.point());
	}
	public default boolean intersectsRect(int x, int y, int w, int h) {
		return hitShape().intersects(new RectShape(x, y, w, h));
	}
	@Override
	public default Rectangle2D boundingBox() {
		final Point cod = point();
		final HitShape hitshape = hitShape();
		return new Rectangle2D.Double(cod.doubleX(), cod.doubleY(), hitshape.width(), hitshape.height());
	}
}
