package physics;

import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

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
	public Point setXY(int x, int y) {
		setX(x);
		setY(y);
		return this;
	}
	public Point setXY(double x, double y) {
		setX(x);
		setY(y);
		return this;
	}
	public Point setXY(Point p) {
		setX(p);
		setY(p);
		return this;
	}
	public Point setXY(HasPoint hasPoint){
		if(hasPoint != null)
			setXY(hasPoint.point());
		return this;
	}
	///////////////addX&Y&XY
	public Point addX(int x) {
		setX(intX() + x);
		return this;
	}
	public Point addY(int y) {
		setY(intY() + y);
		return this;
	}
	public Point addX(double x) {
		setX(doubleX() + x);
		return this;
	}
	public Point addY(double y) {
		setY(doubleY() + y);
		return this;
	}
	public Point addXY(int x, int y) {
		setX(intX() + x);
		setY(intY() + y);
		return this;
	}
	public Point addXY(double x, double y) {
		setX(doubleX() + x);
		setY(doubleY() + y);
		return this;
	}
	///////////////shift
	public Point shift(Direction4 direction, int distance) {
		switch(direction) {
		case W:
			addY(-distance);
			break;
		case S:
			addY(distance);
			break;
		case A:
			addX(-distance);
			break;
		case D:
			addX(distance);
			break;
		}
		return this;
	}
	///////////////clone
	public Point cloneAt(int dx, int dy) {
		return clone().addXY(dx, dy);
	}
	public Point cloneAt(double dx, double dy) {
		return clone().addXY(dx, dy);
	}
	
	///////////////
	//information
	///////////////
	
	///////////////toString
	public String toString_int() {
		return intX() + ", " + intY();
	}
	public String toString_double() {
		return doubleX() + ", " + doubleY();
	}
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
		return hasPoint == null ? GHQ.NONE : angleTo(hasPoint.point());
	}
	///////////////angleToMouse
	public double angleToMouse() {
		return angleTo(GHQ.getMouseX(), GHQ.getMouseY());
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
		return intAbsDX(point) < xDistance;
	}
	public final boolean inRangeY(Point point, int yDistance) {
		return intAbsDY(point) < yDistance;
	}
	public final boolean inRangeX(Point point, double xDistance) {
		return doubleAbsDX(point) < xDistance;
	}
	public final boolean inRangeY(Point point, double yDistance) {
		return doubleAbsDY(point) < yDistance;
	}
	public final boolean inRangeXY(Point point, int xDistance, int yDistance) {
		return inRangeX(point, xDistance) && inRangeY(point, yDistance);
	}
	public final boolean inRangeXY(Point point, int distance) {
		return inRangeXY(point, distance, distance);
	}
	public final boolean inRangeXY(Point point, double xDistance, double yDistance) {
		return inRangeX(point, xDistance) && inRangeY(point, yDistance);
	}
	public final boolean inRangeXY(Point point, double distance) {
		return inRangeXY(point, distance, distance);
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
		return hasPoint == null ? GHQ.MAX : distanceSq(hasPoint.point());
	}
	public final double distance(double x, double y) {
		return Math.sqrt(distanceSq(x, y));
	}
	public final double distance(Point point) {
		return Math.sqrt(distanceSq(point));
	}
	public final double distance(HasPoint hasPoint) {
		return hasPoint == null ? GHQ.MAX : Math.sqrt(distanceSq(hasPoint.point()));
	}
	///////////////inRange&inRangeSq
	public final boolean inRangeSq(double x, double y, double distanceSq) {
		return distanceSq(x, y) < distanceSq;
	}
	public final boolean inRangeSq(Point point, double distanceSq) {
		return distanceSq(point) < distanceSq;
	}
	public final boolean inRangeSq(HasPoint hasPoint, double distanceSq) {
		return hasPoint == null ? false : inRangeSq(hasPoint.point(), distanceSq);
	}
	public final boolean inRange(double x, double y, double distance) {
		return inRangeSq(x, y, distance*distance);
	}
	public final boolean inRange(Point point, double distance) {
		return inRangeSq(point, distance*distance);
	}
	public final boolean inRange(HasPoint hasPoint, double distance) {
		return hasPoint == null ? false : inRangeSq(hasPoint.point(), distance*distance);
	}
	///////////////inStage
	public boolean inStage() {
		return GHQ.stage().inStage(this);
	}
	///////////////isBlocked
	public boolean isBlocked() {
		return !GHQ.stage().inStage(this) || GHQ.stage().structures.intersected_dot(this);
	}
	///////////////isVisible
	public boolean isVisible(int x,int y) {
		return GHQ.stage().checkLoS(intX(), intY(), x, y);
	}
	public boolean isVisible(Point point) {
		return isVisible(point.intX(), point.intY());
	}
	public boolean isVisible(HasPoint hasPoint) {
		return hasPoint == null ? false : isVisible(hasPoint.point());
	}
	///////////////equals
	public boolean equals(int x, int y) {
		return intX() == x && intY() == y;
	}
	public boolean equals(double x, double y) {
		return doubleX() == x && doubleY() == y;
	}
	public boolean equals_int(Point point) {
		return equals(point.intX(), point.intY());
	}
	public boolean equals_double(Point point) {
		return equals(point.doubleX(), point.doubleY());
	}
	///////////////checkVertical&checkHorizontal
	public boolean isVert_int(Point point) {
		return point == null ? false : intX() == point.intX();
	}
	public boolean isVert_double(Point point) {
		return point == null ? false : doubleX() == point.doubleX();
	}
	public boolean isHorz_int(Point point) {
		return point == null ? false : intY() == point.intY();
	}
	public boolean isHorz_double(Point point) {
		return point == null ? false : doubleY() == point.doubleY();
	}
	///////////////checkDirection
	public boolean checkDirection_int(Point point, Direction4 direction) {
		if(point == null)
			return false;
		switch(direction) {
		case W:
			return point.intY() < intY();
		case S:
			return point.intY() > intY();
		case A:
			return point.intX() < intX();
		case D:
			return point.intX() > intX();
		default:
			return false;
		}
	}
	public boolean checkDirection_double(Point point, Direction4 direction) {
		if(point == null)
			return false;
		switch(direction) {
		case W:
			return point.doubleY() < doubleY();
		case S:
			return point.doubleY() > doubleY();
		case A:
			return point.doubleX() < doubleX();
		case D:
			return point.doubleX() > doubleX();
		default:
			return false;
		}
	}
	///////////////isFront
	public boolean isFront_int(Point point, Direction4 direction) {
		return (direction.isVert() ? isVert_int(point) : isHorz_int(point)) && checkDirection_int(point, direction);
	}
	public boolean isFront_double(Point point, Direction4 direction) {
		return (direction.isVert() ? isVert_double(point) : isHorz_double(point)) && checkDirection_double(point, direction);
	}
	///////////////approach&approachIfNoObstacles
	public void approach(double dstX, double dstY, double speed) {
		final double DX = dstX - doubleX(),DY = dstY - doubleY();
		final double DISTANCE = sqrt(DX*DX + DY*DY);
		if(DISTANCE <= speed)
			setXY(dstX, dstY);
		else {
			final double RATE = speed/DISTANCE;
			addXY(DX*RATE, DY*RATE);
		}
	}
	public void approach(Point point, double speed) {
		approach(point.doubleX(), point.doubleY(), speed);
	}
	public void approach(HasPoint target, double speed) {
		if(target != null)
			approach(target.point(), speed);
	}
	public void approachIfNoObstacles(HitInteractable source, double dstX, double dstY, double speed) {
		final double DX = dstX - doubleX(), DY = dstY - doubleY();
		final double DISTANCE = sqrt(DX*DX + DY*DY);
		if(DISTANCE > speed) {
			final double RATE = speed/DISTANCE;
			dstX = doubleX() + DX*RATE;
			dstY = doubleY() + DY*RATE;
		}
		if(!GHQ.stage().hitObstacle(source, cloneAt((int)dstX, (int)dstY)))
			setXY(dstX, dstY);
	}
	public void approachIfNoObstacles(HitInteractable source, Point dstPoint, double speed) {
		approachIfNoObstacles(source, dstPoint.doubleX(), dstPoint.doubleY(), speed);
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
			return intX();
		}
		@Override
		public double doubleY() {
			return intY();
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
			setX((int)x);
		}
		@Override
		public void setY(double y){
			setY((int)y);
		}
		@Override
		public void setX(Point p){
			setX(p.doubleX());
		}
		@Override
		public void setY(Point p){
			setY(p.doubleY());
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
			return (int)doubleX();
		}
		@Override
		public int intY() {
			return (int)doubleY();
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
			setX((double)x);
		}
		@Override
		public void setY(int y){
			setY((double)y);
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
			setX(p.doubleX());
		}
		@Override
		public void setY(Point p){
			setY(p.doubleY());
		}
		@Override
		public Point.DoublePoint clone() {
			return new Point.DoublePoint(this);
		}
	}

	///////////////
	//IntVersionPassivePoint
	///////////////
	public static class IntPassivePoint extends IntPoint{
		private static final long serialVersionUID = 3696216604252789195L;

		private Point activePoint;
		
		public IntPassivePoint() {}
		public IntPassivePoint(Point activePoint, int x, int y) {
			super(x, y);
			this.activePoint = activePoint;
		}
		public IntPassivePoint(Point activePoint, Point point) {
			super(point);
			this.activePoint = activePoint;
		}
		public IntPassivePoint(int x, int y) {
			super(x, y);
		}
		public IntPassivePoint(Point point) {
			super(point);
		}
		
		public void setActivePoint(Point point) {
			activePoint = point;
		}
		public void removeActivePoint() {
			setActivePoint(null);
		}
		@Override
		public int intX() {
			return activePoint == null ? x : activePoint.intX() + x;
		}
		@Override
		public int intY() {
			return activePoint == null ? y : activePoint.intY() + y;
		}
	}

	///////////////
	//DoubleVersionPassivePoint
	///////////////
	public static class DoublePassivePoint extends DoublePoint{
		private static final long serialVersionUID = 3696216604252789195L;

		private Point activePoint;
		
		public DoublePassivePoint() {}
		public DoublePassivePoint(Point activePoint, double x, double y) {
			super(x, y);
			this.activePoint = activePoint;
		}
		public DoublePassivePoint(Point activePoint, Point point) {
			super(point);
			this.activePoint = activePoint;
		}
		public DoublePassivePoint(double x, double y) {
			super(x, y);
		}
		public DoublePassivePoint(Point point) {
			super(point);
		}
		
		public void setActivePoint(Point point) {
			activePoint = point;
		}
		public void removeActivePoint() {
			activePoint = null;
		}
		@Override
		public double doubleX() {
			return activePoint == null ? x : activePoint.doubleX() + x;
		}
		@Override
		public double doubleY() {
			return activePoint == null ? y : activePoint.doubleY() + y;
		}
	}
}