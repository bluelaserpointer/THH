package paint;

import java.awt.Graphics2D;
import java.util.ArrayList;

import core.GHQ;

/**
 * One of the major PaintScript's direct subinterfaces.Describes a paint method which has original width and height but need specified coordinate
 * @author bluelaserpointer
 * @since alpha1.0
 */
public interface DotPaint extends PaintScript{
	//constants
	public static final DotPaint BLANK_SCRIPT = new DotPaint() {
		private static final long serialVersionUID = 834697886156768195L;
		@Override
		public void dotPaint(int x, int y) {}
		@Override
		public void dotPaint_capSize(int x, int y,int maxSize) {}
		@Override
		public void dotPaint_rate(int x, int y, double rate) {}
	};
	
	//tool
	public static int getMaxSize(ArrayList<DotPaint> scripts) {
		int biggestSize = 0;
		for(DotPaint ver : scripts) {
			final int BIGGER_SIZE = Math.max(ver.getDefaultW(), ver.getDefaultH());
			if(biggestSize < BIGGER_SIZE)
				biggestSize = BIGGER_SIZE;
		}
		return biggestSize;
	}
	public static int getMaxSize(DotPaint[] scripts) {
		int biggestSize = 0;
		for(DotPaint ver : scripts) {
			final int BIGGER_SIZE = Math.max(ver.getDefaultW(), ver.getDefaultH());
			if(biggestSize < BIGGER_SIZE)
				biggestSize = BIGGER_SIZE;
		}
		return biggestSize;
	}
	
	//main role
	public abstract void dotPaint(int x,int y);
	public abstract void dotPaint_capSize(int x,int y,int maxSize);
	public abstract void dotPaint_rate(int x,int y,double rate);
	public default void dotPaint_turn(int x,int y,double angle) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.rotate(angle);
		dotPaint(x,y);
		G2.rotate(-angle);
	}
	public default void dotPaint_turnAndCapSize(int x,int y,double angle,int maxSize) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.rotate(angle);
		dotPaint_capSize(x,y,maxSize);
		G2.rotate(-angle);
	}
}
