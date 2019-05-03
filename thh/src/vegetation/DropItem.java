package vegetation;

import core.GHQ;
import item.Item;
import paint.DotPaint;
import physics.Dynam;
import physics.HasDynam;

public class DropItem extends Vegetation{
	private static final long serialVersionUID = -8747093817175771314L;
	public final Item ITEM;
	
	private final int HITBOX_W,HITBOX_H;
	public DropItem(Item item, DotPaint paintScript, int x, int y) {
		super(paintScript, x, y);
		ITEM = item;
		HITBOX_W = HITBOX_H = GHQ.NONE;
	}
	public DropItem(Item item, DotPaint paintScript, int x, int y, int w,int h) {
		super(paintScript, x, y);
		ITEM = item;
		HITBOX_W = w;
		HITBOX_H = h;
	}
	public boolean isCovered(HasDynam di, int distance) {
		final Dynam DYNAM = di.getDynam();
		return Math.abs(DYNAM.getX() - x) < ((HITBOX_W != GHQ.NONE ? HITBOX_W : paintScript.getDefaultW()) + distance)/2
				&& Math.abs(DYNAM.getY() - y) < ((HITBOX_H != GHQ.NONE ? HITBOX_H : paintScript.getDefaultH()) + distance)/2;
	}
	public Item pickup() {
		final Item tmpITEM = ITEM;
		GHQ.deleteVegetation(this);
		return tmpITEM;
	}
}
