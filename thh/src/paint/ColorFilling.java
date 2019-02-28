package paint;

import java.awt.Color;
import java.awt.Graphics2D;

import core.GHQ;

public class ColorFilling implements PaintScript{
	private static final long serialVersionUID = -2891833654516240206L;
	private final Color COLOR;
	private final int w,h;
	public ColorFilling(Color color) {
		COLOR = color;
		w = h = 5;
	}
	public ColorFilling(Color color,int w,int h) {
		COLOR = color;
		this.w = w;
		this.h = h;
	}
	@Override
	public void paint(int x, int y, int w, int h) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(COLOR);
		G2.fillRect(x, y, w, h);
	}
	@Override
	public void paint(int x, int y) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(COLOR);
		G2.fillRect(x, y, w, h);
	}
}
