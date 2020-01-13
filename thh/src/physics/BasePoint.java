package physics;

import java.util.LinkedList;

import loading.ObjectSaveTree;

public class BasePoint extends Point{
	private static final long serialVersionUID = -2585237312428553012L;
	protected LinkedList<Point> relativePoints = new LinkedList<Point>();
	protected Point basePoint;
	
	public BasePoint(Point basePoint) {
		this.basePoint = basePoint;
	}
	public BasePoint(HasPoint base) {
		this.basePoint = base.point();
	}
	public void setBasePoint(Point basePoint) {
		this.basePoint = basePoint;
	}
	public LinkedList<Point> relativePoints(){
		return relativePoints;
	}
	public void addRelativePoint(Point relativePoint) {
		relativePoints.add(relativePoint);
	}
	public void addRelativePoint(HasPoint relativeTarget) {
		addRelativePoint(relativeTarget);
	}
	public Point basePoint() {
		return basePoint;
	}
	public Point relativePoint() {
		return basePoint;
	}
	@Override
	public ObjectSaveTree save() {
		return new ObjectSaveTree(0, basePoint, basePoint);
	}
	@Override
	public RelativePoint clone() {
		return new RelativePoint(basePoint, basePoint.clone());
	}
	@Override
	public int intX() {
		return basePoint.intX() + basePoint.intX();
	}
	@Override
	public int intY() {
		return basePoint.intY() + basePoint.intY();
	}
	@Override
	public double doubleX() {
		return basePoint.doubleX() + basePoint.doubleX();
	}
	@Override
	public double doubleY() {
		return basePoint.doubleY() + basePoint.doubleY();
	}
	@Override
	public Point setAll(Point point) {
		basePoint.setAll(point);
		return this;
	}
	@Override
	public void setX(int x) {
		final int DX = intDX(x);
		for(Point point : relativePoints)
			point.addX(DX);
		basePoint.setX(x);
	}
	@Override
	public void setY(int y) {
		final int DY = intDY(y);
		for(Point point : relativePoints)
			point.addY(DY);
		basePoint.setY(y);
	}
	@Override
	public void setX(double x) {
		final double DX = doubleDX(x);
		for(Point point : relativePoints)
			point.addX(DX);
		basePoint.setX(x);
	}
	@Override
	public void setY(double y) {
		final double DY = doubleDY(y);
		for(Point point : relativePoints)
			point.addY(DY);
		basePoint.setY(y);
	}
	@Override
	public void setX(Point p) {
		final double DX = doubleDX(p);
		for(Point point : relativePoints)
			point.addX(DX);
		basePoint.setX(p);
	}
	@Override
	public void setY(Point p) {
		final double DY = doubleDY(p);
		for(Point point : relativePoints)
			point.addY(DY);
		basePoint.setY(p);
	}
}
