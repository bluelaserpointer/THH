package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import core.GHQ;
import item.ItemData;
import paint.dot.HasDotPaint;
import paint.rect.RectPaint;
import storage.TableStorage;

/**
 * A {@link GUIParts} subclass for managing Excel-like {@link ItemData} display, additionally draws their amount at the right-bottom corner.
 * Mainly used for inventory display.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class ItemStorageViewer extends TableStorageViewer<ItemData> {
	protected RectPaint cellPaint = RectPaint.BLANK_SCRIPT;
	
	public ItemStorageViewer setCellPaint(RectPaint cellPaint) {
		this.cellPaint = cellPaint;
		return this;
	}
	@Override
	public void paintOfCell(int id, HasDotPaint object, int x,int y) {
		cellPaint.rectPaint(x, y, cellSize);
		final Graphics2D G2 = GHQ.getG2D();
		G2.setFont(GHQ.basicFont);
		G2.setColor(Color.GRAY);
		if(object == null) {
			G2.drawString("Empty", x + cellSize - 23, y + cellSize - 9);
			return;
		}
		object.getDotPaint().dotPaint_capSize(x + cellSize/2, y + cellSize/2, (int)(cellSize*0.8));
		if(storage instanceof TableStorage && ((TableStorage<? extends HasDotPaint>)storage).isNullElement(object)) {
			G2.drawString("Empty", x + cellSize - 23, y + cellSize - 9);
		}
		if(object instanceof ItemData && object != ItemData.BLANK_ITEM) {
			final int AMOUNT = ((ItemData)object).getAmount();
			G2.drawString(String.valueOf(AMOUNT), x + cellSize - 23, y + cellSize - 9);
			G2.setColor(Color.BLACK);
			G2.drawString(String.valueOf(AMOUNT), x + cellSize - 24, y + cellSize - 10);
		}
	}

	@Override
	public ItemData objectToT(Object object) {
		if(object instanceof ItemData)
			return (ItemData)object;
		return null;
	}
}
