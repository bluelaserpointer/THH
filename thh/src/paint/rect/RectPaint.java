package paint.rect;

import paint.PaintScript;
import physics.Point;

/**
 * One of the major PaintScript's direct subinterfaces.Describes a paint method which need specified coordinate and size.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public interface RectPaint extends PaintScript{
	public static final RectPaint BLANK_SCRIPT = new RectPaint() {
		private static final long serialVersionUID = -4558496325553265908L;
		@Override
		public void rectPaint(int x, int y, int w, int h) {}
	};
	public abstract void rectPaint(int x,int y,int w,int h);
	public default void rectPaint(int x,int y,int size) {
		rectPaint(x, y, size, size);
	}
	public default void rectPaint(Point point, int width, int height) {
		rectPaint(point.intX(), point.intY(), width, height);
	}
}
