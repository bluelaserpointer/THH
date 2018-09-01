package thh;

public abstract class Entity_int {
	public final int APPEARED_FRAME;
	public final int APPEARED_X,APPEARED_Y;
	public int x,y;
	public Entity_int() {
		APPEARED_FRAME = 0;
		APPEARED_X = 0;
		APPEARED_Y = 0;
	}
	public Entity_int(int x,int y,int nowFrame) {
		APPEARED_FRAME = nowFrame;
		APPEARED_X = this.x = x;
		APPEARED_Y = this.y = y;
	}
	abstract public boolean idle();
	abstract public void paint();
}