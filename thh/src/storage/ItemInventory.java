package storage;

import java.io.Serializable;

import item.Item;

public class ItemInventory implements Serializable{
	private static final long serialVersionUID = 353322954827886207L;
	public final Storage<Item> items;
	public ItemInventory(Storage<Item> storage) {
		items = storage;
	}
	//control
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
						return items.add(newItem);
					}else
						return true;
				}
			}
		}
		//not found same kind (stackable) item.
		return items.add(newItem);
	}
	
	//information
}
