package physics;

import static java.lang.Math.*;

import core.GHQ;
import core.HitInteractable;

/**
 * A major class for managing object physics.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class Dynam extends Point.DoublePoint{
	private static final long serialVersionUID = -3892193817831737958L;
	
	public static final Dynam NULL_DYNAM = new Dynam();
	
	
	protected double xSpd,ySpd;
	protected final Angle angle = new Angle() {
		private static final long serialVersionUID = 6971378942148992685L;
		@Override
		public void set(double angle) {
			super.set(angle);
			final double SPEED = getSpeed();
			xSpd = SPEED*cos();
			ySpd = SPEED*sin();
		}
		@Override
		public void spin(double angle) {
			this.set(this.angle + angle);
		}
	};
	public Dynam() {}
	public Dynam(Dynam dynam) {
		x = dynam.x;
		y = dynam.y;
		xSpd = dynam.xSpd;
		ySpd = dynam.ySpd;
		angle.set(dynam.angle);
	}
	public Dynam(double x,double y) {
		this.x = x;
		this.y = y;
	}
	public Dynam(double x,double y,double angle) {
		this.x = x;
		this.y = y;
		this.angle.set(angle);
	}
	public Dynam(double x,double y,double xSpd,double ySpd) {
		this.x = x;
		this.y = y;
		this.xSpd = xSpd;
		this.ySpd = ySpd;
		angle.set(xSpd, ySpd);
	}
	public Dynam(HasAnglePoint sample) {
		x = sample.getPoint().doubleX();
		y = sample.getPoint().doubleY();
		angle.set(sample.getAngle());
	}
	public void clear() {
		x = y = xSpd = ySpd;
		angle.clear();
	}
	public void setAll(HasDynam hasSample) {
		setAll(hasSample.getDynam());
	}
	public void setAll(Dynam sample) {
		x = sample.x;
		y = sample.y;
		xSpd = sample.xSpd;
		ySpd = sample.ySpd;
		angle.set(sample.angle);
	}
	public Dynam clone() {
		return new Dynam(this);
	}
	public void addX_allowsAngle(double dx) {
		x += dx*angle.sin();
		y -= dx*angle.cos();
	}
	public void addY_allowsAngle(double dy) {
		x += dy*angle.cos();
		y += dy*angle.sin();
	}
	public void addXY_DA(double distance,double angle) {
		x += distance*cos(angle);
		y += distance*sin(angle);
	}
	public void addXY_allowsAngle(double dx,double dy) {
		final double cos_angle = angle.cos(),sin_angle = angle.sin();
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
		this.angle.spin(angle);
		final double cos_angle = this.angle.cos(), sin_angle = this.angle.sin();
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
		this.angle.spin(angle);
		final double cos_angle = this.angle.cos(), sin_angle = this.angle.sin();
		x += distance*cos_angle;
		y += distance*sin_angle;
		xSpd = speed*cos_angle;
		ySpd = speed*sin_angle;
	}
	public void fastParaAdd_DSpd(double distance,double speed) {
		final double cos_angle = angle.cos(),sin_angle = angle.sin();
		x += distance*cos_angle;
		y += distance*sin_angle;
		xSpd = speed*cos_angle;
		ySpd = speed*sin_angle;
	}
	public double moveAngle() {
		return angle.angle();
	}
	public void setAngle(double angle) {
		this.angle.set(angle);
	}
	public void setAngle(HasAngle sample) {
		if(sample == null)
			return;
		setAngle(sample.getAngle().angle());
	}
	public void setAngleToTarget(HasPoint target) {
		if(target != null)
			setAngle(angleTo(target));
	}
	public double spinToTargetCapped(HasDynam target, double maxTurnAngle) {
		return target == null ? GHQ.MAX : angle.spinToTargetCapped(angleTo(target), maxTurnAngle);
	}
	public double spinToTargetSuddenly(HasDynam target, double turnFrame) {
		return target == null ? GHQ.MAX : angle.spinToTargetSuddenly(angleTo(target), turnFrame);
	}
	public void spin(double angle) {
		this.angle.spin(angle);
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
		angle.set(xSpd = xSpeed, ySpd);
	}
	public void setYSpeed(double ySpeed) {
		angle.set(xSpd, ySpd = ySpeed);
	}
	public void setSpeed(double xSpeed,double ySpeed) {
		angle.set(xSpd = xSpeed, ySpd = ySpeed);
	}
	public void setSpeed(double speed) {
		xSpd = speed*angle.cos();
		ySpd = speed*angle.sin();
	}
	public void addSpeed(double xSpeed,double ySpeed) {
		angle.set(xSpd += xSpeed, ySpd += ySpeed);
	}
	public void addSpeed(double accel) {
		final double SPEED = getSpeed();
		if(SPEED != 0) {
			final double RATE = (SPEED + accel)/SPEED;
			xSpd *= RATE;
			ySpd *= RATE;
		}else {
			xSpd = accel*angle.cos();
			ySpd = accel*angle.sin();
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
				xSpd = accel*angle.cos();
				ySpd = accel*angle.sin();
			}
		}
	}
	public void addSpeed_DA(double distance,double angle) {
		this.angle.set(xSpd += distance*cos(angle), ySpd += distance*sin(angle));
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
	public void approach(HasDynam target,double speed) {
		if(target == null)
			return;
		final Dynam targetDynam = target.getDynam();
		approach(targetDynam.x,targetDynam.y,speed);
	}
	public void approachIfNoObstacles(HitInteractable source, double dstX,double dstY,double speed) {
		final double DX = dstX - x,DY = dstY - y;
		final double DISTANCE = sqrt(DX*DX + DY*DY);
		if(DISTANCE > speed) {
			final double RATE = speed/DISTANCE;
			dstX = x + DX*RATE;
			dstY = y + DY*RATE;
		}
		if(!GHQ.hitObstacle_DSTXY(source, (int)dstX, (int)dstY)) {
			x = dstX;
			y = dstY;
		}
	}
	
	//control
	/**
	 * Move forward.
	 */
	public void move() {
		if(xSpd == 0 && ySpd == 0)
			return;
		x += xSpd;
		y += ySpd;
	}
	public void moveIfNoObstacles(HitInteractable source) {
		if(xSpd == 0 && ySpd == 0 || GHQ.hitObstacle_DXDY(source, (int)xSpd, (int)ySpd))
			return;
		x += xSpd;
		y += ySpd;
	}
	/**
	 * Move forward with a limited length.
	 * @param lengthCap
	 * @return lest length need to go
	 */
	public double move(double lengthCap) {
		if(xSpd == 0 && ySpd == 0)
			return 0;
		final double SPEED = getSpeed();
		if(SPEED <= lengthCap) {
			x += xSpd;
			y += ySpd;
			return 0;
		}else {
			final double RATE = lengthCap/SPEED;
			x += xSpd*RATE;
			y += ySpd*RATE;
			return SPEED - lengthCap;
		}
	}
}
