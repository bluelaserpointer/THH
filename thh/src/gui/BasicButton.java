package gui;

import java.awt.Color;

import core.GHQ;
import paint.ColorFilling;
import paint.ColorFraming;
import paint.rect.RectPaint;

public class BasicButton extends GUIParts {
	protected ColorFraming borderPaint;
	public BasicButton(RectPaint paintScript, ColorFraming borderPaint, int x, int y, int w, int h) {
		super.setBGPaint(paintScript);
		super.setBounds(x, y, w, h);
		this.borderPaint = borderPaint;
	}
	public BasicButton(RectPaint paintScript, ColorFraming borderPaint) {
		super.setBGPaint(paintScript);
		this.borderPaint = borderPaint;
	}
	public BasicButton(Color baseColor) {
		super.setBGPaint(new ColorFilling(baseColor));
		borderPaint = null;
	}
	public BasicButton(Color baseColor, Color frameColor) {
		super.setBGPaint(new ColorFilling(baseColor));
		borderPaint = new ColorFraming(frameColor, GHQ.stroke1);
	}
	public BasicButton() {}
}
