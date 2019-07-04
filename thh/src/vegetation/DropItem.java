package vegetation;

import core.GHQ;
import item.ItemData;
import paint.DotPaint;
import physics.Dynam;
import physics.HasDynam;

public class DropItem extends Vegetation{
	private static final long serialVersionUID = -8747093817175771314L;
	public final ItemData ITEM;
	
	private final int HITBOX_W,HITBOX_H;
	public DropItem(ItemData item, DotPaint paintScript, int x, int y) {
		super(paintScript, x, y);
		ITEM = item;
		HITBOX_W = HITBOX_H = GHQ.NONE;
	}
	public DropItem(ItemData item, DotPaint paintScript, int x, int y, int w,int h) {
		super(paintScript, x, y);
		ITEM = item;
		HITBOX_W = w;
		HITBOX_H = h;
	}
	public boolean isCovered(HasDynam di, int distance) {
		final Dynam DYNAM = di.getDynam();
		return DYNAM.intAbsDX(point) < ((HITBOX_W != GHQ.NONE ? HITBOX_W : paintScript.getDefaultW()) + distance)/2
				&& DYNAM.intAbsDY(point) < ((HITBOX_H != GHQ.NONE ? HITBOX_H : paintScript.getDefaultH()) + distance)/2;
	}
	public ItemData pickup() {
		final ItemData tmpITEM = ITEM;
		claimDelete();
		return tmpITEM;
	}
}
