package storage;

/**
 * A class for managing array like Excel.Can set any cell as empty and skip them when traversing. 
 * @author bluelaserpointer
 * @since alpha1.0
 * @param <T> the type of elements in this list
 */
public class TableStorage<T> extends Storage<T>{
	private static final long serialVersionUID = -823825080682852987L;
	protected int storageW,storageH;
	public final T NULL_ELEMENT;
	
	public TableStorage(int w,int h,T nullElement) {
		super(w*h);
		storageW = w;
		storageH = h;
		NULL_ELEMENT = nullElement;
		super.initFill(NULL_ELEMENT);
	}
	public TableStorage(int w,int h) {
		super(w*h);
		storageW = w;
		storageH = h;
		NULL_ELEMENT = null;
		super.initFill(null);
	}
	
	//control
	@Override
	public boolean add(T element) {
		if(!super.hasSpace())
			return false;
		final int BLANK_INDEX = super.indexOf(NULL_ELEMENT);
		if(BLANK_INDEX != -1) {
			super.set(BLANK_INDEX, element);
			return true;
		}
		return false;
	}
	@Override
	public T remove(int index) {
		final T REMOVED = super.set(index, NULL_ELEMENT);
		if(REMOVED != NULL_ELEMENT)
			amount--;
		return REMOVED;
	}
	@Override
	public boolean remove(Object element) {
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
	
	//information
	public int getStorageW() {
		return storageW;
	}
	public int getStorageH() {
		return storageH;
	}
	public T getCell(int x, int y) {
		return super.get(x + y*storageW);
	}
}
