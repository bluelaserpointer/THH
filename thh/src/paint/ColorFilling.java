package paint;

import java.awt.Color;
import java.awt.Graphics2D;

import core.GHQ;

public class ColorFilling implements RectPaint{
	private static final long serialVersionUID = -2891833654516240206L;
	private final Color COLOR;
	public ColorFilling(Color color) {
		COLOR = color;
	}
	@Override
	public void rectPaint(int x, int y, int w, int h) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(COLOR);
		G2.fillRect(x, y, w, h);
	}
}
