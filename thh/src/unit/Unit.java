package unit;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import core.GHQ;
import gui.MessageSource;
import hitShape.HitShape;
import hitShape.Rectangle;
import physics.Angle;
import physics.Entity;
import physics.HasAngleDynam;
import physics.HitInteractable;
import physics.Point;
import physics.Standpoint;

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
		this.hitshape = (hitshape != null ? hitshape : new Rectangle(0, 0));
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
	public final Standpoint standpoint() {
		return standpoint;
	}
	@Override
	public Rectangle2D boundingBox() {
		final int DEFAULT_SIZE = 80;
		return new Rectangle2D.Double(dynam.intX() - DEFAULT_SIZE/2,dynam.intY() - DEFAULT_SIZE/2,DEFAULT_SIZE,DEFAULT_SIZE);
	}
	@Override
	public Angle angle() {
		return baseAngle;
	}
	@Override
	public boolean intersects(HitInteractable object) {
		return isAlive() && !isFriend(object) && hitshape.intersects(point(), object, object.point());
	}
	@Override
	public HitShape hitShape() {
		return hitshape;
	}
	public abstract boolean isAlive();
}