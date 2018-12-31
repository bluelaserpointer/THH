package bullet;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import thh.DynamInteractable;
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
		size = 2;
		team = NONE;
		atk = offSet = 0;
		penetration = reflection = 0;
		limitFrame = limitRange = MAX;
		xSpeed = ySpeed = 0.0;
		accel = 1.0;
		angle = 0.0;
		imageID = NONE;
		hitEnemy = true;
		isLaser = false;
	}
	public static final void addXYSpeed(double xSpeed,double ySpeed) {
		BulletInfo.xSpeed += xSpeed;
		BulletInfo.ySpeed += ySpeed;
	}
	public static final void fastParaSet_SourceADXYSpd(DynamInteractable source,double angle,double dx,double dy,double speed) {
		fastParaSet_XYADXYSpd(source.getX(),source.getY(),source.getAngle() + angle,dx,dy,speed);
	}
	public static final void fastParaSet_SourceDXYSpd(DynamInteractable source,double dx,double dy,double speed) {
		fastParaSet_XYADXYSpd(source.getX(),source.getY(),source.getAngle(),dx,dy,speed);
	}
	public static final void fastParaSet_SourceADSpd(DynamInteractable source,double angle,double distance,double speed) {
		fastParaSet_XYADXYSpd(source.getX(),source.getY(),source.getAngle() + angle,0,distance,speed);
	}
	public static final void fastParaSet_SourceDSpd(DynamInteractable source,double distance,double speed) {
		fastParaSet_XYADXYSpd(source.getX(),source.getY(),source.getAngle(),0,distance,speed);
	}
	public static final void fastParaSet_SourceSpd(DynamInteractable source,double speed) {
		fastParaSet_XYASpd(source.getX(),source.getY(),source.getAngle(),speed);
	}
	public static final void fastParaSet_XYASpd(double launchX,double launchY,double angle,double speed){
		final double cos_angle = cos(angle),sin_angle = sin(angle);
		BulletInfo.angle = angle;
		x = launchX;
		y = launchY;
		xSpeed = speed*cos_angle;
		ySpeed = speed*sin_angle;
	}
	public static final void fastParaSet_XYADXYSpd(double launchX,double launchY,double angle,double dx,double dy,double speed){
		final double cos_angle = cos(angle),sin_angle = sin(angle);
		BulletInfo.angle = angle;
		if(dx == 0.0) {
			if(dy == 0.0) {
				x = launchX;
				y = launchY;
			}else {
				x = launchX + dy*cos_angle;
				y = launchY + dy*sin_angle;
			}
		}else {
			if(dy == 0.0) {
				x = launchX + dx*sin_angle;
				y = launchY - dx*cos_angle;
			}else {
				x = launchX + dx*sin_angle + dy*cos_angle;
				y = launchY - dx*cos_angle + dy*sin_angle;
			}
		}
		xSpeed = speed*cos_angle;
		ySpeed = speed*sin_angle;
	}
	public static final void fastParaSet_onlySpd_SourceSpd(DynamInteractable source,double speed){
		BulletInfo.angle = source.getAngle();
		xSpeed = speed*cos(angle);
		ySpeed = speed*sin(angle);
	}
	//create
	public final static void createBullet_RoundDesign(DynamInteractable source,double radius,int amount) {
		createBullet_RoundDesign(source,source.getX(),source.getY(),radius,amount);
	}
	public final static void createBullet_RoundDesign(DynamInteractable source,double gunnerX,double gunnerY,double radius,int amount){
		final double ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++){
			BulletInfo.x = gunnerX + radius*cos(ANGLE*i);
			BulletInfo.y = gunnerY + radius*sin(ANGLE*i);
			THH.createBullet(source);
		}
	}
	public final static void createBullet_BurstDesign(DynamInteractable source,double gunnerX,double gunnerY,double radius,int speed,int amount){
		final double BASE_ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++){
			final double ANGLE = BASE_ANGLE*i;
			final double COS_ANGLE = cos(ANGLE),SIN_ANGLE = sin(ANGLE);
			BulletInfo.x = gunnerX + radius*COS_ANGLE;
			BulletInfo.y = gunnerY + radius*SIN_ANGLE;
			BulletInfo.angle = ANGLE;
			BulletInfo.xSpeed = speed*COS_ANGLE;
			BulletInfo.ySpeed = speed*SIN_ANGLE;
			THH.createBullet(source);
		}
	}
}
