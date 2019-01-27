package thh;

import static java.lang.Math.*;

import java.awt.geom.Line2D;

import chara.Chara;

public class Dynam{
	
	private double x,y,xSpd,ySpd,angle;
	private double movedDistance;
	private boolean onGround = true;
	public Dynam() {}
	public Dynam(Dynam dynam) {
		x = dynam.x;
		y = dynam.y;
		xSpd = dynam.xSpd;
		ySpd = dynam.ySpd;
		angle = dynam.angle;
		onGround = dynam.onGround;
	}
	public Dynam(double x,double y) {
		this.x = x;
		this.y = y;
	}
	public Dynam(double x,double y,double angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
	public Dynam(double x,double y,double xSpd,double ySpd) {
		this.x = x;
		this.y = y;
		this.xSpd = xSpd;
		this.ySpd = ySpd;
	}
	public Dynam(double x,double y,double xSpd,double ySpd,double angle) {
		this.x = x;
		this.y = y;
		this.xSpd = xSpd;
		this.ySpd = ySpd;
		this.angle = angle;
	}
	@Override
	public Dynam clone() {
		return new Dynam(x,y,xSpd,ySpd,angle);
	}
	public void clear() {
		x = y = xSpd = ySpd = angle = 0.0;
		onGround = true;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public void setXY(double x,double y) {
		this.x = x;
		this.y = y;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void addXY(double x,double y) {
		this.x += x;
		this.y += y;
	}
	public void addXY_DA(double distance,double angle) {
		x += distance*cos(angle);
		y += distance*sin(angle);
	}
	public boolean inStage() {
		return THH.inStage((int)x,(int)y);
	}
	public boolean inArea(int x,int y,int w,int h) {
		return abs(x - this.x) < w && abs(y - this.y) < h;
	}
	public double getDistance(Dynam dynam) {
		return getDistance(dynam.x,dynam.y);
	}
	public double getDistance(double x,double y) {
		final double XD = x - this.x,YD = y - this.y;
		return sqrt(XD*XD + YD*YD);
	}
	public boolean isVisible(Dynam dynam) {
		return THH.getStage().checkLoS(new Line2D.Double(x,y,dynam.x,dynam.y));
	}
	public boolean isVisible(double x,double y) {
		return THH.getStage().checkLoS(new Line2D.Double(this.x,this.y,x,y));
	}
	public double getAngle(double x,double y) {
		return atan2(y - this.y, x - this.x);
	}
	public double getAngle(Chara chara) {
		return atan2(chara.dynam.y - this.y, chara.dynam.x - this.x);
	}
	public double getAngle(DynamInteractable source) {
		return atan2(source.getDynam().y - this.y, source.getDynam().x - this.x);
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
		final double SPEED = sqrt(xSpd*xSpd + ySpd*ySpd);
		xSpd = SPEED*cos(angle);
		ySpd = SPEED*sin(angle);
	}
	public void spin(double angle) {
		this.angle += angle;
		final double SPEED = sqrt(xSpd*xSpd + ySpd*ySpd);
		xSpd = SPEED*cos(this.angle);
		ySpd = SPEED*sin(this.angle);
	}
	public boolean isMovable() {
		return true;
	}
	public boolean isStop() {
		return xSpd == 0.0 && ySpd == 0.0;
	}
	public double getXSpeed() {
		return xSpd;
	}
	public double getYSpeed() {
		return ySpd;
	}
	public double getSpeed() {
		return sqrt(xSpd*xSpd + ySpd*ySpd);
	}
	public void setXSpeed(double xSpeed) {
		xSpd = xSpeed;
	}
	public void setYSpeed(double ySpeed) {
		ySpd = ySpeed;
	}
	public void setSpeed(double xSpeed,double ySpeed) {
		xSpd = xSpeed;
		ySpd = ySpeed;
	}
	public void addSpeed(double xSpeed,double ySpeed) {
		xSpd += xSpeed;
		ySpd += ySpeed;
	}
	public void addSpeed_DA(double distance,double angle) {
		xSpd += distance*cos(angle);
		ySpd += distance*sin(angle);
	}
	public void accelerate(double rate) {
		if(rate == 0.0)
			xSpd = ySpd = 0;
		else if(rate > 1.0) {
			xSpd *= rate;
			ySpd *= rate;
		}else if(rate < 1.0) {
			if(-0.4 < xSpd && xSpd < 0.4)
				xSpd = 0.0;
			else
				xSpd *= rate;
			if(-0.4 < ySpd && ySpd < 0.4)
				ySpd = 0.0;
			else
				ySpd *= rate;
		}
	}
	public double getMovedDistance() {
		return movedDistance;
	}
	public double getMouseAngle() {
		return atan2(THH.getMouseY() - y, THH.getMouseX() - x);
	}
	public void approach(double dstX,double dstY,double speed) {
		final double DX = dstX - x,DY = dstY - y;
		final double DISTANCE = sqrt(DX*DX + DY*DY);
		if(DISTANCE <= speed) {
			x = dstX;
			y = dstY;
		}else {
			final double RATE = speed/DISTANCE;
			x += DX*RATE;
			y += DY*RATE;
		}
	}
	
	//control
	public void move() {
		x += xSpd;
		y += ySpd;
		movedDistance += sqrt(xSpd*xSpd + ySpd*ySpd);
	}
	public void gravity(double g) {
		if (!onGround) {
			if (THH.hitLandscape((int) x - 10, (int) y + 40, 20)) {
				ySpd = 0.0;
				do {
					y -= 1.0;
				} while (THH.hitLandscape((int) x - 10, (int) y + 30, 20));
				if (xSpd == 0.0)
					onGround = true;
			}else
				ySpd += g;
		}
	}
}
