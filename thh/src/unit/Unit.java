package unit;

import action.Action;
import bullet.Bullet;
import core.Dynam;
import core.DynamInteractable;
import core.Entity_double;
import core.ErrorCounter;
import core.GHQ;
import core.MessageSource;

public abstract class Unit extends Entity_double implements MessageSource,DynamInteractable{
	protected static final int
		//system
		NONE = GHQ.NONE,
		MAX = GHQ.MAX,
		MIN = GHQ.MIN;
	
	public final ActionPlan actions = new ActionPlan();
	public final Status status;
	
	//Initialization
	public Unit(Status status) {
		this.status = status;
	}
	public final Unit initialSpawn(int charaTeam,int spawnX,int spawnY) {
		loadImageData();
		loadSoundData();
		battleStarted();
		respawn(charaTeam,spawnX,spawnY);
		return this;
	}
	public final Unit initialSpawn(int charaTeam,int spawnX,int spawnY,int hp) {
		loadImageData();
		loadSoundData();
		battleStarted();
		respawn(charaTeam,spawnX,spawnY,hp);
		return this;
	}
	public abstract void respawn(int charaTeam,int spawnX,int spawnY);
	public abstract void respawn(int charaTeam,int spawnX,int spawnY,int hp);
	public void loadImageData(){}
	public void loadSoundData(){}
	public void battleStarted(){}
	
	//idles
	public static final int ACITIVE_CONS = 0,PASSIVE_CONS = 1,DYNAM = 2,PAINT_ANIMATED = 3,PAINT_FREEZED = 4,STOP_ALL = 5;
	@Override
	public final boolean defaultIdle() {
		this.idle(ACITIVE_CONS);
		return isAlive();
	}
	@Override
	public final void defaultPaint() {
		this.idle(PAINT_ANIMATED);
	}
	public void idle(int stopLevel) {
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
			ErrorCounter.put("Chara.idle error:\"" + stopLevel + "\"");
		}
	}
	public void activeCons() {};
	public void passiveCons() {};
	public void dynam() {};
	public void paint(boolean doAnimation) {};
	
	//control
	@Override
	public final Dynam getDynam() {
		return dynam;
	}
	//move
	public abstract void moveRel(int dx,int dy);
	public abstract void moveTo(int x,int y);
	public abstract void teleportRel(int dx,int dy);
	public abstract void teleportTo(int x,int y);
	public abstract void loadActionPlan(Action action);
	//judge
	public abstract boolean bulletEngage(Bullet bullet);
	//hp
	public abstract void setHP(int hp);
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
	public abstract int getMP();
	public abstract double getMPRate();
	public abstract Status getStatus();
	
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