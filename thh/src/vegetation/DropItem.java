package vegetation;

import core.GHQ;
import item.ItemData;
import paint.dot.DotPaint;
import physics.HasPoint;
import physics.Point;

public class DropItem extends Vegetation{
	private static final long serialVersionUID = -8747093817175771314L;
	public final ItemData ITEM;
	
	private final int HITBOX_W, HITBOX_H;
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
	public boolean isCovered(HasPoint source, int distance) {
		final Point DYNAM = source.point();
		return DYNAM.intAbsDX(point) < (this.width() + distance)/2 && DYNAM.intAbsDY(point) < (this.height() + distance)/2;
	}
	public String getName() {
		return ITEM.getName();
	}
	@Override
	public int width() {
		return HITBOX_W != GHQ.NONE ? HITBOX_W : paintScript.width();
	}
	@Override
	public int height() {
		return HITBOX_H != GHQ.NONE ? HITBOX_H : paintScript.height();
	}
	public ItemData pickup() {
		final ItemData tmpITEM = ITEM;
		claimDelete();
		return tmpITEM;
	}
}
