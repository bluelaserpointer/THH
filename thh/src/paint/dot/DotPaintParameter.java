package paint.dot;

import physics.HasPoint;
import physics.Point;

public class DotPaintParameter implements HasPoint {
	public Point point;
	public double angle;
	public int sizeCap;
	public DotPaintParameter setPoint(Point point) {
		this.point = point;
		return this;
	}
	public DotPaintParameter setAngle(double angle) {
		this.angle = angle;
		return this;
	}
	public DotPaintParameter setSizeCap(int sizeCap) {
		this.sizeCap = sizeCap;
		return this;
	}
	@Override
	public Point point() {
		return point;
	}
}
