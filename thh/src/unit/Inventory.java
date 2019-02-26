package unit;

import java.util.ArrayList;

import core.GHQ;

public class Inventory {
	protected ArrayList<Item> items;
	protected int storageSize;
	public Inventory(int storageSize) {
		items = new ArrayList<Item>(storageSize);
		this.storageSize = storageSize;
	}
	public Inventory() {
		items = new ArrayList<Item>();
		this.storageSize = GHQ.MAX;
	}
	//information
	public boolean isEmpty() {
		return items.isEmpty();
	}
	public boolean hasSpace() {
		return items.size() < storageSize;
	}
	//control
	public void clear() {
		items.clear();
	}
	public void remove(Item targetItem) {
		items.remove(targetItem);
	}
	public boolean add(Item newItem) {
		if(items.size() < storageSize) {
			items.add(newItem);
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param newItem
	 * @return true - successfully added / false - overflowed
	 */
	public boolean add_stack(Item newItem) {
		for(Item item : items) {
			if(item.isStackable(newItem)) {
				item.stack(newItem);
				if(newItem.isEmpty()) {
					if(newItem.keepEvenEmpty()){
						if(hasSpace()) {
							items.add(newItem);
							return true;
						}else
							return false;
					}else
						return true;
				}
			}
		}
		//not found same kind (stackable) item.
		if(hasSpace()) {
			items.add(newItem);
			return true;
		}else
			return false;
	}
}
