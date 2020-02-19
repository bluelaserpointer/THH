package gui;

import java.awt.event.MouseEvent;

import input.mouse.MouseListenerEx;
import item.ItemData;

public class InventorySystem extends GUIParts {
	protected final ItemStorageViewer inventoryViewer;
	protected final ClickMenu<ItemData> rcMenu;
	protected static final MouseListenerEx s_mouseL = new MouseListenerEx();
	public InventorySystem(ItemStorageViewer inventoryViewer, ClickMenu<ItemData> rcMenu) {
		super.setBounds(inventoryViewer);
		addFirst(this.inventoryViewer = inventoryViewer);
		addFirst(this.rcMenu = rcMenu).disable();
	}
	@Override
	public boolean clicked(MouseEvent e) {
		super.clicked(e);
		if(s_mouseL.pullButton3Event()){
			final ItemData HOVERED_ELEMENT = inventoryViewer.getMouseHoveredElement();
			rcMenu.tryOpen(HOVERED_ELEMENT, HOVERED_ELEMENT != ItemData.BLANK_ITEM);
		}
		return true;
	}

}
