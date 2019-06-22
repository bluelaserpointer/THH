package paint;

import java.awt.Graphics2D;
import java.util.ArrayList;

import core.GHQ;
import physics.HasAnglePoint;
import physics.Point;

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
		public void dotPaint_capSize(int x, int y, int maxSize) {}
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
	/**
	 * Drawing at specified coordinate.
	 * The width and height is decided by its original value.
	 * @param x
	 * @param y
	 */
	public abstract void dotPaint(int x, int y);
	/**
	 * Drawing at specified coordinate but keep its maxSize lower than a value.
	 * @param x
	 * @param y
	 * @param maxSize
	 */
	public default void dotPaint(Point coordinate) {
		dotPaint(coordinate.intX(), coordinate.intY());
	}
	public abstract void dotPaint_capSize(int x, int y, int maxSize);
	public default void dotPaint_capSize(Point point, int maxSize) {
		dotPaint_capSize(point.intX(), point.intY(), maxSize);
	}
	/**
	 * Drawing at specified coordinate but resize it by a rate.
	 * @param x
	 * @param y
	 * @param rate
	 */
	public abstract void dotPaint_rate(int x, int y, double rate);
	public default void dotPaint_rate(Point point, int maxSize) {
		dotPaint_rate(point.intX(), point.intY(), maxSize);
	}
	/**
	 * Drawing at specified coordinate but change its angle.
	 * @param x
	 * @param y
	 * @param angle
	 */
	public default void dotPaint_turn(int x, int y, double angle) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.rotate(angle, x, y);
		dotPaint(x,y);
		G2.rotate(-angle, x, y);
	}
	public default void dotPaint_turn(Point point, double angle) {
		dotPaint_turn(point.intX(), point.intY(), angle);
	}
	public default void dotPaint_turn(HasAnglePoint anglePoint) {
		dotPaint_turn(anglePoint.getPoint(), anglePoint.getAngle().angle());
	}
	/**
	 * Drawing at specified coordinate but change its angle and keep its maxSize lower than a value.
	 * @param x
	 * @param y
	 * @param angle
	 * @param maxSize
	 */
	public default void dotPaint_turnAndCapSize(int x, int y, double angle, int maxSize) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.rotate(angle, x, y);
		dotPaint_capSize(x, y, maxSize);
		G2.rotate(-angle, x, y);
	}
	public default void dotPaint_turnAndCapSize(Point point, double angle, int maxSize) {
		dotPaint_turnAndCapSize(point.intX(), point.intY(), angle, maxSize);
	}
	public default void dotPaint_turnAndCapSize(HasAnglePoint anglePoint, int maxSize) {
		dotPaint_turnAndCapSize(anglePoint.getPoint(), anglePoint.getAngle().angle(), maxSize);
	}
}
