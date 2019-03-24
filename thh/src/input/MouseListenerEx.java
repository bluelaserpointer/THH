package input;

import java.io.Serializable;

/**
 * A primal class for managing mouse input event.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class MouseListenerEx implements Serializable{
	private static final long serialVersionUID = 2878445766577630707L;

	protected boolean button1,button2,button3;
	private boolean isEnabled;
	/**
	 * [This method only be called from GHQ.]Make a mouse press event of button1.
	 */
	public void pressButton1Event() {
		button1 = true;
	}
	/**
	 * [This method only be called from GHQ.]Make a mouse press event of button2.
	 */
	public void pressButton2Event() {
		button2 = true;
	}
	/**
	 * [This method only be called from GHQ.]Make a mouse press event of button3.
	 */
	public void pressButton3Event() {
		button3 = true;
	}
	/**
	 * Judge if button1 has a event.
	 * @return true - has a event / false - no event
	 */
	public final boolean hasButton1Event() {
		return button1;
	}
	/**
	 * Judge if button2 has a event.
	 * @return true - has a event / false - no event
	 */
	public final boolean hasButton2Event() {
		return button2;
	}
	/**
	 * Judge if button3 has a event.
	 * @return true - has a event / false - no event
	 */
	public final boolean hasButton3Event() {
		return button3;
	}
	/**
	 * Judge if button1 has a event, and if so, remove the event at the same time.
	 * @return true - has a event / false - no event
	 */
	public final boolean pullButton1Event() {
		if(button1) {
			button1 = false;
			return true;
		}
		return false;
	}
	/**
	 * Judge if button2 has a event, and if so, remove the event at the same time.
	 * @return true - has a event / false - no event
	 */
	public final boolean pullButton2Event() {
		if(button2) {
			button2 = false;
			return true;
		}
		return false;
	}
	/**
	 * Judge if button3 has a event, and if so, remove the event at the same time.
	 * @return true - has a event / false - no event
	 */
	public final boolean pullButton3Event() {
		if(button3) {
			button3 = false;
			return true;
		}
		return false;
	}
	/**
	 * Check if this listener is enabled.
	 * @return true - enabled / false - disabled
	 */
	public final boolean isEnabled() {
		return isEnabled;
	}
	/**
	 * Enable this listener.
	 */
	public final void enable() {
		isEnabled = true;
	}
	/**
	 * Disable this listener.
	 */
	public final void disable() {
		isEnabled = false;
		reset();
	}
	/**
	 * Remove all the event.
	 */
	public void reset() {
		button1 = button2 = button3 = false;
	}
}
