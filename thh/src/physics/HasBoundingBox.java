package physics;

import java.awt.geom.Rectangle2D;

import core.GHQ;
import physics.hitShape.HasArea;

public interface HasBoundingBox extends HasPoint, HasArea {
	public abstract int width();
	public abstract int height();
	public default Rectangle2D boundingBox() {
		return new Rectangle2D.Double(point().intX() - width()/2, point().intY() - height()/2, width(), height());
	}
	public default boolean isMouseOveredBoundingBox() {
		return boundingBox().contains(GHQ.mouseX(),GHQ.mouseY());
	}
	@Override
	public default boolean intersectsDot(int x, int y) {
		return boundingBox().contains(x, y);
	}
}