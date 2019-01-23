package effect;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import thh.Dynam;
import thh.DynamInteractable;
import thh.THH;

public class EffectInfo {
	private final static int 
		NONE = thh.THH.NONE,
		MAX = thh.THH.MAX;
	public static final EffectScript DEFAULT_SCRIPT = new EffectScript();
	public static EffectScript script;
	public static String name;
	public static int
		nowFrame,
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
		script = DEFAULT_SCRIPT;
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
	public static void fastParaSet_SourceADSpd(DynamInteractable source,double angle,int distance,int speed) {
		final Dynam SOURCE_DYNAM = source.getDynam();
		fastParaSet_XYADSpd(SOURCE_DYNAM.getX(),SOURCE_DYNAM.getY(),SOURCE_DYNAM.getAngle() + angle,distance,speed);
	}
	public static void fastParaSet_SourceDSpd(DynamInteractable source,int distance,int speed) {
		final Dynam SOURCE_DYNAM = source.getDynam();
		fastParaSet_XYADSpd(SOURCE_DYNAM.getX(),SOURCE_DYNAM.getY(),SOURCE_DYNAM.getAngle(),distance,speed);
	}
	public static void fastParaSet_SourceSpd(DynamInteractable source,int speed) {
		final Dynam SOURCE_DYNAM = source.getDynam();
		fastParaSet_XYADSpd(SOURCE_DYNAM.getX(),SOURCE_DYNAM.getY(),SOURCE_DYNAM.getAngle(),0,speed);
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
	
	//generation
	public final static void createEffect_RoundDesign(DynamInteractable source,int amount,double gunnerX,double gunnerY,double radius){
		final double ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++){
			EffectInfo.x = gunnerX + radius*cos(ANGLE*i);
			EffectInfo.y = gunnerY + radius*sin(ANGLE*i);
			THH.createEffect(source);
		}
	}
}
