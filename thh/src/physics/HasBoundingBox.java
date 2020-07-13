package physics;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import core.GHQ;
import physics.hitShape.HasArea;

public interface HasBoundingBox extends HasPoint, HasArea {
	public default int centerX() {
		return point().intX();
	}
	public default int centerY() {
		return point().intY();
	}
	public default int leftX() {
		return point().intX() - width()/2;
	}
	public default int topY() {
		return point().intY() - height()/2;
	}
	public default boolean boundingBoxIntersectsDot(int x, int y) {
		return point().inRangeXY(centerX(), centerY(), width()/2, height()/2);
	}
	public default boolean boundingBoxIntersectsDot(Point point) {
		return boundingBoxIntersectsDot(point.intX(), point.intY());
	}
	public default boolean isMouseOveredBoundingBox() {
		return boundingBoxIntersectsDot(GHQ.mouseX(),GHQ.mouseY());
	}
	public default boolean boundingBoxIntersectsLine(int x1, int y1, int x2, int y2) {
		return new Rectangle2D.Double(leftX(), topY(), width(), height()).intersectsLine(x1, y1, x2, y2);
	}
	public default boolean boundingBoxIntersectsLine(Point p1, Point p2) {
		return boundingBoxIntersectsLine(p1.intX(), p1.intY(), p2.intX(), p2.intY());
	}
	public default boolean boundingBoxIntersects(HasBoundingBox target) {
		return Math.abs(centerX() - target.centerX()) < (width() + target.width())/2
				&& Math.abs(centerY() - target.centerY()) < (height() + target.height())/2;
	}
	public default boolean isMouseOvered() {
		return isMouseOveredBoundingBox();
	}
	public default void drawBoundingBox(Color color, Stroke stroke) {
		GHQ.getG2D(color, stroke).drawRect(leftX(), topY(), width(), height());
	}
	public default void fillBoundingBox(Color color) {
		GHQ.getG2D(color).fillRect(leftX(), topY(), width(), height());
	}
	public default void drawBiggerBoundingBox(Color color, Stroke stroke, int sizeAdd) {
		GHQ.getG2D(color, stroke).drawRect(leftX() - sizeAdd/2, topY() - sizeAdd/2, width() + sizeAdd, height() + sizeAdd);
	}
	public default void fillBiggerBoundingBox(Color color, Stroke stroke, int sizeAdd) {
		GHQ.getG2D(color, stroke).fillRect(leftX() - sizeAdd/2, topY() - sizeAdd/2, width() + sizeAdd, height() + sizeAdd);
	}
}