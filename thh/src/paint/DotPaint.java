package paint;

import java.awt.Graphics2D;

import core.GHQ;

public interface DotPaint extends PaintScript{
	public static final DotPaint BLANK_SCRIPT = new DotPaint() {
		private static final long serialVersionUID = 834697886156768195L;
		@Override
		public void paint(int x, int y) {}
	};
	public abstract void paint(int x,int y);
	public default void paint(int x,int y,double angle) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.rotate(angle);
		paint(x,y);
		G2.rotate(-angle);
	}
}
