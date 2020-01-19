package bullet;

import static java.lang.Math.PI;

import calculate.Damage;
import core.GHQ;
import core.GHQObject;
import paint.dot.DotPaint;
import paint.dot.HasDotPaint;
import physics.DstCntDynam;
import physics.HasPoint;
import physics.hitShape.Square;
import unit.Unit;
import weapon.Weapon;

/**
 * A primal class for managing bullet.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class Bullet extends GHQObject implements HasPoint, HasDotPaint{
	public static final Bullet NULL_BULLET = new Bullet(GHQObject.NULL_GHQ_OBJECT);
	protected Weapon originWeapon;
	protected GHQObject shooter; //an information source of user
	
	public Damage
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
	public void init() {
		damage = Damage.NULL_DAMAGE;
		limitFrame = GHQ.MAX;
		limitRange = GHQ.MAX;
		penetration = 0;
		reflection = 0;
		accel = 0.0;
		paintScript = DotPaint.BLANK_SCRIPT;
	}
	public Bullet(GHQObject shooter) {
		physics().setPoint(new DstCntDynam().setXY(shooter));
		super.point().setMoveAngleByBaseAngle(shooter);
		physics().setHitShape(new Square(this, 10));
		physics().setHitGroup(shooter.hitGroup());
		this.shooter = shooter;
		name = getClass().getName();
		init();
	}
	public Bullet(Bullet sample) {
		physics().setPoint(new DstCntDynam().setAll(sample));
		point().setMoveAngleByBaseAngle(shooter);
		physics().setHitShape(sample.hitShape().clone(this));
		physics().setHitGroup(sample.hitGroup());
		originWeapon = sample.originWeapon;
		shooter = sample.shooter;
		name = sample.name;
		damage = Damage.NULL_DAMAGE;
		limitFrame = sample.limitFrame;
		limitRange = sample.limitRange;
		penetration = sample.penetration;
		reflection = sample.reflection;
		accel = sample.accel;
		paintScript = sample.paintScript;
	}
	@Override
	public void idle() {
		super.idle();
		if(defaultDeleteCheck())
			return;
		if(!dynamIdle())
			return;
	}
	public final boolean defaultDeleteCheck() {
		if(checkIsOutofLifeSpan())
			return outOfLifeSpan();
		if(checkIsOutOfRange())
			return outOfRange();
		if(!point().inStage())
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
		return range <= ((DstCntDynam)point()).getMovedDistance();
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
			length = point().move(Math.min(length, maxGap));
			
			////////////
			//landscape collision
			////////////
			if(judgeLandscapeCollision())
				return !hitLandScapeDeleteCheck();
			
			////////////
			//entity collision
			////////////
			for(Unit unit : GHQ.stage().getHitUnits(GHQ.stage().getUnits_standpoint(this, false), this)) {
				if(hitUnitDeleteCheck(unit))
					return false;
			}
		} while(length > 0);
		
		////////////
		//acceleration
		////////////
		point().addSpeed(accel, true);
		return true;
	}
	@Override
	public void paint() {
		defaultPaint();
	}
	public final void defaultPaint() {
		paintScript.dotPaint_turn(point(), point().moveAngle());
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
		unit.damage(damage, this);
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
	//control
	//////////////
	public Weapon setOriginWeapon(Weapon weapon) {
		return originWeapon = weapon;
	}
	
	//////////////
	//clone and split
	//////////////
	public Bullet getOriginal() {
		return new Bullet(this);
	}
	public final Bullet getClone() {
		Bullet BULLET = getOriginal();
		BULLET.point().setAll(point());
		return BULLET;
	}
	public final Bullet addCloneToGHQ() {
		return GHQ.stage().addBullet(getClone());
	}
	public void split_xMirror(double dx,double dy) {
		this.point().addXY_allowsMoveAngle(-dx/2,dy);
		addCloneToGHQ().point().addX_allowsMoveAngle(dx);
	}
	public void split_yMirror(double dx,double dy) {
		this.point().addXY_allowsMoveAngle(dx,-dy/2);
		addCloneToGHQ().point().addY_allowsMoveAngle(dy);
	}
	public void split_Round(int radius,int amount) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 1;i < amount;i++)
			addCloneToGHQ().point().addXY_DA(radius, D_ANGLE*i);
		this.point().addXY_DA(radius, 0);
	}
	public void clone_Round(int radius,int amount) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++)
			addCloneToGHQ().point().addXY_DA(radius, D_ANGLE*i);
	}
	public void split_Burst(int radius,int amount,double speed) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 1;i < amount;i++)
			addCloneToGHQ().point().fastParaAdd_DASpd(radius, D_ANGLE*i, speed);
		this.point().fastParaAdd_DASpd(radius, 0, speed);
	}
	public void clone_Burst(int radius,int amount,double speed) {
		final double D_ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++)
			addCloneToGHQ().point().fastParaAdd_DASpd(radius, D_ANGLE*i, speed);
	}
	public void split_NWay(int radius,double[] angles,double speed) {
		for(int i = 1;i < angles.length;i++)
			addCloneToGHQ().point().fastParaAdd_DASpd(radius, angles[i], speed);
		this.point().fastParaAdd_DASpd(radius, angles[0], speed);
	}
	public void clone_NWay(int radius,double[] angles,double speed) {
		for(double angle : angles)
			addCloneToGHQ().point().fastParaAdd_DASpd(radius, angle, speed);
	}
	public void split_NWay(int radius,double marginAngle,double amount,double speed) {
		this.point().spinMoveAngle(-marginAngle*(double)(amount - 1)/2.0);
		for(int i = 1;i < amount;i++)
			addCloneToGHQ().point().fastParaAdd_DASpd(radius, marginAngle*i, speed);
		this.point().fastParaAdd_DSpd(radius, speed);
	}
	public void clone_NWay(int radius,double marginAngle,double amount,double speed) {
		this.point().spinMoveAngle(-marginAngle*(double)(amount - 1)/2.0);
		for(int i = 0;i < amount;i++)
			addCloneToGHQ().point().fastParaAdd_DASpd(radius, marginAngle*i, speed);
	}

	//////////////
	//information
	//////////////
	public Weapon weapon() {
		return originWeapon;
	}
	public GHQObject shooter() {
		return shooter;
	}
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
	public final DotPaint getDotPaint() {
		return paintScript;
	}
	@Override
	public String name() {
		return "[Bullet]" + name;
	}
}
