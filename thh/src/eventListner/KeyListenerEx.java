package eventListner;

import java.io.Serializable;
import java.util.BitSet;

public abstract class KeyListenerEx implements Serializable{
	private static final long serialVersionUID = -8033137272230272770L;

	protected final int targetKeys[];
	protected final BitSet hasKeyEvents;
	private boolean isEnabled;
	public KeyListenerEx(int targetKeys[]) {
		this.targetKeys = targetKeys;
		hasKeyEvents = new BitSet(targetKeys.length);
	}
	public final void pressEvent(int keyCode) {
		for(int i = 0;i < targetKeys.length;i++) {
			if(targetKeys[i] == keyCode)
				pressEvent_index(i);
		}
	}
	protected abstract void pressEvent_index(int index);
	public final void releaseEvent(int keyCode) {
		for(int i = 0;i < targetKeys.length;i++) {
			if(targetKeys[i] == keyCode)
				releaseEvent_index(i);
		}
	}
	protected abstract void releaseEvent_index(int index);
	public final boolean hasEvent(int keyCode) {
		for(int i = 0;i < targetKeys.length;i++) {
			if(targetKeys[i] == keyCode)
				return hasKeyEvents.get(i);
		}
		return false;
	}
	public final boolean pullEvent(int keyCode) {
		for(int i = 0;i < targetKeys.length;i++) {
			if(targetKeys[i] == keyCode) {
				if(hasKeyEvents.get(i)) {
					hasKeyEvents.clear(i);
					return true;
				}else
					return false;
			}
		}
		return false;
	}
	public final boolean isEnabled() {
		return isEnabled;
	}
	public final void enable() {
		isEnabled = true;
	}
	public final void disable() {
		isEnabled = false;
	}
	public void reset() {
		hasKeyEvents.clear();
	}
}
