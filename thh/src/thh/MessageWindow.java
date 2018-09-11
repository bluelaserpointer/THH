package thh;

public abstract class MessageWindow extends GUIParts{
	@Override
	public void enable() {
		super.isEnabled = true;
	}
	@Override
	public void disable() {
		super.isEnabled = false;
	}
	//role
	public void setText(String text) {
		
	}
	public void setTypeSpeed(int ms) {
		
	}
	public void addText(String text) {
		
	}
	public void setPaintPronpt(PaintPronpt pp,int kind) {
		
	}
}
