package thh;

public abstract class Entity {
	public final int APPEARED_FRAME;
	public final double APPEARED_X,APPEARED_Y;
	public double x,y;
	public Entity() {
		APPEARED_FRAME = 0;
		APPEARED_X = 0;
		APPEARED_Y = 0;
	}
	public Entity(double x,double y,int nowFrame) {
		APPEARED_FRAME = nowFrame;
		APPEARED_X = this.x = x;
		APPEARED_Y = this.y = y;
	}
	abstract public boolean defaultIdle();
	abstract public void defaultPaint();
}