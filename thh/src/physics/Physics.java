package physics;

import physics.hitShape.HitShape;

public abstract class Physics implements HitInteractable, HasAngle{
	public Physics setPoint(Point point) {
		return this;
	}
	public Physics setHitShape(HitShape hitShape) {
		return this;
	}
	public Physics setHitGroup(HitGroup hitGroup) {
		return this;
	}
	public Physics setPointBase(HasPoint base) {
		setPoint(new RelativePoint(base, point(), true));
		return this;
	}
	public Physics setPointBase_keepPos(HasPoint base) {
		point().addXY(-base.point().doubleX(), -base.point().doubleY());
		setPoint(new RelativePoint(base, point(), true));
		return this;
	}
	public Physics addRelativePoint(HasPoint target) {
		if(point() instanceof BasePoint)
			((BasePoint)point()).addRelativePoint(target);
		else
			setPoint(new BasePoint(this));
		return this;
	}
	public Physics removePointBase() {
		if(point() instanceof RelativePoint)
			setPoint(((RelativePoint)point()).relativePoint);
		return this;
	}
}
