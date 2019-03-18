package gui;

import core.GHQ;
import paint.HasDotPaint;
import paint.RectPaint;

public class MouseHook<T extends HasDotPaint> extends GUIParts{
	protected T hookingObject;
	protected final int SIZE;
	
	//init
	public MouseHook(String group, RectPaint paintScript) {
		super(group, paintScript, 0, 0, 0, 0, false);
		SIZE = GHQ.NONE;
	}
	public MouseHook(String group, RectPaint paintScript, int size) {
		super(group, paintScript, 0, 0, 0, 0, false);
		SIZE = size;
	}
	
	//main role
	@Override
	public void idle() {
		if(hookingObject != null) {
			if(SIZE == GHQ.NONE)
				hookingObject.getPaintScript().dotPaint(GHQ.getMouseScreenX(), GHQ.getMouseScreenY());
			else
				hookingObject.getPaintScript().dotPaint(GHQ.getMouseScreenX(), GHQ.getMouseScreenY(), SIZE);
		}
	}
	
	//control
	public void hook(T object) {
		hookingObject = object;
	}
	public T get() {
		return hookingObject;
	}
	public T pull() {
		final T OBJECT = hookingObject;
		hookingObject = null;
		return OBJECT;
	}
	public void destory() {
		hookingObject = null;
	}
	
	//information
	public boolean hasObject() {
		return hookingObject != null;
	}
}
