package math;

public class CellArranger {
	final int MARGIN;
	public int generalW, generalH;
	public int cellXAmount, cellYAmount;
	
	public static class Cell {
		public final int X, Y, W, H;
		private Cell(int x, int y, int w, int h) {
			X = x;
			Y = y;
			W = w;
			H = h;
		}
	}
	
	public CellArranger(int margin, int generalW, int generalH, int cellXAmount, int cellYAmount) {
		MARGIN = margin;
		this.generalW = generalW;
		this.generalH = generalH;
		this.cellXAmount = cellXAmount;
		this.cellYAmount = cellYAmount;
	}
	
	public int cellW() {
		return generalW / cellXAmount;
	}
	public int cellH() {
		return generalH / cellYAmount;
	}
	public Cell getBasicCell(int xPos, int yPos) {
		final int CELL_W = cellW(), CELL_H = cellH();
		return new Cell(xPos*CELL_W + MARGIN, yPos*CELL_H + MARGIN, CELL_W - MARGIN*2, CELL_H - MARGIN*2);
	}
	public Cell getBigCell(int xPos, int yPos, int wCells, int hCells) {
		final int CELL_W = cellW(), CELL_H = cellH();
		return new Cell(xPos*CELL_W + MARGIN, yPos*CELL_H + MARGIN, CELL_W*wCells - MARGIN*2, CELL_H*hCells - MARGIN*2);
	}
	public Cell getFreeCell(int x, int y, int w, int h) {
		return new Cell(x, y, w, h);
	}
}
