package effect;

import thh.Dynam;
import thh.DynamInteractable;
import thh.Entity_double;
import thh.THH;

public class Effect extends Entity_double{
	public final int UNIQUE_ID;
	public static int nowMaxUniqueID = -1;
	
	public final EffectScript SCRIPT;
	public final Dynam dynam;
	
	public String name;
	public final int
		KIND,
		SIZE,
		LIMIT_FRAME,
		LIMIT_RANGE;
	private final double
		ACCEL;
	private final int
		IMAGE_ID;
	
	public Effect(DynamInteractable source) {
		super(source,EffectInfo.x,EffectInfo.y,EffectInfo.nowFrame);
		dynam = new Dynam(EffectInfo.x,EffectInfo.y,EffectInfo.xSpeed,EffectInfo.ySpeed,EffectInfo.angle);
		UNIQUE_ID = ++nowMaxUniqueID;
		this.SCRIPT = EffectInfo.script != null ? EffectInfo.script : EffectInfo.DEFAULT_SCRIPT;
		name = EffectInfo.name;
		KIND = EffectInfo.kind;
		SIZE = EffectInfo.size;
		LIMIT_FRAME = EffectInfo.limitFrame;
		LIMIT_RANGE = EffectInfo.limitRange;
		ACCEL = EffectInfo.accel;
		IMAGE_ID = EffectInfo.imageID;
	}
	
	public Effect(Effect effect) {
		super(effect.SOURCE,EffectInfo.x,EffectInfo.y,EffectInfo.nowFrame);
		dynam = effect.dynam.clone();
		UNIQUE_ID = ++nowMaxUniqueID;
		SCRIPT = effect.SCRIPT != null ? effect.SCRIPT : EffectInfo.DEFAULT_SCRIPT;
		name = effect.name;
		KIND = effect.KIND;
		SIZE = effect.SIZE;
		LIMIT_FRAME = effect.LIMIT_FRAME;
		LIMIT_RANGE = effect.LIMIT_RANGE;
		ACCEL = effect.ACCEL;
		IMAGE_ID = effect.IMAGE_ID;
	}
	
	public final boolean defaultIdle() {
		//LifeSpan & Range
		if(LIMIT_FRAME <= THH.getPassedFrame(super.INITIAL_FRAME)) {
			SCRIPT.effectOutOfLifeSpan(this);
			THH.deleteEffect(this);
			return false;
		}
		if(LIMIT_RANGE <= dynam.getMovedDistance()){
			SCRIPT.effectOutOfRange(this);
			THH.deleteEffect(this);
			return false;
		}
		//OutOfStage
		if(!dynam.inStage()){
			THH.deleteEffect(this);
			return false;
		}
		//Speed & Acceleration
		dynam.move();
		dynam.accelerate(ACCEL);
		return true;
	}
	public final void defaultPaint() {
		THH.drawImageTHH_center(IMAGE_ID, (int)dynam.getX(), (int)dynam.getY(), dynam.getAngle());
	}
}
