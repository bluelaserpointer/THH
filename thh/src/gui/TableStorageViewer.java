package gui;

import core.GHQ;
import paint.dot.HasDotPaint;
import paint.rect.RectPaint;
import storage.Storage;
import storage.TableStorage;

/**
 * A GUIParts subclass for managing Excel-like object display.The Objects to display needs implements {@link HasDotPaint}.<br>
 * It can link to a already exist {@link Storage} and display its contents.
 * @author bluelaserpointer
 * @since alpha1.0
 * @param <T> the type of elements in the storage
 */
public class TableStorageViewer<T extends HasDotPaint> extends GUIParts{
	/**
	 * The data of this TableStorageViewer.
	 */
	public final Storage<T> storage;
	protected int storageW,storageH;
	protected final int CELL_SIZE;
	
	//init
	/**
	 * Crate a TableStorageViewer with an already existed {@link Storage}.
	 * @param group - name of the group this GUI belong to(use in {@link GHQ#enableGUIs(String)}, {@link GHQ#disableGUIs(String)})
	 * @param backgroundPaint - the {@link RectPaint} of this GUI background
	 * @param x - the x coordinate of the upper-left corner of this GUI
	 * @param y - the y coordinate of the upper-left corner of this GUI
	 * @param cellSize - size of the cells
	 * @param datalink
	 */
	public TableStorageViewer(String group, RectPaint backgroundPaint, int x, int y, int cellSize, TableStorage<T> datalink) {
		super(group, backgroundPaint, x, y, cellSize*datalink.getStorageW(), cellSize*datalink.getStorageH(), true);
		CELL_SIZE = cellSize;
		storage = datalink;
		storageW = datalink.getStorageW();
		storageH = datalink.getStorageH();
	}
	/**
	 * Crate a TableStorageViewer with an already existed {@link Storage}, but redefine the columns and rows of the cells.
	 * @param group - name of the group this GUI belong to(use in {@link GHQ#enableGUIs(String)}, {@link GHQ#disableGUIs(String)})
	 * @param backgroundPaint - the {@link RectPaint} of this GUI background
	 * @param x - the x coordinate of the upper-left corner of this GUI
	 * @param y - the y coordinate of the upper-left corner of this GUI
	 * @param cellSize - size of the cells
	 * @param datalink - an already exist {@link Storage} to display
	 * @param storageW - columns of the cells
	 * @param storageH - rows of the cells
	 */
	public TableStorageViewer(String group, RectPaint backgroundPaint, int x, int y, int cellSize, Storage<T> datalink, int storageW, int storageH) {
		super(group, backgroundPaint, x, y, cellSize*storageW, cellSize*storageH, true);
		CELL_SIZE = cellSize;
		storage = datalink;
		this.storageW = storageW;
		this.storageH = storageH;
	}
	
	//main role
	@Override
	public void idle() {
		//paint
		if(storage instanceof TableStorage<?>) {
			storageW = ((TableStorage<?>)storage).getStorageW();
			storageH = ((TableStorage<?>)storage).getStorageH();
			for(int xi = 0;xi < storageW;xi++) {
				for(int yi = 0;yi < storageH;yi++)
					paintOfCell(((TableStorage<? extends HasDotPaint>) storage).getCell(xi, yi), super.x + xi*CELL_SIZE, super.y + yi*CELL_SIZE);
			}
		}else {
			for(int xi = 0;xi < storageW;xi++) {
				for(int yi = 0;yi < storageH;yi++) {
					final int INDEX = xi + yi*storageW;
					paintOfCell(INDEX < storage.size() ? storage.get(INDEX) : null, super.x + xi*CELL_SIZE, super.y + yi*CELL_SIZE);
				}
			}
		}
	}
	
	//extends
	/**
	 * Describes paint of a cells.Override this to quickly change cells appearance.
	 * @param object - the content in this cell({@link HasDotPaint})
	 * @param x - the x coordinate of the upper-left corner of this cell
	 * @param y - the y coordinate of the upper-left corner of this cell
	 */
	protected void paintOfCell(HasDotPaint object, int x,int y) {
		if(object != null)
			object.getPaintScript().dotPaint_rate(x + CELL_SIZE/2, y + CELL_SIZE/2, (int)(CELL_SIZE*0.8));
	}
	
	//information
	/**
	 * Get ID of the cell which mouse is hovering.
	 * @return ID of the hovered cell
	 */
	public int getMouseHoveredID() {
		return (GHQ.getMouseScreenX() - x)/CELL_SIZE + storageW*((GHQ.getMouseScreenY() - y)/CELL_SIZE);
	}
	/**
	 * Get Object of the cell which mouse is hovering.
	 * @return Object of the hovered cell
	 */
	public T getMouseHoveredElement() {
		final int ID = getMouseHoveredID();
		if(0 <= ID && ID < storage.size())
			return storage.get(ID);
		else
			return null;
	}
}
