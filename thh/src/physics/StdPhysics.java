package physics;

import physics.hitShape.HitShape;

public class StdPhysics extends Physics {
	protected Point point;
	protected Angle angle;
	protected HitShape hitShape;
	protected HitGroup hitGroup;
	public StdPhysics(Point point, Angle angle, HitShape hitShape, HitGroup hitGroup) {
		this.point = point;
		this.angle = angle;
		this.hitShape = hitShape;
		this.hitGroup = hitGroup;
	}
	public StdPhysics() {
		point = Point.NULL_POINT;
		angle = new Angle();
		hitShape = HitShape.NULL_HITSHAPE;
		hitGroup = HitGroup.HIT_ALL;
	}
	public StdPhysics setPoint(Point point) {
		this.point = point;
		return this;
	}
	public StdPhysics setHitShape(HitShape hitShape) {
		this.hitShape = hitShape;
		return this;
	}
	public StdPhysics setHitGroup(HitGroup hitGroup) {
		this.hitGroup = hitGroup;
		return this;
	}
	@Override
	public Point point() {
		return point;
	}
	@Override
	public HitShape hitShape() {
		return hitShape;
	}
	@Override
	public HitGroup hitGroup() {
		return hitGroup;
	}
	@Override
	public Angle angle() {
		return angle;
	}
}
