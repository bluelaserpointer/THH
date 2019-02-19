package effect;

import static java.lang.Math.PI;

import core.DynamInteractable;
import core.Entity_double;
import core.GHQ;

public class Effect extends Entity_double{
	public final int UNIQUE_ID;
	public static int nowMaxUniqueID = -1;
	
	public final EffectScript SCRIPT;
	
	public String name;
	public final int
		SIZE,
		LIMIT_FRAME,
		LIMIT_RANGE;
	private final double
		ACCEL;
	private final int
		IMAGE_ID;
	
	public Effect(DynamInteractable source) {
		super(source,EffectBlueprint.dynam,EffectBlueprint.nowFrame);
		UNIQUE_ID = ++nowMaxUniqueID;
		this.SCRIPT = EffectBlueprint.script != null ? EffectBlueprint.script : EffectBlueprint.DEFAULT_SCRIPT;
		name = EffectBlueprint.name;
		SIZE = EffectBlueprint.size;
		LIMIT_FRAME = EffectBlueprint.limitFrame;
		LIMIT_RANGE = EffectBlueprint.limitRange;
		ACCEL = EffectBlueprint.accel;
		IMAGE_ID = EffectBlueprint.imageID;
	}
	
	public Effect(Effect effect) {
		super(effect.source,effect.dynam,EffectBlueprint.nowFrame);
		UNIQUE_ID = ++nowMaxUniqueID;
		SCRIPT = effect.SCRIPT != null ? effect.SCRIPT : EffectBlueprint.DEFAULT_SCRIPT;
		name = effect.name;
		SIZE = effect.SIZE;
		LIMIT_FRAME = effect.LIMIT_FRAME;
		LIMIT_RANGE = effect.LIMIT_RANGE;
		ACCEL = effect.ACCEL;
		IMAGE_ID = effect.IMAGE_ID;
	}

	@Override
	public final boolean defaultIdle() {
		//LifeSpan & Range
		if(LIMIT_FRAME <= GHQ.getPassedFrame(super.INITIAL_FRAME)) {
			SCRIPT.effectOutOfLifeSpan(this);
			GHQ.deleteEffect(this);
			return false;
		}
		if(LIMIT_RANGE <= dynam.getMovedDistance()){
			SCRIPT.effectOutOfRange(this);
			GHQ.deleteEffect(this);
			return false;
		}
		//OutOfStage
		if(!dynam.inStage()){
			GHQ.deleteEffect(this);
			return false;
		}
		//Speed & Acceleration
		dynam.move();
		dynam.addSpeed(ACCEL,true);
		return true;
	}
	@Override
	public final void defaultPaint() {
		GHQ.drawImageGHQ_center(IMAGE_ID, (int)dynam.getX(), (int)dynam.getY(), dynam.getAngle());
	}
	
	//tool
	public void split_xMirror(double dx,double dy) {
		this.dynam.addXY_allowsAngle(-dx/2,dy);
		GHQ.createEffect(this).dynam.addX_allowsAngle(dx);
	}
	public void split_yMirror(double dx,double dy) {
		this.dynam.addXY_allowsAngle(dx,-dy/2);
		GHQ.createEffect(this).dynam.addY_allowsAngle(dy);
	}
	public void split_Round(int radius,int amount) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 1;i < amount;i++)
			GHQ.createEffect(this).dynam.addXY_DA(radius, D_ANGLE*i);
		this.dynam.addXY_DA(radius, 0);
	}
	public void clone_Round(int radius,int amount) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++)
			GHQ.createEffect(this).dynam.addXY_DA(radius, D_ANGLE*i);
	}
	public void split_Burst(int radius,int amount,double speed) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 1;i < amount;i++)
			GHQ.createEffect(this).dynam.fastParaAdd_DASpd(radius, D_ANGLE*i, speed);
		this.dynam.fastParaAdd_DASpd(radius, 0 ,speed);
	}
	public void clone_Burst(int radius,int amount,double speed) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++)
			GHQ.createEffect(this).dynam.fastParaAdd_DASpd(radius, D_ANGLE*i, speed);
	}
	public int getPassedFrame() {
		return GHQ.getPassedFrame(INITIAL_FRAME);
	}
}
