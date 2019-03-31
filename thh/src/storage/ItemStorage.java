package storage;

import java.io.Serializable;

import item.Item;

/**
 * A class for managing unit's inventory, holds a final {@link Storage}{@literal <}{@link Item}{@literal >}.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class ItemStorage implements Serializable{
	private static final long serialVersionUID = 353322954827886207L;
	public final Storage<Item> items;
	/**
	 * Create ItemInventory.It will use new {@link Storage}{@literal <}{@link Item}{@literal >} automatically.)
	 * @param storage
	 */
	public ItemStorage() {
		items = new Storage<Item>();
	}
	/**
	 * Create ItemInventory with a {@link Storage}{@literal <}{@link Item}{@literal >} or its subclasses.(like {@link TableStorage}{@literal <}{@link Item}{@literal >})
	 * @param storage
	 */
	public ItemStorage(Storage<Item> storage) {
		items = storage;
	}
	//control
	/**
	 * Add item with automatic stack.
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
