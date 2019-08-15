package unit;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import core.GHQ;
import gui.MessageSource;
import hitShape.HitShape;
import physics.Angle;
import physics.Dynam;
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
	
	public final Angle baseAngle = new Angle();
	public final Dynam dynam;
	
	/////////////
	//Initialization
	/////////////
	public Unit(HitShape hitshape, int initialGroup) { 
		super(hitshape, new Standpoint(initialGroup));
		dynam = (Dynam)super.hitShape.point();
	}
	public static final <T extends Unit>T initialSpawn(T unit, int spawnX,int spawnY) {
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
	public Point point() {
		return dynam;
	}
	@Override
	public Dynam dynam() {
		return dynam;
	}
	@Override
	public Angle angle() {
		return baseAngle;
	}
	@Override
	public boolean intersects(HitInteractable object) {
		return isAlive() && super.intersects(object);
	}
	@Override
	public HitShape hitShape() {
		return hitShape;
	}
	public abstract boolean isAlive();
}