package paint;

import java.awt.Color;
import java.awt.Graphics2D;

import core.GHQ;

/**
 * A minor PaintScript subclass which fill a rectangle with specified color.
 * Useful when debugging.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class ColorFilling implements RectPaint{
	private static final long serialVersionUID = -2891833654516240206L;
	private final Color COLOR;
	/**
	 * @param color The rectangle color.
	 */
	public ColorFilling(Color color) {
		COLOR = color;
	}
	public static void rectPaint(Color color, int x, int y, int w, int h) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(color);
		G2.fillRect(x, y, w, h);
	}
	@Override
	public void rectPaint(int x, int y, int w, int h) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(COLOR);
		G2.fillRect(x, y, w, h);
	}
}
