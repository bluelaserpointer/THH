package structure;

import java.awt.geom.Line2D;
import java.io.Serializable;

import core.Dynam;
import core.DynamInteractable;
import core.GHQ;
import core.HasBoundingBox;

public abstract class Structure implements Serializable,HasBoundingBox{
	private static final long serialVersionUID = -641218813005671688L;
	protected final int
		NONE = GHQ.NONE;
	
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
	//role
	
	//information
	public abstract boolean contains(int x,int y,int w,int h);
	public abstract boolean contains(int x,int y);
	public boolean hit(int team,int x,int y,int w,int h) {
		return GHQ.isRival(team,getTeam()) && contains(x,y,w,h);
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
	public abstract int getTeam();
	
}