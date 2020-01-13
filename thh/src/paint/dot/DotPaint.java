package paint.dot;

import java.awt.Graphics2D;
import java.util.Collection;

import core.GHQ;
import paint.PaintScript;
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
		public int width() {
			return 0;
		}
		@Override
		public int height() {
			return 0;
		}
	};
	
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
	public default void dotPaint_capSize(int x, int y, int maxSize) {
		final int SIZE_BIG = sizeOfBigger();
		if(SIZE_BIG > maxSize) {
			dotPaint_rate(x, y, (double)maxSize/SIZE_BIG);
		}else
			dotPaint(x, y);
	}
	public default void dotPaint_capSize(Point point, int maxSize) {
		dotPaint_capSize(point.intX(), point.intY(), maxSize);
	}
	/**
	 * Drawing at specified coordinate but resize it by a rate.
	 * @param x
	 * @param y
	 * @param rate
	 */
	public default void dotPaint_rate(int x, int y, double rate) {
		GHQ.getG2D().translate(x, y);
		GHQ.scale(rate);
		dotPaint(0, 0);
		GHQ.scale(1.0/rate);
		GHQ.getG2D().translate(-x, -y);
	}
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
		GHQ.rotate(angle, x, y);
		dotPaint(x, y);
		GHQ.rotate(-angle, x, y);
	}
	public default void dotPaint_turn(Point point, double angle) {
		dotPaint_turn(point.intX(), point.intY(), angle);
	}
	public default void dotPaint_turn(HasAnglePoint anglePoint) {
		dotPaint_turn(anglePoint.point(), anglePoint.angle().get());
	}
	/**
	 * Drawing at specified coordinate but change its angle and keep its maxSize lower than a value.
	 * @param x
	 * @param y
	 * @param angle
	 * @param maxSize
	 */
	public default void dotPaint_turnAndCapSize(int x, int y, double angle, int maxSize) {
		final Graphics2D G2 = GHQ.getG2D();
		G2.rotate(angle, x, y);
		dotPaint_capSize(x, y, maxSize);
		G2.rotate(-angle, x, y);
	}
	public default void dotPaint_turnAndCapSize(Point point, double angle, int maxSize) {
		dotPaint_turnAndCapSize(point.intX(), point.intY(), angle, maxSize);
	}
	public default void dotPaint_turnAndCapSize(HasAnglePoint anglePoint, int maxSize) {
		dotPaint_turnAndCapSize(anglePoint.point(), anglePoint.angle().get(), maxSize);
	}
	
	//information
	public default int sizeOfBigger() {
		return Math.max(width(), height());
	}
	public abstract int width();
	public abstract int height();
	
	//tool
	public static int getMaxSize(Collection<DotPaint> scripts) {
		int biggestSize = 0;
		for(DotPaint ver : scripts) {
			final int BIGGER_SIZE = ver.sizeOfBigger();
			if(biggestSize < BIGGER_SIZE)
				biggestSize = BIGGER_SIZE;
		}
		return biggestSize;
	}
	public static int getMaxSize(DotPaint[] scripts) {
		int biggestSize = 0;
		for(DotPaint ver : scripts) {
			final int BIGGER_SIZE = ver.sizeOfBigger();
			if(biggestSize < BIGGER_SIZE)
				biggestSize = BIGGER_SIZE;
		}
		return biggestSize;
	}
}
