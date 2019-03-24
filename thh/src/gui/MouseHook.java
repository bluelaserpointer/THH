package gui;

import core.GHQ;
import paint.HasDotPaint;
import paint.RectPaint;

/**
 * A subclass of {@link GUIParts} for managing mouse drag&drop actions.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class MouseHook<T extends HasDotPaint> extends GUIParts{
	protected T hookingObject;
	protected final int SIZE;
	
	//init
	/**
	 * Create a MouseHook.
	 * @param group - name of the group this GUI belong to(use in {@link GHQ#enableGUIs(String)}, {@link GHQ#disableGUIs(String)})
	 * @param paintScript - the {@link RectPaint} of this GUI background
	 */
	public MouseHook(String group, RectPaint paintScript) {
		super(group, paintScript, 0, 0, 0, 0, false);
		SIZE = GHQ.NONE;
	}
	/**
	 * Create a MouseHook.
	 * @param group - name of the group this GUI belong to(use in {@link GHQ#enableGUIs(String)}, {@link GHQ#disableGUIs(String)})
	 * @param paintScript - the {@link RectPaint} of this GUI background
	 * @param size - the display size of dragging object
	 */
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
				hookingObject.getPaintScript().dotPaint_capSize(GHQ.getMouseScreenX(), GHQ.getMouseScreenY(), SIZE);
		}
	}
	
	//control
	/**
	 * Hooking a object to this MouseHook.
	 * @param object
	 */
	public void hook(T object) {
		hookingObject = object;
	}
	/**
	 * Get the hooking object.
	 * @return hooking object
	 */
	public T get() {
		return hookingObject;
	}
	/**
	 * Removing the hooking object.
	 * @return hooked object
	 */
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
