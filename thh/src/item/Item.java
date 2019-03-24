package item;

import java.io.Serializable;

import core.GHQ;
import paint.DotPaint;
import paint.HasDotPaint;
import storage.Storage;
import vegetation.DropItem;

/**
 * A primal class for managing item.
 * Note that Item class and {@link DropItem} class is different.(One commonly put in {@link storage.Storage} but the another one can put in stage as {@link vegetation.Vegetation} object.)
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class Item implements Serializable,HasDotPaint{
	private static final long serialVersionUID = 1587964067620280674L;
	protected int amount;
	protected final int STACK_CAP;
	protected final DotPaint paintScript;
	public abstract String getName();
	
	public static final Item BLANK_ITEM = new Item(null) {
		private static final long serialVersionUID = 7797829501331686714L;

		@Override
		public String getName() {
			return "<Blank Item>";
		}
	};
	
	//init
	public Item(int stackCap,DotPaint paintScript) {
		STACK_CAP = stackCap;
		this.paintScript = paintScript == null ? DotPaint.BLANK_SCRIPT : paintScript;
	}
	public Item(DotPaint paintScript) {
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
	public boolean isStackable(Item item) {
		return item.getName().equals(this.getName());
	}
	public final boolean isEmpty() {
		return amount == 0;
	}
	public boolean keepEvenEmpty() {
		return false;
	}
	public int getAmount() {
		return amount;
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
		final int leftSpace = STACK_CAP - (this.amount + amount);
		if(leftSpace <= 0)
			this.amount = STACK_CAP;
		else if(this.amount < 0)
			this.amount = 0;
		else
			this.amount += amount;
		return leftSpace;
	}
	/**
	 * 
	 * @param item
	 * @return true - not overflowed / false - overflowed
	 */
	public boolean stack(Item item) {
		final int leftSpace = STACK_CAP - (amount + item.amount);
		if(leftSpace <= 0) {
			this.amount = STACK_CAP;
			item.amount = -leftSpace;
			return false;
		}else {
			this.amount += amount;
			item.amount = 0;
			return true;
		}
	}
	
	//public tool
	public static boolean add_stack(Storage<Item> itemStorage, Item newItem) {
		for(Item item : itemStorage) {
			if(item.isStackable(newItem)) {
				item.stack(newItem);
				if(newItem.isEmpty()) {
					if(newItem.keepEvenEmpty()){
						return itemStorage.add(newItem);
					}else
						return true;
				}
			}
		}
		//not found same kind (stackable) item.
		return itemStorage.add(newItem);
	}
}
