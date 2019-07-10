package gui.grouped;

import gui.BasicButton;
import math.CellArranger;
import paint.rect.RectPaint;

public abstract class ArrangedButtons<T> extends GUIGroup{
	
	public final CellArranger arranger;
	
	public ArrangedButtons(String name, RectPaint bgPaint, int x, int y, CellArranger arranger) {
		super(name, bgPaint, x, y, arranger.generalW, arranger.generalH);
		this.arranger = arranger;
	}
	protected abstract void clicked(T buttonValue);
	//addButtons
	public BasicButton addButton(T buttonValue, RectPaint buttonPaint, CellArranger.Cell cell) {
		final ArrangedButtons<T> outer = this;
		return super.addLast(new BasicButton(this.NAME + " pos: (" + (x + cell.X) + ", " + (y + cell.Y) + ")", buttonPaint, x + cell.X, y + cell.Y, cell.W, cell.H) {
			@Override
			public void clicked() {
				super.clicked();
				outer.clicked(buttonValue);
			}
		});
	}
	public BasicButton addButton(T buttonValue, RectPaint buttonPaint, int xPos, int yPos) {
		return addButton(buttonValue, buttonPaint, arranger.getBasicCell(xPos, yPos));
	}
	public ArrangedButtons<T> appendButton(T buttonValue, RectPaint buttonPaint, CellArranger.Cell cell) {
		addButton(buttonValue, buttonPaint, cell);
		return this;
	}
	public ArrangedButtons<T> appendButton(T buttonValue, RectPaint buttonPaint, int xPos, int yPos) {
		return appendButton(buttonValue, buttonPaint, arranger.getBasicCell(xPos, yPos));
	}
}
