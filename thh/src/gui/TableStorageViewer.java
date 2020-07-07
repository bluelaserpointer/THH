package gui;

import java.awt.event.MouseEvent;

import core.GHQ;
import core.T_Verifier;
import paint.dot.HasDotPaint;
import paint.rect.RectPaint;
import storage.StorageWithSpace;
import storage.TableStorage;

/**
 * A GUIParts subclass for managing Excel-like object display.The Objects to display needs implements {@link HasDotPaint}.<br>
 * It can link to a already exist {@link StorageWithSpace} and display its contents.
 * @author bluelaserpointer
 * @since alpha1.0
 * @param <T> the type of elements in the storage
 */
public abstract class TableStorageViewer<T extends HasDotPaint> extends GUIParts implements T_Verifier<T> {
	/**
	 * The data of this TableStorageViewer.
	 */
	public TableStorage<T> storage;
	protected int cellSize = 50;
	protected RectPaint cellPaint = RectPaint.BLANK_SCRIPT;
	
	//init
	public TableStorageViewer<T> setTableStorage(TableStorage<T> tableStorage) {
		storage = tableStorage;
		updateBoundingBox();
		return this;
	}
	public TableStorageViewer<T> setCellSize(int cellSize) {
		this.cellSize = cellSize;
		updateBoundingBox();
		return this;
	}
	public void updateBoundingBox() {
		if(storage != null)
			super.setBoundsSize(storage.xCells()*cellSize, storage.yCells()*cellSize);
	}
	//main role
	@Override
	public void paint() {
		super.paint();
		//paint
		final int storageW = storage.xCells();
		final int storageH = storage.yCells();
		for(int xi = 0; xi < storageW; ++xi) {
			for(int yi = 0; yi < storageH; ++yi) {
				final int index = storage.cellIndex(xi, yi);
				paintOfCell(index, storage.isValidIndex(index) ? storage.get(index) : null, super.point().intX() + xi*cellSize, super.point().intY() + yi*cellSize);
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
		cellPaint.rectPaint(x, y, cellSize);
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
		final T element = getMouseHoveredElement();
		if(element != null && !storage.isSpaceElement(element) && GHQ.mouseHook.isEmpty()) {
			GHQ.mouseHook.hook(element, this);
		}
		return true;
	}
	@Override
	public boolean checkDragIn(GUIParts sourceUI, Object dropObject) {
		//only check if the position is legal
		final int id = getMouseHoveredIndex();
		if(storage.isValidIndex(id) && objectToTAccepts(dropObject)) {
			final T overWrittenObject = storage.get(id);
			//when swap, only accept if the original element is accepted by the source UI.
			return storage.isSpaceElement(overWrittenObject) || sourceUI instanceof TableStorageViewer || sourceUI.checkDragIn(this, overWrittenObject);
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
		final int id = getMouseHoveredIndex();
		storage.set(id, objectToT(dropObject));
	}
	@Override
	public Object peekDragObject() {
		final T originalObj = storage.get(getMouseHoveredIndex());
		return storage.isSpaceElement(originalObj) ? null : originalObj;
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
		return storage.xCells();
	}
	public int storageH() {
		return storage.yCells();
	}
	/**
	 * Get　the index of the cell which mouse is hovering.
	 * @return index of the hovered cell, or -1 if mouse is not hovering any cell.
	 */
	public int getMouseHoveredIndex() {
		return storage.cellIndex((GHQ.mouseScreenX() - point().intX())/cellSize, (GHQ.mouseScreenY() - point().intY())/cellSize);
	}
	/**
	 * Get Object of the cell which mouse is hovering.
	 * @return Object of the hovered cell, or null if mouse is not hovering any cell.
	 */
	public T getMouseHoveredElement() {
		final int index = getMouseHoveredIndex();
		return storage.isValidIndex(index) ? storage.get(index) : null;
	}
	@Override
	public abstract T objectToT(Object object);
}
