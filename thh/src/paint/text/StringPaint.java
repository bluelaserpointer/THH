package paint.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import core.GHQ;
import paint.dot.DotPaint;

public class StringPaint extends DotPaint {
	public String words;
	public Color color;
	public Font font;

	public static Color defaultColor = Color.BLACK;
	public static Font defaultFont = GHQ.basicFont;

	public StringPaint(String words) {
		this.words = words;
		this.color = defaultColor;
		this.font = defaultFont;
	}
	public StringPaint(String words, Color color) {
		this.words = words;
		this.color = color;
		this.font = defaultFont;
	}
	public StringPaint(String words, Font font, Color color) {
		this.words = words;
		this.color = color;
		this.font = font;
	}
	@Override
	public void dotPaint(int x, int y) {
		final Graphics2D G2 = GHQ.getG2D();
		final Font PREV_FONT = G2.getFont();
		G2.setFont(font.deriveFont(40F));
		G2.setColor(color);
		G2.drawString(words, x, y);
		G2.setFont(PREV_FONT);
	}
	@Override
	public void dotPaint_capSize(int x, int y, int maxSize) {
		dotPaint(x, y);
	}

	@Override
	public void dotPaint_rate(int x, int y, double rate) {
		dotPaint(x, y);
	}
	@Override
	public int width() {
		return 0;
	}
	@Override
	public int height() {
		return 0;
	}
}
