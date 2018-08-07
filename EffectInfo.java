package thhMemo;


public class EffectInfo {
	public static String
		name;
	public static int
		kind,
		size;
	public static double
		accel;
	public static int
		x,y,
		xSpeed,ySpeed,
		imageID;
	
	public static void fastDynamSet_XYADSpd(double x,double y,double angle,int distance,int speed) {
		final double cos_angle = Math.cos(angle),sin_angle = Math.sin(angle);
		EffectInfo.x = (int)(x + distance*cos_angle);
		EffectInfo.y = (int)(y + distance*sin_angle);
		EffectInfo.xSpeed = (int)(speed*cos_angle);
		EffectInfo.ySpeed = (int)(speed*sin_angle);
	}
	public static void fastDynamSet_ADSpd(double angle,int distance,int speed) {
		EffectInfo.xSpeed = (int)(speed*Math.cos(angle));
		EffectInfo.ySpeed = (int)(speed*Math.sin(angle));
	}
}
