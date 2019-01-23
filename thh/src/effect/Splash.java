package effect;

import static java.lang.Math.PI;
import static java.lang.Math.random;

import thh.Dynam;
import thh.DynamInteractable;
import thh.THH;

public class Splash {
	public static String name;
	public static int amount;
	public static int speed;
	public static double accel;
	public static int limitFrame;
	public static int maxSpeed;
	public static int imageID;
	public static boolean doImageRotate;
	public static void clear() {
		name = "SPLASH";
		amount = 1;
		accel = 1.0;
		doImageRotate = true;
	}
	public static void setEffect(DynamInteractable source) {
		EffectInfo.name = name;
		EffectInfo.script = splash_script;
		EffectInfo.size = THH.NONE;
		EffectInfo.limitFrame = limitFrame;
		EffectInfo.limitRange = THH.MAX;
		EffectInfo.imageID = imageID;
		for(int i = 0;i < amount;i++) {
			final Dynam SOURCE_DYNAM = source.getDynam();
			EffectInfo.fastParaSet_XYADSpd(SOURCE_DYNAM.getX(),SOURCE_DYNAM.getY(),2*PI*random(),10,THH.random2(0,maxSpeed));
			THH.createEffect(source);
		}
	}
	private static final EffectScript splash_script = new EffectScript() {
		@Override
		public final void effectNoAnmPaint(Effect effect) {
			if(doImageRotate) {
				THH.setImageAlpha((float)(1.0 - (double)THH.getPassedFrame(effect.INITIAL_FRAME)/effect.LIMIT_FRAME));
				effect.defaultPaint();
				THH.setImageAlpha();
			}else {
				THH.setImageAlpha((float)(1.0 - (double)THH.getPassedFrame(effect.INITIAL_FRAME)/effect.LIMIT_FRAME));
				THH.drawImageTHH_center(imageID, (int)effect.dynam.getX(), (int)effect.dynam.getY());
				THH.setImageAlpha();
			}
		}
	};
}
