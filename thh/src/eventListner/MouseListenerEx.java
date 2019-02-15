package eventListner;

import java.io.Serializable;

public class MouseListenerEx implements Serializable{
	private static final long serialVersionUID = 2878445766577630707L;

	protected boolean button1,button2,button3;
	public void setButton1Event() {
		button1 = true;
	}
	public void setButton2Event() {
		button2 = true;
	}
	public void setButton3Event() {
		button3 = true;
	}
	public final boolean hasButton1Event() {
		return button1;
	}
	public final boolean hasButton2Event() {
		return button2;
	}
	public final boolean hasButton3Event() {
		return button3;
	}
	public final boolean pullButton1Event() {
		if(button1) {
			button1 = false;
			return true;
		}
		return false;
	}
	public final boolean pullButton2Event() {
		if(button2) {
			button2 = false;
			return true;
		}
		return false;
	}
	public final boolean pullButton3Event() {
		if(button3) {
			button3 = false;
			return true;
		}
		return false;
	}
	public void reset() {
		button1 = button2 = button3 = false;
	}
}
