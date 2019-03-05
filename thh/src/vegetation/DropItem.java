package vegetation;

import core.Dynam;
import core.DynamInteractable;
import paint.DotPaint;
import unit.Item;

public class DropItem extends Vegetation{
	private static final long serialVersionUID = -8747093817175771314L;
	public final Item ITEM;
	
	private final int HITBOX_W,HITBOX_H;
	public DropItem(Item item, DotPaint paintScript, int x, int y) {
		super(paintScript, x, y);
		ITEM = item;
		HITBOX_W = paintScript.getDefaultW();
		HITBOX_H = paintScript.getDefaultH();
	}
	public DropItem(Item item, DotPaint paintScript, int x, int y, int w,int h) {
		super(paintScript, x, y);
		ITEM = item;
		HITBOX_W = w;
		HITBOX_H = h;
	}
	public boolean isCovered(DynamInteractable di) {
		final Dynam DYNAM = di.getDynam();
		return Math.abs(DYNAM.getX() - x) < HITBOX_W && Math.abs(DYNAM.getY() - y) < HITBOX_H;
	}
}
