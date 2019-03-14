package structure;

import java.awt.geom.Line2D;
import java.io.Serializable;

import core.GHQ;
import core.HasBoundingBox;
import core.HasStandpoint;
import core.Standpoint;
import physicis.Dynam;
import physicis.DynamInteractable;

public abstract class Structure implements Serializable,HasBoundingBox,HasStandpoint{
	private static final long serialVersionUID = -641218813005671688L;
	public final int UNIQUE_ID;
	public static int nowMaxUniqueID = -1;
	protected final int
		NONE = GHQ.NONE;
	
	public final Standpoint standpoint = Standpoint.NULL_STANDPOINT;
	
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
	
	//information
	public abstract boolean contains(int x,int y,int w,int h);
	public abstract boolean contains(int x,int y);
	public boolean hit(HasStandpoint source,int x,int y,int w,int h) {
		return isFriendly(source) && contains(x,y,w,h);
	}
	public abstract boolean intersectsLine(Line2D line);
	public final boolean intersectsLine(double x1,double y1,double x2,double y2) {
		return intersectsLine(new Line2D.Double(x1,y1,x2,y2));
	}
	public final boolean intersectsLine(DynamInteractable d1,DynamInteractable d2) {
		final Dynam D1 = d1.getDynam(),D2 = d2.getDynam();
		return intersectsLine(D1.getX(),D1.getY(),D2.getX(),D2.getY());
	}
	public final boolean intersectsLine(Dynam d1,Dynam d2) {
		return intersectsLine(d1.getX(),d1.getY(),d2.getX(),d2.getY());
	}
	@Override
	public final Standpoint getStandpoint() {
		return Standpoint.NULL_STANDPOINT;
	}
	@Override
	public boolean isFriendly(HasStandpoint target) {
		return Standpoint.NULL_STANDPOINT.isFriendly(target.getStandpoint());
	}
	
}