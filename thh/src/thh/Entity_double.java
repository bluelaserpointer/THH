package thh;

public abstract class Entity_double {
	public final int APPEARED_FRAME;
	public final double APPEARED_X,APPEARED_Y;
	public double x,y;
	public Entity_double() {
		APPEARED_FRAME = 0;
		APPEARED_X = 0;
		APPEARED_Y = 0;
	}
	public Entity_double(double x,double y,int nowFrame) {
		APPEARED_FRAME = nowFrame;
		APPEARED_X = this.x = x;
		APPEARED_Y = this.y = y;
	}
	abstract public boolean idle();
	abstract public void paint();
}