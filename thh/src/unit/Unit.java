package unit;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import bullet.Bullet;
import calculate.Damage;
import core.GHQ;
import core.GHQObject;
import gui.MessageSource;
import item.ItemData;
import physics.HasAnglePoint;
import physics.HitInteractable;
import physics.Point;

/**
 * A primal class for managing unit.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class Unit extends GHQObject implements MessageSource, HasAnglePoint, Serializable{
	private static final long serialVersionUID = 7140005723063155203L;
	
	public String originalName = "";
	
	/////////////
	//Initialization
	/////////////
	public static final <T extends Unit>T initialSpawn(T unit, int spawnX, int spawnY) {
		unit.loadImageData();
		unit.respawn(spawnX,spawnY);
		return unit;
	}
	public static final <T extends Unit>T initialSpawn(T unit, Point point) {
		return initialSpawn(unit, point.intX(), point.intY());
	}
	public abstract Unit respawn(int spawnX, int spawnY);
	public final Unit respawn(Point point) {
		return respawn(point.intX(), point.intY());
	}
	public void loadImageData(){}
	
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
	public void damagedTarget(Unit targetUnit, Bullet bullet) {}
	public abstract void damage(Damage damage, Bullet bullet);
	public final void damage(Damage damage) {
		damage(damage, Bullet.NULL_BULLET);
	}

	/////////////
	//event
	/////////////
	public void removedItem(ItemData item) {}
	
	/////////////
	//information
	/////////////
	public String name() {
		return "[Unit]" + GHQ.NOT_NAMED;
	}
	@Override
	public Rectangle2D boundingBox() {
		final int DEFAULT_SIZE = 80;
		return new Rectangle2D.Double(point().intX() - DEFAULT_SIZE/2, point().intY() - DEFAULT_SIZE/2, DEFAULT_SIZE, DEFAULT_SIZE);
	}
	@Override
	public boolean intersects(HitInteractable object) {
		return isAlive() && super.intersects(object);
	}
	public abstract boolean isAlive();
}