package physics;

import java.util.Collection;

public enum Direction4 {
	W, D, S, A;
	
	public static final int W_ID = 0, D_ID = 1, S_ID = 2, A_ID = 3;
	public Direction8 left45() {
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
	public Direction8 right45() {
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
	public Direction4 left() {
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
	public Direction4 right() {
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
	public Direction4 back() {
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
	///////////////
	//getOutermost
	///////////////
	public static Point getOutermost_int(Direction4 outerDirection, Point... points) {
		Point assumePoint = null;
		for(Point ver : points) {
			if(ver != null && (assumePoint == null || assumePoint.checkDirection_int(ver, outerDirection)))
				assumePoint = ver;
		}
		return assumePoint;
	}
	public static Point getOutermost_double(Direction4 outerDirection, Point... points) {
		Point assumePoint = null;
		for(Point ver : points) {
			if(ver != null && (assumePoint == null || assumePoint.checkDirection_double(ver, outerDirection)))
				assumePoint = ver;
		}
		return assumePoint;
	}
	///////////////
	//getVertHorzClosest
	///////////////
	public static Point[] getVertHorzClosest_int(Point basePoint, Direction4[] directions, Collection<? extends Point> points) {
		if(directions.length == 0 || points == null)
			return null;
		final Point[] assumePoints = new Point[directions.length];
		for(Point point : points) {
			if(point == null)
				continue;
			if(point.checkVert_int(basePoint)) { //vertical - W or S
				for(int i = 0;i < directions.length;i++) {
					switch(directions[i]) {
					case W:
					case S:
						if(basePoint.checkDirection_int(point, directions[i]) && (assumePoints[i] == null || point.checkDirection_int(assumePoints[i], directions[i])))
							assumePoints[i] = point;
					default:
						continue;
					}
				}
			}else if(point.checkHorz_int(basePoint)) { //horizontal - A or D
				for(int i = 0;i < directions.length;i++) {
					switch(directions[i]) {
					case A:
					case D:
						if(basePoint.checkDirection_int(point, directions[i]) && (assumePoints[i] == null || point.checkDirection_int(assumePoints[i], directions[i])))
							assumePoints[i] = point;
					default:
						continue;
					}
				}
			}
		}
		return assumePoints;
	}
	public static Point[] getVertHorzClosest_double(Point basePoint, Direction4[] directions, Collection<? extends Point> points) {
		if(directions.length == 0 || points == null)
			return null;
		final Point[] assumePoints = new Point[directions.length];
		for(Point point : points) {
			if(point == null)
				continue;
			if(point.checkVert_double(basePoint)) { //vertical - W or S
				for(int i = 0;i < directions.length;i++) {
					switch(directions[i]) {
					case W:
					case S:
						if(basePoint.checkDirection_double(point, directions[i]) && (assumePoints[i] == null || point.checkDirection_double(assumePoints[i], directions[i])))
							assumePoints[i] = point;
					default:
						continue;
					}
				}
			}else if(point.checkHorz_double(basePoint)) { //horizontal - A or D
				for(int i = 0;i < directions.length;i++) {
					switch(directions[i]) {
					case A:
					case D:
						if(basePoint.checkDirection_double(point, directions[i]) && (assumePoints[i] == null || point.checkDirection_double(assumePoints[i], directions[i])))
							assumePoints[i] = point;
					default:
						continue;
					}
				}
			}
		}
		return assumePoints;
	}
}
