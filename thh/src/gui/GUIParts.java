package gui;

import core.GHQ;
import paint.RectPaint;

public abstract class GUIParts {
	public final String GROUP;
	protected boolean isEnabled;
	public int x,y;
	public final int w,h;
	protected final RectPaint PAINT_SCRIPT;
	public GUIParts(String group,RectPaint paintScript,int x,int y,int w,int h) {
		GROUP = group;
		if(paintScript == null)
			PAINT_SCRIPT = RectPaint.BLANK_SCRIPT;
		else
			PAINT_SCRIPT = paintScript;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public final void defaultIdle() {
		if(isEnabled)
			paint();
	}
	public void paint() {
		PAINT_SCRIPT.paint(x, y, w, h);
	}
	public void enable() {
		isEnabled = true;
	}
	public void disable() {
		isEnabled = false;
	}
	public final boolean isEnabled() {
		return isEnabled;
	}
	public boolean isMouseEntered() {
		return GHQ.isMouseInArea_Screen(x + w/2, y + h/2, w, h);
	}
	public void clicked() {}
	public void outsideClicked() {}
	public void mouseOvered() {}
	public void outsideMouseOvered() {}
	public boolean absorbClickEvent() {
		return true;
	}
	public void setBounds(int x,int y) {
		this.x = x;
		this.y = y;
	}
}
