package core;

import java.util.Arrays;
import java.util.BitSet;

public abstract class AStar {
	private static final byte W = 0, A = 1, S = 2, D = 3;
	private static final byte WA = 10, AS = 11, SD = 12, DW = 14;
	private static final byte CENTER = 20;
	private final class Pass{
		int dstX = GHQ.NONE, dstY = GHQ.NONE;
		private final boolean isPassible() {
			return dstX == GHQ.NONE;
		}
	}
	private final Pass[][] pass;
	private final BitSet checkedPoints;
	private int finalDstX = GHQ.NONE, finalDstY = GHQ.NONE;
	
	public AStar(int w, int h) {
		pass = new Pass[w][h];
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
		finalDstX = x;finalDstY = y;
		lookDst(x, y, x + 1, y);
		lookDst(x, y, x - 1, y);
		lookDst(x, y, x, y + 1);
		lookDst(x, y, x, y - 1);
	}
	protected void lookDst(int nearDstX, int nearDstY, int x, int y) {
		
	}
	//tool
	private int xShift(byte direction) {
		switch(direction) {
		case A:
		case WA:
		case AS:
			return -1;
		case D:
		case SD:
		case DW:
			return +1;
		default:
			return 0;
		}
	}
	private int yShift(byte direction) {
		switch(direction) {
		case W:
		case DW:
		case WA:
			return -1;
		case S:
		case AS:
		case SD:
			return +1;
		default:
			return 0;
		}
	}
	private byte getDirection(int x, int y) {
		if(x == 0 && y == 0)
			return CENTER;
		if(x == 0)
			return y > 0 ? S : W;
		if(y == 0)
			return x > 0 ? D : A;
		if(x > 0)
			return y > 0 ? SD : DW;
		else
			return y > 0 ? AS : WA;
	}
	private byte getDstDirection(Pass pass) {
		return getDirection(pass.dstX, pass.dstY);
	}
	private byte getReversedDirection(byte direction) {
		switch(direction) {
		case W:
			return S;
		case A:
			return D;
		case S:
			return W;
		case D:
			return A;
		case WA:
			return SD;
		case AS:
			return DW;
		case SD:
			return WA;
		case DW:
			return AS;
		default:
			System.out.println("Illegal direction: " + direction);
			return -1;
		}
	}
	private final boolean isAlreadyCheckedPass(int x, int y) {
		return checkedPoints.get(x + y*pass.length);
	}
	private void directPoint(int targetPassX, int targetPassY, byte dstDirection) {
		//direction legal check;
		switch(dstDirection) {
		case W:
		case A:
		case S:
		case D:
			System.out.println("Illegal direction for directPoint: " + dstDirection);
			return;
		}
		//
		final Pass PASS = pass[targetPassX][targetPassY];
		if(isAlreadyCheckedPass(targetPassX, targetPassY) && getDstDirection(PASS) == dstDirection) { //not need edit
			return;
		}
		final int X_SHIFT = xShift(dstDirection), Y_SHIFT = yShift(dstDirection);
		PASS.dstX = targetPassX + X_SHIFT;
		PASS.dstY = targetPassY + Y_SHIFT;
		directPoint(PASS.dstX - X_SHIFT, PASS.dstY - Y_SHIFT, dstDirection);
		//call further calculation
		if(X_SHIFT == 0) { // vertical
			weakPoint(PASS.dstX - 1, PASS.dstY, dstDirection);
			weakPoint(PASS.dstX + 1, PASS.dstY, dstDirection);
		}else { // horizontal
			weakPoint(PASS.dstX, PASS.dstY - 1, dstDirection);
			weakPoint(PASS.dstX, PASS.dstY + 1, dstDirection);
		}
	}
	private void weakPoint(int targetPassX, int targetPassY, byte dstDirection) {
		//direction legal check;
		switch(dstDirection) {
		case W:
		case A:
		case S:
		case D:
			System.out.println("Illegal direction for weakPoint: " + dstDirection);
			return;
		}
		//side pass check
		final int SIDE_PASS_X = targetPassX + xShift(dstDirection), SIDE_PASS_Y = targetPassY + yShift(dstDirection);
		final Pass SIDE_PASS = pass[SIDE_PASS_X][SIDE_PASS_Y];
		//if(isBlocked(SIDE_PASS_X, SIDE_PASS_Y))
			//
		//
		final Pass PASS = pass[targetPassX][targetPassY];
		if(isAlreadyCheckedPass(targetPassX, targetPassY) && getDstDirection(PASS) == dstDirection) { //not need edit
			return;
		}
		final int X_SHIFT = xShift(dstDirection), Y_SHIFT = yShift(dstDirection);
		PASS.dstX = targetPassX + X_SHIFT;
		PASS.dstY = targetPassY + Y_SHIFT;
		directPoint(PASS.dstX - X_SHIFT, PASS.dstY - Y_SHIFT, dstDirection);
		//call further calculation
		if(X_SHIFT == 0) { // vertical
			weakPoint(PASS.dstX - 1, PASS.dstY, dstDirection);
			weakPoint(PASS.dstX + 1, PASS.dstY, dstDirection);
		}else { // horizontal
			weakPoint(PASS.dstX, PASS.dstY - 1, dstDirection);
			weakPoint(PASS.dstX, PASS.dstY + 1, dstDirection);
		}
	}
	//extend
	public abstract boolean isBlocked(int x, int y);
	//control
	public final void clear() {
		finalDstX = finalDstY = GHQ.NONE;
		for(Pass[] ver : pass)
			Arrays.fill(ver, null);
	}
	//information
	public final boolean inArea(int x, int y) {
		return 0 < x && x < pass.length && 0 < y && y < pass[0].length;
	}
}
