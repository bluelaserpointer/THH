package thh;

public abstract class Entity_int {
	public final int INITIAL_FRAME;
	public final int INITIAL_X,INITIAL_Y;
	protected int x,y;
	public Entity_int() {
		INITIAL_FRAME = 0;
		INITIAL_X = 0;
		INITIAL_Y = 0;
	}
	public Entity_int(int x,int y) {
		INITIAL_FRAME = THH.getNowFrame();
		INITIAL_X = this.x = x;
		INITIAL_Y = this.y = y;
	}
	public Entity_int(int x,int y,int nowFrame) {
		INITIAL_FRAME = nowFrame;
		INITIAL_X = this.x = x;
		INITIAL_Y = this.y = y;
	}
	abstract public boolean idle();
	abstract public void paint();
}