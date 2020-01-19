package paint.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import core.GHQ;
import paint.dot.DotPaint;

public class StringPaint implements DotPaint{
	private static final long serialVersionUID = 2882856528171908321L;
	
	public final String WORDS;
	public final Color COLOR;
	public final Font FONT;

	public static Color defaultColor = Color.BLACK;
	public static Font defaultFont = GHQ.basicFont;

	public StringPaint(String words) {
		WORDS = words;
		COLOR = defaultColor;
		FONT = defaultFont;
	}
	public StringPaint(String words, Color color) {
		WORDS = words;
		COLOR = color;
		FONT = defaultFont;
	}
	public StringPaint(String words, Font font, Color color) {
		WORDS = words;
		COLOR = color;
		FONT = font;
	}
	@Override
	public void dotPaint(int x, int y) {
		final Graphics2D G2 = GHQ.getG2D();
		final Font PREV_FONT = G2.getFont();
		G2.setFont(FONT.deriveFont(40F));
		G2.setColor(COLOR);
		G2.drawString(WORDS, x, y);
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
