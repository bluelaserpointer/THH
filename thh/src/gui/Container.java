package gui;

import java.util.ArrayList;

import paint.HasRectPaint;
import paint.RectPaint;

public class Container<T extends HasRectPaint> extends GUIParts{
	private int cellXAmount,cellYAmount;
	private final int CELL_W,CELL_H;
	private final ArrayList<T> contents;
	private RectPaint cellPaint;
	
	//init
	public Container(String group, RectPaint backgroundPaint, RectPaint cellPaint, int x, int y, int cellW, int cellH, int cellXAmount, int cellYAmount, ArrayList<T> datalink) {
		super(group, backgroundPaint, x, y, cellW*cellXAmount, cellH*cellYAmount);
		this.cellPaint = cellPaint != null ? cellPaint : RectPaint.BLANK_SCRIPT;
		CELL_W = cellW;
		CELL_H = cellH;
		contents = datalink;
	}
	public Container(String group, RectPaint backgroundPaint, RectPaint cellPaint, int x, int y, int cellW, int cellH, int cellXAmount, int cellYAmount) {
		super(group, backgroundPaint, x, y, cellW*cellXAmount, cellH*cellYAmount);
		this.cellPaint = cellPaint != null ? cellPaint : RectPaint.BLANK_SCRIPT;
		CELL_W = cellW;
		CELL_H = cellH;
		contents = new ArrayList<T>();
	}
	
	//main role
	@Override
	public void paint() {
		for(int xi = 0;xi < cellXAmount;xi++) {
			for(int yi = 0;yi < cellYAmount;yi++) {
				final int PAINT_X = super.x + xi,PAINT_Y = super.y + yi;
				if(cellPaint != null)
					cellPaint.paint(PAINT_X, PAINT_Y, CELL_W, CELL_H);
				((HasRectPaint)contents).getPaintScript().paint(PAINT_X, PAINT_Y, CELL_W, CELL_H);
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
