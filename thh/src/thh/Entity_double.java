package thh;

public abstract class Entity_double {
	public final int INITIAL_FRAME;
	public final double INITIAL_X,INITIAL_Y;
	public double x,y;
	public Entity_double() {
		INITIAL_FRAME = 0;
		INITIAL_X = 0;
		INITIAL_Y = 0;
	}
	public Entity_double(double x,double y) {
		INITIAL_FRAME = THH.getNowFrame();
		INITIAL_X = this.x = x;
		INITIAL_Y = this.y = y;
	}
	public Entity_double(double x,double y,int nowFrame) {
		INITIAL_FRAME = nowFrame;
		INITIAL_X = this.x = x;
		INITIAL_Y = this.y = y;
	}
	abstract public boolean idle();
	abstract public void paint();
}