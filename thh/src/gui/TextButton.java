package gui;

import java.awt.Color;

import core.GHQ;
import paint.ColorFraming;
import paint.rect.RectPaint;

public class TextButton extends BasicButton{

	public TextButton(String name, RectPaint paintScript, ColorFraming borderPaint, int x, int y, int w, int h) {
		super(paintScript, borderPaint);
		super.setName(name);
		super.setBounds(x, y, w, h);
	}
	public TextButton(String name, RectPaint paintScript, ColorFraming borderPaint) {
		super(paintScript, borderPaint);
		super.setName(name);
	}
	public TextButton(String name, Color baseColor) {
		super(baseColor);
		super.setName(name);
	}
	public TextButton(String name, Color baseColor, Color frameColor) {
		super(baseColor, frameColor);
		super.setName(name);
	}

	@Override
	public void idle() {
		super.idle();
		GHQ.getGraphics2D(Color.BLACK, GHQ.stroke1);
		GHQ.drawStringGHQ(NAME, x, y + 15);
	}

}
