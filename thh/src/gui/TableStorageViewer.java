package gui;

import core.GHQ;
import paint.HasDotPaint;
import paint.RectPaint;
import storage.Storage;
import storage.TableStorage;

public class TableStorageViewer<T extends HasDotPaint> extends GUIParts{
	public final Storage<T> storage;
	protected int storageW,storageH;
	protected final int CELL_SIZE;
	
	//init
	public TableStorageViewer(String group, RectPaint backgroundPaint, int x, int y, int cellSize, TableStorage<T> datalink) {
		super(group, backgroundPaint, x, y, cellSize*datalink.getStorageW(), cellSize*datalink.getStorageH(), true);
		CELL_SIZE = cellSize;
		storage = datalink;
		storageW = datalink.getStorageW();
		storageH = datalink.getStorageH();
	}
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
		for(int xi = 0;xi < storageW;xi++) {
			for(int yi = 0;yi < storageH;yi++) {
				final int INDEX = xi + yi*storageW;
				paintOfCell(INDEX < storage.size() ? storage.get(INDEX) : null, super.x + xi*CELL_SIZE, super.y + yi*CELL_SIZE);
			}
		}
	}
	
	//extends
	protected void paintOfCell(HasDotPaint object, int x,int y) {
		if(object != null)
			object.getPaintScript().dotPaint_resize(x + CELL_SIZE/2, y + CELL_SIZE/2, (int)(CELL_SIZE*0.8));
	}
	
	//information
	public int getMouseHoveredID() {
		return (GHQ.getMouseScreenX() - x)/CELL_SIZE + storageW*((GHQ.getMouseScreenY() - y)/CELL_SIZE);
	}
	public T getMouseHoveredElement() {
		return storage.get(getMouseHoveredID());
	}
}
