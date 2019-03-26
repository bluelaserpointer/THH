package core;

import java.awt.geom.Rectangle2D;

import geom.HasHitShape;
import geom.HitShape;
import physicis.Coordinate;
import physicis.HasCoordinate;

public interface HitInteractable extends HasCoordinate,HasHitShape,HasStandpoint,HasBoundingBox{
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
