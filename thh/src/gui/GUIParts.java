package gui;

import core.GHQ;
import paint.rect.RectPaint;

/**
 * A primal class for managing GUI.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class GUIParts {
	public final String NAME;
	protected boolean isEnabled;
	protected boolean isClicking;
	private boolean absorbsClickEvent;
	public int x,y;
	public int w,h;
	protected final RectPaint PAINT_SCRIPT;
	public GUIParts(String name, RectPaint paintScript, int x, int y, int w, int h, boolean absorbsClickEvent) {
		NAME = name;
		PAINT_SCRIPT = paintScript == null ? RectPaint.BLANK_SCRIPT : paintScript;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.absorbsClickEvent = absorbsClickEvent;
	}
	public GUIParts(String group, RectPaint paintScript, boolean absorbsClickEvent) {
		NAME = group;
		PAINT_SCRIPT = paintScript == null ? RectPaint.BLANK_SCRIPT : paintScript;
		x = y = 0;
		w = GHQ.screenW();
		h = GHQ.screenH();
		this.absorbsClickEvent = absorbsClickEvent;
	}
	protected void idle() {
		PAINT_SCRIPT.rectPaint(x, y, w, h);
	}
	public final void idleIfEnabled() {
		if(isEnabled)
			idle();
	}
	protected void extendIdle() {
	}
	public void enable() {
		isEnabled = true;
	}
	public void disable() {
		isClicking = false;
		isEnabled = false;
	}
	public final boolean isEnabled() {
		return isEnabled;
	}
	public final boolean isClicking() {
		return isClicking;
	}
	public boolean isMouseEntered() {
		return GHQ.isMouseInArea_Screen(x, y, w, h);
	}
	public void clicked() {
		isClicking = true;
	}
	public void outsideClicked() {
		isClicking = false;
	}
	public void released() {
		isClicking = false;
	}
	public void outsideReleased() {
		isClicking = false;
	}
	public void mouseOvered() {}
	public void outsideMouseOvered() {}
	public boolean absorbsClickEvent() {
		return absorbsClickEvent;
	}
	public void setAbsorbsClickEvent(boolean b) {
		absorbsClickEvent = b;
	}
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void addXY(int dx, int dy) {
		setXY(x + dx, y + dy);
	}
	public void setBounds(int x, int y, int w, int h) {
		setXY(x, y);
		this.w = w;
		this.h = h;
	}
	public void flit() {
		if(isEnabled)
			disable();
		else
			enable();
	}
}
