package gui;

import core.GHQ;
import paint.PaintScript;

public abstract class GUIParts {
	public final String GROUP;
	protected boolean isEnabled;
	protected int x,y,w,h;
	private final PaintScript PAINT_SCRIPT;
	public GUIParts(String group,PaintScript paintScript,int x,int y,int w,int h) {
		GROUP = group;
		if(paintScript == null)
			PAINT_SCRIPT = new PaintScript() {};
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
	public final void enable() {
		isEnabled = true;
	}
	public void disable() {
		isEnabled = false;
	}
	public final boolean isEnabled() {
		return isEnabled;
	}
	public final boolean isMouseOvered() {
		return GHQ.isMouseInArea_Screen(x, y, w, h);
	}
	public void clicked() {}
	public void outsideClicked() {}
	public void setXY(int x,int y) {
		this.x = x;
		this.y = y;
	}
	public void setBounds(int x,int y,int w,int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
}
