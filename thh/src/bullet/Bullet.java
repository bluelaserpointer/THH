package bullet;

import static java.lang.Math.*;

import thh.Chara;
import thh.DynamInteractable;
import thh.Entity_double;
import thh.THH;

public class Bullet extends Entity_double implements DynamInteractable{
	public final BulletSource SOURCE;
	
	public String name;
	public final int
		KIND,
		SIZE;
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
		IS_LASER;
	 
	public Bullet(BulletSource source) {
		super(BulletInfo.x,BulletInfo.y,BulletInfo.nowFrame);
		this.SOURCE = source;
		name = BulletInfo.name;
		KIND = BulletInfo.kind;
		SIZE = BulletInfo.size;
		LIMIT_FRAME = BulletInfo.limitFrame;
		LIMIT_RANGE = BulletInfo.limitRange;
		team = BulletInfo.team;
		atk = BulletInfo.atk;
		offSet = BulletInfo.offSet;
		penetration = BulletInfo.penetration;
		reflection = BulletInfo.reflection;
		ACCEL = BulletInfo.accel;
		xSpeed = BulletInfo.xSpeed;
		ySpeed = BulletInfo.ySpeed;
		angle = BulletInfo.angle;
		IMAGE_ID = BulletInfo.imageID;
		IS_LASER = BulletInfo.isLaser;
	}
		
	public Bullet(Bullet bullet) {
		super(BulletInfo.x,BulletInfo.y,BulletInfo.nowFrame);
		this.SOURCE = bullet.SOURCE;
		name = bullet.name;
		KIND = bullet.KIND;
		SIZE = bullet.SIZE;
		LIMIT_FRAME = bullet.LIMIT_FRAME;
		LIMIT_RANGE = bullet.LIMIT_RANGE;
		team = bullet.team;
		atk = bullet.atk;
		offSet = bullet.offSet;
		penetration = bullet.penetration;
		reflection = bullet.reflection;
		ACCEL = bullet.ACCEL;
		xSpeed = bullet.xSpeed;
		ySpeed = bullet.ySpeed;
		angle = bullet.angle;
		IMAGE_ID = bullet.IMAGE_ID;
		IS_LASER = bullet.IS_LASER;
	}

	public final boolean idle() {
		//LifeSpan & Range
		if(LIMIT_FRAME <= THH.getPassedFrame(super.INITIAL_FRAME)) {
			SOURCE.bulletOutOfLifeSpan(this);
			THH.deleteBullet(this);
			return false;
		}
		if(LIMIT_RANGE <= movedDistance){
			SOURCE.bulletOutOfRange(this);
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
		if(SOURCE.bulletIfHitLandscape(this,(int)x,(int)y)){
			if(reflection > 0) {
				reflection--;
				//edit
			}else
				SOURCE.bulletOutOfReflection(this);
		}
		//Damaging
		for(Chara chara : THH.callBulletEngage(this)) {
			if(chara.getTeam() != team && atk > 0) {
				chara.damage_amount(atk);
				SOURCE.bulletHitObject(this);
				if(penetration > 0)
					penetration--;
				else
					SOURCE.bulletOutOfPenetration(this);
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
	public final double getSpeed() {
		return sqrt(xSpeed*xSpeed + ySpeed*ySpeed);
	}
	public final void setSpeedX(double value) {
		xSpeed = value;
	}
}
