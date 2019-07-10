package physics;

import java.io.Serializable;

import core.GHQ;

public class Angle implements Serializable{
	private static final long serialVersionUID = 5495261807326477442L;
	protected double angle;
	
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
	public void clear() {
		angle = GHQ.NONE;
	}
	//information
	public double angle() {
		return angle;
	}
	public double sin() {
		return Math.sin(angle);
	}
	public double cos() {
		return Math.cos(angle);
	}
	public double getDelta(double angle) {
		return GHQ.angleFormat(angle - this.angle);
	}
	public boolean isDeltaSmaller(double angle, double range) {
		return Math.abs(getDelta(angle)) < range;
	}
}
