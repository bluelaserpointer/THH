package physics;

import physics.hitShape.HitShape;

public class StdPhysics extends Physics {
	protected Point point;
	protected Angle angle;
	protected HitShape hitShape;
	protected HitRule hitRule;
	public StdPhysics(Point point, Angle angle, HitShape hitShape, HitRule hitGroup) {
		this.point = point;
		this.angle = angle;
		this.hitShape = hitShape;
		this.hitRule = hitGroup;
	}
	public StdPhysics() {
		point = Point.NULL_POINT;
		angle = Angle.NULL_ANGLE;
		hitShape = HitShape.NULL_HITSHAPE;
		hitRule = HitRule.HIT_ALL;
	}
	public StdPhysics setPoint(Point point) {
		this.point = point;
		return this;
	}
	public StdPhysics setAngle(Angle angle) {
		this.angle = angle;
		return this;
	}
	public StdPhysics setHitShape(HitShape hitShape) {
		this.hitShape = hitShape;
		return this;
	}
	public StdPhysics setHitRule(HitRule hitRule) {
		this.hitRule = hitRule;
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
	public HitRule hitGroup() {
		return hitRule;
	}
	@Override
	public Angle angle() {
		return angle;
	}
}
