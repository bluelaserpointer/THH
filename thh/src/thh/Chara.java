package thh;

import java.io.Serializable;

import bullet.Bullet2;

public abstract class Chara implements Serializable{
	/**
	 * 
	 */
	//•’•£©`•Î•…
	//•∑•π•∆•‡ÈvﬂB
	private static final long serialVersionUID = -59140414705285416L;
	protected final static int
		//system
		NONE = THH.NONE,
		MAX = THH.MAX,
		MIN = THH.MIN,
		EXIST = THH.EXIST;
	
	protected THH thh;
	
	//•·•Ω•√•…
	
	//Initialization
	final void giveInstance(THH thh){
		this.thh = thh;
	}
	protected void battleStarted(int charaID){}
	protected abstract void spawn(int charaID,int charaTeam,int spawnX,int spawnY);
	protected void turnStarted(){}
	protected void loadImageData(){} //ª≠œÒ’i§ﬂﬁz§ﬂ
	protected void loadSoundData(){} //•µ•¶•Û•…’i§ﬂﬁz§ﬂ
	
	//idle
	protected abstract void idle(boolean isActive); //Include painting
	protected abstract void bulletIdle(Bullet2 bullet,boolean IsCharaActive); //Include painting
	protected abstract void effectIdle(int id,int kind,boolean IsCharaActive); //Include painting
	
	//contact
	protected abstract boolean bulletEngage(Bullet2 bullet);
	
	//decrease
	protected abstract int decreaseME_amount(int amount);
	protected abstract int decreaseME_rate(double rate);
	public abstract int damage_amount(int damage);
	public abstract int damage_rate(double rate);
	protected abstract boolean kill();
	//infomation
	protected abstract int getHP();
	protected abstract double getHPRate();
	
	//acceleration
	protected abstract void addAccel(double xAccel,double yAccel);
	protected abstract void setAccel(double xAccel,double yAccel);
	//bullet
	public void bulletOutOfLifeSpan(Bullet2 bullet){}
	public void bulletOutOfRange(Bullet2 bullet){}
	public void bulletOutOfPenetration(Bullet2 bullet) {
		if(!bullet.IS_LASER)
			thh.deleteBullet(bullet);
	}
	public void bulletOutOfReflection(Bullet2 bullet) {
		if(!bullet.IS_LASER)
			thh.deleteBullet(bullet);
	}
	public void bulletHitObject(Bullet2 bullet){}
	protected boolean deleteBullet(Bullet2 bullet){
		return true;
	}
	protected boolean deleteEffect(int kind,int id){
		return true;
	}
	public boolean bulletIfHitLandscape(Bullet2 bullet,int x,int y){
		return thh.hitLandscape(x,y,10,10);
	}
}