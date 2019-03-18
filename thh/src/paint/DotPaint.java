package paint;

import java.awt.Graphics2D;

import core.GHQ;

public interface DotPaint extends PaintScript{
	public static final DotPaint BLANK_SCRIPT = new DotPaint() {
		private static final long serialVersionUID = 834697886156768195L;
		@Override
		public void dotPaint(int x, int y) {}
		@Override
		public void dotPaint(int x, int y,int maxSize) {}
	};
	
	public abstract void dotPaint(int x,int y);
	public abstract void dotPaint(int x,int y,int maxSize);
	public default void dotPaint(int x,int y,double angle) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.rotate(angle);
		dotPaint(x,y);
		G2.rotate(-angle);
	}
	public default void dotPaint(int x,int y,double angle,int maxSize) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.rotate(angle);
		dotPaint(x,y,maxSize);
		G2.rotate(-angle);
	}
}
