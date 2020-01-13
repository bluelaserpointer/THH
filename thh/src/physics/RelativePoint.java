package physics;

import loading.ObjectSaveTree;

public class RelativePoint extends Point{
	private static final long serialVersionUID = -2585237312428553012L;
	protected Point basePoint;
	protected Point relativePoint;
	
	public RelativePoint(Point basePoint, Point relativePoint) {
		this.basePoint = basePoint;
		this.relativePoint = relativePoint;
	}
	public RelativePoint(HasPoint base, HasPoint target) {
		this.basePoint = base.point();
		this.relativePoint = target.point();
	}
	public void setBasePoint(Point basePoint) {
		this.basePoint = basePoint;
	}
	public void setRelativePoint(Point relativePoint) {
		this.relativePoint = relativePoint;
	}
	public Point basePoint() {
		return basePoint;
	}
	public Point relativePoint() {
		return relativePoint;
	}
	@Override
	public ObjectSaveTree save() {
		return new ObjectSaveTree(0, basePoint, relativePoint);
	}
	@Override
	public RelativePoint clone() {
		return new RelativePoint(basePoint, relativePoint.clone());
	}
	@Override
	public int intX() {
		return basePoint.intX() + relativePoint.intX();
	}
	@Override
	public int intY() {
		return basePoint.intY() + relativePoint.intY();
	}
	@Override
	public double doubleX() {
		return basePoint.doubleX() + relativePoint.doubleX();
	}
	@Override
	public double doubleY() {
		return basePoint.doubleY() + relativePoint.doubleY();
	}
	@Override
	public Point setAll(Point point) {
		relativePoint.setAll(point);
		return this;
	}
	@Override
	public void setX(int x) {
		relativePoint.setX(x);
	}
	@Override
	public void setY(int y) {
		relativePoint.setY(y);
	}
	@Override
	public void setX(double x) {
		relativePoint.setX(x);
	}
	@Override
	public void setY(double y) {
		relativePoint.setY(y);
	}
	@Override
	public void setX(Point p) {
		relativePoint.setX(p);
	}
	@Override
	public void setY(Point p) {
		relativePoint.setY(p);
	}
}
