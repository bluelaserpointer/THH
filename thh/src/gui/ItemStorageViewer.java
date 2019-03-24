package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import core.GHQ;
import item.Item;
import paint.HasDotPaint;
import paint.RectPaint;
import storage.TableStorage;

/**
 * A {@link GUIParts} subclass for managing Excel-like {@link Item} display, additionally draws their amount at the right-bottom corner.
 * Mainly used for inventory display.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class ItemStorageViewer extends TableStorageViewer<Item>{
	protected RectPaint cellPaint;
	/**
	 * Create an ItemStorageViewer with an already existed {@link TableStorage}{@literal <}{@link Item}{@literal >}.
	 * @param group - name of the group this GUI belong to(use in {@link GHQ#enableGUIs(String)}, {@link GHQ#disableGUIs(String)})
	 * @param backgroundPaint - the {@link RectPaint} of this GUI background
	 * @param cellPaint - the {@link RectPaint} of the cells background
	 * @param x - the x coordinate of the upper-left corner of this GUI
	 * @param y - the y coordinate of the upper-left corner of this GUI
	 * @param cellSize - size of the cells
	 * @param items - an already exist {@link TableStorage}{@literal <}{@link Item}{@literal >} to display
	 */
	public ItemStorageViewer(String group, RectPaint backgroundPaint, RectPaint cellPaint, int x, int y, int cellSize, TableStorage<Item> items) {
		super(group, backgroundPaint, x, y, cellSize, items);
		this.cellPaint = cellPaint;
	}
	
	@Override
	public void paintOfCell(HasDotPaint object, int x,int y) {
		cellPaint.rectPaint(x, y, CELL_SIZE);
		if(object == null)
			return;
		object.getPaintScript().dotPaint_capSize(x + CELL_SIZE/2, y + CELL_SIZE/2, (int)(CELL_SIZE*0.8));
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
