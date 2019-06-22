package core;

import java.awt.geom.Rectangle2D;

import geom.HasHitShape;
import geom.HitShape;
import physics.HasPoint;
import physics.Point;

public interface HitInteractable extends HasPoint,HasHitShape,HasStandpoint,HasBoundingBox{
	public static final HitInteractable NULL_HIT_INTERACTABLE = new HitInteractable() {
		@Override
		public Point getPoint() {
			return null;
		}
		@Override
		public HitShape getHitShape() {
			return null;
		}
		@Override
		public Standpoint getStandpoint() {
			return null;
		}
	};
	public default boolean isHit(HitInteractable target) {
		return !isFriend(target) && getHitShape().intersects(getPoint(), target, target.getPoint());
	}
	@Override
	public default Rectangle2D getBoundingBox() {
		final Point cod = getPoint();
		final HitShape hitshape = getHitShape();
		return new Rectangle2D.Double(cod.doubleX(), cod.doubleY(), hitshape.getWidth(), hitshape.getHeight());
	}
}
