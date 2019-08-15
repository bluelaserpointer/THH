package gui;

import core.GHQ;
import paint.dot.HasDotPaint;

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
	 */
	public MouseHook() {
		SIZE = GHQ.NONE;
	}
	/**
	 * Create a MouseHook.
	 * @param size - the display size of dragging object
	 * @param initialObject - initial hooking object
	 */
	public MouseHook(int size, T initialObject) {
		SIZE = size;
		hookingObject = initialObject;
	}
	
	//main role
	@Override
	public void idle() {
		if(hookingObject != null) {
			if(SIZE == GHQ.NONE)
				hookingObject.getDotPaint().dotPaint(GHQ.mouseScreenX(), GHQ.mouseScreenY());
			else
				hookingObject.getDotPaint().dotPaint_capSize(GHQ.mouseScreenX(), GHQ.mouseScreenY(), SIZE);
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

	public void hookInStorageViewer(TableStorageViewer<T> inventoryViewer) {
		final int HOVERED_ID = inventoryViewer.getMouseHoveredID();
		if(hasObject()) {
			hook(inventoryViewer.storage.set(HOVERED_ID, get()));
		}else {
			hook(inventoryViewer.storage.remove(HOVERED_ID));
		}
	}
	
	//information
	public boolean hasObject() {
		return hookingObject != null;
	}
}
