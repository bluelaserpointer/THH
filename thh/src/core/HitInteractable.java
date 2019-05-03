package core;

import java.awt.geom.Rectangle2D;

import geom.HasHitShape;
import geom.HitShape;
import physics.Coordinate;
import physics.HasCoordinate;

public interface HitInteractable extends HasCoordinate,HasHitShape,HasStandpoint,HasBoundingBox{
	public static final HitInteractable NULL_HIT_INTERACTABLE = new HitInteractable() {

		@Override
		public Coordinate getCoordinate() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public HitShape getHitShape() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Standpoint getStandpoint() {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	public default boolean isHit(HitInteractable target) {
		return !isFriend(target) && getHitShape().intersects(getCoordinate(), target, target.getCoordinate());
	}
	@Override
	public default Rectangle2D getBoundingBox() {
		final Coordinate cod = getCoordinate();
		final HitShape hitshape = getHitShape();
		return new Rectangle2D.Double(cod.getX(), cod.getY(), hitshape.getWidth(), hitshape.getHeight());
	}
}
