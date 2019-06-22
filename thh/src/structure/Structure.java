package structure;

import java.awt.geom.Line2D;
import java.io.Serializable;

import core.Deletable;
import core.GHQ;
import core.HasBoundingBox;
import core.HasStandpoint;
import core.Standpoint;
import physics.Dynam;
import physics.HasDynam;

/**
 * A primal class for managing structure.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class Structure implements Serializable, HasBoundingBox, HasStandpoint, Deletable{
	private static final long serialVersionUID = -641218813005671688L;
	public final int UNIQUE_ID;
	public static int nowMaxUniqueID = -1;
	protected final int
		NONE = GHQ.NONE;
	
	public final Standpoint standpoint = Standpoint.NULL_STANDPOINT;
	private boolean isDeleted;
	
	//init
	public Structure(int initialStandpoint) {
		//standpoint = new Standpoint(initialStandpoint);
		UNIQUE_ID = ++nowMaxUniqueID;
	}
	public Structure() {
		//standpoint = Standpoint.NULL_STANDPOINT;
		UNIQUE_ID = ++nowMaxUniqueID;
	}
	
	//main role
	public boolean defaultIdle() {
		return true;
	}
	public abstract void defaultPaint();
	public void activeCons() {};
	public void passiveCons() {};
	public void dynam() {};
	public abstract void paint(boolean doAnimation);
	public final void paint() {
		paint(true);
	}
	
	//control
	public void beforeDelete() {
		isDeleted = true;
	}
	@Override
	public void delete() {
		GHQ.deleteStructure(this);
	}
	
	//information
	public abstract boolean contains(int x,int y,int w,int h);
	public abstract boolean contains(int x,int y);
	public boolean hit(HasStandpoint source,int x,int y,int w,int h) {
		return isFriend(source) && contains(x,y,w,h);
	}
	public abstract boolean intersectsLine(Line2D line);
	public final boolean intersectsLine(double x1,double y1,double x2,double y2) {
		return intersectsLine(new Line2D.Double(x1,y1,x2,y2));
	}
	public final boolean intersectsLine(Dynam dynam1,Dynam dynam2) {
		return intersectsLine(dynam1.doubleX(),dynam1.doubleY(),dynam2.doubleX(),dynam2.doubleY());
	}
	public final boolean intersectsLine(HasDynam object1,HasDynam object2) {
		return object1 == null || object2 == null ? false : intersectsLine(object1.getDynam(), object2.getDynam());
	}
	@Override
	public final Standpoint getStandpoint() {
		return Standpoint.NULL_STANDPOINT;
	}
	@Override
	public boolean isFriend(HasStandpoint target) {
		return Standpoint.NULL_STANDPOINT.isFriend(target.getStandpoint());
	}
	@Override
	public final boolean isDeleted() {
		return isDeleted;
	}
	
}