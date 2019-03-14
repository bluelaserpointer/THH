package gui;

import core.GHQ;
import paint.HasDotPaint;
import paint.RectPaint;

public class MouseHook<T extends HasDotPaint> extends GUIParts{
	private T hookingObject;
	
	//init
	public MouseHook(String group, RectPaint paintScript) {
		super(group, paintScript, 0, 0, 0, 0, false);
	}
	
	//main role
	@Override
	public void idle() {
		if(hookingObject != null)
			hookingObject.getPaintScript().paint(GHQ.getMouseScreenX(), GHQ.getMouseScreenY());
	}
	
	//control
	public void hook(T object) {
		hookingObject = object;
	}
	public T get() {
		return hookingObject;
	}
}
