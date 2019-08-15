package bullet;

import static java.lang.Math.PI;

import core.GHQ;
import hitShape.HitShape;
import hitShape.Square;
import paint.dot.DotPaint;
import paint.dot.HasDotPaint;
import physics.DstCntDynam;
import physics.Dynam;
import physics.Entity;
import physics.HasAnglePoint;
import physics.HasDynam;
import physics.Point;
import physics.Standpoint;
import unit.Unit;
import weapon.Weapon;

/**
 * A primal class for managing bullet.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class Bullet extends Entity implements HasDynam, HasDotPaint{
	public final Weapon ORIGIN_WEAPON;
	public final HasAnglePoint SHOOTER; //an information source of user
	protected final DstCntDynam dynam;
	
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
		super(new Square(new DstCntDynam(), 10), standpoint);
		dynam = (DstCntDynam)super.hitShape.point();
		dynam.setXYAngle(shooter);
		ORIGIN_WEAPON = originWeapon;
		SHOOTER = shooter;
		name = GHQ.NOT_NAMED;
		damage = 0;
		limitFrame = GHQ.MAX;
		limitRange = GHQ.MAX;
		penetration = 1;
		reflection = 0;
		accel = 0.0;
		paintScript = DotPaint.BLANK_SCRIPT;
	}
	public Bullet(Bullet bullet) {
		super(bullet.hitShape.clone(new DstCntDynam(bullet.dynam())), bullet.standpoint());
		dynam = (DstCntDynam)super.hitShape.point();
		dynam.setAll(bullet);
		ORIGIN_WEAPON = bullet.ORIGIN_WEAPON;
		SHOOTER = bullet.SHOOTER;
		name = bullet.name;
		damage = 0;
		limitFrame = bullet.limitFrame;
		limitRange = bullet.limitRange;
		penetration = bullet.penetration;
		reflection = bullet.reflection;
		accel = bullet.accel;
		paintScript = bullet.paintScript;
	}
	@Override
	public void idle() {
		if(defaultDeleteCheck())
			return;
		if(!dynamIdle())
			return;
		paint();
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
		return lifeSpan <= GHQ.passedFrame(super.INITIAL_FRAME);
	}
	public boolean checkIsOutOfRange(){
		return checkIsOutOfRange(limitRange);
	}
	public boolean checkIsOutOfRange(int range) {
		return range <= dynam.getMovedDistance();
	}
	public boolean isOutOfStage() {
		return !dynam.inStage();
	}
	public boolean outOfLifeSpan() {
		claimDelete();
		return true;
	}
	public boolean outOfRange() {
		claimDelete();
		return true;
	}
	public boolean outOfStage() {
		claimDelete();
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
			for(Unit unit : GHQ.stage().getHitUnits(GHQ.stage().getUnits_standpoint(this, false),this)) {
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
	@Override
	public void paint(boolean doAnimation) {
		defaultPaint();
	}
	public final void defaultPaint() {
		paintScript.dotPaint_turn(dynam, dynam.moveAngle());
	}
	//extends
	public boolean judgeLandscapeCollision() {
		return GHQ.stage().structures.intersected(this);
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
		claimDelete();
		return true;
	}
	public void hitObject() {
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
		return GHQ.stage().addBullet(getClone());
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
		return GHQ.passedFrame(INITIAL_FRAME);
	}
	public final int getPenetration() {
		return penetration;
	}
	public final int getReflection() {
		return reflection;
	}
	@Override
	public final HitShape hitShape() {
		return hitShape;
	}
	@Override
	public final DotPaint getDotPaint() {
		return paintScript;
	}
	@Override
	public String getName() {
		return "[Bullet]" + name;
	}
	@Override
	public Dynam dynam() {
		return dynam;
	}
	@Override
	public Point point() {
		return dynam;
	}
}
