package physics;

import static java.lang.Math.atan2;

import java.awt.geom.Line2D;
import java.io.Serializable;

import core.GHQ;

/**
 * A major class for managing object coordinate.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class Point implements Serializable{
	private static final long serialVersionUID = -4012705097956450689L;

	@Override
	public abstract Point clone();
	
	///////////////
	//XY
	///////////////
	
	public abstract int intX();
	public abstract int intY();
	public abstract double doubleX();
	public abstract double doubleY();
	
	///////////////
	//update
	///////////////
	
	///////////////setX&Y&XY
	public abstract void setX(int x);
	public abstract void setY(int y);
	public abstract void setX(double x);
	public abstract void setY(double y);
	public abstract void setX(Point p);
	public abstract void setY(Point p);
	public abstract void setXY(int x, int y);
	public abstract void setXY(double x, double y);
	public abstract void setXY(Point p);
	public void setXY(HasPoint hasPoint){
		if(hasPoint != null)
			setXY(hasPoint.getPoint());
	}
	///////////////addX&Y&XY
	public void addX(int x) {
		setX(intX() + x);
	}
	public void addY(int y) {
		setY(intY() + y);
	}
	public void addX(double x) {
		setX(doubleX() + x);
	}
	public void addY(double y) {
		setY(doubleY() + y);
	}
	public void addXY(int x, int y) {
		setX(intX() + x);
		setY(intY() + y);
	}
	public void addXY(double x, double y) {
		setX(doubleX() + x);
		setY(doubleY() + y);
	}
	
	///////////////
	//information
	///////////////
	
	///////////////angleTo
	public double angleTo(int x,int y) {
		return atan2(intDY(y), intDX(x));
	}
	public double angleTo(double x,double y) {
		return atan2(doubleDY(y), doubleDX(x));
	}
	public double angleTo(Point point) {
		return atan2(doubleDY(point), doubleDX(point));
	}
	public double angleTo(HasPoint hasPoint) {
		return hasPoint == null ? GHQ.NONE : angleTo(hasPoint.getPoint());
	}
	///////////////intDX&DY
	public final int intDX(int x) {
		return x - intX();
	}
	public final int intDY(int y) {
		return y - intY();
	}
	public final int intDX(Point p) {
		return p.intX() - intX();
	}
	public final int intDY(Point p) {
		return p.intY() - intY();
	}
	///////////////intAbsDX&DY
	public final int intAbsDX(int x) {
		return Math.abs(intDX(x));
	}
	public final int intAbsDY(int y) {
		return Math.abs(intDY(y));
	}
	public final int intAbsDX(Point p) {
		return Math.abs(intDX(p));
	}
	public final int intAbsDY(Point p) {
		return Math.abs(intDY(p));
	}
	///////////////doubleDX&DY
	public final double doubleDX(double x) {
		return x - doubleX();
	}
	public final double doubleDY(double y) {
		return y - doubleY();
	}
	public final double doubleDX(Point p) {
		return p.doubleX() - doubleX();
	}
	public final double doubleDY(Point p) {
		return p.doubleY() - doubleY();
	}
	///////////////doubleAbsDX&DY
	public final double doubleAbsDX(double x) {
		return Math.abs(doubleDX(x));
	}
	public final double doubleAbsDY(double y) {
		return Math.abs(doubleDY(y));
	}
	public final double doubleAbsDX(Point p) {
		return Math.abs(doubleDX(p));
	}
	public final double doubleAbsDY(Point p) {
		return Math.abs(doubleDY(p));
	}
	///////////////inRangeX&Y&XY
	public final boolean inRangeX(Point point, int xDistance) {
		return intDX(point) < xDistance;
	}
	public final boolean inRangeY(Point point, int yDistance) {
		return intDY(point) < yDistance;
	}
	public final boolean inRangeXY(Point point, int distance) {
		return intDX(point) < distance && intDY(point) < distance;
	}
	public final boolean inRangeXY(Point point, int xDistance, int yDistance) {
		return intDX(point) < xDistance && intDY(point) < yDistance;
	}
	///////////////distance&distanceSq
	public final double distanceSq(double x, double y) {
		final double DX = doubleDX(x),DY = doubleDY(y);
		return DX*DX + DY*DY;
	}
	public final double distanceSq(Point point) {
		final double DX = doubleDX(point),DY = doubleDY(point);
		return DX*DX + DY*DY;
	}
	public final double distanceSq(HasPoint hasPoint) {
		return hasPoint == null ? GHQ.MAX : distanceSq(hasPoint.getPoint());
	}
	public final double distance(double x, double y) {
		return Math.sqrt(distanceSq(x, y));
	}
	public final double distance(Point point) {
		return Math.sqrt(distanceSq(point));
	}
	public final double distance(HasPoint hasPoint) {
		return hasPoint == null ? GHQ.MAX : Math.sqrt(distanceSq(hasPoint.getPoint()));
	}
	///////////////inRange&inRangeSq
	public final boolean inRangeSq(double x, double y, double distanceSq) {
		return distanceSq(x, y) < distanceSq;
	}
	public final boolean inRangeSq(Point point, double distanceSq) {
		return distanceSq(point) < distanceSq;
	}
	public final boolean inRangeSq(HasPoint hasPoint, double distanceSq) {
		return hasPoint == null ? false : inRangeSq(hasPoint.getPoint(), distanceSq);
	}
	public final boolean inRange(double x, double y, double distance) {
		return inRangeSq(x, y, distance*distance);
	}
	public final boolean inRange(Point point, double distance) {
		return inRangeSq(point, distance*distance);
	}
	public final boolean inRange(HasPoint hasPoint, double distance) {
		return hasPoint == null ? false : inRangeSq(hasPoint.getPoint(), distance*distance);
	}
	///////////////inStage
	public boolean inStage() {
		return GHQ.inStage(intX(), intY());
	}
	///////////////isVisible
	public boolean isVisible(double x,double y) {
		return GHQ.checkLoS(new Line2D.Double(doubleX(), doubleY(), x, y));
	}
	public boolean isVisible(Point point) {
		return isVisible(point.doubleX(), point.doubleY());
	}
	public boolean isVisible(HasPoint hasPoint) {
		return hasPoint == null ? false : isVisible(hasPoint.getPoint());
	}
	//////////////////////////////classes

	///////////////
	//IntVersionPoint
	///////////////
	public static class IntPoint extends Point{
		private static final long serialVersionUID = -7032532124881792105L;
		protected int x,y;
		public IntPoint() {}
		public IntPoint(int x, int y) {
			this.x = x;
			this.y = y;
		}
		public IntPoint(Point point) {
			this.x = point.intX();
			this.y = point.intY();
		}
		@Override
		public int intX() {
			return x;
		}
		@Override
		public int intY() {
			return y;
		}
		@Override
		public double doubleX() {
			return x;
		}
		@Override
		public double doubleY() {
			return y;
		}
		@Override
		public void setX(int x){
			this.x = x;
		}
		@Override
		public void setY(int y){
			this.y = y;
		}
		@Override
		public void setX(double x){
			this.x = (int)x;
		}
		@Override
		public void setY(double y){
			this.y = (int)y;
		}
		@Override
		public void setX(Point p){
			x = p.intX();
		}
		@Override
		public void setY(Point p){
			y = p.intY();
		}
		@Override
		public void setXY(int x, int y){
			this.x = x;
			this.y = y;
		}
		@Override
		public void setXY(double x, double y){
			this.x = (int)x;
			this.y = (int)y;
		}
		@Override
		public void setXY(Point p){
			x = p.intX();
			y = p.intY();
		}
		@Override
		public Point.IntPoint clone() {
			return new Point.IntPoint(this);
		}
	}
	
	///////////////
	//DoubleVersionPoint
	///////////////
	public static class DoublePoint extends Point{
		private static final long serialVersionUID = 2505276574895896202L;
		protected double x,y;
		public DoublePoint() {}
		public DoublePoint(double x, double y) {
			this.x = x;
			this.y = y;
		}
		public DoublePoint(Point point) {
			this.x = point.doubleX();
			this.y = point.doubleY();
		}
		@Override
		public int intX() {
			return (int)x;
		}
		@Override
		public int intY() {
			return (int)y;
		}
		@Override
		public double doubleX() {
			return x;
		}
		@Override
		public double doubleY() {
			return y;
		}
		@Override
		public void setX(int x){
			this.x = x;
		}
		@Override
		public void setY(int y){
			this.y = y;
		}
		@Override
		public void setX(double x){
			this.x = x;
		}
		@Override
		public void setY(double y){
			this.y = y;
		}
		@Override
		public void setX(Point p){
			x = p.doubleX();
		}
		@Override
		public void setY(Point p){
			y = p.doubleY();
		}
		@Override
		public void setXY(int x, int y){
			this.x = x;
			this.y = y;
		}
		@Override
		public void setXY(double x, double y){
			this.x = x;
			this.y = y;
		}
		@Override
		public void setXY(Point p){
			x = p.doubleX();
			y = p.doubleY();
		}
		@Override
		public Point.DoublePoint clone() {
			return new Point.DoublePoint(this);
		}
	}
}