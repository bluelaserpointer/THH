package thh;

public interface DynamInteractable {
	//XY
	public abstract double getX();
	public abstract double getY();
	public default void setXY(double x,double y) {
		setX(x);
		setY(y);
	}
	public abstract void setX(double x);
	public abstract void setY(double y);
	public default void addXY(double x,double y) {
		setX(getX() + x);
		setY(getY() + y);
	}
	public abstract double getAngle();
	public abstract void setAngle(double angle);
	public abstract boolean isMovable();
	public default boolean inStage() {
		return THH.inStage((int)getX(),(int)getY());
	}
	public default boolean inArea(int x,int y,int w,int h) {
		return Math.abs(x - getX()) < w && Math.abs(y - getY()) < h;
	}
	public default double getDistance(double x,double y) {
		final double XD = x - getX(),YD = y - getY();
		return Math.sqrt(XD*XD + YD*YD);
	}
	//speed
	public default boolean isStop() {
		return this.getXSpeed() == 0.0 && this.getYSpeed() == 0.0;
	}
	public abstract double getXSpeed();
	public abstract double getYSpeed();
	public default double getSpeed() {
		final double XSPD = getXSpeed(),YSPD = getYSpeed();
		return Math.sqrt(XSPD*XSPD + YSPD*YSPD);
	}
	public abstract void setXSpeed(double xSpeed);
	public abstract void setYSpeed(double ySpeed);
	public default void setSpeed(double xSpeed,double ySpeed) {
		setXSpeed(xSpeed);
		setYSpeed(ySpeed);
	}
	public default void addSpeed(double xSpeed,double ySpeed) {
		setXSpeed(getXSpeed() + xSpeed);
		setYSpeed(getYSpeed() + ySpeed);
	}
	public default void acceleration(double rate) {}
}