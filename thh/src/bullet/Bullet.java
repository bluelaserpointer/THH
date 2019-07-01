package bullet;

import static java.lang.Math.PI;

import core.Deletable;
import core.Entity;
import core.GHQ;
import core.HitInteractable;
import core.Standpoint;
import geom.HitShape;
import geom.Square;
import paint.DotPaint;
import paint.HasDotPaint;
import physics.DstCntDynam;
import physics.Dynam;
import physics.HasAnglePoint;
import unit.Unit;
import weapon.Weapon;

/**
 * A primal class for managing bullet.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class Bullet extends Entity implements HitInteractable, HasDotPaint, Deletable{
	public final int UNIQUE_ID;
	public static int nowMaxUniqueID = -1;
	
	private int idleExecuted = 0;

	public final Weapon ORIGIN_WEAPON;
	public final HasAnglePoint SHOOTER; //an information source of user
	public final Standpoint STANDPOINT;
	public HitShape hitShape;
	private boolean isDeleted;
	
	public String name;
	public int
		damage;
	public int
		limitFrame,
		limitRange;
	public int
		penetration,
		reflection;
	public double
		accel;
	public DotPaint
		paintScript;
	public Bullet(Weapon originWeapon, HasAnglePoint shooter, Standpoint standpoint) {
		dynam.setXYAngle(shooter);
		UNIQUE_ID = ++nowMaxUniqueID;
		ORIGIN_WEAPON = originWeapon;
		SHOOTER = shooter;
		name = GHQ.NOT_NAMED;
		hitShape = new Square(10);
		damage = 0;
		limitFrame = GHQ.MAX;
		limitRange = GHQ.MAX;
		this.STANDPOINT = standpoint;
		penetration = 1;
		reflection = 0;
		accel = 0.0;
		paintScript = DotPaint.BLANK_SCRIPT;
	}
	public Bullet(Bullet bullet) {
		dynam.setAll(bullet);
		UNIQUE_ID = ++nowMaxUniqueID;
		ORIGIN_WEAPON = bullet.ORIGIN_WEAPON;
		SHOOTER = bullet.SHOOTER;
		name = bullet.name;
		hitShape = bullet.hitShape.clone();
		damage = 0;
		limitFrame = bullet.limitFrame;
		limitRange = bullet.limitRange;
		STANDPOINT = new Standpoint(bullet.STANDPOINT.get());
		penetration = bullet.penetration;
		reflection = bullet.reflection;
		accel = bullet.accel;
		paintScript = bullet.paintScript;
	}
	
	@Override
	public boolean idle() {
		return defaultIdle();
	}
	public final boolean defaultIdle() {
		if(defaultDeleteCheck())
			return false;
		if(!dynamIdle())
			return false;
		paint();
		return true;
	}
	public final boolean defaultDeleteCheck() {
		if(checkIsOutofLifeSpan())
			return outOfLifeSpan();
		if(checkIsOutOfRange())
			return outOfRange();
		if(isOutOfStage())
			return outOfStage();
		return false;
	}
	public boolean checkIsOutofLifeSpan(){
		return checkIsOutofLifeSpan(limitFrame);
	}
	public boolean checkIsOutofLifeSpan(int lifeSpan) {
		return lifeSpan <= GHQ.getPassedFrame(super.INITIAL_FRAME);
	}
	public boolean checkIsOutOfRange(){
		return checkIsOutOfRange(limitRange);
	}
	public boolean checkIsOutOfRange(int range) {
		return range <= ((DstCntDynam)dynam).getMovedDistance();
	}
	public boolean isOutOfStage() {
		return !dynam.inStage();
	}
	public boolean outOfLifeSpan() {
		delete();
		return true;
	}
	public boolean outOfRange() {
		delete();
		return true;
	}
	public boolean outOfStage() {
		delete();
		return true;
	}
	public final boolean dynamIdle() {
		return dynamIdle(GHQ.MAX);
	}
	public final boolean dynamIdle(int maxGap) {
		double length = GHQ.MAX;
		do {
			////////////
			//speed
			////////////
			length = dynam.move(Math.min(length, maxGap));
			
			////////////
			//landscape collision
			////////////
			if(judgeLandscapeCollision())
				return !hitLandScapeDeleteCheck();
			
			////////////
			//entity collision
			////////////
			for(Unit unit : GHQ.getHitUnits(GHQ.getUnits_standpoint(this, false),this)) {
				if(hitUnitDeleteCheck(unit))
					return false;
			}
		} while(length > 0);
		
		////////////
		//acceleration
		////////////
		dynam.addSpeed(accel,true);
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
	public boolean judgeLandscapeCollision() {
		return GHQ.hitStructure(this);
	}
	public boolean hitLandScapeDeleteCheck() {
		hitObject();
		if(penetration > 0) {
			if(penetration != GHQ.MAX)
				penetration--;
		}else {
			if(reflection > 0) {
				if(reflection != GHQ.MAX)
					reflection--;
				//edit reflection process
			}else {
				return outOfPenetration();
			}
		}
		return false;
	}
	public boolean hitUnitDeleteCheck(Unit unit) {
		unit.damage_amount(damage);
		hitObject();
		if(penetration > 0) {
			if(penetration != GHQ.MAX)
				penetration--;
		}else {
			return outOfPenetration();
		}
		return false;
	}
	public boolean outOfPenetration() {
		GHQ.deleteBullet(this);
		return true;
	}
	public void hitObject() {
	}
	public void beforeDelete() {
		isDeleted = true;
	}
	@Override
	public final void delete() {
		GHQ.deleteBullet(this);
	}
	
	//////////////
	//clone and split
	//////////////
	public Bullet getOriginal() {
		return new Bullet(this);
	}
	public final Bullet getClone() {
		Bullet BULLET = getOriginal();
		BULLET.dynam.setAll(dynam);
		return BULLET;
	}
	public final Bullet addCloneToGHQ() {
		return GHQ.addBullet(getClone());
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
		this.dynam.fastParaAdd_DASpd(radius, 0, speed);
	}
	public void clone_Burst(int radius,int amount,double speed) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++)
			addCloneToGHQ().dynam.fastParaAdd_DASpd(radius, D_ANGLE*i, speed);
	}
	public void split_NWay(int radius,double[] angles,double speed) {
		for(int i = 1;i < angles.length;i++)
			addCloneToGHQ().dynam.fastParaAdd_DASpd(radius, angles[i], speed);
		this.dynam.fastParaAdd_DASpd(radius, angles[0], speed);
	}
	public void clone_NWay(int radius,double[] angles,double speed) {
		for(double angle : angles)
			addCloneToGHQ().dynam.fastParaAdd_DASpd(radius, angle, speed);
	}
	public void split_NWay(int radius,double marginAngle,double amount,double speed) {
		this.dynam.spinMoveAngle(-marginAngle*(double)(amount - 1)/2.0);
		for(int i = 1;i < amount;i++)
			addCloneToGHQ().dynam.fastParaAdd_DASpd(radius, marginAngle*i, speed);
		this.dynam.fastParaAdd_DSpd(radius, speed);
	}
	public void clone_NWay(int radius,double marginAngle,double amount,double speed) {
		this.dynam.spinMoveAngle(-marginAngle*(double)(amount - 1)/2.0);
		for(int i = 0;i < amount;i++)
			addCloneToGHQ().dynam.fastParaAdd_DASpd(radius, marginAngle*i, speed);
	}

	//////////////
	//information
	//////////////
	public int getPassedFrame() {
		return GHQ.getPassedFrame(INITIAL_FRAME);
	}
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
		return STANDPOINT;
	}
	@Override
	public final HitShape getHitShape() {
		return hitShape;
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
