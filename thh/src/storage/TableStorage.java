package storage;

import java.util.Collection;

import item.ItemData;

/**
 * A class for managing array like Excel.Can set any cell as empty and skip them when traversing. 
 * @author bluelaserpointer
 * @since alpha1.0
 * @param <T> the type of elements in this list
 */
public class TableStorage<T> extends Storage<T> {
	private static final long serialVersionUID = -823825080682852987L;
	protected int storageW,storageH;
	public final T NULL_ELEMENT;
	
	public TableStorage(int w, int h, T nullElement) {
		super(w*h);
		storageW = w;
		storageH = h;
		super.initFill(NULL_ELEMENT = nullElement);
	}
	public TableStorage(Collection<T> sample, int w, int h, T nullElement) {
		super(w*h);
		storageW = w;
		storageH = h;
		super.addAll(sample);
		super.initFill(NULL_ELEMENT = nullElement);
	}
	public TableStorage(T[] sample, int w, int h, T nullElement) {
		super(w*h);
		storageW = w;
		storageH = h;
		for(T ver : sample)
			super.add(ver);
		super.initFill(NULL_ELEMENT = nullElement);
	}
	//control
	@Override
	public boolean add(T element) {
		if(element == null || element == NULL_ELEMENT || !super.hasSpace()) {
			if(!super.hasSpace())
				System.out.println("No space avaliable. " + amount + "/" + max_amount);
			if(element == null)
				System.out.println("added null item.");
			if(element == NULL_ELEMENT)
				System.out.println("added NULL_ELEMENT.");
			return false;
		}
		for(int i = 0;i < size();i++) {
			if(get(i) == NULL_ELEMENT || get(i) == null || get(i) == ItemData.BLANK_ITEM) {
				set(i, element);
				return true;
			}
		}
		super.add(element);
		return false;
	}
	@Override
	public T remove(int index) {
		final T REMOVED = set(index, NULL_ELEMENT);
		return REMOVED;
	}
	@Override
	public boolean remove(Object element) {
		if(element == null || element == NULL_ELEMENT)
			return false;
		final int INDEX = super.indexOf(element);
		if(INDEX != -1) {
			if(element != NULL_ELEMENT) {
				super.set(INDEX, NULL_ELEMENT);
				amount--;
			}
			return true;
		}
		return false;
	}
	@Override
	public T set(int index, T element) {
		if(index < size()) {
			if(element == null)
				element = NULL_ELEMENT;
			final T PREV_ELEMENT = super.set(index, element);
			if(PREV_ELEMENT == NULL_ELEMENT)
				amount++;
			if(element == NULL_ELEMENT)
				amount--;
			return PREV_ELEMENT;
		}else {
			System.out.println("TableStorage set has called Illegally.Index: " + index);
			return NULL_ELEMENT;
		}
	}
	@Override
	public void clear() {
		amount = 0;
		initFill(NULL_ELEMENT);
	}
	
	//information
	public int traverseNext(int currentIndex) {
		for(int i = currentIndex + 1;i < size();i++) {
			if(get(i) != NULL_ELEMENT)
				return i;
		}
		return -1;
	}
	public int storageW() {
		return storageW;
	}
	public int storageH() {
		return storageH;
	}
	public boolean isNullElement(Object element) {
		return element == NULL_ELEMENT;
	}
	public int getCellID(int posX, int posY) {
		return posX + posY*storageW;
	}
	public T getCell(int posX, int posY) {
		final int ID = getCellID(posX, posY);
		return 0 <= ID && ID < super.size() ? super.get(ID) : null;
	}
}
