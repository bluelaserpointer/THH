package bullet;

import chara.Chara;
import thh.Dynam;
import thh.DynamInteractable;
import thh.Entity_double;
import thh.THH;

public class Bullet extends Entity_double implements DynamInteractable{
	public final int UNIQUE_ID;
	public static int nowMaxUniqueID = -1;
	
	private int idleExecuted = 0;
	
	public final BulletScript SCRIPT; //a script of unique behaviors
	public final Dynam dynam;
	
	public String name;
	public final int
		KIND,
		SIZE,
		INITIAL_TEAM,
		INITIAL_ATK;
	public int
		team,
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
		super(source,BulletInfo.x,BulletInfo.y,BulletInfo.nowFrame);
		dynam = new Dynam(BulletInfo.x,BulletInfo.y,BulletInfo.xSpeed,BulletInfo.ySpeed,BulletInfo.angle);
		UNIQUE_ID = ++nowMaxUniqueID;
		SCRIPT = BulletInfo.script != null ? BulletInfo.script : BulletInfo.DEFAULT_SCRIPT;
		name = BulletInfo.name;
		KIND = BulletInfo.kind;
		SIZE = BulletInfo.size;
		LIMIT_FRAME = BulletInfo.limitFrame;
		LIMIT_RANGE = BulletInfo.limitRange;
		INITIAL_TEAM = team = BulletInfo.team;
		INITIAL_ATK = atk = BulletInfo.atk;
		offSet = BulletInfo.offSet;
		penetration = BulletInfo.penetration;
		reflection = BulletInfo.reflection;
		ACCEL = BulletInfo.accel;
		IMAGE_ID = BulletInfo.imageID;
		HIT_ENEMY = BulletInfo.hitEnemy;
		IS_LASER = BulletInfo.isLaser;
	}
	public Bullet(Bullet bullet) {
		super(bullet.SOURCE,BulletInfo.x,BulletInfo.y,BulletInfo.nowFrame);
		dynam = bullet.dynam.clone();
		UNIQUE_ID = ++nowMaxUniqueID;
		SCRIPT = bullet.SCRIPT != null ? bullet.SCRIPT : BulletInfo.DEFAULT_SCRIPT;
		name = bullet.name;
		KIND = bullet.KIND;
		SIZE = bullet.SIZE;
		LIMIT_FRAME = bullet.LIMIT_FRAME;
		LIMIT_RANGE = bullet.LIMIT_RANGE;
		INITIAL_TEAM = team = bullet.team;
		INITIAL_ATK = atk = bullet.atk;
		offSet = bullet.offSet;
		penetration = bullet.penetration;
		reflection = bullet.reflection;
		ACCEL = bullet.ACCEL;
		IMAGE_ID = bullet.IMAGE_ID;
		HIT_ENEMY = bullet.HIT_ENEMY;
		IS_LASER = bullet.IS_LASER;
	}
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
		if(limitFrame <= THH.getPassedFrame(super.INITIAL_FRAME) && SCRIPT.bulletOutOfLifeSpan(this)) {
			THH.deleteBullet(this);
			return true;
		}
		return false;
	}
	public final boolean rangeCheck() {
		return lifeSpanCheck(LIMIT_FRAME);
	}
	public final boolean rangeCheck(int limitRange) {
		if(limitRange <= dynam.getMovedDistance() && SCRIPT.bulletOutOfRange(this)){
			THH.deleteBullet(this);
			return true;
		}
		return false;
	}
	public final boolean inStageCheck() {
		if(!dynam.inStage()){
			THH.deleteBullet(this);
			return true;
		}
		return false;
	}
	public final boolean dynam() {
		return dynam(true,THH.MAX);
	}
	public final boolean dynam(boolean doHit) {
		return dynam(doHit,THH.MAX);
	}
	public final boolean dynam(int maxGap) {
		return dynam(true,maxGap);
	}
	public final boolean dynam(boolean doHit,int maxGap) {
		//speed & acceleration
		dynam.move();
		dynam.accelerate(ACCEL);
		if(!doHit)
			return true;
		//landscape collision
		if(SCRIPT.bulletIfHitLandscape(this,(int)dynam.getX(),(int)dynam.getY())){
			SCRIPT.bulletHitObject(this);
			if(penetration > 0) {
				if(penetration != THH.MAX)
					penetration--;
			}else {
				if(reflection > 0) {
					if(reflection != THH.MAX)
						reflection--;
					//edit reflection process
				}else if(SCRIPT.bulletOutOfDurability(this)) {
					THH.deleteBullet(this);
					return false;
				}
			}
		}
		//entity collision
		for(Chara chara : THH.callBulletEngage(THH.getCharacters_team(team,!HIT_ENEMY),this)) {
			chara.damage_amount(atk);
			SCRIPT.bulletHitObject(this);
			if(penetration > 0) {
				if(penetration != THH.MAX)
					penetration--;
			}else if(SCRIPT.bulletOutOfDurability(this)) {
				THH.deleteBullet(this);
				return false;
			}
		}
		return true;
	}
	public final void defaultPaint() {
		THH.drawImageTHH_center(IMAGE_ID, (int)dynam.getX(),(int)dynam.getY(), dynam.getAngle());
	}
	//tool
	public int getPassedFrame() {
		return THH.getPassedFrame(INITIAL_FRAME);
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
}
