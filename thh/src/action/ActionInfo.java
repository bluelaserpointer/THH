package action;

public class ActionInfo {
	private final static int 
		NONE = thh.THH.NONE,
		MAX = thh.THH.MAX;
	
	public static String name;
	public static int[]
		passX, 
		passY,
		frame,
		accel;
	public static int
		startX,
		startY;
	public static final void clear() {
		passX = passY = accel = null;
	}
	public static final void setStartPoint(int x,int y) {
		startX = x;startY = y;
	}
}