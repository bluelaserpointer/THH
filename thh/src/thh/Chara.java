package thh;

import bullet.Bullet;
import effect.Effect;

public abstract class Chara{
	//•’•£©`•Î•…
	//•∑•π•∆•‡ÈvﬂB
	protected final static int
		//system
		NONE = THH.NONE,
		MAX = THH.MAX,
		MIN = THH.MIN;
	
	protected static THH thh;
	
	//•·•Ω•√•…
	
	//Initialization
	public void battleStarted(int charaID){}
	public abstract void spawn(int charaID,int charaTeam,int spawnX,int spawnY);
	public void turnStarted(){}
	public void loadImageData(){} //ª≠œÒ’i§ﬂﬁz§ﬂ
	public void loadSoundData(){} //•µ•¶•Û•…’i§ﬂﬁz§ﬂ
	
	//idle
	public abstract void idle(boolean isActive); //Include painting
	public abstract void animationPaint();
	public abstract void freezePaint();
	public void bulletIdle(Bullet bullet,boolean isCharaActive) { //Include painting
		bullet.defaultIdle();
		bullet.defaultPaint();
	}
	public void bulletAnimationPaint(Bullet bullet) {
		this.bulletPaint(bullet);
	}
	public void bulletPaint(Bullet bullet) {
		bullet.defaultPaint();
	}
	public void effectIdle(Effect effect,boolean isCharaActive) { //Include painting
		effect.defaultIdle();
		effect.defaultPaint();
	}
	public void effectAnimationPaint(Effect effect) {
		this.effectPaint(effect);
	}
	public void effectPaint(Effect effect) {
		effect.defaultPaint();
	}
	
	//control
	//judge
	public abstract boolean bulletEngage(Bullet bullet);
	//decrease
	public abstract int decreaseME_amount(int amount);
	public abstract int decreaseME_rate(double rate);
	public abstract int damage_amount(int damage);
	public abstract int damage_rate(double rate);
	public abstract boolean kill();
	//information
	public abstract String getName();
	public abstract int getTeam();
	public abstract int getHP();
	public abstract double getHPRate();
	public abstract int getME();
	public abstract double getMERate();
	public abstract int getStatus();
	public abstract double getX();
	public abstract double getY();
	//acceleration
	public abstract void addAccel(double xAccel,double yAccel);
	public abstract void setAccel(double xAccel,double yAccel);
	public abstract void gravity(double value);

	//bullet
	public void bulletOutOfLifeSpan(Bullet bullet){}
	public void bulletOutOfRange(Bullet bullet){}
	public void bulletOutOfPenetration(Bullet bullet) {
		if(!bullet.IS_LASER)
			THH.deleteBullet(bullet);
	}
	public void bulletOutOfReflection(Bullet bullet) {
		if(!bullet.IS_LASER)
			THH.deleteBullet(bullet);
	}
	public void bulletHitObject(Bullet bullet){}
	public boolean bulletIfHitLandscape(Bullet bullet,int x,int y){
		return THH.hitLandscape(x,y,10,10);
	}
	public boolean deleteBullet(Bullet bullet){
		return true;
	}
	
	//effect
	public void effectOutOfLifeSpan(Effect effect) {}
	public void effectOutOfRange(Effect effect) {}
	public boolean deleteEffect(Effect effect){
		return true;
	}
	
	//specialEvent
	public int weaponChangeOrder;
	public boolean attackOrder,moveOrder,dodgeOrder,spellOrder;
	public void resetOrder() {
		weaponChangeOrder = 0;
		attackOrder = moveOrder = dodgeOrder = spellOrder = false;
	}
	public void resetSingleOrder() {
		weaponChangeOrder = 0;
		spellOrder = dodgeOrder = false;
	}
	public void eventNotice(int event) {}
}