package paint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import core.GHQ;

public class ColorFraming implements RectPaint{
	private static final long serialVersionUID = 6260543840507733715L;
	private final Color COLOR;
	private final Stroke STROKE;
	public ColorFraming(Color color,Stroke stroke) {
		COLOR = color;
		STROKE = stroke;
	}
	@Override
	public void rectPaint(int x, int y, int w, int h) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(COLOR);
		G2.setStroke(STROKE);
		G2.drawRect(x, y, w, h);
	}
}
