package bullet;

import static java.lang.Math.PI;

import core.Dynam;
import core.DynamInteractable;
import core.Entity_double;
import core.GHQ;
import core.HasStandpoint;
import core.Standpoint;
import unit.Unit;

public class Bullet extends Entity_double implements DynamInteractable,HasStandpoint{
	public final int UNIQUE_ID;
	public static int nowMaxUniqueID = -1;
	
	private int idleExecuted = 0;
	
	public final BulletScript SCRIPT; //a script of unique behaviors
	public final Standpoint standpoint;
	
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
	private final int
		IMAGE_ID;
	public final boolean
		HIT_ENEMY,
		IS_LASER;
	public Bullet(DynamInteractable source) {
		super(source,BulletBlueprint.dynam,BulletBlueprint.nowFrame);
		UNIQUE_ID = ++nowMaxUniqueID;
		SCRIPT = BulletBlueprint.script != null ? BulletBlueprint.script : BulletBlueprint.DEFAULT_SCRIPT;
		name = BulletBlueprint.name;
		SIZE = BulletBlueprint.size;
		LIMIT_FRAME = BulletBlueprint.limitFrame;
		LIMIT_RANGE = BulletBlueprint.limitRange;
		standpoint = new Standpoint(BulletBlueprint.standpointGroup);
		INITIAL_ATK = atk = BulletBlueprint.atk;
		offSet = BulletBlueprint.offSet;
		penetration = BulletBlueprint.penetration;
		reflection = BulletBlueprint.reflection;
		ACCEL = BulletBlueprint.accel;
		IMAGE_ID = BulletBlueprint.imageID;
		HIT_ENEMY = BulletBlueprint.hitEnemy;
		IS_LASER = BulletBlueprint.isLaser;
	}
	public Bullet(Bullet bullet) {
		super(bullet.source,bullet.dynam,BulletBlueprint.nowFrame);
		UNIQUE_ID = ++nowMaxUniqueID;
		SCRIPT = bullet.SCRIPT != null ? bullet.SCRIPT : BulletBlueprint.DEFAULT_SCRIPT;
		name = bullet.name;
		SIZE = bullet.SIZE;
		LIMIT_FRAME = bullet.LIMIT_FRAME;
		LIMIT_RANGE = bullet.LIMIT_RANGE;
		standpoint = new Standpoint(bullet.standpoint.get());
		INITIAL_ATK = atk = bullet.atk;
		offSet = bullet.offSet;
		penetration = bullet.penetration;
		reflection = bullet.reflection;
		ACCEL = bullet.ACCEL;
		IMAGE_ID = bullet.IMAGE_ID;
		HIT_ENEMY = bullet.HIT_ENEMY;
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
		for(Unit chara : GHQ.callBulletEngage(GHQ.getCharacters_standpoint(this,!HIT_ENEMY),this)) {
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
		GHQ.drawImageGHQ_center(IMAGE_ID, (int)dynam.getX(),(int)dynam.getY(), dynam.getAngle());
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
	
	//control
	@Override
	public Dynam getDynam() {
		return dynam;
	}
	@Override
	public boolean isMovable() {
		return true;
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
	public boolean isFriendly(HasStandpoint target) {
		return standpoint.isFriendly(target.getStandpoint());
	}
}
