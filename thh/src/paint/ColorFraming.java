package paint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import core.GHQ;

public class ColorFraming implements PaintScript{
	private final Color COLOR;
	private final Stroke STROKE;
	private final int w,h;
	public ColorFraming(Color color,Stroke stroke) {
		COLOR = color;
		STROKE = stroke;
		w = h = 5;
	}
	public ColorFraming(Color color,Stroke stroke,int w,int h) {
		COLOR = color;
		STROKE = stroke;
		this.w = w;
		this.h = h;
	}
	@Override
	public void paint(int x, int y, int w, int h) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(COLOR);
		G2.setStroke(STROKE);
		G2.drawRect(x - w/2, y - h/2, w, h);
	}
	@Override
	public void paint(int x, int y) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(COLOR);
		G2.setStroke(STROKE);
		G2.drawRect(x - w/2, y - h/2, w, h);
	}
}
