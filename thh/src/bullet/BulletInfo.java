package bullet;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import thh.THH;

public final class BulletInfo {
	private final static int
		NONE = thh.THH.NONE,
		MAX = thh.THH.MAX;
	public static final BulletScript DEFAULT_SCRIPT = new BulletScript();
	public static BulletScript script;
	public static String name;
	public static int
		nowFrame,
		kind,
		size,
		team,
		atk,
		offSet,
		penetration,
		reflection,
		limitFrame,
		limitRange;
	public static double
		x,y,xSpeed,ySpeed,accel,
		angle;
	public static int
		imageID;
	public static boolean
		hitEnemy,
		isLaser;
	
	public static final void clear() {
		name = THH.NOT_NAMED;
		script = DEFAULT_SCRIPT;
		kind = NONE;
		size = 0;
		team = NONE;
		atk = 0;
		offSet = 0;
		penetration = 0;
		reflection = 0;
		limitFrame = MAX;
		limitRange = MAX;
		xSpeed = 0.0;
		ySpeed = 0.0;
		accel = 1.0;
		angle = 0.0;
		imageID = NONE;
		hitEnemy = true;
		isLaser = false;
	}
	public static final void fastParaSet_XYADSpd(double gunnerX,double gunnerY,double angle,double distance,double speed){
		final double cos_angle = cos(angle),sin_angle = sin(angle);
		BulletInfo.angle = angle;
		x = gunnerX + distance*cos_angle;
		y = gunnerY + distance*sin_angle;
		xSpeed = speed*cos_angle;
		ySpeed = speed*sin_angle;
	}
	public static final void fastParaSet_ASpd(double angle,double speed){
		BulletInfo.angle = angle;
		xSpeed = speed*cos(angle);
		ySpeed = speed*sin(angle);
	}
}
