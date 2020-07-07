package physics;

import java.awt.geom.Rectangle2D;

import core.GHQ;
import physics.hitShape.HasArea;

public interface HasBoundingBox extends HasPoint, HasArea {
	public default Rectangle2D boundingBox() {
		return new Rectangle2D.Double(point().intX() - width()/2, point().intY() - height()/2, width(), height());
	}
	public default boolean isMouseOveredBoundingBox() {
		return boundingBox().contains(GHQ.mouseX(),GHQ.mouseY());
	}
	public default boolean intersectsDot(int x, int y) {
		return boundingBox().contains(x, y);
	}
	public default boolean intersectsDot(Point point) {
		return intersectsDot(point.intX(), point.intY());
	}
	public default boolean isMouseOvered() {
		return intersectsDot(GHQ.mouseX(), GHQ.mouseY());
	}
}