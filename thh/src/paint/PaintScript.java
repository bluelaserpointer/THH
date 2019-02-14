package paint;

public interface PaintScript {
	public default void paint(int x,int y) {}
	public default void paint(int x,int y,int w,int h) {
		paint(x,y);
	}
}
