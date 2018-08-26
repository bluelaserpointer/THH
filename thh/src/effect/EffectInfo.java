package effect;

import thh.THH;

public class EffectInfo {
	private final static int 
		NONE = thh.THH.NONE,
		MAX = thh.THH.MAX;
	public static String name;
	public static int
		nowFrame,
		source,
		kind,
		size,
		limitFrame,
		limitRange;
	public static double
		x,y,xSpeed,ySpeed,accel,
		angle;
	public static int
		imageID;
	
	public static final void clear() {
		name = THH.NOT_NAMED;
		kind = NONE;
		size = NONE;
		limitFrame = MAX;
		limitRange = MAX;
		xSpeed = 0.0;
		ySpeed = 0.0;
		accel = 1.0;
		angle = 0.0;
		imageID = NONE;
	}
	public static void fastParaSet_XYADSpd(double x,double y,double angle,int distance,int speed) {
		final double cos_angle = Math.cos(angle),sin_angle = Math.sin(angle);
		EffectInfo.angle = angle;
		EffectInfo.x = (int)(x + distance*cos_angle);
		EffectInfo.y = (int)(y + distance*sin_angle);
		EffectInfo.xSpeed = (int)(speed*cos_angle);
		EffectInfo.ySpeed = (int)(speed*sin_angle);
	}
	public static void fastParaSet_ADSpd(double angle,int distance,int speed) {
		EffectInfo.angle = angle;
		EffectInfo.xSpeed = (int)(speed*Math.cos(angle));
		EffectInfo.ySpeed = (int)(speed*Math.sin(angle));
	}
}
