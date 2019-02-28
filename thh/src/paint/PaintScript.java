package paint;

import java.io.Serializable;

public interface PaintScript extends Serializable{
	public static final PaintScript BLANK_SCRIPT = new PaintScript() {
		private static final long serialVersionUID = -1201824004902133813L;
	};
	public default void paint(int x,int y) {}
	public default void paint(int x,int y,int w,int h) {
		paint(x,y);
	}
}
