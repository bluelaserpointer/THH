package bullet;

import static java.lang.Math.*;

import thh.Chara;
import thh.Entity;
import thh.THH;

public class Bullet extends Entity{
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
	
	public Bullet() {
		super(BulletInfo.x,BulletInfo.y,BulletInfo.nowFrame,BulletInfo.source);
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
	
	public final boolean defaultIdle() {
		final Chara chara = THH.getCharaClass(SOURCE);
		//LifeSpan & Range
		if(LIMIT_FRAME <= THH.getPassedFrame(super.APPEARED_FRAME)) {
			chara.bulletOutOfLifeSpan(this);
			Chara.thh.deleteBullet(this);
			return false;
		}
		if(LIMIT_RANGE <= movedDistance){
			chara.bulletOutOfRange(this);
			Chara.thh.deleteBullet(this);
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
		if(chara.bulletIfHitLandscape(this,(int)x,(int)y)){
			if(reflection > 0) {
				reflection--;
				//edit
			}else
				chara.bulletOutOfReflection(this);
		}
		//Damaging
		for(int charaID : Chara.thh.callBulletEngage(this)) {
			if(THH.getCharaTeam(charaID) != team && atk > 0) {
				THH.getCharaClass(charaID).damage_amount(atk);
				chara.bulletHitObject(this);
				if(penetration > 0)
					penetration--;
				else
					chara.bulletOutOfPenetration(this);
			}
		}
		return true;
	}
	public final void defaultPaint() {
		if(angle%(2*PI) == 0.0)
			Chara.thh.drawImageTHH_center(IMAGE_ID, (int)x, (int)y);
		else
			Chara.thh.drawImageTHH_center(IMAGE_ID, (int)x, (int)y, angle);
	}
	public final int getPenetration() {
		return penetration;
	}
	public final int getReflection() {
		return reflection;
	}
	public final double getSpeedX() {
		return xSpeed;
	}
	public final void setSpeedX(double value) {
		xSpeed = value;
	}
	public final double getSpeedY() {
		return ySpeed;
	}
	public final void setSpeedY(double value) {
		ySpeed = value;
	}
}
