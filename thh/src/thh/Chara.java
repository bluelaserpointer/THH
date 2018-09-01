package thh;

import bullet.Bullet;
import bullet.BulletSource;
import effect.EffectSource;

public abstract class Chara implements BulletSource,EffectSource,MessageSource,DynamInteractable{
	//•’•£©`•Î•…
	//•∑•π•∆•‡ÈvﬂB
	protected static final int
		//system
		NONE = THH.NONE,
		MAX = THH.MAX,
		MIN = THH.MIN;
		
	protected static THH thh;
	
	//•·•Ω•√•…
	
	//Initialization
	public void battleStarted(){}
	public abstract void spawn(int charaID,int charaTeam,int spawnX,int spawnY);
	public void turnStarted(){}
	public void loadImageData(){} //ª≠œÒ’i§ﬂﬁz§ﬂ
	public void loadSoundData(){} //•µ•¶•Û•…’i§ﬂﬁz§ﬂ
	
	//idles
	public abstract void idle(boolean isActive); //Include painting
	public abstract void animationPaint();
	public abstract void freezePaint();
	
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
	//acceleration
	public abstract void gravity(double value);
	
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
}