package unit;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import action.Action;
import core.Entity_double;
import core.ErrorCounter;
import core.GHQ;
import core.Standpoint;
import geom.HitShape;
import gui.MessageSource;
import physicis.Coordinate;
import physicis.Dynam;
import core.HitInteractable;

/**
 * A primal class for managing unit.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class Unit extends Entity_double implements MessageSource,HitInteractable,Serializable{
	private static final long serialVersionUID = 7140005723063155203L;

	protected static final int
		//system
		NONE = GHQ.NONE,
		MAX = GHQ.MAX,
		MIN = GHQ.MIN;
	
	public String originalName = "";
	public final HitShape hitshape;
	public final Standpoint standpoint;
	
	//Initialization
	public Unit(HitShape hitshape, int initialGroup) {
		this.hitshape = (hitshape != null ? hitshape : HitShape.NULL_HITSHAPE);
		standpoint = new Standpoint(initialGroup);
	}
	public static final <T extends Unit>T initialSpawn(T unit, int spawnX,int spawnY) {
		unit.loadImageData();
		unit.loadSoundData();
		unit.battleStarted();
		unit.respawn(spawnX,spawnY);
		return unit;
	}
	public abstract void respawn(int spawnX,int spawnY);
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
	
	//move
	public void moveRel(int dx,int dy) {
		final Dynam DYNAM = getDynam();
		DYNAM.approach(DYNAM.getX() + dx,DYNAM.getY() + dy, 10);
	}
	public void moveTo(int x,int y) {
		getDynam().approach(x, y, 10);
	}
	public void teleportRel(int dx,int dy) {
		getCoordinate().addXY(dx, dy);
	}
	public void teleportTo(int x,int y) {
		getCoordinate().setXY(x, y);
	}
	public abstract void loadActionPlan(Action action);
	
	//judge
	@Override
	public Rectangle2D getBoundingBox() {
		final int DEFAULT_SIZE = 80;
		final Coordinate COD = getCoordinate();
		return new Rectangle2D.Double(COD.getX() - DEFAULT_SIZE/2,COD.getY() - DEFAULT_SIZE/2,DEFAULT_SIZE,DEFAULT_SIZE);
	}
	//decrease
	public abstract int damage_amount(int damage);
	public abstract int damage_rate(double rate);

	public abstract boolean kill(boolean force);
	public void killed() {}
	
	//information
	public String getName() {
		return GHQ.NOT_NAMED;
	}
	@Override
	public final Standpoint getStandpoint() {
		return standpoint;
	}
	@Override
	public boolean isHit(HitInteractable object) {
		return isAlive() && !isFriend(object) && getHitShape().intersects(getCoordinate(), object, object.getCoordinate());
	}
	public HitShape getHitShape() {
		return hitshape;
	}
	public abstract boolean isAlive();
	
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