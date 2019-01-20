package effect;

import static java.lang.Math.*;

import thh.DynamInteractable;
import thh.Entity_double;
import thh.THH;

public class Effect extends Entity_double{
	public final int UNIQUE_ID;
	public static int nowMaxUniqueID = -1;
	
	public final EffectScript SCRIPT;
	
	public String name;
	public final int
		KIND,
		SIZE,
		LIMIT_FRAME,
		LIMIT_RANGE;
	private int
		movedDistance;
	private final double
		ACCEL;
	private double
		xSpeed,ySpeed,
		angle;
	private final int
		IMAGE_ID;
	
	public Effect(DynamInteractable source) {
		super(source,EffectInfo.x,EffectInfo.y,EffectInfo.nowFrame);
		UNIQUE_ID = ++nowMaxUniqueID;
		this.SCRIPT = EffectInfo.script != null ? EffectInfo.script : EffectInfo.DEFAULT_SCRIPT;
		name = EffectInfo.name;
		KIND = EffectInfo.kind;
		SIZE = EffectInfo.size;
		LIMIT_FRAME = EffectInfo.limitFrame;
		LIMIT_RANGE = EffectInfo.limitRange;
		ACCEL = EffectInfo.accel;
		xSpeed = EffectInfo.xSpeed;
		ySpeed = EffectInfo.ySpeed;
		angle = EffectInfo.angle;
		IMAGE_ID = EffectInfo.imageID;
	}
	
	public Effect(Effect effect) {
		super(effect.SOURCE,EffectInfo.x,EffectInfo.y,EffectInfo.nowFrame);
		UNIQUE_ID = ++nowMaxUniqueID;
		SCRIPT = effect.SCRIPT != null ? effect.SCRIPT : EffectInfo.DEFAULT_SCRIPT;
		name = effect.name;
		KIND = effect.KIND;
		SIZE = effect.SIZE;
		LIMIT_FRAME = effect.LIMIT_FRAME;
		LIMIT_RANGE = effect.LIMIT_RANGE;
		ACCEL = effect.ACCEL;
		xSpeed = effect.xSpeed;
		ySpeed = effect.ySpeed;
		angle = effect.angle;
		IMAGE_ID = effect.IMAGE_ID;
	}
	
	public final void spin(double angle) {
		this.angle += angle;
	}
	public final boolean defaultIdle() {
		//LifeSpan & Range
		if(LIMIT_FRAME <= THH.getPassedFrame(super.INITIAL_FRAME)) {
			SCRIPT.effectOutOfLifeSpan(this);
			THH.deleteEffect(this);
			return false;
		}
		if(LIMIT_RANGE <= movedDistance){
			SCRIPT.effectOutOfRange(this);
			THH.deleteEffect(this);
			return false;
		}
		//OutOfStage
		if(!THH.inStage((int)x, (int)y)){
			THH.deleteEffect(this);
			return false;
		}
		//Speed & Acceleration
		x += xSpeed;
		y += ySpeed;
		movedDistance += sqrt(xSpeed*xSpeed + ySpeed*ySpeed);
		if(ACCEL != 1.0) {
			xSpeed *= ACCEL;
			ySpeed *= ACCEL;
		}
		return true;
	}
	public final void defaultPaint() {
		if(angle == 0)
			THH.drawImageTHH_center(IMAGE_ID, (int)x, (int)y);
		else
			THH.drawImageTHH_center(IMAGE_ID, (int)x, (int)y, angle);
	}
}
