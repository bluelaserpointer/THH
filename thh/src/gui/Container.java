package gui;

import java.util.ArrayList;

import paint.HasDotPaint;
import paint.RectPaint;

public class Container<T extends HasDotPaint> extends GUIParts{
	protected int cellXAmount,cellYAmount;
	protected final int CELL_SIZE;
	protected final ArrayList<T> contents;
	protected RectPaint cellPaint;
	
	//init
	public Container(String group, RectPaint backgroundPaint, RectPaint cellPaint, int x, int y, int cellSize, int cellXAmount, int cellYAmount, ArrayList<T> datalink) {
		super(group, backgroundPaint, x, y, cellSize*cellXAmount, cellSize*cellYAmount, true);
		this.cellPaint = cellPaint != null ? cellPaint : RectPaint.BLANK_SCRIPT;
		CELL_SIZE = cellSize;
		this.cellXAmount = cellXAmount;
		this.cellYAmount = cellYAmount;
		contents = datalink;
	}
	public Container(String group, RectPaint backgroundPaint, RectPaint cellPaint, int x, int y, int cellSize, int cellXAmount, int cellYAmount) {
		super(group, backgroundPaint, x, y, cellSize*cellXAmount, cellSize*cellYAmount, true);
		this.cellPaint = cellPaint != null ? cellPaint : RectPaint.BLANK_SCRIPT;
		CELL_SIZE = cellSize;
		this.cellXAmount = cellXAmount;
		this.cellYAmount = cellYAmount;
		contents = new ArrayList<T>(cellXAmount*cellYAmount);
	}
	
	//main role
	@Override
	public void idle() {
		for(int xi = 0;xi < cellXAmount;xi++) {
			for(int yi = 0;yi < cellYAmount;yi++) {
				final int PAINT_X = super.x + xi*CELL_SIZE,PAINT_Y = super.y + yi*CELL_SIZE;
				if(cellPaint != null)
					cellPaint.paint(PAINT_X, PAINT_Y, CELL_SIZE, CELL_SIZE);
				final int ID = xi + yi*cellXAmount;
				if(ID < contents.size()) {
					final T element = contents.get(ID);
					if(element != null)
						((HasDotPaint)element).getPaintScript().paint(PAINT_X + CELL_SIZE/2, PAINT_Y + CELL_SIZE/2, (int)(CELL_SIZE*0.8));
				}
			}
		}
	}
	
	//control
	public boolean add(T element) {
		if(contents.size() < cellXAmount*cellYAmount) {
			contents.add(element);
			return true;
		}else
			return false;
	}
	public boolean remove(T element) {
		return contents.remove(element);
	}
	public void clear() {
		contents.clear();
	}
	public void setCellAmount(int xAmount, int yAmount) {
		cellXAmount = xAmount;
		cellYAmount = yAmount;
	}
	public void addCellAmount(int xAmount, int yAmount) {
		cellXAmount += xAmount;
		cellYAmount += yAmount;
	}
}
