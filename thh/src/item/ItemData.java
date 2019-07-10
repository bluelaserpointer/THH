package item;

import java.io.Serializable;

import core.GHQ;
import paint.dot.DotPaint;
import paint.dot.HasDotPaint;
import storage.Storage;
import vegetation.DropItem;

/**
 * A primal class for managing item data.
 * Note that Item class and {@link DropItem} class is different.(One commonly put in {@link storage.Storage} but the another one can put in stage as {@link vegetation.Vegetation} object.)
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class ItemData implements Serializable,HasDotPaint{
	private static final long serialVersionUID = 1587964067620280674L;
	protected int amount;
	protected final int STACK_CAP;
	protected final DotPaint paintScript;
	public abstract String getName();
	
	public static final ItemData BLANK_ITEM = new ItemData(null){
		private static final long serialVersionUID = 7797829501331686714L;

		@Override
		public String getName() {
			return "<Blank Item>";
		}
	};
	
	//init
	public ItemData(int stackCap,DotPaint paintScript) {
		STACK_CAP = stackCap;
		this.paintScript = paintScript == null ? DotPaint.BLANK_SCRIPT : paintScript;
	}
	public ItemData(DotPaint paintScript) {
		STACK_CAP = GHQ.MAX;
		this.paintScript = paintScript == null ? DotPaint.BLANK_SCRIPT : paintScript;
	}
	//main role
	public DropItem drop(DotPaint newPaintScript, int x, int y) {
		return new DropItem(this,newPaintScript,x,y);
	}
	public DropItem drop(int x, int y) {
		return new DropItem(this,paintScript,x,y);
	}
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
		return STACK_CAP != GHQ.MAX ? STACK_CAP - amount : GHQ.MAX;
	}
	@Override
	public final DotPaint getPaintScript() {
		return paintScript;
	}
	//control
	public void clear() {
		this.amount = 0;
	}
	public int add(int amount) {
		final int LFET_SPACE = getLeftSpace();
		if(LFET_SPACE > amount) {
			this.amount += amount;
			return amount;
		}else {
			this.amount = STACK_CAP;
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
}
