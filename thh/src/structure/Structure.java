package structure;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import thh.Dynam;
import thh.DynamInteractable;
import thh.ErrorCounter;
import thh.THH;

public abstract class Structure{
	protected final int
		NONE = THH.NONE;
	
	public final void idle(int stopLevel) {
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
			ErrorCounter.put("Tile.idleの不正使用:\"" + stopLevel + "\"");
		}
	}
	public void activeCons() {};
	public void passiveCons() {};
	public void dynam() {};
	public void paint(boolean doAnimation) {};
	
	//role
	public abstract void doFill(Graphics2D g2);
	public abstract void doDraw(Graphics2D g2);
	
	//information
	public abstract boolean contains(int x,int y,int w,int h);
	public boolean hit(int team,int x,int y,int w,int h) {
		return THH.isRival(team,getTeam()) && contains(x,y,w,h);
	}
	public abstract boolean intersectsLine(Line2D line);
	public final boolean intersectsLine(double x1,double y1,double x2,double y2) {
		return intersectsLine(new Line2D.Double(x1,y1,x2,y2));
	}
	public final boolean intersectsLine(DynamInteractable d1,DynamInteractable d2) {
		final Dynam D1 = d1.getDynam(),D2 = d2.getDynam();
		return intersectsLine(D1.getX(),D1.getY(),D2.getX(),D2.getY());
	}
	public abstract int getTeam();
	
}