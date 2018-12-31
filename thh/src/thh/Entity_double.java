package thh;

public abstract class Entity_double {
	public final DynamInteractable SOURCE; //an information source of user
	public final int INITIAL_FRAME;
	public final double INITIAL_X,INITIAL_Y;
	protected double x,y;
	public Entity_double(DynamInteractable source) {
		SOURCE = source;
		INITIAL_FRAME = 0;
		INITIAL_X = 0;
		INITIAL_Y = 0;
	}
	public Entity_double(DynamInteractable source,double x,double y) {
		SOURCE = source;
		INITIAL_FRAME = THH.getNowFrame();
		INITIAL_X = this.x = x;
		INITIAL_Y = this.y = y;
	}
	public Entity_double(DynamInteractable source,double x,double y,int nowFrame) {
		SOURCE = source;
		INITIAL_FRAME = nowFrame;
		INITIAL_X = this.x = x;
		INITIAL_Y = this.y = y;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	abstract public boolean defaultIdle();
	abstract public void defaultPaint();
}