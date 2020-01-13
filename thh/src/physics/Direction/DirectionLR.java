package physics.Direction;

public enum DirectionLR {
	LEFT, RIGHT;
	
	public int getID() {
		switch(this) {
		case LEFT:
			return 0;
		case RIGHT:
			return 1;
		default:
			return -1;
		}
	}
}
