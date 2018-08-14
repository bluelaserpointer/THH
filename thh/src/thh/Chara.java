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
	
	public static THH thh;
	
	//•·•Ω•√•…
	
	//Initialization
	protected void battleStarted(int charaID){}
	protected abstract void spawn(int charaID,int charaTeam,int spawnX,int spawnY);
	protected void turnStarted(){}
	protected void loadImageData(){} //ª≠œÒ’i§ﬂﬁz§ﬂ
	protected void loadSoundData(){} //•µ•¶•Û•…’i§ﬂﬁz§ﬂ
	
	//idle
	protected abstract void idle(boolean isActive); //Include painting
	protected abstract void animationPaint();
	protected abstract void freezePaint();
	protected void bulletIdle(Bullet bullet,boolean isCharaActive) { //Include painting
		bullet.defaultIdle();
		bullet.defaultPaint();
	}
	protected void bulletAnimationPaint(Bullet bullet) {
		this.bulletPaint(bullet);
	}
	protected void bulletPaint(Bullet bullet) {
		bullet.defaultPaint();
	}
	protected void effectIdle(Effect effect,boolean isCharaActive) { //Include painting
		effect.defaultIdle();
		effect.defaultPaint();
	}
	protected void effectAnimationPaint(Effect effect) {
		this.effectPaint(effect);
	}
	protected void effectPaint(Effect effect) {
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

	//bullet
	public void bulletOutOfLifeSpan(Bullet bullet){}
	public void bulletOutOfRange(Bullet bullet){}
	public void bulletOutOfPenetration(Bullet bullet) {
		if(!bullet.IS_LASER)
			thh.deleteBullet(bullet);
	}
	public void bulletOutOfReflection(Bullet bullet) {
		if(!bullet.IS_LASER)
			thh.deleteBullet(bullet);
	}
	public void bulletHitObject(Bullet bullet){}
	public boolean bulletIfHitLandscape(Bullet bullet,int x,int y){
		return THH.stage.hitLandscape(x,y,10,10);
	}
	protected boolean deleteBullet(Bullet bullet){
		return true;
	}
	
	//effect
	public void effectOutOfLifeSpan(Effect effect) {}
	public void effectOutOfRange(Effect effect) {}
	protected boolean deleteEffect(Effect effect){
		return true;
	}
	
	//specialEvent
	protected int weaponChangeOrder;
	protected boolean attackOrder,moveOrder,dodgeOrder,spellOrder;
	protected void resetOrder() {
		weaponChangeOrder = 0;
		attackOrder = moveOrder = dodgeOrder = spellOrder = false;
	}
	protected void resetSingleOrder() {
		weaponChangeOrder = 0;
		spellOrder = dodgeOrder = false;
	}
	protected void eventNotice(int event) {}
}