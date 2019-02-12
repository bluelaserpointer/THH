package core;

public abstract class GUIParts {
	public final int GROUP;
	protected boolean isEnabled;
	protected int x,y,w,h;
	public GUIParts(int group) {
		GROUP = group;
	}
	public final void defaultIdle() {
		if(isEnabled)
			paint();
	}
	public abstract void paint();
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
		return GHQ.isMouseInArea(x, y, w, h);
	}
	public abstract void clicked();
}
