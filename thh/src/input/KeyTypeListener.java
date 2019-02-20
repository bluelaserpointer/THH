package input;

import static java.awt.event.KeyEvent.*;

import core.GHQ;

public abstract class KeyTypeListener {
	private boolean isEnabled;
	private final int LIMIT_LENGTH;
	private String contentStr = "";
	private static String clipBoard = "";
	private static final SingleKeyListener copyListener = new SingleKeyListener(new int[] {VK_CONTROL,VK_C,VK_V});
	public KeyTypeListener() {
		LIMIT_LENGTH = GHQ.MAX;
		GHQ.addListenerEx(copyListener);
	}
	public KeyTypeListener(int limitLength) {
		LIMIT_LENGTH = limitLength;
		GHQ.addListenerEx(copyListener);
	}
	public void typed(char typedChar) {
		final int nowLength = contentStr.length();
		switch(typedChar) {
		case '\n': //enter
			typeEnd(contentStr);
			disable();
			break;
		case '\b': //backspace
			if(nowLength > 0)
				contentStr = contentStr.substring(0,nowLength - 1);
			break;
		case '\t': //tab
			break;
		default:
			//copy & paste
			if(copyListener.hasEvent(VK_CONTROL)) {
				if(copyListener.hasEvent(VK_C))
					clipBoard = contentStr;
				else if(copyListener.hasEvent(VK_V))
					contentStr = clipBoard;
				break;
			}
			//normal input
			if(nowLength < LIMIT_LENGTH)
				contentStr += typedChar;
		}
	}
	public abstract void typeEnd(String text);
	public String getText() {
		return contentStr;
	}
	public void setText(String text) {
		contentStr = text == null ? "" : text;
	}
	public void clear() {
		contentStr = "";
	}
	public boolean textEquals(String text) {
		return contentStr.equals(text);
	}
	public final boolean isEnabled() {
		return isEnabled;
	}
	public final void enable() {
		isEnabled = true;
		copyListener.enable();
	}
	public final void disable() {
		isEnabled = false;
		copyListener.disable();
	}
}
