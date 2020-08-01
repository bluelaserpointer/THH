package physics.direction;

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
	public boolean isRight() {
		return this == RIGHT;
	}
	public boolean isLeft() {
		return this == LEFT;
	}
	public int plusMinusR() {
		return this == RIGHT ? +1 : -1;
	}
	public int plusMinusL() {
		return this == LEFT ? +1 : -1;
	}
}
