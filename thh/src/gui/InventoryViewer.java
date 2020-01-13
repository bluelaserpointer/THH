package gui;

import item.ItemData;
import paint.rect.RectPaint;
import storage.TableStorage;

public class InventoryViewer extends ItemStorageViewer{
	public InventoryViewer(RectPaint cellPaint, int x, int y, int cellSize, TableStorage<ItemData> items) {
		super(cellPaint, x, y, cellSize, items);
	}
}
