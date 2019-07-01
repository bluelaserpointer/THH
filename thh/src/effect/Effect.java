package effect;

import static java.lang.Math.PI;

import core.Deletable;
import core.Entity;
import core.GHQ;
import paint.DotPaint;
import paint.HasDotPaint;
import physics.DstCntDynam;
import physics.Dynam;
import physics.HasDynam;

/**
 * A primal class for managing effect.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class Effect extends Entity implements HasDotPaint, Deletable{
	public final int UNIQUE_ID;
	public static int nowMaxUniqueID = -1;

	public final HasDynam SHOOTER;
	private boolean isDeleted = false;
	public String
		name;
	public int
		limitFrame,
		limitRange;
	public double
		accel;
	public DotPaint
		paintScript;
	
	public Effect() {
		SHOOTER = HasDynam.NULL_DYNAM_SOURCE;
		UNIQUE_ID = ++nowMaxUniqueID;
		name = GHQ.NOT_NAMED;
		limitFrame = GHQ.MAX;
		limitRange = GHQ.MAX;
		accel = 0.0;
		paintScript = DotPaint.BLANK_SCRIPT;
	}
	public Effect(HasDynam source) {
		dynam.setAll(source.getDynam());
		SHOOTER = source;
		UNIQUE_ID = ++nowMaxUniqueID;
		name = GHQ.NOT_NAMED;
		limitFrame = GHQ.MAX;
		limitRange = GHQ.MAX;
		accel = 0.0;
		paintScript = DotPaint.BLANK_SCRIPT;
	}
	public Effect(Effect effect) {
		dynam.setAll(effect.dynam);
		SHOOTER = effect.SHOOTER;
		UNIQUE_ID = ++nowMaxUniqueID;
		name = effect.name;
		limitFrame = effect.limitFrame;
		limitRange = effect.limitRange;
		accel = effect.accel;
		paintScript = effect.paintScript;
	}

	@Override
	public boolean idle() {
		return defaultIdle();
	}
	public final boolean defaultIdle() {
		//LifeSpan & Range
		if(isOutOfLifeSpan() && outOfLifeSpanProcess())
			return false;
		if(isOutOfRange() && outOfRangeProcess())
			return false;
		//OutOfStage
		if(isOutOfRange() && outOfStageProcess())
			return false;
		//Speed & Acceleration
		dynam.move();
		dynam.addSpeed(accel,true);
		//Paint
		paint();
		return true;
	}
	public void paint() {
		defaultPaint();
	}
	@Override
	public final void defaultPaint() {
		paintScript.dotPaint_turn(dynam, dynam.moveAngle());
	}
	
	//extends
	public boolean isOutOfLifeSpan() {
		return limitFrame <= GHQ.getPassedFrame(super.INITIAL_FRAME);
	}
	public boolean isOutOfRange() {
		return limitRange <= ((DstCntDynam)dynam).getMovedDistance();
	}
	public boolean isOutOfStage() {
		return !dynam.inStage();
	}
	public boolean outOfLifeSpanProcess() {
		delete();
		return true;
	}
	public boolean outOfRangeProcess() {
		delete();
		return true;
	}
	public boolean outOfStageProcess() {
		delete();
		return true;
	}
	public void beforeDelete() {
		isDeleted = true;
	}
	@Override
	public final void delete() {
		GHQ.deleteEffect(this);
	}
	
	//tool
	//////////////
	//paint
	//////////////
	public final void fadingPaint() {
		GHQ.setImageAlpha((float)(1.0 - (double)GHQ.getPassedFrame(INITIAL_FRAME)/limitFrame));
		defaultPaint();
		GHQ.setImageAlpha();
	}
	public final void fadingPaint(int delay) {
		final int PASSED_FRAME = GHQ.getPassedFrame(INITIAL_FRAME);
		if(PASSED_FRAME < delay)
			defaultPaint();
		else {
			GHQ.setImageAlpha((float)(1.0 - (double)(PASSED_FRAME - delay)/(limitFrame - delay)));
			defaultPaint();
			GHQ.setImageAlpha();
		}
	}
	
	//////////////
	//clone and split
	//////////////
	public Effect getOriginal() {
		return new Effect(this);
	}
	public Effect getClone() {
		final Effect EFFECT = getOriginal();
		EFFECT.dynam.setAll(dynam);
		return EFFECT;
	}
	public final Effect addCloneToGHQ() {
		return GHQ.addEffect(getClone());
	}
	public void split_xMirror(double dx,double dy) {
		this.dynam.addXY_allowsMoveAngle(-dx/2,dy);
		addCloneToGHQ().dynam.addX_allowsMoveAngle(dx);
	}
	public void split_yMirror(double dx,double dy) {
		this.dynam.addXY_allowsMoveAngle(dx,-dy/2);
		addCloneToGHQ().dynam.addY_allowsMoveAngle(dy);
	}
	public void split_Round(int radius,int amount) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 1;i < amount;i++)
			addCloneToGHQ().dynam.addXY_DA(radius, D_ANGLE*i);
		this.dynam.addXY_DA(radius, 0);
	}
	public void clone_Round(int radius,int amount) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++)
			addCloneToGHQ().dynam.addXY_DA(radius, D_ANGLE*i);
	}
	public void split_Burst(int radius,int amount,double speed) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 1;i < amount;i++)
			addCloneToGHQ().dynam.fastParaAdd_DASpd(radius, D_ANGLE*i, speed);
		this.dynam.fastParaAdd_DASpd(radius, 0 ,speed);
	}
	public void clone_Burst(int radius,int amount,double speed) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++)
			addCloneToGHQ().dynam.fastParaAdd_DASpd(radius, D_ANGLE*i, speed);
	}
	
	//////////////
	//information
	//////////////
	public int getPassedFrame() {
		return GHQ.getPassedFrame(INITIAL_FRAME);
	}
	@Override
	public final DotPaint getPaintScript() {
		return paintScript;
	}
	@Override
	public final boolean isDeleted() {
		return isDeleted;
	}
	@Override
	public Dynam def_dynam() {
		return new DstCntDynam();
	}
}
