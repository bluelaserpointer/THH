package core;

import static physics.Direction4.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import physics.Direction4;
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
			Point neighbors[] = Direction4.getVertHorzClosest_int(this, corners, W, D, S, A);
			//check 4 neighbor Corner are reachable
			addAnchorIfNotNull(explore1(Direction4.W, (Corner)neighbors[Direction4.W_ID]));
			addAnchorIfNotNull(explore1(Direction4.D, (Corner)neighbors[Direction4.D_ID]));
			addAnchorIfNotNull(explore1(Direction4.S, (Corner)neighbors[Direction4.S_ID]));
			addAnchorIfNotNull(explore1(Direction4.A, (Corner)neighbors[Direction4.A_ID]));
			//
		}
		protected Anchor[] explore0(Anchor settedAnchors[]) {
			//find 4 neighbor Corner
			Corner neighbors[] = searchCorner4direction(x, y);
			//check 4 neighbor Corner are reachable
			settedAnchors[Direction4.W_ID] = explore1(Direction4.W, neighbors[Direction4.W_ID]);
			settedAnchors[Direction4.D_ID] = explore1(Direction4.D, neighbors[Direction4.D_ID]);
			settedAnchors[Direction4.S_ID] = explore1(Direction4.S, neighbors[Direction4.S_ID]);
			settedAnchors[Direction4.A_ID] = explore1(Direction4.A, neighbors[Direction4.A_ID]);
			//
			return settedAnchors;
		}
		protected Anchor explore1(Direction4 direction, Corner neighborPoint) {
			Point point = new Point.IntPoint(this);
			if(hasWall(point.shift(direction, EQUAL_GAP))) { //has wall in first step
				linkableCorners[direction.left45().getID()] = null;
				linkableCorners[direction.right45().getID()] = null;
				return null;
			}else {
				while(true) {
					if(hasWall(point.shift(direction, EQUAL_GAP))) {
						point.shift(direction.back(), EQUAL_GAP);
						return new Anchor(this, point, direction);
					}
					if(!point.checkDirection_int(neighborPoint, direction)) { //arrive at the neighbor corner point
						linkableCorners[direction.left45().getID()] = neighborPoint;
						linkableCorners[direction.right45().getID()] = null;	
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
		final Corner SRC_CORNER;
		protected Anchor(Corner source, Point point, Direction4 directionID) {
			super(point);
			ANCHOR_DIRECTION = directionID;
			SRC_CORNER = source;
		}
		protected final Corner searchAnchorOrConerCWASD(Point basePoint, Direction4 direction) {
			final Corner foundCorner = Direction4.getVertHorzClosest_int(basePoint, corners, direction);
			final Anchor foundAnchor = Direction4.getVertHorzClosest_int(basePoint, anchors, direction);
			return Direction4.getOutermost_int(direction, foundCorner, foundAnchor) != foundCorner ? foundCorner : foundAnchor.SRC_CORNER;
		}
		protected void explore0() {
			SRC_CORNER.linkableCorners[ANCHOR_DIRECTION.left45().getID()] = explore1(this, ANCHOR_DIRECTION.left());
			SRC_CORNER.linkableCorners[ANCHOR_DIRECTION.right45().getID()] = explore1(this, ANCHOR_DIRECTION.right());
		}
		protected Corner explore1(Point basePoint, Direction4 direction) {
			final Point point = new Point.IntPoint(basePoint);
			point.shift(direction, EQUAL_GAP);
			if(hasWall(point)) //hit wall at first step -> stop searching
				return null;
			Corner neighbor = searchAnchorOrConerCWASD(basePoint, direction);
			do {
				if(!point.checkDirection_int(neighbor, direction)) //arrive
					return neighbor;
				point.shift(direction, EQUAL_GAP);
			}while(!hasWall(point));
			//hit a wall
			point.shift(direction.back(), EQUAL_GAP);
			//check blocked direction (No way both of them are opened)
			final boolean RIGHT_BLOCKED = hasWall(point.shift(direction.right(), EQUAL_GAP));
			point.shift(direction.left(), EQUAL_GAP);
			return explore1(point, RIGHT_BLOCKED ? direction.left() : direction.right());
		}
	}
	
	protected LinkedList<Corner> corners = new LinkedList<Corner>();
	protected LinkedList<Anchor> anchors = new LinkedList<Anchor>();
	
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
