package physics;

public enum Direction8 {
	WD, WA, DW, DS, SD, SA, AS, AW;
	
	public int getID() {
		switch(this) {
		case WD:
			return 0;
		case WA:
			return 1;
		case DW:
			return 2;
		case DS:
			return 3;
		case SD:
			return 4;
		case SA:
			return 5;
		case AS:
			return 6;
		case AW:
			return 7;
		default:
			return -1;
		}
	}
}
