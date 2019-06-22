package physics;

public enum Direction4 {
	W, D, S, A;
	
	public static final int W_ID = 0, D_ID = 1, S_ID = 2, A_ID = 3;
	public Direction8 getLeft45() {
		switch(this) {
		case W:
			return Direction8.WA;
		case D:
			return Direction8.DW;
		case S:
			return Direction8.SD;
		case A:
			return Direction8.AS;
		default:
			return null;
		}
	}
	public Direction8 getRight45() {
		switch(this) {
		case W:
			return Direction8.WD;
		case D:
			return Direction8.DS;
		case S:
			return Direction8.SA;
		case A:
			return Direction8.AW;
		default:
			return null;
		}
	}
	public Direction4 getLeft() {
		switch(this) {
		case W:
			return A;
		case D:
			return W;
		case S:
			return D;
		case A:
			return S;
		default:
			return null;
		}
	}
	public Direction4 getRight() {
		switch(this) {
		case W:
			return D;
		case D:
			return S;
		case S:
			return A;
		case A:
			return W;
		default:
			return null;
		}
	}
	public Direction4 getBack() {
		switch(this) {
		case W:
			return S;
		case D:
			return A;
		case S:
			return W;
		case A:
			return D;
		default:
			return null;
		}
	}
	public int getID() {
		switch(this) {
		case W:
			return W_ID;
		case D:
			return D_ID;
		case S:
			return S_ID;
		case A:
			return A_ID;
		default:
			return -1;
		}
	}
}
