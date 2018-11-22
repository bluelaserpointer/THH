package thh;

import action.Action;
import bullet.Bullet;
import bullet.BulletSource;
import effect.EffectSource;

public abstract class Chara implements BulletSource,EffectSource,MessageSource,DynamInteractable{
	//フィールド
	//システム関連
	protected static final int
		//system
		NONE = THH.NONE,
		MAX = THH.MAX,
		MIN = THH.MIN;
		
	protected static THH thh;
	
	//メソッド
	
	//Initialization
	public void battleStarted(){}
	public abstract void spawn(int charaID,int charaTeam,int spawnX,int spawnY);
	public abstract void spawn(int charaID,int charaTeam,int spawnX,int spawnY,int hp);
	public void loadImageData(){} //画像読み込み
	public void loadSoundData(){} //サウンド読み込み
	
	//idles
	public static final int ACITIVE_CONS = 0,PASSIVE_CONS = 1,DYNAM = 2,PAINT_ANIMATED = 3,PAINT_FREEZED = 4,STOP_ALL = 5;
	public final void idle() {
		this.idle(ACITIVE_CONS);
	}
	public final void idle(int stopLevel) {
		switch(stopLevel) {
		case 0:
			activeCons();
		case 1:
			passiveCons();
		case 2:
			dynam();
		case 3:
			paint(true);
			break;
		case 4:
			paint(false);
			break;
		case 5:
			break;
		default:
			ErrorCounter.put("Chara.idleの不正使用:\"" + stopLevel + "\"");
		}
	}
	public void activeCons() {};
	public void passiveCons() {};
	public void dynam() {};
	public void paint(boolean doAnimation) {};
	
	//control
	//move
	public abstract void moveRel(int dx,int dy);
	public abstract void moveTo(int x,int y);
	public abstract void teleportRel(int dx,int dy);
	public abstract void teleportTo(int x,int y);
	public abstract void loadActionPlan(Action action);
	//judge
	public abstract boolean bulletEngage(Bullet bullet);
	public boolean isVisibleFrom(int x,int y) {
		return true;
	}
	//decrease
	public abstract int decreaseME_amount(int amount);
	public abstract int decreaseME_rate(double rate);
	public abstract int damage_amount(int damage);
	public abstract int damage_rate(double rate);

	public abstract boolean kill(boolean force);
	public void killed() {}
	//information
	public abstract String getName();
	public abstract int getTeam();
	public boolean isAlive() {
		return getHP() > 0;
	}
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