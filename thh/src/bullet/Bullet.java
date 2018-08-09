package bullet;

import thh.THH;

public abstract class Bullet {
	public final int SOURCE,APPEARED_FRAME;
	public final double APPEARED_X,APPEARED_Y;
	public double x,y;
	public Bullet() {
		SOURCE = thh.THH.NONE;
		APPEARED_FRAME = 0;
		APPEARED_X = 0;
		APPEARED_Y = 0;
	}
	public Bullet(double x,double y,int nowFrame,int source) {
		SOURCE = source;
		APPEARED_FRAME = nowFrame;
		APPEARED_X = this.x = x;
		APPEARED_Y = this.y = y;
	}
	/*public final boolean isExpired(int nowFrame,int limitFrame) {
		return (nowFrame - APPEARED_FRAME) >= limitFrame;
	}*/
	abstract public boolean defaultIdle(THH thh);
	abstract public void defaultPaint(THH thh);
}