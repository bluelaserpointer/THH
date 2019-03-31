package bullet;

import static java.lang.Math.PI;

import core.Entity_double;
import core.GHQ;
import core.HitInteractable;
import core.Standpoint;
import geom.HitShape;
import paint.DotPaint;
import paint.HasDotPaint;
import physicis.DstCntDynam;
import physicis.Dynam;
import physicis.HasDynam;
import unit.Unit;

/**
 * A primal class for managing bullet.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class Bullet extends Entity_double implements HitInteractable,HasDotPaint{
	public final int UNIQUE_ID;
	public static int nowMaxUniqueID = -1;
	
	private int idleExecuted = 0;
	
	public final BulletScript SCRIPT; //a script of unique behaviors
	public final Standpoint standpoint;
	public final HitShape hitshape;
	private final DstCntDynam dynam = new DstCntDynam();
	
	public String name;
	public final int
		SIZE,
		INITIAL_ATK;
	public int
		atk,
		offSet;
	private final int
		LIMIT_FRAME,
		LIMIT_RANGE;
	private int
		penetration,
		reflection;
	private final double
		ACCEL;
	public final DotPaint
		paintScript;
	public final boolean
		IS_LASER;
	public Bullet(HasDynam source) {
		super(source, BulletBlueprint.nowFrame);
		UNIQUE_ID = ++nowMaxUniqueID;
		SCRIPT = BulletBlueprint.script != null ? BulletBlueprint.script : BulletBlueprint.DEFAULT_SCRIPT;
		name = BulletBlueprint.name;
		SIZE = BulletBlueprint.size;
		dynam.setAllBySample(BulletBlueprint.dynam);
		LIMIT_FRAME = BulletBlueprint.limitFrame;
		LIMIT_RANGE = BulletBlueprint.limitRange;
		standpoint = new Standpoint(BulletBlueprint.standpointGroup);
		hitshape = BulletBlueprint.hitshape;
		INITIAL_ATK = atk = BulletBlueprint.atk;
		offSet = BulletBlueprint.offSet;
		penetration = BulletBlueprint.penetration;
		reflection = BulletBlueprint.reflection;
		ACCEL = BulletBlueprint.accel;
		paintScript = BulletBlueprint.paintScript;
		IS_LASER = BulletBlueprint.isLaser;
	}
	public Bullet(Bullet bullet) {
		super(bullet.source, GHQ.getNowFrame());
		UNIQUE_ID = ++nowMaxUniqueID;
		SCRIPT = bullet.SCRIPT != null ? bullet.SCRIPT : BulletBlueprint.DEFAULT_SCRIPT;
		name = bullet.name;
		SIZE = bullet.SIZE;
		dynam.setAllBySample(bullet.dynam);
		LIMIT_FRAME = bullet.LIMIT_FRAME;
		LIMIT_RANGE = bullet.LIMIT_RANGE;
		standpoint = new Standpoint(bullet.standpoint.get());
		hitshape = bullet.hitshape.clone();
		INITIAL_ATK = atk = bullet.atk;
		offSet = bullet.offSet;
		penetration = bullet.penetration;
		reflection = bullet.reflection;
		ACCEL = bullet.ACCEL;
		paintScript = bullet.paintScript;
		IS_LASER = bullet.IS_LASER;
	}
	@Override
	public final boolean defaultIdle() {
		if(allDeleteCheck())
			return false;
		dynam();
		return true;
	}
	public final boolean allDeleteCheck() {
		if(lifeSpanCheck())
			return true;
		if(rangeCheck())
			return true;
		if(inStageCheck())
			return true;
		return false;
	}
	public final boolean lifeSpanCheck() {
		return lifeSpanCheck(LIMIT_FRAME);
	}
	public final boolean lifeSpanCheck(int limitFrame) {
		if(limitFrame <= GHQ.getPassedFrame(super.INITIAL_FRAME) && SCRIPT.bulletOutOfLifeSpan(this)) {
			GHQ.deleteBullet(this);
			return true;
		}
		return false;
	}
	public final boolean rangeCheck() {
		return lifeSpanCheck(LIMIT_FRAME);
	}
	public final boolean rangeCheck(int limitRange) {
		if(limitRange <= dynam.getMovedDistance() && SCRIPT.bulletOutOfRange(this)){
			GHQ.deleteBullet(this);
			return true;
		}
		return false;
	}
	public final boolean inStageCheck() {
		if(!dynam.inStage()){
			GHQ.deleteBullet(this);
			return true;
		}
		return false;
	}
	public final boolean dynam() {
		return dynam(true,GHQ.MAX);
	}
	public final boolean dynam(boolean doHit) {
		return dynam(doHit,GHQ.MAX);
	}
	public final boolean dynam(int maxGap) {
		return dynam(true,maxGap);
	}
	public final boolean dynam(boolean doHit,int maxGap) {
		//speed & acceleration
		dynam.move();
		dynam.addSpeed(ACCEL,true);
		if(!doHit)
			return true;
		//landscape collision
		if(SCRIPT.bulletIfHitLandscape(this,(int)dynam.getX(),(int)dynam.getY())){
			SCRIPT.bulletHitObject(this);
			if(penetration > 0) {
				if(penetration != GHQ.MAX)
					penetration--;
			}else {
				if(reflection > 0) {
					if(reflection != GHQ.MAX)
						reflection--;
					//edit reflection process
				}else if(SCRIPT.bulletOutOfDurability(this)) {
					GHQ.deleteBullet(this);
					return false;
				}
			}
		}
		//entity collision
		for(Unit chara : GHQ.getHitCharacters(GHQ.getCharacters_standpoint(this, false),this)) {
			chara.damage_amount(atk);
			SCRIPT.bulletHitObject(this);
			if(penetration > 0) {
				if(penetration != GHQ.MAX)
					penetration--;
			}else if(SCRIPT.bulletOutOfDurability(this)) {
				GHQ.deleteBullet(this);
				return false;
			}
		}
		return true;
	}
	@Override
	public final void defaultPaint() {
		paintScript.dotPaint_turn((int)dynam.getX(),(int)dynam.getY(), dynam.getAngle());
	}
	//tool
	public void split_xMirror(double dx,double dy) {
		this.dynam.addXY_allowsAngle(-dx/2,dy);
		GHQ.createBullet(this).dynam.addX_allowsAngle(dx);
	}
	public void split_yMirror(double dx,double dy) {
		this.dynam.addXY_allowsAngle(dx,-dy/2);
		GHQ.createBullet(this).dynam.addY_allowsAngle(dy);
	}
	public void split_Round(int radius,int amount) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 1;i < amount;i++)
			GHQ.createBullet(this).dynam.addXY_DA(radius, D_ANGLE*i);
		this.dynam.addXY_DA(radius, 0);
	}
	public void clone_Round(int radius,int amount) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++)
			GHQ.createBullet(this).dynam.addXY_DA(radius, D_ANGLE*i);
	}
	public void split_Burst(int radius,int amount,double speed) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 1;i < amount;i++)
			GHQ.createBullet(this).dynam.fastParaAdd_DASpd(radius, D_ANGLE*i, speed);
		this.dynam.fastParaAdd_DASpd(radius, 0, speed);
	}
	public void clone_Burst(int radius,int amount,double speed) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++)
			GHQ.createBullet(this).dynam.fastParaAdd_DASpd(radius, D_ANGLE*i, speed);
	}
	public void split_NWay(int radius,double[] angles,double speed) {
		for(int i = 1;i < angles.length;i++)
			GHQ.createBullet(this).dynam.fastParaAdd_DASpd(radius, angles[i], speed);
		this.dynam.fastParaAdd_DASpd(radius, angles[0], speed);
	}
	public void clone_NWay(int radius,double[] angles,double speed) {
		for(double angle : angles)
			GHQ.createBullet(this).dynam.fastParaAdd_DASpd(radius, angle, speed);
	}
	public void split_NWay(int radius,double marginAngle,double amount,double speed) {
		this.dynam.spin(-marginAngle*(double)(amount - 1)/2.0);
		for(int i = 1;i < amount;i++)
			GHQ.createBullet(this).dynam.fastParaAdd_DASpd(radius, marginAngle*i, speed);
		this.dynam.fastParaAdd_DSpd(radius, speed);
	}
	public void clone_NWay(int radius,double marginAngle,double amount,double speed) {
		this.dynam.spin(-marginAngle*(double)(amount - 1)/2.0);
		for(int i = 0;i < amount;i++)
			GHQ.createBullet(this).dynam.fastParaAdd_DASpd(radius, marginAngle*i, speed);
	}
	public int getPassedFrame() {
		return GHQ.getPassedFrame(INITIAL_FRAME);
	}
	
	//information
	public final int getIdleCount() {
		return idleExecuted;
	}
	public final int getPenetration() {
		return penetration;
	}
	public final int getReflection() {
		return reflection;
	}
	@Override
	public final Standpoint getStandpoint() {
		return standpoint;
	}
	@Override
	public final HitShape getHitShape() {
		return hitshape;
	}
	@Override
	public final DotPaint getPaintScript() {
		return paintScript;
	}
	@Override
	public final Dynam getDynam() {
		return dynam;
	}
}
