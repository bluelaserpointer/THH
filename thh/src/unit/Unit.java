package unit;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import core.Entity;
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
public abstract class Unit extends Entity implements MessageSource,HitInteractable,Serializable{
	private static final long serialVersionUID = 7140005723063155203L;

	protected static final int
		NONE = GHQ.NONE,
		MAX = GHQ.MAX,
		MIN = GHQ.MIN;
	
	public String originalName = "";
	public final HitShape hitshape;
	public final Standpoint standpoint;

	/////////////
	//Initialization
	/////////////
	public Unit(Dynam dynam, HitShape hitshape, int initialGroup) {
		super(dynam);
		this.hitshape = (hitshape != null ? hitshape : HitShape.NULL_HITSHAPE);
		standpoint = new Standpoint(initialGroup);
	}
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
	
	/////////////
	//idle
	/////////////
	@Override
	public final boolean idle() {
		//baseIdle
		if(isAlive()) {
			baseIdle();
		}else
			return false;
		//extendIdle
		if(isAlive()) {
			extendIdle();
		}else
			return false;
		//paint
		if(isAlive()) {
			paint(GHQ.isNoStopEvent());
		}else
			return false;
		return isAlive();
	}
	@Override
	public final void defaultPaint() {
		paint(true);
	}
	protected void baseIdle() {}
	protected void extendIdle() {}
	public void paint(boolean doAnimation) {}
	public abstract int damage_amount(int value);

	/////////////
	//control
	/////////////
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
	public abstract boolean kill(boolean force);
	protected void killed() {}
	
	/////////////
	//information
	/////////////
	public String getName() {
		return GHQ.NOT_NAMED;
	}
	@Override
	public final Standpoint getStandpoint() {
		return standpoint;
	}
	@Override
	public Rectangle2D getBoundingBox() {
		final int DEFAULT_SIZE = 80;
		final Coordinate COD = getCoordinate();
		return new Rectangle2D.Double(COD.getX() - DEFAULT_SIZE/2,COD.getY() - DEFAULT_SIZE/2,DEFAULT_SIZE,DEFAULT_SIZE);
	}
	@Override
	public boolean isHit(HitInteractable object) {
		return isAlive() && !isFriend(object) && hitshape.intersects(getCoordinate(), object, object.getCoordinate());
	}
	public HitShape getHitShape() {
		return hitshape;
	}
	public abstract boolean isAlive();
}