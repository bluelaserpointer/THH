package hitShape;

import physics.HasBoundingBox;
import physics.Point;

/**
 * Subclasses of this has a accessible {@link HitShape} instance.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public interface HasHitShape extends HasBoundingBox{
	public abstract HitShape hitShape();
	public default int width() {
		return hitShape().width();
	}
	public default int height() {
		return hitShape().height();
	}
	public default Point point() {
		return hitShape().point();
	}
}
