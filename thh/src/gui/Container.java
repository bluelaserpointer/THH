package gui;

import java.util.ArrayList;

import paint.HasRectPaint;
import paint.RectPaint;

public class Container<T extends HasRectPaint> extends GUIParts{
	protected int cellXAmount,cellYAmount;
	protected final int CELL_W,CELL_H;
	protected final ArrayList<T> contents;
	protected RectPaint cellPaint;
	
	//init
	public Container(String group, RectPaint backgroundPaint, RectPaint cellPaint, int x, int y, int cellW, int cellH, int cellXAmount, int cellYAmount, ArrayList<T> datalink) {
		super(group, backgroundPaint, x, y, cellW*cellXAmount, cellH*cellYAmount, true);
		this.cellPaint = cellPaint != null ? cellPaint : RectPaint.BLANK_SCRIPT;
		CELL_W = cellW;
		CELL_H = cellH;
		this.cellXAmount = cellXAmount;
		this.cellYAmount = cellYAmount;
		contents = datalink;
	}
	public Container(String group, RectPaint backgroundPaint, RectPaint cellPaint, int x, int y, int cellW, int cellH, int cellXAmount, int cellYAmount) {
		super(group, backgroundPaint, x, y, cellW*cellXAmount, cellH*cellYAmount, true);
		this.cellPaint = cellPaint != null ? cellPaint : RectPaint.BLANK_SCRIPT;
		CELL_W = cellW;
		CELL_H = cellH;
		this.cellXAmount = cellXAmount;
		this.cellYAmount = cellYAmount;
		contents = new ArrayList<T>(cellXAmount*cellYAmount);
	}
	
	//main role
	@Override
	public void paint() {
		for(int xi = 0;xi < cellXAmount;xi++) {
			for(int yi = 0;yi < cellYAmount;yi++) {
				final int PAINT_X = super.x + xi*CELL_W,PAINT_Y = super.y + yi*CELL_H;
				if(cellPaint != null)
					cellPaint.paint(PAINT_X, PAINT_Y, CELL_W, CELL_H);
				final int ID = xi + yi*cellXAmount;
				if(ID < contents.size()) {
					final T element = contents.get(ID);
					if(element != null)
						((HasRectPaint)element).getPaintScript().paint(PAINT_X + 10, PAINT_Y + 10, CELL_W - 20, CELL_H - 20);
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
