package core;

import static physics.Direction4.*;
import static physics.Direction8.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import physics.Direction4;
import physics.GridPoint;
import physics.Point;

public abstract class CornerNavigation {
	
	protected final int EQUAL_GAP;
	
	protected class Corner extends Point.IntPoint{
		private static final long serialVersionUID = 2079598768300719520L;
		final Corner[] linkableCorners = new Corner[8];
		Route makedRoute;
		protected Corner(int x, int y) {
			super(x, y);
		}
		protected final Corner[] searchCorner4direction(int x, int y) {
			final Corner[] founds = new Corner[4];
			for(Corner ver : corners) {
				if(ver.x == x) {
					if(ver.y < y) {
						if(founds[0] == null || founds[0].y < ver.y)
							founds[0] = ver;
					}else {
						if(founds[1] == null || ver.y < founds[1].y)
							founds[1] = ver;
					}
				}else if(ver.y == y) {
					if(ver.x < x) {
						if(founds[2] == null || founds[2].x < ver.x)
							founds[2] = ver;
					}else {
						if(founds[3] == null || ver.x < founds[3].x)
							founds[3] = ver;
					}
				}
			}
			return founds;
		}
		protected void addAnchorIfNotNull(Anchor anchor) {
			if(anchor != null)
				anchors.add(anchor);
		}
		protected void explore0() {
			//find 4 neighbor Corner
			Corner neighbors[] = searchCorner4direction(x, y);
			//check 4 neighbor Corner are reachable
			addAnchorIfNotNull(explore1(Direction4.W, y - neighbors[Direction4.W_ID].y, neighbors[Direction4.W_ID]));
			addAnchorIfNotNull(explore1(Direction4.D, neighbors[Direction4.D_ID].x - x, neighbors[Direction4.D_ID]));
			addAnchorIfNotNull(explore1(Direction4.S, x - neighbors[Direction4.S_ID].x, neighbors[Direction4.S_ID]));
			addAnchorIfNotNull(explore1(Direction4.A, neighbors[Direction4.A_ID].y - y, neighbors[Direction4.A_ID]));
			//
		}
		protected Anchor[] explore0(Anchor settedAnchors[]) {
			//find 4 neighbor Corner
			Corner neighbors[] = searchCorner4direction(x, y);
			//check 4 neighbor Corner are reachable
			settedAnchors[Direction4.W_ID] = explore1(Direction4.W, y - neighbors[Direction4.W_ID].y, neighbors[Direction4.W_ID]);
			settedAnchors[Direction4.D_ID] = explore1(Direction4.D, neighbors[Direction4.D_ID].x - x, neighbors[Direction4.D_ID]);
			settedAnchors[Direction4.S_ID] = explore1(Direction4.S, x - neighbors[Direction4.S_ID].x, neighbors[Direction4.S_ID]);
			settedAnchors[Direction4.A_ID] = explore1(Direction4.A, neighbors[Direction4.A_ID].y - y, neighbors[Direction4.A_ID]);
			//
			return settedAnchors;
		}
		protected Anchor explore1(Direction4 direction, int limitDistance, Corner neighborPoint) {
			GridPoint gridPoint = new GridPoint(x, y, EQUAL_GAP, direction);
			if(hasWall(gridPoint.moveFoward())) { //has wall in first step
				linkableCorners[gridPoint.direction.getLeft45().getID()] = null;
				linkableCorners[gridPoint.direction.getRight45().getID()] = null;
				return null;
			}else {
				while(true) {
					if(hasWall(gridPoint.moveFoward())) {
						gridPoint.moveBack();
						return new Anchor(this, gridPoint, direction);
					}
					if((limitDistance -= EQUAL_GAP) <= 0) { //arrive at a neighbor corner point
						linkableCorners[gridPoint.direction.getLeft45().getID()] = neighborPoint;
						linkableCorners[gridPoint.direction.getRight45().getID()] = null;	
						return null;
					}
				}
			}
		}
		protected int getAvaliableLinkAmount() {
			int amount = 0;
			for(Corner ver : linkableCorners) {
				if(ver != null)
					amount++;
			}
			return amount;
		}
	}
	protected class Anchor extends Point.IntPoint{
		private static final long serialVersionUID = 3823523254019408178L;
		final Direction4 ANCHOR_DIRECTION;
		final Corner SOURCE;
		protected Anchor(Corner source, Point point, Direction4 directionID) {
			super(point);
			ANCHOR_DIRECTION = directionID;
			SOURCE = source;
		}
		protected final Corner searchAnchorOrConerCWASD(int x, int y, Direction4 direction) {
			final Corner found = searchNearstPointCWASD(corners, x, y, direction);
			final Anchor foundAnchor = searchNearstPointCWASD(anchors, x, y, direction);
			switch(direction) {
			case W:
				return foundAnchor.y > found.intY() ? foundAnchor.SOURCE : found;
			case S:
				return foundAnchor.y < found.intY() ? foundAnchor.SOURCE : found;
			case A:
				return foundAnchor.x > found.intX() ? foundAnchor.SOURCE : found;
			case D:
				return foundAnchor.x < found.intX() ? foundAnchor.SOURCE : found;
			default:
				System.out.println("searchAnchorOrConerCWASD received illegal directionID:" + direction);
				return null;
			}
		}
		protected void explore0() {
			switch(ANCHOR_DIRECTION) {
			case W:
				SOURCE.linkableCorners[WA.getID()] = explore1(x, y, A);
				SOURCE.linkableCorners[WD.getID()] = explore1(x, y, D);
				break;
			case S:
				SOURCE.linkableCorners[SA.getID()] = explore1(x, y, A);
				SOURCE.linkableCorners[SD.getID()] = explore1(x, y, D);
				break;
			case A:
				SOURCE.linkableCorners[AW.getID()] = explore1(x, y, W);
				SOURCE.linkableCorners[AS.getID()] = explore1(x, y, S);
				break;
			case D:
				SOURCE.linkableCorners[DW.getID()] = explore1(x, y, W);
				SOURCE.linkableCorners[DS.getID()] = explore1(x, y, S);
				break;
			}
		}
		protected Corner explore1(int x, int y, Direction4 direction) {
			if(direction == W) {
				int nowy = y - EQUAL_GAP;
				if(hasWall(x, nowy)) { //hit wall at first step -> stop searching
					return null;
				}
				Corner wNeighbor = searchAnchorOrConerCWASD(x, y, W);
				do {
					if(nowy < wNeighbor.intY()) //arrive
						return wNeighbor;
					nowy -= EQUAL_GAP;
				}while(!hasWall(x, nowy));
				nowy += EQUAL_GAP;
				return hasWall(x + EQUAL_GAP, nowy) ? explore1(x, nowy, A) : explore1(x, nowy, D);
			} else if(direction == S) {
				int nowy = y + EQUAL_GAP;
				if(hasWall(x, nowy)) { //hit wall at first step -> stop searching
					return null;
				}
				Corner sNeighbor = searchAnchorOrConerCWASD(x, y, S);
				do {
					if(nowy > sNeighbor.intY()) //arrive
						return sNeighbor;
					nowy += EQUAL_GAP;
				}while(!hasWall(x, nowy));
				nowy -= EQUAL_GAP;
				return hasWall(x + EQUAL_GAP, nowy) ? explore1(x, nowy, A) : explore1(x, nowy, D);
			} else if(direction == A) {
				int nowx = x - EQUAL_GAP;
				if(hasWall(nowx, y)) { //hit wall at first step -> stop searching
					return null;
				}
				Corner aNeighbor = searchAnchorOrConerCWASD(x, y, A);
				do {
					if(nowx < aNeighbor.intX()) //arrive
						return aNeighbor;
					nowx -= EQUAL_GAP;
				}while(!hasWall(nowx, y));
				nowx += EQUAL_GAP;
				return hasWall(nowx, y + EQUAL_GAP) ? explore1(nowx, y, W) : explore1(nowx, y, S);
			}else { //D
				int nowx = x + EQUAL_GAP;
				if(hasWall(nowx, y)) { //hit wall at first step -> stop searching
					return null;
				}
				Corner dNeighbor = searchAnchorOrConerCWASD(x, y, D);
				do {
					if(nowx > dNeighbor.intX()) //arrive
						return dNeighbor;
					nowx += EQUAL_GAP;
				}while(!hasWall(nowx, y));
				nowx -= EQUAL_GAP;
				return hasWall(nowx, y + EQUAL_GAP) ? explore1(nowx, y, W) : explore1(nowx, y, S);
			}
		}
	}
	
	
	protected LinkedList<Corner> corners = new LinkedList<Corner>();
	protected LinkedList<Anchor> anchors = new LinkedList<Anchor>();
	
	protected final <T extends Point>T searchNearstPointCWASD(LinkedList<T> points,int x, int y, Direction4 direction) {
		T found = null;
		switch(direction) {
		case W:
		case S:
			for(T ver : points) {
				if(ver.intX() == x) {
					if(direction == W) {
						if(ver.intY() < y && (found == null || found.intY() < ver.intY()))
							found = ver;
					}else {
						if(y < ver.intY() && (found == null || ver.intY() < found.intY()))
							found = ver;
					}
				}
			}
			break;
		case A:
		case D:
			for(T ver : points) {
				if(ver.intY() == y) {
					if(direction == A) {
						if(ver.intX() < x && (found == null || found.intX() < ver.intX()))
							found = ver;
					}else {
						if(x < ver.intX() && (found == null || ver.intX() < found.intX()))
							found = ver;
					}
				}
			}
			break;
		default:
			System.out.println("searchNearstPointCWASD received illegal directionID:" + direction);
			break;
		}
		return found;
	}
	
	public CornerNavigation(int equalGap) {
		EQUAL_GAP = equalGap;
	}
	public CornerNavigation() {
		EQUAL_GAP = 1;
	}
	public void addCornerPoint(int x, int y) {
		corners.add(new Corner(x, y));
	}
	public void defaultCornerCollect() {
		final int X_LIMIT = GHQ.getEngine().getStageW(), Y_LIMIT = GHQ.getEngine().getStageH();
		boolean state = false;
		for(int xi = EQUAL_GAP/2;xi < X_LIMIT;xi += EQUAL_GAP) {
			for(int yi = EQUAL_GAP/2;yi < Y_LIMIT;yi += EQUAL_GAP) {
				final boolean nowState = hasWall(xi, yi);
				if(xi > EQUAL_GAP/2 && yi > EQUAL_GAP/2 && state != nowState) {
					if(!hasWall(xi - EQUAL_GAP, yi - EQUAL_GAP) && !hasWall(xi - EQUAL_GAP, yi))
						addCornerPoint(xi - EQUAL_GAP, state ? yi : yi - EQUAL_GAP);
					if(!hasWall(xi + EQUAL_GAP, yi - EQUAL_GAP) && !hasWall(xi + EQUAL_GAP, yi))
						addCornerPoint(xi + EQUAL_GAP, state ? yi : yi - EQUAL_GAP);
				}
				state = nowState;
			}
		}
	}
	public void startCornerLink() {
		for(Corner ver : corners) {
			ver.explore0();
		}
		for(Anchor ver : anchors) {
			ver.explore0();
		}
	}
	public void reset() {
		corners.clear();
		anchors.clear();
	}
	class Route {
		final Queue<Corner> routeQueue = new ArrayDeque<Corner>();
		Corner nowCorner;
		double distance;
		public Route(Corner startCorner) {
			routeQueue.add(nowCorner = startCorner);
			distance = 0;
		}
		boolean isLonger(Route targetRoute) {
			return distance > targetRoute.distance;
		}
		boolean atSamePoint(Corner corner) {
			return nowCorner == corner;
		}
		void to(Corner corner) {
			routeQueue.add(corner);
			distance += nowCorner.distanceSq(corner);
			nowCorner = corner;
		}
	}
	protected Corner makeTemporaryCorner(int x, int y) {
		Corner myCorner = new Corner(x, y);
		final Anchor myAnchor[] = new Anchor[4];
		myCorner.explore0(myAnchor);
		for(Anchor ver : myAnchor) {
			if(ver != null)
				ver.explore0();
		}
		return myCorner;
	}
	public Queue<Point> getRoot(int x1, int y1, int x2, int y2) {
		final Queue<Point> result = new ArrayDeque<Point>();
		//find all route start point
		final Corner startCorner = makeTemporaryCorner(x1, y1);
		final ArrayList<Route> activeRoutes = new ArrayList<Route>();
		for(Corner linkableCorner : startCorner.linkableCorners) {
			if(linkableCorner != null)
				activeRoutes.add(new Route(linkableCorner));
		}
		//find all goal corner point
		final Corner goalCorner = makeTemporaryCorner(x2, y2);
		final ArrayList<Corner> goalCorners = new ArrayList<Corner>();
		for(Corner linkableCorner : goalCorner.linkableCorners) {
			if(linkableCorner != null)
				goalCorners.add(linkableCorner);
		}
		//find closest route
		Route reachedRoute = null;
		while(activeRoutes.size() > 0) {
			for(Route ver1_route : activeRoutes) {
				if(ver1_route == reachedRoute)
					continue;
				branchCheck: for(Corner ver_linkableCorner : ver1_route.nowCorner.linkableCorners) {
					if(ver_linkableCorner == null)
						continue;
					//check if there is a route duplicated in this Corner.
					for(Route ver2_route : activeRoutes) {
						if(ver1_route != ver2_route && ver2_route.atSamePoint(ver_linkableCorner)) {
							activeRoutes.remove(ver1_route.isLonger(ver2_route) ? ver1_route : ver2_route);
							continue branchCheck;
						}
					}
					//extends routes
					ver1_route.to(ver_linkableCorner);
				}
			}
		}
		//return the result
		return result;
		
	}
	
	//extend
	public abstract boolean hasWall(int x, int y);
	public final boolean hasWall(Point point) {
		return hasWall(point.intX(), point.intY());
	}
	public abstract boolean hasWall(int x1, int y1, int x2, int y2);
}
