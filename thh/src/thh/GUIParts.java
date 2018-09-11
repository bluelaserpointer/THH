package thh;

public abstract class GUIParts {
	protected boolean isEnabled;
	protected abstract void paint();
	public abstract void enable();
	public abstract void disable();
	public final boolean isEnabled() {
		return isEnabled;
	}
	protected abstract boolean isInArea(int x,int y);
	protected abstract void clicked();
}
