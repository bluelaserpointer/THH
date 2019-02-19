package core;

import java.awt.geom.Rectangle2D;

public interface HasBody {
	public abstract Rectangle2D getBoundingBox();
	public default boolean isMouseOveredBoundingBox() {
		return getBoundingBox().contains(GHQ.getMouseX(),GHQ.getMouseY());
	}
}
