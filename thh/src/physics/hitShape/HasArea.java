package physics.hitShape;

import core.GHQ;

public interface HasArea {
	public static HasArea blankArea = new HasArea() {
		@Override
		public boolean intersectsDot(int x, int y) {
			return false;
		}
	};
	
	public abstract boolean intersectsDot(int x, int y);
	public default boolean isMouseOvered() {
		return intersectsDot(GHQ.mouseX(), GHQ.mouseY());
	}
}
