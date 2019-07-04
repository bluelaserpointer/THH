package unit;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import core.Entity;
import core.GHQ;
import core.Standpoint;
import geom.HitShape;
import gui.MessageSource;
import physics.Angle;
import physics.HasAngleDynam;
import physics.Point;
import core.HitInteractable;

/**
 * A primal class for managing unit.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class Unit extends Entity implements MessageSource, HitInteractable, HasAngleDynam, Serializable{
	private static final long serialVersionUID = 7140005723063155203L;
	
	public String originalName = "";
	public final HitShape hitshape;
	public final Standpoint standpoint;
	
	public final Angle baseAngle = new Angle();
	
	/////////////
	//Initialization
	/////////////
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
	public static final <T extends Unit>T initialSpawn(T unit, Point point) {
		return initialSpawn(unit, point.intX(), point.intY());
	}
	public abstract void respawn(int spawnX,int spawnY);
	public final void respawn(Point point) {
		respawn(point.intX(), point.intY());
	}
	public void loadImageData(){}
	public void loadSoundData(){}
	public void battleStarted(){}
	
	@Override
	public void idle() {
		super.idle();
		if(!isAlive()) {
			claimDelete();
		}
	}
	
	/////////////
	//control
	/////////////
	public abstract int damage_amount(int value);
	
	/////////////
	//information
	/////////////
	public String getName() {
		return "[Unit]" + GHQ.NOT_NAMED;
	}
	@Override
	public final Standpoint getStandpoint() {
		return standpoint;
	}
	@Override
	public Rectangle2D getBoundingBox() {
		final int DEFAULT_SIZE = 80;
		return new Rectangle2D.Double(dynam.intX() - DEFAULT_SIZE/2,dynam.intY() - DEFAULT_SIZE/2,DEFAULT_SIZE,DEFAULT_SIZE);
	}
	@Override
	public Angle getAngle() {
		return baseAngle;
	}
	@Override
	public boolean isHit(HitInteractable object) {
		return isAlive() && !isFriend(object) && hitshape.intersects(getPoint(), object, object.getPoint());
	}
	@Override
	public HitShape getHitShape() {
		return hitshape;
	}
	public abstract boolean isAlive();
}