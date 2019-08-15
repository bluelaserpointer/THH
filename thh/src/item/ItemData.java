package item;

import java.io.Serializable;

import calculate.Filter;
import core.GHQ;
import core.GHQObject;
import hitShape.HitShape;
import hitShape.Rectangle;
import paint.dot.DotPaint;
import paint.dot.HasDotPaint;
import physics.HitInteractable;
import physics.Point;
import physics.Standpoint;
import storage.Storage;
import unit.Unit;
import vegetation.DropItem;

/**
 * A primal class for managing item data.
 * Note that Item class and {@link DropItem} class is different.(One commonly put in {@link storage.Storage} but the another one can put in stage as {@link vegetation.Vegetation} object.)
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class ItemData extends GHQObject implements Serializable, HasDotPaint, HitInteractable{
	private static final long serialVersionUID = 1587964067620280674L;
	protected Unit owner;
	protected int amount;
	protected int stackCap = GHQ.MAX;
	protected final Point point = new Point.IntPoint();
	protected final DotPaint paintScript;
	protected int width = GHQ.NONE, height = GHQ.NONE;

	@Override
	public String getName() {
		return "<Blank Item>";
	}
	
	public static final ItemData BLANK_ITEM = new ItemData(null){
		private static final long serialVersionUID = 7797829501331686714L;

	};
	
	//init
	public ItemData(DotPaint paintScript) {
		this.paintScript = paintScript == null ? DotPaint.BLANK_SCRIPT : paintScript;
	}
	//main role
	@Override
	public void idle() {
		if(!hasOwner()) //dropped in stage
			paint();
	}
	@Override
	public void paint(boolean doAnimation) {
		super.paint(doAnimation);
		paintScript.dotPaint(point());
	}
	//control
	public void setStackCap(int amount) {
		stackCap = amount;
	}
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public void setOwner(Unit unit) {
		owner = unit;
	}
	public boolean hasOwner() {
		return owner != null;
	}
	public void use() {}
	//information
	public boolean isStackable(ItemData item) {
		return item.getName().equals(this.getName());
	}
	public final boolean isEmpty() {
		return amount == 0;
	}
	public boolean keepEvenEmpty() {
		return false;
	}
	public final int getAmount() {
		return amount;
	}
	public final int getLeftSpace() {
		return stackCap != GHQ.MAX ? stackCap - amount : GHQ.MAX;
	}
	@Override
	public final DotPaint getDotPaint() {
		return paintScript;
	}
	//control
	public void clear() {
		this.amount = 0;
	}
	public ItemData drop(int x, int y) {
		setOwner(null);
		cancelDelete();
		point().setXY(x, y);
		return GHQ.stage().addItem(this);
	}
	public ItemData pickup(Unit newOwner) {
		claimDelete();
		setOwner(newOwner);
		return this;
	}
	public int add(int amount) {
		final int LFET_SPACE = getLeftSpace();
		if(LFET_SPACE > amount) {
			this.amount += amount;
			return amount;
		}else {
			this.amount = stackCap;
			return LFET_SPACE;
		}
	}
	public int consume(int amount) {
		if(this.amount > amount) {
			this.amount -= amount;
			return amount;
		}else {
			final int CONSUMED = this.amount;
			this.amount = 0;
			return CONSUMED;
		}
	}
	/**
	 * 
	 * @param item
	 * @return
	 */
	public int stack(ItemData item) {
		final int ADDED = add(item.amount);
		item.consume(ADDED);
		return ADDED;
	}
	/**
	 * 
	 * @param item
	 * @return
	 */
	public int setOff(ItemData item) {
		final int CONSUMED = consume(item.amount);
		item.consume(CONSUMED);
		return CONSUMED;
	}
	//public tool
	public static boolean addInInventory(Storage<ItemData> itemStorage, ItemData targetItem) {
		for(int i = itemStorage.traverseFirst();i != -1;i = itemStorage.traverseNext(i)) {
			final ItemData ITEM = itemStorage.get(i);
			if(ITEM.isStackable(targetItem)) {
				ITEM.stack(targetItem);
				if(targetItem.isEmpty()) {
					if(targetItem.keepEvenEmpty()){
						return itemStorage.add(targetItem);
					}else
						return true;
				}
			}
		}
		//not found same kind (stackable) item.
		return itemStorage.add(targetItem);
	}
	public static int removeInInventory(Storage<ItemData> itemStorage, ItemData targetItem) {
		int removed = 0;
		for(int i = itemStorage.traverseFirst();i != -1;i = itemStorage.traverseNext(i)) {
			final ItemData ITEM = itemStorage.get(i);
			if(ITEM.isStackable(targetItem)) {
				removed += ITEM.setOff(targetItem);
				if(targetItem.isEmpty())
					return removed;
			}
		}
		for(int i = itemStorage.traverseFirst();i != -1;i = itemStorage.traverseNext(i)) {
			final ItemData ITEM = itemStorage.get(i);
			if(ITEM.isEmpty() && !ITEM.keepEvenEmpty())
				itemStorage.remove(ITEM);
		}
		//not found enough amount of the item.
		return removed;
	}
	public static int removeInInventory(Storage<ItemData> itemStorage, Filter<ItemData> filter, int amount) {
		int removed = 0;
		for(int i = itemStorage.traverseFirst();i != -1;i = itemStorage.traverseNext(i)) {
			final ItemData ITEM = itemStorage.get(i);
			if(filter.judge(ITEM)) {
				removed += ITEM.consume(amount);
				if(ITEM.isEmpty())
					return removed;
			}
		}
		for(int i = itemStorage.traverseFirst();i != -1;i = itemStorage.traverseNext(i)) {
			final ItemData ITEM = itemStorage.get(i);
			if(ITEM.isEmpty() && !ITEM.keepEvenEmpty())
				itemStorage.remove(ITEM);
		}
		//not found enough amount of the item.
		return removed;
	}
	@Override
	public HitShape hitShape() {
		return new Rectangle(point, width(), height());
	}
	@Override
	public int width() {
		return width != GHQ.NONE ? width : paintScript.width();
	}
	@Override
	public int height() {
		return height != GHQ.NONE ? height : paintScript.height();
	}
	@Override
	public Standpoint standpoint() {
		return new Standpoint(GHQ.NONE);
	}
	public static boolean isValid(ItemData item) {
		return item != null && item != ItemData.BLANK_ITEM;
	}
}
