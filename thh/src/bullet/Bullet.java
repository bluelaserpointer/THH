package bullet;

import static java.lang.Math.*;

import thh.Chara;
import thh.DynamInteractable;
import thh.Entity_double;
import thh.THH;

public class Bullet extends Entity_double implements DynamInteractable{
	public final int UNIQUE_ID;
	public static int nowMaxUniqueID = 0;
	
	private int idleExecuted = 0;
	
	public final DynamInteractable SOURCE; //an information source of user
	public final BulletScript SCRIPT; //a script of unique behaviors
	
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
		reflection,
		movedDistance;
	private final double
		ACCEL;
	private double
		xSpeed,ySpeed,
		angle;
	private final int
		IMAGE_ID;
	public final boolean
		HIT_ENEMY,
		IS_LASER;
	public Bullet(DynamInteractable source) {
		super(BulletInfo.x,BulletInfo.y,BulletInfo.nowFrame);
		UNIQUE_ID = nowMaxUniqueID++;
		this.SOURCE = source;
		this.SCRIPT = BulletInfo.script;
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
		xSpeed = BulletInfo.xSpeed;
		ySpeed = BulletInfo.ySpeed;
		angle = BulletInfo.angle;
		IMAGE_ID = BulletInfo.imageID;
		HIT_ENEMY = BulletInfo.hitEnemy;
		IS_LASER = BulletInfo.isLaser;
	}
	public Bullet(Bullet bullet) {
		super(BulletInfo.x,BulletInfo.y,BulletInfo.nowFrame);
		UNIQUE_ID = nowMaxUniqueID++;
		this.SOURCE = bullet.SOURCE;
		this.SCRIPT = bullet.SCRIPT;
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
		xSpeed = bullet.xSpeed;
		ySpeed = bullet.ySpeed;
		angle = bullet.angle;
		IMAGE_ID = bullet.IMAGE_ID;
		HIT_ENEMY = bullet.HIT_ENEMY;
		IS_LASER = bullet.IS_LASER;
	}

	public final boolean idle() {
		//LifeSpan & Range
		if(LIMIT_FRAME <= THH.getPassedFrame(super.INITIAL_FRAME) && SCRIPT.bulletOutOfLifeSpan(this)) {
			THH.deleteBullet(this);
			return false;
		}
		if(LIMIT_RANGE <= movedDistance && SCRIPT.bulletOutOfRange(this)){
			THH.deleteBullet(this);
			return false;
		}
		//OutOfStage
		if(!THH.inStage((int)x, (int)y)){
			THH.deleteBullet(this);
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
		//Penetration & Reflection
		if(SCRIPT.bulletIfHitLandscape(this,(int)x,(int)y)){
			if(reflection > 0) {
				reflection--;
				//edit
			}else {
				if(SCRIPT.bulletOutOfReflection(this))
					THH.deleteBullet(this);
			}
		}
		//Damaging
		for(Chara chara : THH.callBulletEngage(THH.getCharacters_team(team,!HIT_ENEMY),this)) {
			chara.damage_amount(atk);
			SCRIPT.bulletHitObject(this);
			if(penetration > 0)
				penetration--;
			else {
				if(SCRIPT.bulletOutOfPenetration(this))
					THH.deleteBullet(this);
			}
		}
		return true;
	}
	public final void paint() {
		if(angle == 0.0)
			THH.thh.drawImageTHH_center(IMAGE_ID, (int)x, (int)y);
		else
			THH.thh.drawImageTHH_center(IMAGE_ID, (int)x, (int)y, angle);
	}
	//control
	@Override
	public final void setXY(double x,double y) {
		this.x = x;this.y = y;
	}
	@Override
	public final void setX(double x) {
		this.x = x;
	}
	@Override
	public final void setY(double y) {
		this.y = y;
	}
	@Override
	public final void setAngle(double angle) {
		this.angle = angle;
	}
	@Override
	public final void setXSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}
	@Override
	public final void setYSpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}
	@Override
	public final void setSpeed(double xSpeed,double ySpeed) {
		this.xSpeed = xSpeed;this.ySpeed = ySpeed;
	}
	@Override
	public final void addSpeed(double xSpeed,double ySpeed) {
		this.xSpeed += xSpeed;this.ySpeed += ySpeed;
	}
	@Override
	public final void acceleration(double rate) {
		this.xSpeed *= rate;this.ySpeed *= rate;
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
	public final double getX() {
		return x;
	}
	@Override
	public final double getY() {
		return y;
	}
	@Override
	public final double getAngle() {
		return angle;
	}
	@Override
	public final boolean isMovable() {
		return true;
	}
	@Override
	public final boolean inStage() {
		return THH.inStage((int)x,(int)y);
	}
	@Override
	public final boolean inArea(int x,int y,int w,int h) {
		return abs(x - this.x) < w && abs(y - this.y) < h;
	}
	@Override
	public final double getDistance(double x,double y) {
		final double XD = x - this.x,YD = y - this.y;
		return sqrt(XD*XD + YD*YD);
	}
	@Override
	public final boolean isStop() {
		return xSpeed == 0.0 && ySpeed == 0.0;
	}
	@Override
	public final double getXSpeed() {
		return xSpeed;
	}
	@Override
	public final double getYSpeed() {
		return ySpeed;
	}
	@Override
	public final double getSpeed() {
		return sqrt(xSpeed*xSpeed + ySpeed*ySpeed);
	}
}
