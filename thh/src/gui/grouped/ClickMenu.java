package gui.grouped;

import core.GHQ;
import paint.rect.RectPaint;

public class ClickMenu<T> extends AutoResizeMenu{
	
	protected T targetObject;
	
	public ClickMenu(String group, RectPaint paintScript, int w, int defaultLineH) {
		super(group, paintScript, 0, 0, w, defaultLineH);
	}
	public ClickMenu(String group, RectPaint paintScript, int w, int defaultLineH, int partsMargin) {
		super(group, paintScript, 0, 0, w, defaultLineH, partsMargin);
	}
	
	//control
	public void setWithEnable(T object) {
		if((targetObject = object) != null) {
			super.enable();
			setXY(GHQ.getMouseScreenX(), GHQ.getMouseScreenY());
		}
	}
	public T get() {
		return targetObject;
	}
	public void remove() {
		targetObject = null;
	}
	@Override
	public void enable() {
	}
	@Override
	public void disable() {
		super.disable();
		remove();
	}
	@Override
	public void outsideClicked() {
		super.outsideClicked();
		disable();
	}

}
