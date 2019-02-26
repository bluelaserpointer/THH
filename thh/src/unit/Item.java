package unit;

import core.GHQ;

public abstract class Item {
	protected int amount;
	protected final int STACK_CAP;
	public abstract String getName();
	
	public Item(int stackCap) {
		STACK_CAP = stackCap;
	}
	public Item() {
		STACK_CAP = GHQ.MAX;
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
}
