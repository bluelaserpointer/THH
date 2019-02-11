package core;

import static java.lang.Math.*;

import java.awt.geom.Line2D;

public class Dynam{
	
	private double x,y,xSpd,ySpd,angle;
	private double movedDistance;
	public Dynam() {}
	public Dynam(Dynam dynam) {
		x = dynam.x;
		y = dynam.y;
		xSpd = dynam.xSpd;
		ySpd = dynam.ySpd;
		angle = dynam.angle;
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
	public void clear() {
		x = y = xSpd = ySpd = angle = movedDistance = 0.0;
	}
	public void initBySample(Dynam sample) {
		x = sample.x;
		y = sample.y;
		xSpd = sample.xSpd;
		ySpd = sample.ySpd;
		angle = sample.angle;
		movedDistance = 0.0;
	}
	public void setAllBySample(Dynam sample) {
		x = sample.x;
		y = sample.y;
		xSpd = sample.xSpd;
		ySpd = sample.ySpd;
		angle = sample.angle;
		movedDistance = sample.movedDistance;
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
	public void setXY(DynamInteractable targetDI) {
		if(targetDI == null)
			return;
		final Dynam targetDynam = targetDI.getDynam();
		this.x = targetDynam.x;
		this.y = targetDynam.y;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void addX(double dx) {
		this.x += dx;
	}
	public void addY(double dy) {
		this.y += dy;
	}
	public void addX_allowsAngle(double dx) {
		x += dx*sin(angle);
		y -= dx*cos(angle);
	}
	public void addY_allowsAngle(double dy) {
		x += dy*sin(angle);
		y += dy*cos(angle);
	}
	public void addXY(double dx,double dy) {
		this.x += dx;
		this.y += dy;
	}
	public void addXY_DA(double distance,double angle) {
		x += distance*cos(angle);
		y += distance*sin(angle);
	}
	public void addXY_allowsAngle(double dx,double dy) {
		final double cos_angle = cos(angle),sin_angle = sin(angle);
		if(dx != 0.0) {
			x += dx*sin_angle;
			y -= dx*cos_angle;
		}
		if(dy != 0.0) {
			x += dy*cos_angle;
			y += dy*sin_angle;
		}
	}
	public void fastParaAdd_ADXYSpd(double angle,double dx,double dy,double speed) {
		this.angle += angle;
		final double cos_angle = cos(this.angle),sin_angle = sin(this.angle);
		if(dx != 0.0) {
			x += dx*sin_angle;
			y -= dx*cos_angle;
		}
		if(dy != 0.0) {
			x += dy*cos_angle;
			y += dy*sin_angle;
		}
		xSpd = speed*cos_angle;
		ySpd = speed*sin_angle;
	}
	public void fastParaAdd_DASpd(double distance,double angle,double speed) {
		this.angle += angle;
		final double cos_angle = cos(this.angle),sin_angle = sin(this.angle);
		x += distance*cos_angle;
		y += distance*sin_angle;
		xSpd = speed*cos_angle;
		ySpd = speed*sin_angle;
	}
	public void fastParaAdd_DSpd(double distance,double speed) {
		final double cos_angle = cos(angle),sin_angle = sin(angle);
		x += distance*cos_angle;
		y += distance*sin_angle;
		xSpd = speed*cos_angle;
		ySpd = speed*sin_angle;
	}
	public boolean inStage() {
		return GHQ.inStage((int)x,(int)y);
	}
	public boolean inArea(int x,int y,int w,int h) {
		return abs(x - this.x) < w && abs(y - this.y) < h;
	}
	public double getDistance(DynamInteractable targetDI) {
		if(targetDI == null)
			return GHQ.NONE;
		final Dynam targetDynam = targetDI.getDynam();
		return getDistance(targetDynam.x,targetDynam.y);
	}
	public double getDistance(double x,double y) {
		final double DX = x - this.x,DY = y - this.y;
		if(DX == 0.0) {
			if(DY == 0.0)
				return 0.0;
			else
				return DY;
		}else {
			if(DY == 0.0)
				return DX;
			else
				return sqrt(DX*DX + DY*DY);
		}
	}
	public boolean isVisible(DynamInteractable targetDI) {
		if(targetDI == null)
			return false;
		final Dynam targetDynam = targetDI.getDynam();
		return GHQ.getStage().checkLoS(new Line2D.Double(x,y,targetDynam.x,targetDynam.y));
	}
	public boolean isVisible(double x,double y) {
		return GHQ.getStage().checkLoS(new Line2D.Double(this.x,this.y,x,y));
	}
	public double getAngle() {
		return angle;
	}
	public double getAngle(double x,double y) {
		return atan2(y - this.y, x - this.x);
	}
	public double getAngle(DynamInteractable targetDI) {
		if(targetDI == null)
			return GHQ.NONE;
		final Dynam targetDynam = targetDI.getDynam();
		return atan2(targetDynam.y - this.y, targetDynam.x - this.x);
	}
	public void setAngle(double angle) {
		this.angle = angle;
		final double SPEED = getSpeed();
		xSpd = SPEED*cos(angle);
		ySpd = SPEED*sin(angle);
	}
	public void spin(double angle) {
		this.angle += angle;
		final double SPEED = getSpeed();
		xSpd = SPEED*cos(this.angle);
		ySpd = SPEED*sin(this.angle);
	}
	public void setAngle_right() {
		this.angle = 0.0;
		final double SPEED = getSpeed();
		xSpd = SPEED;
		ySpd = 0.0;
	}
	public void setAngle(DynamInteractable targetDI) {
		if(targetDI == null)
			return;
		this.angle = getAngle(targetDI);
	}
	public void setAngle(DynamInteractable targetDI,double maxTurnAngle) {
		if(targetDI == null || maxTurnAngle <= 0)
			return;
		final double TARGET_ANGLE = getAngle(targetDI);
		final double D_ANGLE = GHQ.angleFormat(TARGET_ANGLE - this.angle);
		if(D_ANGLE < 0) {
			if(D_ANGLE < -maxTurnAngle)
				spin(-maxTurnAngle);
			else
				setAngle(TARGET_ANGLE);
		}else if(D_ANGLE > 0){
			if(D_ANGLE > maxTurnAngle)
				spin(maxTurnAngle);
			else
				setAngle(TARGET_ANGLE);
		}
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
		if(xSpd == 0.0) {
			if(ySpd == 0.0)
				return 0.0;
			else
				return ySpd;
		}else {
			if(ySpd == 0.0)
				return xSpd;
			else
				return sqrt(xSpd*xSpd + ySpd*ySpd);
		}
	}
	public void setXSpeed(double xSpeed) {
		angle = atan2(ySpd,xSpd = xSpeed);
	}
	public void setYSpeed(double ySpeed) {
		angle = atan2(ySpd = ySpeed,xSpd);
	}
	public void setSpeed(double xSpeed,double ySpeed) {
		angle = atan2(ySpd = ySpeed,xSpd = xSpeed);
	}
	public void setSpeed(double speed) {
		xSpd = speed*cos(angle);
		ySpd = speed*sin(angle);
	}
	public void addSpeed(double xSpeed,double ySpeed) {
		angle = atan2(ySpd += ySpeed,xSpd += xSpeed);
	}
	public void addSpeed(double accel) {
		final double SPEED = getSpeed();
		if(SPEED != 0) {
			final double RATE = (SPEED + accel)/SPEED;
			xSpd *= RATE;
			ySpd *= RATE;
		}else {
			xSpd = accel*cos(angle);
			ySpd = accel*sin(angle);
		}
	}
	public void addSpeed(double accel,boolean brakeMode) {
		final double SPEED = getSpeed(),NEW_SPEED = SPEED + accel;
		if(accel < 0 && brakeMode && NEW_SPEED <= 0)
				xSpd = ySpd = 0;
		else {
			if(SPEED != 0) {
				final double RATE = NEW_SPEED/SPEED;
				xSpd *= RATE;
				ySpd *= RATE;
			}else {
				xSpd = accel*cos(angle);
				ySpd = accel*sin(angle);
			}
		}
	}
	public void addSpeed_DA(double distance,double angle) {
		angle = atan2(ySpd += distance*sin(angle),xSpd += distance*cos(angle));
	}
	public void accelerate_MUL(double rate) {
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
	public void stop() {
		xSpd = ySpd = 0.0;
	}
	public double getMovedDistance() {
		return movedDistance;
	}
	public double getMouseAngle() {
		return atan2(GHQ.getMouseY() - y, GHQ.getMouseX() - x);
	}
	public boolean squreCollision(Dynam targetDynam,int distance) {
		final int DX = (int)(targetDynam.x - x),DY = (int)(targetDynam.y - y);
		return (DX > 0 ? DX < distance : DX > -distance) && (DY > 0 ? DY < distance : DY > -distance);
	}
	public boolean squreCollision(int x,int y,int distance) {
		final int DX = (int)(x - this.x),DY = (int)(y - this.y);
		return (DX > 0 ? DX < distance : DX > -distance) && (DY > 0 ? DY < distance : DY > -distance);
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
	public void approach(DynamInteractable targetDI,double speed) {
		if(targetDI == null)
			return;
		final Dynam targetDynam = targetDI.getDynam();
		approach(targetDynam.x,targetDynam.y,speed);
	}
	
	//control
	public void move() {
		if(xSpd != 0 || ySpd != 0) {
			x += xSpd;
			y += ySpd;
			movedDistance += sqrt(xSpd*xSpd + ySpd*ySpd);
		}
	}
	
	//StringOutput&Input
	public String stringOutput() {
		return GHQ.buildStringArray(x,y,xSpd,ySpd,angle,movedDistance);
	}
	public void stringInput(String[] inputStrs) {
		if(inputStrs.length != 6)
			System.out.println("Illegal inputStrs length to Dynam: " + inputStrs.length);
		x = Double.parseDouble(inputStrs[0]);
		y = Double.parseDouble(inputStrs[1]);
		xSpd = Double.parseDouble(inputStrs[2]);
		ySpd = Double.parseDouble(inputStrs[3]);
		angle = Double.parseDouble(inputStrs[4]);
		movedDistance = Double.parseDouble(inputStrs[5]);
	}
}
