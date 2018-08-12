package effect;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

import thh.Chara;
import thh.Entity;
import thh.THH;

public class Effect extends Entity{
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
	
	public Effect() {
		super(EffectInfo.x,EffectInfo.y,EffectInfo.nowFrame,EffectInfo.source);
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

	public final boolean defaultIdle(THH thh) {
		final Chara chara = thh.getCharaClass(SOURCE);
		//LifeSpan & Range
		if(LIMIT_FRAME <= thh.getNowFrame() - super.APPEARED_FRAME) {
			chara.effectOutOfLifeSpan(this);
			thh.deleteEffect(this);
			return false;
		}
		if(LIMIT_RANGE <= movedDistance){
			chara.effectOutOfRange(this);
			thh.deleteEffect(this);
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
	public final void defaultPaint(THH thh) {
		if(angle%(2*PI) == 0)
			thh.drawImageTHH_center(IMAGE_ID, (int)x, (int)y);
		else
			thh.drawImageTHH_center(IMAGE_ID, (int)x, (int)y, angle);
	}
}
