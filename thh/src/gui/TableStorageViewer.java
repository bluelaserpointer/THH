package gui;

import java.awt.event.MouseEvent;
import java.util.Collection;

import core.GHQ;
import core.T_Verifier;
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
public abstract class TableStorageViewer<T extends HasDotPaint> extends GUIParts implements T_Verifier<T> {
	/**
	 * The data of this TableStorageViewer.
	 */
	public TableStorage<T> storage;
	protected int cellSize;
	protected RectPaint cellPaint = RectPaint.BLANK_SCRIPT;
	
	//init
	public TableStorageViewer<T> setTableStorage(TableStorage<T> tableStorage) {
		storage = tableStorage;
		return this;
	}
	public TableStorageViewer<T> setTableStorage(Collection<T> storage, T nullElement, int storageW, int storageH) {
		this.storage = new TableStorage<T>(storage, storageW, storageH, nullElement);
		return this;
	}
	public TableStorageViewer<T> setCellSize(int cellSize) {
		this.cellSize = cellSize;
		return this;
	}
	//main role
	@Override
	public void idle() {
		//paint
		super.idle();
		final int storageW = storage.storageW();
		final int storageH = storage.storageH();
		for(int xi = 0; xi < storageW; ++xi) {
			for(int yi = 0; yi < storageH; ++yi) {
				final int INDEX = ((TableStorage<? extends HasDotPaint>)storage).getCellID(xi, yi);
				paintOfCell(INDEX, INDEX < storage.size() ? storage.get(INDEX) : null, super.point().intX() + xi*cellSize, super.point().intY() + yi*cellSize);
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
	protected void paintOfCell(int id, HasDotPaint object, int x, int y) {
		if(object != null)
			object.getDotPaint().dotPaint_capSize(x + cellSize/2, y + cellSize/2, (int)(cellSize*0.8));
	}
	
	//control
	public TableStorageViewer<T> setCellPaint(RectPaint paintScript) {
		cellPaint = paintScript;
		return this;
	}
	@Override
	public boolean clicked(MouseEvent e) {
		super.clicked(e);
		final T ELEMENT = getMouseHoveredElement();
		if(ELEMENT != storage.NULL_ELEMENT && GHQ.mouseHook.isEmpty()) {
			GHQ.mouseHook.hook(ELEMENT, this);
		}
		return true;
	}
	@Override
	public boolean checkDragIn(GUIParts sourceUI, Object dropObject) {
		//only check if the position is legal
		final int id = getMouseHoveredID();
		if(0 <= id && id < storage.size() && objectToTAccepts(dropObject)) {
			final T overWrittenObject = storage.get(id);
			 //when swap, only accept if the original element is accepted by the source UI.
			return storage.isNullElement(overWrittenObject) || sourceUI instanceof TableStorageViewer || sourceUI.checkDragIn(this, overWrittenObject);
		}
		return false;
	}
	@Override
	public boolean checkDragOut(GUIParts targetUI, Object dropObject) {
		//check nothing
		return true;
	}
	@Override
	public void dragIn(GUIParts sourceUI, Object dropObject) {
		final int id = getMouseHoveredID();
		storage.set(id, objectToT(dropObject));
	}
	@Override
	public Object peekDragObject() {
		final T originalObj = storage.get(getMouseHoveredID());
		return storage.isNullElement(originalObj) ? null : originalObj;
	}
	@Override
	public void dragOut(GUIParts targetUI, Object dropObject, Object swapObject) {
		final int id = storage.indexOf(dropObject);
		if(swapObject == null) //move
			storage.remove(id);
		else //swap
			storage.set(id, objectToT(swapObject));
	}
	
	//information
	public int storageW() {
		return storage.storageW();
	}
	public int storageH() {
		return storage.storageH();
	}
	/**
	 * Get ID of the cell which mouse is hovering.
	 * @return ID of the hovered cell
	 */
	public int getMouseHoveredID() {
		return (GHQ.mouseScreenX() - point().intX())/cellSize + storageW()*((GHQ.mouseScreenY() - point().intY())/cellSize);
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
	@Override
	public abstract T objectToT(Object object);
}
