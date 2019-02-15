package eventListner;

import com.sun.glass.events.KeyEvent;

import core.GHQ;

public class SingleNumKeyListener extends KeyListenerEx{
	private static final long serialVersionUID = -7351066034418156232L;
	public SingleNumKeyListener(int maxNumber) {
		super(new int[Math.min(maxNumber + 1, 9)]);
		for(int i = 0;i <= targetKeys.length;i++)
			super.targetKeys[i] = KeyEvent.VK_0 + i;
	}
	@Override
	public void pressEvent_index(int index) {
		super.hasKeyEvents.set(index);
	}
	@Override
	public void releaseEvent_index(int index) {
		super.hasKeyEvents.clear(index);
	}
	public final int getHasEventKeyNum() {
		for(int i = 0;i < targetKeys.length;i++) {
			if(hasKeyEvents.get(i))
				return i;
		}
		return GHQ.NONE;
	}
}
