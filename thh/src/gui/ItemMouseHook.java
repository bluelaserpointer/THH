package gui;

import java.awt.Color;

import core.GHQ;
import item.ItemData;

public class ItemMouseHook extends MouseHook<ItemData>{
	public ItemMouseHook(int size, ItemData initialObject) {
		super(size, initialObject);
	}
	@Override
	public void idle() {
		super.idle();
		if(hookingObject instanceof ItemData && hookingObject != ItemData.BLANK_ITEM) {
			final String AMOUNT_TEXT = String.valueOf(hookingObject.getAmount());
			GHQ.getGraphics2D(Color.GRAY);
			GHQ.drawStringGHQ(AMOUNT_TEXT, GHQ.mouseScreenX() + SIZE/2 - 23, GHQ.mouseScreenY() + SIZE/2 - 9, GHQ.basicFont);
			GHQ.getGraphics2D(Color.BLACK);
			GHQ.drawStringGHQ(AMOUNT_TEXT, GHQ.mouseScreenX() + SIZE/2 - 24, GHQ.mouseScreenY() + SIZE/2 - 10, GHQ.basicFont);
		}
	}
}
