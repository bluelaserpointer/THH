package physics;

import static java.lang.Math.*;

import core.GHQ;
import loading.ObjectSaveTree;

/**
 * A major class for managing object physics.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class Dynam extends Point.DoublePoint{
	private static final long serialVersionUID = -3892193817831737958L;
	
	public static final Dynam NULL_DYNAM = new Dynam();
	
	protected double xSpd, ySpd;
	/*protected final Angle moveAngle = new Angle() {
		private static final long serialVersionUID = 6971378942148992685L;
		@Override
		public void set(double angle) {
			if(super.angle != angle) {
				super.angle = angle;
				final double SPEED = speed();
				if(SPEED != 0.0) {
					xSpd = SPEED*cos();
					ySpd = SPEED*sin();
				}
			}
		}
		@Override
		public void set(Angle sample) {
			super.angle = sample.angle();
		}
	};*/
	public Dynam() {}
	public Dynam(Point dynam) {
		setAll(dynam);
	}
	public Dynam(double x, double y) {
		setXY(x, y);
	}
	public Dynam(double x, double y, double moveAngle) {
		setXY(x, y);
		setMoveAngle(moveAngle);
	}
	public Dynam(double x, double y, double xSpd, double ySpd) {
		setXY(x, y);
		setSpeed(xSpd, ySpd);
	}
	public Dynam(HasAnglePoint sample) {
		setXYAngle(sample);
	}
	public Dynam(ObjectSaveTree tree) {
		super(tree.pollSaveTreeForSuper());
		setSpeed(tree.pollInt(), tree.pollInt());
	}
	@Override
	public ObjectSaveTree save() {
		return new ObjectSaveTree(0, super.save(), xSpd, ySpd);
	}
	public void clear() {
		x = y = xSpd = ySpd = 0;
	}
	@Override
	public Dynam setAll(Point sample) {
		setXY(sample);
		xSpd = sample.xSpeed();
		ySpd = sample.ySpeed();
		return this;
	}
	public void setXYAngle(HasAnglePoint sample) {
		setXY(sample);
		setMoveAngleByBaseAngle(sample);
	}
	@Override
	public Dynam clone() {
		return new Dynam(this);
	}
	@Override
	public void addX_allowsMoveAngle(double dx) {
		final double moveAngle = atan2(ySpd, xSpd);
		x += dx*sin(moveAngle);
		y -= dx*cos(moveAngle);
	}
	@Override
	public void addY_allowsMoveAngle(double dy) {
		final double moveAngle = atan2(ySpd, xSpd);
		x += dy*sin(moveAngle);
		y += dy*cos(moveAngle);
	}
	@Override
	public void addXY_allowsMoveAngle(double dx, double dy) {
		final double moveAngle = moveAngle();
		final double cos_angle = cos(moveAngle), sin_angle = sin(moveAngle);
		if(dx != 0.0) {
			x += dx*sin_angle;
			y -= dx*cos_angle;
		}
		if(dy != 0.0) {
			x += dy*cos_angle;
			y += dy*sin_angle;
		}
	}
	@Override
	public void addXY_DA(double distance, double angle) {
		x += distance*cos(angle);
		y += distance*sin(angle);
	}
	@Override
	public void fastParaAdd_ADXYSpd(double angle, double dx, double dy, double speed) {
		angle += moveAngle();
		final double cos_angle = cos(angle), sin_angle = sin(angle);
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
	@Override
	public void fastParaAdd_DASpd(double distance, double angle, double speed) {
		fastParaAdd_ADXYSpd(angle, 0.0, distance, speed);
	}
	@Override
	public void fastParaAdd_DSpd(double distance, double speed) {
		fastParaAdd_DASpd(distance, 0.0, speed);
	}
	@Override
	public double moveAngle() {
		return atan2(ySpd, xSpd);
	}
	@Override
	public void setMoveAngle(double angle) {
		final double SPEED = speed();
		if(SPEED != 0.0) {
			xSpd = SPEED*cos(angle);
			ySpd = SPEED*sin(angle);
		}else {
			xSpd = cos(angle);
			ySpd = sin(angle);
		}
	}
	@Override
	public void setMoveAngleToTarget(HasPoint target) {
		if(target != null)
			setMoveAngle(angleTo(target));
	}
	@Override
	public void spinMoveAngleToTargetCapped(HasPoint target, double maxTurnAngle) {
		if(target != null)
			setMoveAngle(Angle.spinTo_ConstSpd(moveAngle(), angleTo(target), maxTurnAngle));
	}
	@Override
	public void spinMoveAngleToTargetSuddenly(HasPoint target, double turnFrame) {
		if(target != null)
			setMoveAngle(Angle.spinTo_Suddenly(moveAngle(), angleTo(target), turnFrame));
	}
	@Override
	public void spinMoveAngle(double angle) {
		setMoveAngle(moveAngle() + angle);
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
	public double xSpeed() {
		return xSpd;
	}
	@Override
	public double ySpeed() {
		return ySpd;
	}
	@Override
	public double speed() {
		if(xSpd == 0.0) {
			if(ySpd == 0.0)
				return 0.0;
			else
				return Math.abs(ySpd);
		}else {
			if(ySpd == 0.0)
				return Math.abs(xSpd);
			else
				return sqrt(xSpd*xSpd + ySpd*ySpd);
		}
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
	public void setSpeed(double xSpeed, double ySpeed) {
		xSpd = xSpeed;
		ySpd = ySpeed;
	}
	@Override
	public void setSpeed(double speed) {
		final double RATE = speed/speed();
		xSpd *= RATE;
		ySpd *= RATE;
	}
	@Override
	public void addSpeed(double xSpeed, double ySpeed) {
		xSpd += xSpeed;
		ySpd += ySpeed;
	}
	@Override
	public void addSpeed(double accel) {
		final double SPEED = speed();
		if(SPEED != 0.0) {
			final double RATE = (SPEED + accel)/SPEED;
			xSpd *= RATE;
			ySpd *= RATE;
		}else {
			xSpd += accel;
		}
	}
	@Override
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
				xSpd += accel;
			}
		}
	}
	@Override
	public void addSpeed_DA(double distance, double angle) {
		xSpd += distance*cos(angle);
		ySpd += distance*sin(angle);
	}
	@Override
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
	@Override
	public void stop() {
		xSpd = ySpd = 0.0;
	}
	
	//control
	/**
	 * Move forward.
	 */
	@Override
	public void move() {
		if(xSpd == 0 && ySpd == 0)
			return;
		x += xSpd;
		y += ySpd;
	}
	@Override
	public void moveIfNoObstacles(HitInteractable source) {
		if(xSpd == 0 && ySpd == 0 || GHQ.stage().hitObstacle_atNewPoint(source, (int)xSpd, (int)ySpd))
			return;
		x += xSpd;
		y += ySpd;
	}
	/**
	 * Move forward with a limited length.
	 * @param lengthCap
	 * @return lest length need to go
	 */
	@Override
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