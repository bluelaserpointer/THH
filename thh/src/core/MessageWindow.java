package core;

import gui.GUIParts;
import paint.PaintScript;

public abstract class MessageWindow extends GUIParts{
	public MessageWindow(String group,PaintScript paintScript,int x,int y,int w,int h) {
		super(group,paintScript,x,y,w,h);
	}
	//role
	public void setText(String text) {
		
	}
	public void setTypeSpeed(int ms) {
		
	}
	public void addText(String text) {
		
	}
	public void setPaintPronpt(PaintScript pp,int kind) {
		
	}
}
