package physics;

import java.awt.geom.Rectangle2D;

import core.GHQ;

public interface HasBoundingBox{
	public abstract Rectangle2D boundingBox();
	public default boolean isMouseOveredBoundingBox() {
		return boundingBox().contains(GHQ.getMouseX(),GHQ.getMouseY());
	}
}