package thh;

public class DynamNode implements DynamInteractable{
	
	private double x,y,xSpd,ySpd,angle;
	public DynamNode(double x,double y) {
		this.x = x;
		this.y = y;
	}
	public DynamNode(double x,double y,double angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
	public DynamNode(double x,double y,double xSpd,double ySpd) {
		this.x = x;
		this.y = y;
		this.xSpd = xSpd;
		this.ySpd = ySpd;
	}
	public DynamNode(double x,double y,double xSpd,double ySpd,double angle) {
		this.x = x;
		this.y = y;
		this.xSpd = xSpd;
		this.ySpd = ySpd;
		this.angle = angle;
	}
	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}
	@Override
	public void setXY(double x,double y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}
	@Override
	public void addXY(double x,double y) {
		this.x += x;
		this.y += y;
	}
	public boolean inStage() {
		return THH.inStage((int)x,(int)y);
	}
	public boolean inArea(int x,int y,int w,int h) {
		return Math.abs(x - this.x) < w && Math.abs(y - this.y) < h;
	}
	public double getDistance(double x,double y) {
		final double XD = x - this.x,YD = y - this.y;
		return Math.sqrt(XD*XD + YD*YD);
	}

	@Override
	public double getAngle() {
		return angle;
	}

	@Override
	public void setAngle(double angle) {
		this.angle = angle;
	}

	@Override
	public boolean isMovable() {
		return true;
	}
	@Override
	public boolean isStop() {
		return xSpd == 0.0 && ySpd == 0.0;
	}
	@Override
	public double getXSpeed() {
		return xSpd;
	}
	@Override
	public double getYSpeed() {
		return ySpd;
	}
	@Override
	public double getSpeed() {
		return Math.sqrt(xSpd*xSpd + ySpd*ySpd);
	}
	@Override
	public void setXSpeed(double xSpeed) {
		xSpd = xSpeed;
	}
	@Override
	public void setYSpeed(double ySpeed) {
		ySpd = ySpeed;
	}
	@Override
	public void addSpeed(double xSpeed,double ySpeed) {
		xSpd += xSpeed;
		ySpd += ySpeed;
	}
}
