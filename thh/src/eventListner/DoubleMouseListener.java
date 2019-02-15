package eventListner;

import core.GHQ;

public class DoubleMouseListener extends MouseListenerEx{
	private static final long serialVersionUID = -2572480668759757245L;
	
	private int button1PressedFrame,button2PressedFrame,button3PressedFrame;
	private final int LIMIT_SPAN;
	public DoubleMouseListener(int limitSpan) {
		LIMIT_SPAN = limitSpan;
	}
	@Override
	public void setButton1Event() {
		if(!GHQ.isExpired_frame(button1PressedFrame,LIMIT_SPAN))
			super.button1 = true;
		else
			button1PressedFrame = GHQ.getNowFrame();
	}
	@Override
	public void setButton2Event() {
		if(!GHQ.isExpired_frame(button2PressedFrame,LIMIT_SPAN))
			super.button2 = true;
		else
			button2PressedFrame = GHQ.getNowFrame();
	}
	@Override
	public void setButton3Event() {
		if(!GHQ.isExpired_frame(button3PressedFrame,LIMIT_SPAN))
			super.button3 = true;
		else
			button3PressedFrame = GHQ.getNowFrame();
	}
}
