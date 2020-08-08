package camera;

import core.GHQ;
import physics.Point;

/**
 * Manages view scrolling.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class Camera {
	protected int cameraLeft, cameraTop;
	protected final Point dstPoint;
	public double zoom = 1.0;
	public Camera(Point dstPoint) {
		this.dstPoint = dstPoint;
	}
	public void applyChanges() {
		cameraLeft = dstPoint.intX() - GHQ.screenW()/2;
		cameraTop = dstPoint.intY() - GHQ.screenH()/2;
	}
	public int x() {
		return cameraLeft + GHQ.screenW()/2;
	}
	public int y() {
		return cameraTop + GHQ.screenH()/2;
	}
	public int left() {
		return cameraLeft;
	}
	public int top() {
		return cameraTop;
	}
	public int right() {
		return cameraLeft + GHQ.screenW();
	}
	public int bottom() {
		return cameraTop + GHQ.screenH();
	}
	public Point dstPoint() {
		return dstPoint;
	}
}
