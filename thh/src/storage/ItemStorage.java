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
	 * @param targetItem
	 * @return true - successfully added / false - overflowed
	 */
	public boolean add_stack(Item targetItem) {
		for(int i = items.traverseFirst();i != -1;i = items.traverseNext(i)) {
			final Item ITEM = items.get(i);
			if(ITEM.isStackable(targetItem)) {
				ITEM.stack(targetItem);
				if(targetItem.isEmpty()) {
					if(targetItem.keepEvenEmpty()){
						return items.add(targetItem);
					}else
						return true;
				}
			}
		}
		//not found same kind (stackable) item.
		return items.add(targetItem);
	}
	
	//information
	public int countItem(Item targetItem) {
		int total = 0;
		for(int i = items.traverseFirst();i != -1;i = items.traverseNext(i)) {
			if(targetItem.isStackable(items.get(i)))
				total += items.get(i).getAmount();
		}
		return total;
	}
	public int countItemByName(String targetItemName) {
		int total = 0;
		for(int i = items.traverseFirst();i != -1;i = items.traverseNext(i)) {
			if(targetItemName.equals(items.get(i).getName()))
				total += items.get(i).getAmount();
		}
		return total;
	}
}
