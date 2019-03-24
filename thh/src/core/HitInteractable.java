package core;

import geom.HasHitShape;
import physicis.CoordinateInteractable;

public interface HitInteractable extends CoordinateInteractable,HasHitShape,HasStandpoint{
	public default boolean isHit(HitInteractable target) {
		return isEnemy(target) && getHitShape().intersects(target);
	}
}
