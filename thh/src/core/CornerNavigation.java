package core;

import static physics.Direction4.*;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import effect.debugEffect.DebugEffect;
import paint.ColorFilling;
import physics.Direction4;
import physics.Point;

public class CornerNavigation {
	
	protected final int EQUAL_GAP;
	
	protected class Corner extends Point.IntPoint{
		private static final long serialVersionUID = 2079598768300719520L;
		final Corner[] linkableCorners = new Corner[8];
		Route makedRoute;
		protected Corner(int x, int y) {
			super(x, y);
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
			Point neighbors[] = Direction4.getVertHorzClosest_int(this, corners, W, D, S, A);
			//check 4 neighbor Corner are reachable
			settedAnchors[Direction4.W_ID] = explore1(Direction4.W, (Corner)neighbors[Direction4.W_ID]);
			settedAnchors[Direction4.D_ID] = explore1(Direction4.D, (Corner)neighbors[Direction4.D_ID]);
			settedAnchors[Direction4.S_ID] = explore1(Direction4.S, (Corner)neighbors[Direction4.S_ID]);
			settedAnchors[Direction4.A_ID] = explore1(Direction4.A, (Corner)neighbors[Direction4.A_ID]);
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
					if(neighborPoint != null && !point.checkDirection_int(neighborPoint, direction)) { //arrive at the neighbor corner point
						linkableCorners[direction.left45().getID()] = neighborPoint;
						linkableCorners[direction.right45().getID()] = null;
						DebugEffect.setLine(Color.BLUE, GHQ.stroke3, intX(), intY(), neighborPoint.intX(), neighborPoint.intY());
						return null;
					}
					if(hasWall(point.shift(direction, EQUAL_GAP))) {
						point.shift(direction.back(), EQUAL_GAP);
						return new Anchor(this, point, direction);
					}
				}
			}
		}
	}
	protected class Anchor extends Point.IntPoint{
		private static final long serialVersionUID = 3823523254019408178L;
		final Direction4 ANCHOR_DIRECTION;
		final Corner NEST_CORNER;
		protected Anchor(Corner nestCorner, Point point, Direction4 directionID) {
			super(point);
			ANCHOR_DIRECTION = directionID;
			NEST_CORNER = nestCorner;
		}
		protected final Corner searchAnchorOrConer(Point basePoint, Direction4 direction) {
			final Corner foundCorner = Direction4.getVertHorzClosest_int(basePoint, corners, direction);
			final Anchor foundAnchor = Direction4.getVertHorzClosest_int(basePoint, anchors, direction);
			if(Direction4.getOutermost_int(direction.back(), foundCorner, foundAnchor) == foundCorner)
				return  foundCorner;
			else
				return  foundAnchor == null ? null : foundAnchor.NEST_CORNER;
		}
		protected void explore0() {
			NEST_CORNER.linkableCorners[ANCHOR_DIRECTION.left45().getID()] = explore1(this, ANCHOR_DIRECTION.left());
			NEST_CORNER.linkableCorners[ANCHOR_DIRECTION.right45().getID()] = explore1(this, ANCHOR_DIRECTION.right());
			
			Corner corner = NEST_CORNER.linkableCorners[ANCHOR_DIRECTION.left45().getID()];
			if(corner != null) {
				DebugEffect.setLine(Color.CYAN, NEST_CORNER.intX(), NEST_CORNER.intY(), intX(), intY());
				DebugEffect.setLine(Color.CYAN, intX(), intY(), corner.intX(), corner.intY());
				DebugEffect.setLine(Color.WHITE, NEST_CORNER.intX(), NEST_CORNER.intY(), corner.intX(), corner.intY());
			}
			corner = NEST_CORNER.linkableCorners[ANCHOR_DIRECTION.right45().getID()];
			if(corner != null) {
				DebugEffect.setLine(Color.CYAN, NEST_CORNER.intX(), NEST_CORNER.intY(), intX(), intY());
				DebugEffect.setLine(Color.CYAN, intX(), intY(), corner.intX(), corner.intY());
				DebugEffect.setLine(Color.WHITE, NEST_CORNER.intX(), NEST_CORNER.intY(), corner.intX(), corner.intY());
			}
		}
		protected Corner explore1(Point basePoint, Direction4 direction) {
			final Point point = new Point.IntPoint(basePoint);
			point.shift(direction, EQUAL_GAP);
			if(hasWall(point)) //hit wall at first step -> stop searching
				return null;
			Corner neighbor = searchAnchorOrConer(basePoint, direction);
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
		boolean oldBlockState = false;
		final Point point = new Point.IntPoint(EQUAL_GAP/2, EQUAL_GAP/2);
		for(;point.intX() < X_LIMIT;point.shift(D, EQUAL_GAP)) {
			point.setY(EQUAL_GAP/2);
			oldBlockState = hasWall(point);
			while(point.intY() < Y_LIMIT) {
				point.shift(S, EQUAL_GAP);
				final boolean nowBlockState = hasWall(point);
				if(oldBlockState != nowBlockState) {
					if(!hasWall(point.intX() - EQUAL_GAP, point.intY() - EQUAL_GAP) && !hasWall(point.intX() - EQUAL_GAP, point.intY()))
						addCornerPoint(point.intX() - EQUAL_GAP, nowBlockState ? point.intY() - EQUAL_GAP : point.intY());
					if(!hasWall(point.intX() + EQUAL_GAP, point.intY() - EQUAL_GAP) && !hasWall(point.intX() + EQUAL_GAP, point.intY()))
						addCornerPoint(point.intX() + EQUAL_GAP, nowBlockState ? point.intY() - EQUAL_GAP : point.intY());
				}
				oldBlockState = nowBlockState;
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
	public void debugPreview() {
		for(Corner ver : corners)
			ColorFilling.rectPaint(Color.BLUE, ver.intX() - 5, ver.intY() - 5, 10, 10);
		for(Anchor ver : anchors)
			ColorFilling.rectPaint(Color.CYAN, ver.intX() - 5, ver.intY() - 5, 10, 10);
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
	public boolean hasWall(int x, int y) {
		return GHQ.hitStructure_Dot(x, y) || !GHQ.inStage(x, y);
	}
	public final boolean hasWall(Point point) {
		return hasWall(point.intX(), point.intY());
	}
	public boolean hasWall(int x1, int y1, int w, int h) {
		return GHQ.hitStructure_Rect(x1, y1, w, h) || !GHQ.inStage(x1, y1);
	}
}
