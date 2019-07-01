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
	protected final Angle moveAngle = new Angle() {
		private static final long serialVersionUID = 6971378942148992685L;
		@Override
		public void set(double angle) {
			if(angle != super.angle) {
				final double SPEED = speed();
				if(SPEED != 0.0) {
					xSpd = SPEED*cos();
					ySpd = SPEED*sin();
				}
			}
			super.angle = angle;
		}
		@Override
		public void set(Angle sample) {
			super.angle = sample.angle();
		}
	};
	public Dynam() {}
	public Dynam(Dynam dynam) {
		setAll(dynam);
	}
	public Dynam(double x,double y) {
		setXY(x, y);
	}
	public Dynam(double x,double y,double moveAngle) {
		setXY(x, y);
		setMoveAngle(moveAngle);
	}
	public Dynam(double x,double y,double xSpd,double ySpd) {
		setXY(x, y);
		setSpeed(xSpd, ySpd);
	}
	public Dynam(HasAnglePoint sample) {
		setXYAngle(sample);
	}
	public void clear() {
		x = y = xSpd = ySpd = 0;
		moveAngle.clear();
	}
	public void setAll(Dynam sample) {
		setXY(sample);
		xSpd = sample.xSpd;
		ySpd = sample.ySpd;
		moveAngle.set(sample.moveAngle);
	}
	public void setAll(HasDynam hasSample) {
		setAll(hasSample.getDynam());
	}
	public void setXYAngle(HasAnglePoint sample) {
		setXY(sample);
		setMoveAngle(sample);
	}
	public Dynam clone() {
		return new Dynam(this);
	}
	public void addX_allowsMoveAngle(double dx) {
		x += dx*moveAngle.sin();
		y -= dx*moveAngle.cos();
	}
	public void addY_allowsMoveAngle(double dy) {
		x += dy*moveAngle.cos();
		y += dy*moveAngle.sin();
	}
	public void addXY_DA(double distance,double angle) {
		x += distance*cos(angle);
		y += distance*sin(angle);
	}
	public void addXY_allowsMoveAngle(double dx,double dy) {
		final double cos_angle = moveAngle.cos(),sin_angle = moveAngle.sin();
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
		this.moveAngle.spin(angle);
		final double cos_angle = this.moveAngle.cos(), sin_angle = this.moveAngle.sin();
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
		this.moveAngle.spin(angle);
		final double cos_angle = this.moveAngle.cos(), sin_angle = this.moveAngle.sin();
		x += distance*cos_angle;
		y += distance*sin_angle;
		xSpd = speed*cos_angle;
		ySpd = speed*sin_angle;
	}
	public void fastParaAdd_DSpd(double distance,double speed) {
		final double cos_angle = moveAngle.cos(),sin_angle = moveAngle.sin();
		x += distance*cos_angle;
		y += distance*sin_angle;
		xSpd = speed*cos_angle;
		ySpd = speed*sin_angle;
	}
	public double moveAngle() {
		return moveAngle.angle();
	}
	public void setMoveAngle(double angle) {
		this.moveAngle.set(angle);
	}
	public void setMoveAngle(HasAngle sample) {
		if(sample == null)
			return;
		setMoveAngle(sample.getAngle().angle());
	}
	public void setMoveAngleToTarget(HasPoint target) {
		if(target != null)
			setMoveAngle(angleTo(target));
	}
	public double spinMoveAngleToTargetCapped(HasDynam target, double maxTurnAngle) {
		return target == null ? GHQ.MAX : moveAngle.spinTo_ConstSpd(angleTo(target), maxTurnAngle);
	}
	public double spinMoveAngleToTargetSuddenly(HasDynam target, double turnFrame) {
		return target == null ? GHQ.MAX : moveAngle.spinTo_Suddenly(angleTo(target), turnFrame);
	}
	public void spinMoveAngle(double angle) {
		this.moveAngle.spin(angle);
	}
	public boolean isMovable() {
		return true;
	}
	public boolean isStop() {
		return xSpd == 0.0 && ySpd == 0.0;
	}
	public double xSpeed() {
		return xSpd;
	}
	public double ySpeed() {
		return ySpd;
	}
	public double speed() {
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
		moveAngle.set(xSpd = xSpeed, ySpd);
	}
	public void setYSpeed(double ySpeed) {
		moveAngle.set(xSpd, ySpd = ySpeed);
	}
	public void setSpeed(double xSpeed, double ySpeed) {
		moveAngle.set(xSpd = xSpeed, ySpd = ySpeed);
	}
	public void setSpeed(double speed) {
		xSpd = speed*moveAngle.cos();
		ySpd = speed*moveAngle.sin();
	}
	public void addSpeed(double xSpeed, double ySpeed) {
		moveAngle.set(xSpd += xSpeed, ySpd += ySpeed);
	}
	public void addSpeed(double accel) {
		final double SPEED = speed();
		if(SPEED != 0) {
			final double RATE = (SPEED + accel)/SPEED;
			xSpd *= RATE;
			ySpd *= RATE;
		}else {
			xSpd = accel*moveAngle.cos();
			ySpd = accel*moveAngle.sin();
		}
	}
	public void addSpeed(double accel, boolean brakeMode) {
		final double SPEED = speed(),NEW_SPEED = SPEED + accel;
		if(accel < 0 && brakeMode && NEW_SPEED <= 0)
				xSpd = ySpd = 0;
		else {
			if(SPEED != 0) {
				final double RATE = NEW_SPEED/SPEED;
				xSpd *= RATE;
				ySpd *= RATE;
			}else {
				xSpd = accel*moveAngle.cos();
				ySpd = accel*moveAngle.sin();
			}
		}
	}
	public void addSpeed_DA(double distance, double angle) {
		this.moveAngle.set(xSpd += distance*cos(angle), ySpd += distance*sin(angle));
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
		final double SPEED = speed();
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