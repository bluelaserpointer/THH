package thh;

public interface DynamInteractable {
	//XY
	public abstract double getX();
	public abstract double getY();
	public default void setXY(double x,double y) {
		this.setX(x);
		this.setY(y);
	}
	public default void setX(double x) {}
	public default void setY(double y) {}
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
		return this.getSpeed() == 0.0;
	}
	public abstract double getSpeed();
	public default void setSpeed(double xSpeed,double ySpeed) {}
	public default void addSpeed(double xSpeed,double ySpeed) {}
	public default void acceleration(double rate) {}
}