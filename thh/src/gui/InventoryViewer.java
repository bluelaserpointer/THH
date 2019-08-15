package gui;

import item.ItemData;
import paint.rect.RectPaint;
import storage.TableStorage;

public class InventoryViewer extends ItemStorageViewer{
	protected final ItemMouseHook itemMouseHook = new ItemMouseHook(70, ItemData.BLANK_ITEM);
	public InventoryViewer(RectPaint cellPaint, int x, int y, int cellSize, TableStorage<ItemData> items) {
		super(cellPaint, x, y, cellSize, items);
		itemMouseHook.enable();
	}
	@Override
	public void clicked() {
		super.clicked();
		itemMouseHook.hookInStorageViewer(this);
	}
}
