package paint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import core.GHQ;

public class ColorLine implements LinePaint{
	private static final long serialVersionUID = -4572501536482586169L;

	private final Color COLOR;
	private final Stroke STROKE;
	
	public ColorLine(Color color, Stroke stroke) {
		COLOR = color;
		STROKE = stroke;
	}
	@Override
	public void linePaint(int x1, int y1, int x2, int y2) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(COLOR);
		G2.setStroke(STROKE);
	}
}
