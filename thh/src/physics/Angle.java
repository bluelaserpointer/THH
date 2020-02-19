package physics;

import core.GHQ;

public class Angle {
	protected double angle;
	public static final Angle NULL_ANGLE = new Angle() {
		public double get() {
			return 0.0;
		}
	};
	
	//initialization
	public Angle() {
		this.angle = 0.0;
	}
	public Angle(double angle) {
		this.angle = angle;
	}
	//control
	public void set(double angle) {
		this.angle = angle;
	}
	public void set(Angle sample) {
		set(sample.angle);
	}
	public void set(HasAngle sample) {
		set(sample.angle());
	}
	public void set(double dx, double dy) {
		set(Math.atan2(dy, dx));
	}
	public void spin(double angle) {
		this.set(this.angle + angle);
	}
	public double spinTo_ConstSpd(double targetAngle, double angularSpeed) {
		if(angularSpeed <= 0)
			return GHQ.MAX;
		final double D_ANGLE = getDelta(targetAngle);
		if(D_ANGLE < 0) {
			if(D_ANGLE < -angularSpeed) {
				spin(-angularSpeed);
				return Math.abs(D_ANGLE + angularSpeed);
			} else {
				set(targetAngle);
				return 0.0;
			}
		}else if(D_ANGLE > 0){
			if(D_ANGLE > angularSpeed) {
				spin(angularSpeed);
				return Math.abs(D_ANGLE - angularSpeed);
			} else {
				set(targetAngle);
				return 0.0;
			}
		}else
			return 0.0;
	}
	public double spinTo_Suddenly(double targetAngle, double turnFrame) {
		final double D_ANGLE = getDelta(targetAngle);
		final double TURN_ANGLE = D_ANGLE/turnFrame;
		spin(TURN_ANGLE);
		return Math.abs(D_ANGLE - TURN_ANGLE);
	}
	public static double spinTo_ConstSpd(double nowAngle, double targetAngle, double angularSpeed) {
		if(angularSpeed <= 0)
			return GHQ.MAX;
		final double D_ANGLE = targetAngle - nowAngle;
		if(D_ANGLE < 0) {
			if(D_ANGLE < -angularSpeed) {
				return nowAngle - angularSpeed;
			} else {
				return targetAngle;
			}
		}else if(D_ANGLE > 0){
			if(D_ANGLE > angularSpeed) {
				return nowAngle + angularSpeed;
			} else {
				return targetAngle;
			}
		}else
			return nowAngle;
	}
	public static double spinTo_Suddenly(double nowAngle, double targetAngle, double turnFrame) {
		return nowAngle + (targetAngle - nowAngle)/turnFrame;
	}
	public void clear() {
		angle = GHQ.NONE;
	}
	//tool
	public static double random() {
		return Math.random()*Math.PI*2;
	}
	//information
	public double get() {
		return angle;
	}
	public double sin() {
		return Math.sin(angle);
	}
	public double cos() {
		return Math.cos(angle);
	}
	public double sin(double r) {
		return r*sin();
	}
	public double cos(double r) {
		return r*cos();
	}
	public double getDelta(double angle) {
		return GHQ.angleFormat(angle - this.angle);
	}
	public boolean isDeltaSmaller(double angle, double range) {
		return Math.abs(getDelta(angle)) < range;
	}
}
