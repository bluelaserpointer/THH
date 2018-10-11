package structure;

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
	
	public abstract boolean hitLandscape(int x,int y,int w,int h);
	public boolean hitLandscape(int team,int x,int y,int w,int h) {
		return THH.isRival(team,getTeam()) && hitLandscape(x,y,w,h);
	}
	public abstract int getTeam();
}