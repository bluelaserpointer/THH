package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import core.GHQ;
import item.Item;
import paint.HasDotPaint;
import paint.RectPaint;
import storage.TableStorage;

public class ItemStorageViewer extends TableStorageViewer<Item>{
	protected RectPaint cellPaint;
	public ItemStorageViewer(String group, RectPaint backgroundPaint, RectPaint cellPaint, int x, int y, int cellSize, TableStorage<Item> items) {
		super(group, backgroundPaint, x, y, cellSize, items);
		this.cellPaint = cellPaint;
	}
	
	@Override
	public void paintOfCell(HasDotPaint object, int x,int y) {
		cellPaint.rectPaint(x, y, CELL_SIZE);
		if(object == null)
			return;
		object.getPaintScript().dotPaint(x + CELL_SIZE/2, y + CELL_SIZE/2, (int)(CELL_SIZE*0.8));
		if(object instanceof Item && object != Item.BLANK_ITEM) {
			final int AMOUNT = ((Item)object).getAmount();
			final Graphics2D G2 = GHQ.getGraphics2D();
			G2.setColor(Color.GRAY);
			G2.drawString(String.valueOf(AMOUNT), x + CELL_SIZE - 23, y + CELL_SIZE - 9);
			G2.setColor(Color.BLACK);
			G2.drawString(String.valueOf(AMOUNT), x + CELL_SIZE - 24, y + CELL_SIZE - 10);
		}
	}
}
