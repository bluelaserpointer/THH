package core;

import java.awt.Point;
import java.util.Arrays;
import java.util.BitSet;

public class AStar {
	private final Point[][] nearDsts;
	private final BitSet checkedPoints;
	private static final Point NOT_PASSIBLE_POINT = new Point(GHQ.NONE + 1, GHQ.NONE + 1);
	private static final Point DSTINATION_POINT = new Point(GHQ.NONE, GHQ.NONE);
	private int dstX = GHQ.NONE, dstY = GHQ.NONE;
	
	public AStar(int w, int h) {
		nearDsts = new Point[w][h];
		checkedPoints = new BitSet(w*h);
	}
	private boolean inAreaCheckWithErrorOutput(int x, int y) {
		if(inArea(x, y))
			return true;
		System.out.println("Point (" + x + ", " + y + ") is out of range.");
		return false;
	}
	public void setDst(int x, int y) {
		if(!inAreaCheckWithErrorOutput(x, y))
			return;
		dstX = x;dstY = y;
		nearDsts[x][y] = DSTINATION_POINT;
		lookDst(x, y, x + 1, y);
		lookDst(x, y, x - 1, y);
		lookDst(x, y, x, y + 1);
		lookDst(x, y, x, y - 1);
	}
	protected void lookDst(int nearDstX, int nearDstY, int x, int y) {
		
	}
	//control
	public void clear() {
		dstX = dstY = GHQ.NONE;
		for(Point[] ver : nearDsts)
			Arrays.fill(ver, null);
	}
	//information
	public boolean inArea(int x, int y) {
		return 0 < x && x < nearDsts.length && 0 < y && y < nearDsts[0].length;
	}
}
