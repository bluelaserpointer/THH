package physics;

import java.io.Serializable;

import core.GHQ;

public class Angle implements Serializable{
	private static final long serialVersionUID = 5495261807326477442L;
	protected double angle;
	
	//initialization
	public Angle() {
		this.angle = GHQ.NONE;
	}
	public Angle(double angle) {
		this.angle = angle;
	}
	//control
	public void set(double angle) {
		this.angle = angle;
	}
	public void set(Angle sample) {
		this.angle = sample.angle;
	}
	public void set(double dx, double dy) {
		angle = Math.atan2(dy, dx);
	}
	public void spin(double angle) {
		this.angle += angle;
	}
	public double spinToTargetCapped(double targetAngle, double maxTurnAngle) {
		if(maxTurnAngle <= 0)
			return GHQ.MAX;
		final double D_ANGLE = getDelta(targetAngle);
		if(D_ANGLE < 0) {
			if(D_ANGLE < -maxTurnAngle) {
				spin(-maxTurnAngle);
				return Math.abs(D_ANGLE + maxTurnAngle);
			} else {
				set(targetAngle);
				return 0.0;
			}
		}else if(D_ANGLE > 0){
			if(D_ANGLE > maxTurnAngle) {
				spin(maxTurnAngle);
				return Math.abs(D_ANGLE - maxTurnAngle);
			} else {
				set(targetAngle);
				return 0.0;
			}
		}else
			return 0.0;
	}
	public double spinToTargetSuddenly(double targetAngle, double turnFrame) {
		final double D_ANGLE = getDelta(targetAngle);
		final double TURN_ANGLE = D_ANGLE/turnFrame;
		spin(TURN_ANGLE);
		return Math.abs(D_ANGLE - TURN_ANGLE);
	}
	public void clear() {
		angle = GHQ.NONE;
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
	public double getDelta(double angle) {
		return GHQ.angleFormat(angle - this.angle);
	}
	public boolean isDeltaSmaller(double angle, double range) {
		return Math.abs(getDelta(angle)) < range;
	}
}
