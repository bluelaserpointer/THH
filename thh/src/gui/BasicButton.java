package gui;

import paint.RectPaint;

public class BasicButton extends GUIParts{
	public BasicButton(String group, RectPaint paintScript, int x, int y, int w, int h) {
		super(group, paintScript, x, y, w, h, true);
	}
	public BasicButton(String group, RectPaint paintScript) {
		super(group, paintScript,true);
	}
}
