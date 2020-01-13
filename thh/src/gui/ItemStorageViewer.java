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
public class ItemStorageViewer extends TableStorageViewer<ItemData>{
	protected RectPaint cellPaint;
	/**
	 * Create an ItemStorageViewer with an already existed {@link TableStorage}{@literal <}{@link ItemData}{@literal >}.
	 * @param group - name of the group this GUI belong to(use in {@link GHQ#enableGUIs(String)}, {@link GHQ#disableGUIs(String)})
	 * @param backgroundPaint - the {@link RectPaint} of this GUI background
	 * @param cellPaint - the {@link RectPaint} of the cells background
	 * @param x - the x coordinate of the upper-left corner of this GUI
	 * @param y - the y coordinate of the upper-left corner of this GUI
	 * @param cellSize - size of the cells
	 * @param items - an already exist {@link TableStorage}{@literal <}{@link ItemData}{@literal >} to display
	 */
	public ItemStorageViewer(RectPaint cellPaint, int x, int y, int cellSize, TableStorage<ItemData> items) {
		super(x, y, cellSize, items);
		this.cellPaint = cellPaint;
	}
	
	@Override
	public void paintOfCell(HasDotPaint object, int x,int y) {
		cellPaint.rectPaint(x, y, CELL_SIZE);
		if(object == null) {
			final Graphics2D G2 = GHQ.getG2D();
			G2.setColor(Color.GRAY);
			G2.drawString("Empty", x + CELL_SIZE - 23, y + CELL_SIZE - 9);
			return;
		}
		object.getDotPaint().dotPaint_capSize(x + CELL_SIZE/2, y + CELL_SIZE/2, (int)(CELL_SIZE*0.8));
		if(storage instanceof TableStorage && ((TableStorage<? extends HasDotPaint>)storage).isNullElement(object)) {
			final Graphics2D G2 = GHQ.getG2D();
			G2.setColor(Color.GRAY);
			G2.drawString("Empty", x + CELL_SIZE - 23, y + CELL_SIZE - 9);
		}
		if(object instanceof ItemData && object != ItemData.BLANK_ITEM) {
			final int AMOUNT = ((ItemData)object).getAmount();
			final Graphics2D G2 = GHQ.getG2D();
			G2.setColor(Color.GRAY);
			G2.drawString(String.valueOf(AMOUNT), x + CELL_SIZE - 23, y + CELL_SIZE - 9);
			G2.setColor(Color.BLACK);
			G2.drawString(String.valueOf(AMOUNT), x + CELL_SIZE - 24, y + CELL_SIZE - 10);
		}
	}

	@Override
	public ItemData objectToT(Object object) {
		if(object instanceof ItemData)
			return (ItemData)object;
		return null;
	}
}
